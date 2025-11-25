package bg.sofia.uni.fmi.mjt.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileTest {

    @Test
    void testSetContentWithNull() {
        File file = new File("file.txt");
        assertThrows(IllegalArgumentException.class, () -> file.setContent(null),
            "When setting file content with null should throw IllegalArgumentException");
    }

}
