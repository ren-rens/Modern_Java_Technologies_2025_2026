package bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.embed;

import bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking.ImagePair;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking.TaskQueue;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.images.ImagesOperations;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.threads.ThreadOperations;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EmbedPNGImages implements ImagesOperations, ThreadOperations {

    public EmbedPNGImages() {
        blockingQueue = new TaskQueue<>();
    }

    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {
        if (!validateDirectoryExistence(Path.of(coverSourceDirectory)) ||
            !validateDirectoryExistence(Path.of(secretSourceDirectory))) {
            return;
        }

        // create output directory if it does not exist
        createDirectory(outputDirectory);

        // get all PNG files from cover and secret sources
        List<Path> coverFiles = getPNGFiles(coverSourceDirectory);
        List<Path> secretFiles = getPNGFiles(secretSourceDirectory);

        processImagesToEmbed(coverFiles, secretFiles, outputDirectory);
    }

    private void processImagesToEmbed(List<Path> coverFiles, List<Path> secretFiles, String outputDirectory) {
        List<Thread> consumers = startConsumers(outputDirectory);
        List<Thread> producers = startProducers(coverFiles, secretFiles);
        try {
            // wait all Producer threads
            waitThreads(producers);

            // signal no more tasks
            blockingQueue.signalNoMoreTasks();

            // wait Consumer threads
            waitThreads(consumers);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public List<Thread> startConsumers(String outputDirectory) {
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            Thread currentConsumer = startCurrentConsumer(outputDirectory);
            consumers.add(currentConsumer);
        }

        return consumers;
    }

    @Override
    public Thread startCurrentConsumer(String outputDirectory) {
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    ImagePair pair = blockingQueue.take();
                    if (pair == null) {
                        break; // done
                    }

                    // process images
                    BufferedImage embeddedImage = embedImage(pair.coverImage(), pair.secretImage());
                    saveImage(embeddedImage, outputDirectory, pair.outputFileName());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        return consumer;
    }

    @SafeVarargs
    @Override
    public final List<Thread> startProducers(List<Path>... files) {
        List<Thread> producers = new ArrayList<>();

        List<Path> coverFiles = files[0];
        List<Path> secretFiles = files[1];

        if (coverFiles == null || coverFiles.isEmpty() ||
            secretFiles == null || secretFiles.isEmpty()) {
            return producers;
        }

        int size = coverFiles.size();

        for (int i = 0; i < size; i++) {
            // start producer thread
            Thread currentProducer = startCurrentProducer(coverFiles.get(i), secretFiles.get(i));
            producers.add(currentProducer);
        }

        return producers;
    }

    @Override
    public Thread startCurrentProducer(Path... files) {
        Path coverFile = files[0];
        Path secretFile = files[1];
        Thread producer = new Thread(() -> {
            try {
                // load images
                BufferedImage coverImage = loadImage(coverFile);
                BufferedImage secretImage = loadImage(secretFile);

                // validate pixels sizes
                int coverPixels = coverImage.getHeight() * coverImage.getWidth();
                int secretPixels = secretImage.getHeight() * secretImage.getWidth();
                if (!validatePixels(coverPixels, secretPixels)) {
                    return; // skip this pair
                }

                //add to queue
                String outputFileName = coverFile.getFileName().toString();
                ImagePair pair = new ImagePair(coverImage, secretImage, outputFileName);
                blockingQueue.put(pair);

            } catch (Exception e) {
                throw new RuntimeException("Problem occurred with starting producer thread", e);
            }
        });

        producer.start();
        return producer;
    }

    private BufferedImage embedImage(BufferedImage coverImage, BufferedImage secretImage) {
        int secretWidth = secretImage.getWidth();
        int secretHeight = secretImage.getHeight();

        BufferedImage result = copyImage(coverImage);

        processFourPixels(result, FIRST_FOUR_PIXELS, secretWidth);
        processFourPixels(result, SECOND_FOUR_PIXELS, secretHeight);

        processSecretImageToEmbed(result, secretImage, secretWidth, secretHeight);

        return result;
    }

    private void processFourPixels(BufferedImage image, int start, int value) {
        for (int bit = BITS; bit >= 0; bit--) {
            int bitValue = (value >> bit) & 1;
            int pixelNum = (BITS - bit) / COLORS_COUNT;

            int x = (start + pixelNum) % image.getWidth();
            int y = (start + pixelNum) / image.getWidth();

            int rgb = image.getRGB(x, y);
            int r = (rgb >> RED_COLOR) & COLOR_MASK;
            int g = (rgb >> GREEN_COLOR) & COLOR_MASK;
            int b = rgb & COLOR_MASK;

            int component = bit % COLORS_COUNT;
            if (component == 0) {
                r = (r & COMPONENT_MASK) | bitValue;
            } else if (component == 1) {
                g = (g & COMPONENT_MASK) | bitValue;
            } else {
                b = (b & COMPONENT_MASK) | bitValue;
            }

            int newRgb = (COLOR_MASK << MASK_OFFSET) | (r << RED_COLOR) | (g << GREEN_COLOR) | b;
            image.setRGB(x, y, newRgb);
        }
    }

    private void processSecretImageToEmbed(BufferedImage result, BufferedImage secretImage,
                                           int secretWidth, int secretHeight) {
        int coverIdx = PIXELS;

        for (int y = 0; y < secretHeight; y++) {
            for (int x = 0; x < secretWidth; x++) {
                int rgb = secretImage.getRGB(x, y);

                int r = (rgb >> RED_COLOR) & COLOR_MASK;
                int g = (rgb >> GREEN_COLOR) & COLOR_MASK;
                int b = rgb & COLOR_MASK;

                int gray = (r + g + b) / COLORS_COUNT;

                writeSecretPixel(result, coverIdx, gray);
                coverIdx++;
            }
        }
    }

    private void writeSecretPixel(BufferedImage result, int idx, int gray) {
        int x = idx % result.getWidth();
        int y = idx / result.getWidth();

        int rgb = result.getRGB(x, y);
        int r = (rgb >> RED_COLOR) & COLOR_MASK;
        int g = (rgb >> GREEN_COLOR) & COLOR_MASK;
        int b = rgb & COLOR_MASK;

        int bit7 = (gray >> GRAY_OFFSET_FIRST) & 1;
        int bit6 = (gray >> GRAY_OFFSET_SECOND) & 1;
        int bit5 = (gray >> GRAY_OFFSET_THIRD) & 1;

        r = (r & COMPONENT_MASK) | bit7;
        g = (g & COMPONENT_MASK) | bit6;
        b = (b & COMPONENT_MASK) | bit5;

        int newRgb = (COLOR_MASK << MASK_OFFSET) | (r << RED_COLOR) | (g << GREEN_COLOR) | b;
        result.setRGB(x, y, newRgb);
    }

    private final TaskQueue<ImagePair> blockingQueue;

}
