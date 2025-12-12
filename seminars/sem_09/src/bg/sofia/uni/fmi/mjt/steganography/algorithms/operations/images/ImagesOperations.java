package bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface ImagesOperations {

    // validators
    default boolean validatePNGImage(Path file) {
        return file.toString().toLowerCase().endsWith(".png");
    }

    default boolean validateDirectoryExistence(Path directory) {
        return Files.exists(directory);
    }

    default boolean validatePixels(int coverPixels, int secretPixels) {
        return coverPixels >= secretPixels + PIXELS;
    }

    // images operations
    default List<Path> getPNGFiles(String directory) {
        List<Path> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(directory))) {

            for (Path curr : stream) {
                if (!validatePNGImage(curr)) {
                    continue;
                }

                result.add(curr);
            }

        } catch (IOException | DirectoryIteratorException e) {
            throw new RuntimeException("Problem occurred with getting PNG images", e);
        }

        Collections.sort(result); // sort alphabetically
        return result;
    }

    default void createDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    default BufferedImage copyImage(BufferedImage coverImage) {
        BufferedImage result = new BufferedImage(
            coverImage.getWidth(), coverImage.getHeight(), coverImage.getType()
        );

        int height = coverImage.getHeight();
        int width = coverImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, coverImage.getRGB(x, y));
            }
        }

        return result;
    }

    default void saveImage(BufferedImage image, String outputDir, String name) {
        try {
            ImageIO.write(image, "png", new File(outputDir, name));
            System.out.println("Saved " + name + " to " + outputDir);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("While saving image %s", name), e);
        }
    }

    default BufferedImage loadImage(Path imagePath) {
        try {
            return ImageIO.read(imagePath.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load image", e);
        }
    }

    // pixels constants
    static final int PIXELS = 8;
    static final int FIRST_FOUR_PIXELS = 0;
    static final int SECOND_FOUR_PIXELS = 4;

    // colors constants
    static final int RED_COLOR = 16;
    static final int GREEN_COLOR = 8;
    static final int COLOR_MASK = 0xFF;
    static final int COLORS_COUNT = 3; // red, green, blue
    static final int MASK_OFFSET = 24;
    static final int COMPONENT_MASK = 0xFE;

    // offsets constants
    static final int GRAY_OFFSET_FIRST = 7;
    static final int GRAY_OFFSET_SECOND = 6;
    static final int GRAY_OFFSET_THIRD = 5;

    static final int BITS = 11; // 0-11

}
