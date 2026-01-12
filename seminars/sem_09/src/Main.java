import bg.sofia.uni.fmi.mjt.steganography.ImageCodec;
import bg.sofia.uni.fmi.mjt.steganography.ImageCodecImpl;

public class Main {
    static void main() {
        ImageCodec codec = new ImageCodecImpl();

        //codec.extractPNGImages("resources", "output");

        codec.embedPNGImages("resources", "output", "embedded");
    }
}
