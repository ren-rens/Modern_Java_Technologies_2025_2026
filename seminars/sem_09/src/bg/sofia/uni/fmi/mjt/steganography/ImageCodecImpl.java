package bg.sofia.uni.fmi.mjt.steganography;

import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.embed.EmbedPNGImages;
import bg.sofia.uni.fmi.mjt.steganography.algorithms.operations.extract.ExtractPNGImages;

public class ImageCodecImpl implements ImageCodec {

    private final EmbedPNGImages embedder = new EmbedPNGImages();
    private final ExtractPNGImages extractor = new ExtractPNGImages();

    // embed images
    @Override
    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {
        embedder.embedPNGImages(coverSourceDirectory, secretSourceDirectory, outputDirectory);
    }

    // extract images
    @Override
    public void extractPNGImages(String sourceDirectory, String outputDirectory) {
        extractor.extractPNGImages(sourceDirectory, outputDirectory);
    }

}