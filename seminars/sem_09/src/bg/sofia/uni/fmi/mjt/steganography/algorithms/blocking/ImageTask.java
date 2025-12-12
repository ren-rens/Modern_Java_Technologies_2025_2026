package bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking;

import java.awt.image.BufferedImage;

public record ImageTask(BufferedImage embeddedImage, String outputFileName) {
}
