package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LuminosityGrayscaleTest {

    private LuminosityGrayscale grayscale = new LuminosityGrayscale();

    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null),
            "When given null image to process should throw IllegalArgumentException");
    }

    @Test
    void testProcessWithValidImage() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(0, 0, 255).getRGB()); // blue pixel
        image.setRGB(1, 0, new Color(0, 255, 0).getRGB()); // green
        image.setRGB(0, 1, new Color(255, 0, 0).getRGB()); // red
        image.setRGB(1, 1, new Color(100, 100, 100).getRGB()); // grayish

        BufferedImage result = grayscale.process(image);

        // Expected grayscale values
        int p001 = (int) Math.round(0.21 * 0 + 0.72 * 0   + 0.07 * 255);
        int p010 = (int) Math.round(0.21 * 0   + 0.72 * 255 + 0.07 * 0);
        int p100 = (int) Math.round(0.21 * 255   + 0.72 * 0   + 0.07 * 0);
        int p111 = (int) Math.round(0.21 * 100 + 0.72 * 100 + 0.07 * 100);

        // we could put both results in two arrays and check if they are equals
        List<Integer> expected = List.of(p001, p010, p100, p111);

        // to extract grayscale values from result image we could use new Color and get the red color for example
        // red = green = blue so it does not matter which color we take
        List<Integer> actual = List.of(
            new Color(result.getRGB(0, 0)).getRed(),
            new Color(result.getRGB(1, 0)).getRed(),
            new Color(result.getRGB(0, 1)).getRed(),
            new Color(result.getRGB(1, 1)).getRed()
        );

        assertIterableEquals(expected, actual,
            "When given a valid image to grayscale should return the image in black and white");
    }

}