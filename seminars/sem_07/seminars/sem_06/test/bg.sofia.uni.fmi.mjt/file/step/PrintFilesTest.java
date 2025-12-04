package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrintFilesTest {

    private PrintFiles step;

    @BeforeEach
    void setUp() {
        step = new PrintFiles();
    }

    @Test
    void testProcessWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> step.process(null),
            "When given null input collection in PrintFiles.process() should throw IllegalArgumentException");
    }

    @Test
    void testProcessWitInputNotNullButNullFileToProcess() {
        Collection<File> input = new ArrayList<>();
        input.add(null);

        assertThrows(IllegalArgumentException.class,
            () -> step.process(input),
            "Should throw IllegalArgumentException when a file in collection is null");
    }

    @Test
    void testProcessWithSingleFile() {
        File file = mock(File.class);
        String content = "content";
        when(file.getContent()).thenReturn(content);

        Collection<File> files = List.of(file);

        Collection<File> result = step.process(files);

        assertSame(files, result,
            "Should return the same collection if the input is correct and consists of a single file");

        verify(file).getContent();
    }

    @Test
    void testProcessWithMultipleFiles() {
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        File file3 = mock(File.class);

        String content = "content";

        when(file1.getContent()).thenReturn(content);
        when(file2.getContent()).thenReturn(content);
        when(file3.getContent()).thenReturn(content);

        Collection<File> files = List.of(file1, file2, file3);

        Collection<File> result = step.process(files);

        assertSame(files, result,
            "Should return the same collection if input collection is correct and consists of multiple files");

        verify(file1).getContent();
        verify(file2).getContent();
        verify(file3).getContent();
    }

}
