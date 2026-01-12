package bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.extract;

import bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking.ImageTask;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking.TaskQueue;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.images.ImagesOperations;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.threads.ThreadOperations;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExtractPNGImages implements ImagesOperations, ThreadOperations {

    public ExtractPNGImages() {
        blockingQueue = new TaskQueue<>();
    }

    public void extractPNGImages(String sourceDirectory, String outputDirectory) {
        if (!validateDirectoryExistence(Path.of(sourceDirectory))) {
            return;
        }

        // create output directory if it does not exist
        createDirectory(outputDirectory);

        List<Path> sourceFiles = getPNGFiles(sourceDirectory);
        processImagesToExtract(sourceFiles, outputDirectory);
    }

    private void processImagesToExtract(List<Path> sourceFiles, String outputDirectory) {
        // start Consumer threads
        List<Thread> consumers = startConsumers(outputDirectory);

        // start Producer threads
        List<Thread> producers = startProducers(sourceFiles);

        try {
            // wait all Producer threads
            waitThreads(producers);

            // signal
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
                    ImageTask task = blockingQueue.take();
                    if (task == null) {
                        break; // done
                    }

                    //get secret image
                    BufferedImage secret = extract(task.embeddedImage());
                    if (secret == null) {
                        continue;
                    }

                    saveImage(secret, outputDirectory, task.outputFileName());
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

        for (Path sourceFile : files[0]) {
            Thread currentProducer = startCurrentProducer(sourceFile);
            producers.add(currentProducer);
        }

        return producers;
    }

    @Override
    public Thread startCurrentProducer(Path... files) {
        Path sourceFile = files[0];
        Thread producer = new Thread(() -> {
            try {
                // load image
                BufferedImage embeddedImage = loadImage(sourceFile);
                String outputFileName = sourceFile.getFileName().toString();

                // add to queue
                ImageTask task = new ImageTask(embeddedImage, outputFileName);
                blockingQueue.put(task);

            } catch (Exception e) {
                throw new RuntimeException("Problem occurred with starting producer thread in extracting image", e);
            }
        });

        producer.start();
        return producer;
    }

    private BufferedImage extract(BufferedImage embeddedImage) {
        // read secret image's width from the first four bits
        int width = readFourBits(embeddedImage, FIRST_FOUR_PIXELS);

        // read secret image's height from the second four bits
        int height = readFourBits(embeddedImage, SECOND_FOUR_PIXELS);

        return processSecretImageToExtract(embeddedImage, height, width);
    }

    private int readFourBits(BufferedImage embeddedImage, int start) {
        int value = 0;

        for (int bit = BITS; bit >= 0; bit--) {
            int pixelNum = (BITS - bit) / COLORS_COUNT;

            int x = (start + pixelNum) % embeddedImage.getWidth();
            int y = (start + pixelNum) / embeddedImage.getWidth();

            int rgb = embeddedImage.getRGB(x, y);
            int r = (rgb >> RED_COLOR) & COLOR_MASK;
            int g = (rgb >> GREEN_COLOR) & COLOR_MASK;
            int b = rgb & COLOR_MASK;

            int component = bit % COLORS_COUNT;
            int bitValue = (component == 0 ? r & 1 : (component == 1 ? g & 1 : b & 1));

            value = (value << 1) | bitValue;
        }

        return value;
    }

    private BufferedImage processSecretImageToExtract(BufferedImage embeddedImage, int height, int width) {
        BufferedImage secret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int embeddedIdx = PIXELS;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = readSecretPixel(embeddedImage, embeddedIdx);
                embeddedIdx++;

                int rgb = (COLOR_MASK << MASK_OFFSET) | (gray << RED_COLOR) | (gray << GREEN_COLOR) | gray;
                secret.setRGB(x, y, rgb);
            }
        }

        return secret;
    }

    private int readSecretPixel(BufferedImage embeddedImage, int embeddedIdx) {
        int x = embeddedIdx % embeddedImage.getWidth();
        int y = embeddedIdx / embeddedImage.getWidth();

        int rgb = embeddedImage.getRGB(x, y);
        int r = (rgb >> RED_COLOR) & COLOR_MASK;
        int g = (rgb >> GREEN_COLOR) & COLOR_MASK;
        int b = rgb & COLOR_MASK;

        int bit7 = r & 1;
        int bit6 = g & 1;
        int bit5 = b & 1;

        return (bit7 << GRAY_OFFSET_FIRST) | (bit6 << GRAY_OFFSET_SECOND) | (bit5 << GRAY_OFFSET_THIRD);
    }

    private final TaskQueue<ImageTask> blockingQueue;

}
