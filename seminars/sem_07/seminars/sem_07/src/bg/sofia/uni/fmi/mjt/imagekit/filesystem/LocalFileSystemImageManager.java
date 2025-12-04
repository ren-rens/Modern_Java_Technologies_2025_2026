package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    public LocalFileSystemImageManager() {
    }

    /**
     * Loads a single image from the given file path.
     *
     * @param imageFile the file containing the image.
     * @return the loaded BufferedImage.
     * @throws IllegalArgumentException if the file is null
     * @throws IOException              if the file does not exist, is not a regular file,
     *                                  or is not in one of the supported formats.
     */
    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Invalid file to load: NULL");
        }

        String fileName = imageFile.getName().toLowerCase();
        String format = getFormat(fileName);
        if (!imageFile.exists() || !imageFile.isFile() || !validateFormat(format)) {
            throw new IOException(
                "Invalid file to load: not exist OR not regular file" + " OR not in the supported formats");
        }

        BufferedImage img = ImageIO.read(imageFile);

        return img;
    }

    private String getFormat(String fileName) {
        String format = "";

        if (fileName.endsWith(".png")) {
            format = "png";
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            format = "jpeg";
        } else if (fileName.endsWith(".bmp")) {
            format = "bmp";
        }

        return format;
    }

    private boolean validateFormat(String format) {
        return format.equals("png") || format.equals("jpeg") || format.equals("bmp");
    }

    /**
     * Loads all images from the specified directory.
     *
     * @param imagesDirectory the directory containing the images.
     * @return A list of BufferedImages representing the loaded images.
     * @throws IllegalArgumentException if the directory is null.
     * @throws IOException              if the directory does not exist, is not a directory,
     *                                  or contains files that are not in one of the supported formats.
     */
    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Invalid directory to load: NULL");
        }

        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("Invalid directory to load: not exist OR not directory");
        }

        return loadAllImagesFromDirectoryRec(imagesDirectory);
    }

    private List<BufferedImage> loadAllImagesFromDirectoryRec(File imagesDirectory) {
        List<BufferedImage> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(imagesDirectory.toPath())) {

            for (Path fileOrSubDir : stream) {
                result.add(loadImage(fileOrSubDir.toFile()));
            }

        } catch (IOException | DirectoryIteratorException e) {
            throw new RuntimeException("Configure error", e);
        }

        return result;
    }

    /**
     * Saves the given image to the specified file path.
     *
     * @param image     the image to save.
     * @param imageFile the file to save the image to.
     * @throws IllegalArgumentException if the image or file is null.
     * @throws IOException              if the file already exists or the parent directory does not exist.
     */
    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Invalid image or image file to load: NULL");
        }

        File parent = imageFile.getParentFile();
        if (imageFile.exists() || (parent != null && !parent.exists())) {
            throw new IOException("Invalid directory to load: already exists OR no parent directory exists");
        }

        String format = getFormat(imageFile.getName());
        if (!validateFormat(format)) {
            format = "png";
        }

        boolean success = ImageIO.write(image, format, imageFile);
    }

}
