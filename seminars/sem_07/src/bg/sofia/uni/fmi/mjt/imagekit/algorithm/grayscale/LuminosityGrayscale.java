package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    public LuminosityGrayscale() {

    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Invalid image given to process to grayscale: NULL");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, TYPE_INT_RGB);
        processPixels(image, result, width, height);

        return result;
    }

    private void processPixels(BufferedImage image, BufferedImage result, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);       // ARGB packed int
                int red   = (argb >> 16) & 0xFF;    // extract red
                int green = (argb >> 8) & 0xFF;     // extract green
                int blue  = argb & 0xFF;            // extract blue

                int gray = (int) Math.round(RED_COEFFICIENT * red
                    + GREEN_COEFFICIENT * green
                    + BLUE_COEFFICIENT * blue);
                gray = Math.max(MIN_GRAY, Math.min(MAX_GRAY, gray));

                int grayRGB = (gray << 16) | (gray << 8) | gray;
                result.setRGB(x, y, grayRGB);
            }
        }
    }

    private static final double RED_COEFFICIENT = 0.21;
    private static final double GREEN_COEFFICIENT = 0.72;
    private static final double BLUE_COEFFICIENT = 0.07;

    private static final int MAX_GRAY = 255;
    private static final int MIN_GRAY = 0;

}
