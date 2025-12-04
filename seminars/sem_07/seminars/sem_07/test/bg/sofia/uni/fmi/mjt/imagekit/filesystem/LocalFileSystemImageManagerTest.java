package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalFileSystemImageManagerTest {

    // by using temporary directory we create new files inside
    // but once the tests finish everything inside the directory gets deleted with it
    @TempDir
    Path tempDir;

    private LocalFileSystemImageManager manager = new LocalFileSystemImageManager();

    @Test
    void testLoadImageWithImageFileNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null),
            "When given imageFile = null to load should throw IllegalArgumentException");
    }

    @Test
    void testLoadImageWithImageFileNoneExistant() {
        File file = new File("file.txt"); // here we created an object not a real file on the disk
        file.delete(); // we ensure that if such file exists it will be deleted

        assertThrows(IOException.class, () -> manager.loadImage(file),
            "When given imageFile that does NOT exists to load should throw IOException");
    }

    @Test
    void testLoadImageWithImageFileNotFile() {
        File dir = new File(tempDir.toFile(), "myDir");
        dir.mkdir();

        assertThrows(IOException.class, () -> manager.loadImage(dir),
            "When given imageFile that is NOT file to load should throw IOException");
    }

    @Test
    void testLoadImageWithImageFileNotInCorrectFormat() {
        try {
            File file = new File(tempDir.toFile(), "test_file.txt");
            file.createNewFile();

            assertThrows(IOException.class, () -> manager.loadImage(file),
                "When given imageFile that is NOT in the correct format to load should throw IOException");
        } catch (IOException e) {
            throw new RuntimeException("Could not create file", e);
        }
    }

    @Test
    void testLoadImageWithValidImageFileJpegFormat() {
        try {
            File file = new File(tempDir.toFile(), "test_file.jpeg");

            // Create a valid test image and save it
            BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage, "jpeg", file);

            BufferedImage res = manager.loadImage(file);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred", e);
        }
    }

    @Test
    void testLoadImageWithValidImageFileJpgFormat() {
        try {
            File file = new File(tempDir.toFile(), "test_file.jpg");

            // Create a valid test image and save it
            BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage, "jpg", file);

            BufferedImage res = manager.loadImage(file);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred", e);
        }
    }

    @Test
    void testLoadImageWithValidImageFilePngFormat() {
        try {
            File file = new File(tempDir.toFile(), "test_file.png");

            // Create a valid test image and save it
            BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage, "png", file);

            BufferedImage res = manager.loadImage(file);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred", e);
        }
    }

    @Test
    void testLoadImageWithValidImageFileBmpFormat() {
        try {
            File file = new File(tempDir.toFile(), "test_file.bmp");

            // Create a valid test image and save it
            BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage, "bmp", file);

            BufferedImage res = manager.loadImage(file);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred", e);
        }
    }

    @Test
    void testLoadImagesFromDirectoryWithDirectoryNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImagesFromDirectory(null),
            "When given imagesDirectory = null to load should throw IllegalArgumentException");
    }

    @Test
    void testLoadImagesFromDirectoryWithDirectoryNonExistant() {
        File dir = new File(tempDir.toFile(), "nonexistent");

        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(dir),
            "When given imagesDirectory that does NOT exists to load should throw IOException");
    }

    @Test
    void testLoadImagesFromDirectoryWithDirectoryNotDirectory() {
        try {
            File file = new File(tempDir.toFile(), "not_directory.txt");

            file.createNewFile();

            assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(file),
                "When given imagesDirectory that does NOT a directory to load should throw IOException");
        } catch (IOException e) {
            throw new RuntimeException("Could not create a file", e);
        }
    }

    @Test
    void testLoadIMagesFromDirectoryReadFileInDirectoryError() {
        try {
            File invalidFile = new File(tempDir.toFile(), "invalid.txt");
            invalidFile.createNewFile();

            // When loadImage() encounters the .txt file should throws IOException
            assertThrows(RuntimeException.class, () -> manager.loadImagesFromDirectory(tempDir.toFile()),
                "Should throw RuntimeException when directory contains invalid image files");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLoadImagesFromDirectoryWithValidDirectoryJpegFormat() {
        File dir = new File(tempDir.toFile(), "images");

        try {
            dir.mkdir();

            File fileInDir = new File(dir, "fileInDir.jpeg");
            BufferedImage testImage1 = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage1, "jpeg", fileInDir);

            List<BufferedImage> res = manager.loadImagesFromDirectory(dir);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("Could not create a file", e);
        }
    }

    @Test
    void testLoadImagesFromDirectoryWithValidDirectoryPngFormat() {
        File dir = new File(tempDir.toFile(), "images");

        try {
            dir.mkdir();

            File fileInDir = new File(dir, "fileInDir.png");
            BufferedImage testImage1 = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage1, "png", fileInDir);

            List<BufferedImage> res = manager.loadImagesFromDirectory(dir);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("Could not create a file", e);
        }
    }

    @Test
    void testLoadImagesFromDirectoryWithValidDirectoryBmpFormat() {
        File dir = new File(tempDir.toFile(), "images");

        try {
            dir.mkdir();

            File fileInDir = new File(dir, "fileInDir.bmp");
            BufferedImage testImage1 = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(testImage1, "bmp", fileInDir);

            List<BufferedImage> res = manager.loadImagesFromDirectory(dir);

            assertNotNull(res, "When given valid imageFile to load loaded image should not be null");

        } catch (IOException e) {
            throw new RuntimeException("Could not create a file", e);
        }
    }

    @Test
    void testSaveImageWithImageNull() {
        assertThrows(IllegalArgumentException.class,
            () -> manager.saveImage(null, new File(tempDir.toFile(), "file.txt")),
            "When given image = null to save should throw IllegalArgumentException");
    }

    @Test
    void testSaveImageWithImageFileNull() {
        assertThrows(IllegalArgumentException.class,
            () -> manager.saveImage(new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB), null),
            "When given imageFile = null to save should throw IllegalArgumentException");
    }

    @Test
    void testSaveImageWithImageFileAndImageNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, null),
            "When given imageFile = null AND image = null to save should throw IllegalArgumentException");
    }

    @Test
    void testSaveImageWithImageFileExists() {
        try {
            BufferedImage buff = new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB);
            File file = new File(tempDir.toFile(), "existing.png");

            file.delete(); // just in case
            file.createNewFile(); // here we create the file

            assertThrows(IOException.class, () -> manager.saveImage(buff, file),
                "When given imageFile = null AND image = null to save should throw IOException");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveImageWithImageFileParentNonExistant() {
        BufferedImage buff = new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB);
        File nonExistentParent = new File(tempDir.toFile(), "nonexistent");
        File imageFile = new File(nonExistentParent, "output.png");

        assertThrows(IOException.class, () -> manager.saveImage(buff, imageFile),
            "Should throw IOException when parent directory does not exist");

    }

    @Test
    void testSaveImageWithImageFileParentNull() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        File imageFile = new File("output.png");  // no parent

        assertDoesNotThrow(() -> manager.saveImage(image, imageFile),
            "Should save successfully when file has no parent directory");

        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    @Test
    void testSaveImageWithValidDataButInvalidFormat() {
        // Create a simple BufferedImage
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        // File to save to
        File outputFile = new File(tempDir.toFile(), "test-output.");

        // Delete if it exists
        if (outputFile.exists()) {
            outputFile.delete();
        }

        // Call the method
        assertDoesNotThrow(() -> manager.saveImage(image, outputFile),
            "Should throw IOException when parent directory does not exist");
    }

    @Test
    void testSaveImageWithValidData() {
        // Create a simple BufferedImage
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        // File to save to
        File outputFile = new File(tempDir.toFile(), "test-output.png");

        // Delete if it exists
        if (outputFile.exists()) {
            outputFile.delete();
        }

        // Call the method
        assertDoesNotThrow(() -> manager.saveImage(image, outputFile),
            "Should throw IOException when parent directory does not exist");
    }

}