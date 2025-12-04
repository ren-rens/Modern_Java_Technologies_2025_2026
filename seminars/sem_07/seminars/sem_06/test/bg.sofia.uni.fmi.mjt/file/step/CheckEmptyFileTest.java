package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import bg.sofia.uni.fmi.mjt.file.exception.EmptyFileException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckEmptyFileTest {

    @Test
    void testProcessWithNullFile() {
        CheckEmptyFile step = new CheckEmptyFile();
        assertThrows(EmptyFileException.class, () -> step.process(null),
            "When given a null file to CheckEmptyFile.process() should throw EmptyFileException");
    }

    @Test
    void testProcessWithNullFileContent() {
        CheckEmptyFile step = new CheckEmptyFile();
        File file = mock(File.class);
        when(file.getContent()).thenReturn(null);

        assertThrows(EmptyFileException.class, () -> step.process(file),
            "When given a file with null content, CheckEmptyFile.process() should throw EmptyFileException");

        verify(file).getContent();
    }

    @Test
    void testProcessWithEmptyFileContent() {
        CheckEmptyFile step = new CheckEmptyFile();
        File file = mock(File.class);
        when(file.getContent()).thenReturn("");  // Return empty string

        assertThrows(EmptyFileException.class, () -> step.process(file),
            "When given a file with empty content, CheckEmptyFile.process() should throw EmptyFileException");

        verify(file, times(2)).getContent();
    }

    @Test
    void testProcessWithPresentContent() {
        CheckEmptyFile step = new CheckEmptyFile();

        File file = mock(File.class);
        String content = "content.txt";

        when(file.getContent()).thenReturn(content);

        assertEquals(file, step.process(file),
            "When given a file with present content, CheckEmptyFile.process() should return it");

        verify(file, times(2)).getContent();
    }

}
