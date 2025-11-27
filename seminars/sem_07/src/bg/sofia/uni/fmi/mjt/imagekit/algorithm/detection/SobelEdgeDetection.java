package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Invalid image given to process to grayscale: NULL");
        }

        // first we make the image black and white with the given grayscale algorithm
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        processGrayPicture(grayscaleImage, result, height, width);

        return result;
    }

    private void processGrayPicture(BufferedImage image, BufferedImage result, int height, int width) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int[][] pixels = findPixels(image, x, y);
                int changeGx = findChangeBySobelMatrix(pixels, GX);
                int changeGy = findChangeBySobelMatrix(pixels, GY);

                double changeG = Math.sqrt(changeGx * changeGx + changeGy * changeGy);
                int pixelValue = Math.min(MAX_PIXEL, (int) Math.round(changeG));

                int rgb = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                result.setRGB(y, x, rgb);
            }
        }
    }

    private int[][] findPixels(BufferedImage image, int x, int y) {
        int[][] pixels = new int[MATRIX_MAX][MATRIX_MAX];

        for (int i = 0; i < MATRIX_MAX; i++) {         // row
            for (int j = 0; j < MATRIX_MAX; j++) {     // coll
                int xi = x + i - 1;           // row in the image
                int yj = y + j - 1;           // coll in the image
                pixels[i][j] = getPixelOrZero(image, xi, yj);
            }
        }

        return pixels;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private int getPixelOrZero(BufferedImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getHeight() || y >= image.getWidth()) {
            return 0; // zero padding
        }

        int rgb = image.getRGB(y, x); // getRGB(column, row)
        return (rgb >> 16) & 0xFF; // get red color
    }

    private int findChangeBySobelMatrix(int[][] pixels, int[][] sobelMatrix) {
        int change = 0;

        for (int x = 0; x < MATRIX_MAX; x++) {
            for (int y = 0; y < MATRIX_MAX; y++) {
                change += (sobelMatrix[x][y] * pixels[x][y]);
            }
        }

        return change;
    }

    private ImageAlgorithm grayscaleAlgorithm;
    private static final int MAX_PIXEL = 255;

    private static final int MATRIX_MAX = 3;
    private static final int[][] GX = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };
    private static final int[][] GY = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };

}
