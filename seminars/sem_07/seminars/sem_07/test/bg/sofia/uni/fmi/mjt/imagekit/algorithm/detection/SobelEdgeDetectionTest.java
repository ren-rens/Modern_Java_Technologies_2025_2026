package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SobelEdgeDetectionTest {

    @Test
    void testProcessWithNullImage() {
        SobelEdgeDetection edgeDetection = new SobelEdgeDetection(null);
        assertThrows(IllegalArgumentException.class, () -> edgeDetection.process(null),
            "When given null image to process should throw IllegalArgumentException");
    }

    @Test
    void testProcessWithValidImage() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        int[][] pixels = {
            {0, 0, 0}, // black black black
            {0, 255, 0}, // black white black
            {0, 0, 0} // black black black
        };

        GrayscaleAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscaleAlgorithm);
        BufferedImage result = sobel.process(image);

        // Expected result
        // Gx = (-1*0 + 0*0 + 1*0 + -2*0 + 0*255 + 2*0 + -1*0 + 0*0 + 1*0) = 0
        // Gy = (-1*0 + -2*0 + -1*0 + 0*0 + 0*255 + 0*0 + 1*0 + 2*0 + 1*0) = 0
        // G = sqrt(Gx^2 + Gy^2) = 0
        List<Integer> expected = List.of(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        );

        List<Integer> actual = List.of(
            new Color(result.getRGB(0, 0)).getRed(),
            new Color(result.getRGB(1, 0)).getRed(),
            new Color(result.getRGB(2, 0)).getRed(),
            new Color(result.getRGB(0, 1)).getRed(),
            new Color(result.getRGB(1, 1)).getRed(),
            new Color(result.getRGB(2, 1)).getRed(),
            new Color(result.getRGB(0, 2)).getRed(),
            new Color(result.getRGB(1, 2)).getRed(),
            new Color(result.getRGB(2, 2)).getRed()
        );

        assertIterableEquals(expected, actual,
            "When given valid image to SobelEdgeDetector to process should return the picture's edges");
    }

}