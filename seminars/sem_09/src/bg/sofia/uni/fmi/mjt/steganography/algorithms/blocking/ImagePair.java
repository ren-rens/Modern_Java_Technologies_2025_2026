package bg.sofia.uni.fmi.mjt.steganography.algorithms.blocking;

import java.awt.image.BufferedImage;

public record ImagePair(BufferedImage coverImage, BufferedImage secretImage, String outputFileName) {
}
