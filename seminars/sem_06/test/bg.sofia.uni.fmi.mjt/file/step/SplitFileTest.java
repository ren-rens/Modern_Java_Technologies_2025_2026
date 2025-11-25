package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SplitFileTest {

    @Test
    void testProcessWithNullInput() {
        SplitFile step = new SplitFile();

        assertThrows(IllegalArgumentException.class, () -> step.process(null),
            "When input given to process is null should throw IllegalArgumentException");
    }

    @Test
    void testProcessWithNullContent() {
        SplitFile step = new SplitFile();
        File file = mock(File.class);

        when(file.getContent()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> step.process(file),
            "When input given to process has null content should throw IllegalArgumentException");

        verify(file).getContent();
    }

    @Test
    void testProcessWithEmptyContent() {
        SplitFile step = new SplitFile();
        File file = mock(File.class);

        when(file.getContent()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> step.process(file),
            "When input given to process has empty content should throw IllegalArgumentException");

        verify(file, times(2)).getContent();
    }

    @Test
    void testProcessWithSingleWord() {
        SplitFile step = new SplitFile();
        File file = new File("hello");

        Set<File> result = step.process(file);

        assertEquals(1, result.size(), "Single word should produce one file");

        File resultFile = result.iterator().next();
        assertEquals("hello", resultFile.getContent(),
            "Single word should produce one file");
    }

    @Test
    void testProcessWithMultipleWords() {
        SplitFile step = new SplitFile();
        File file = mock(File.class);

        when(file.getContent()).thenReturn("hello world test");

        Set<File> result = step.process(file);

        assertEquals(3, result.size(), "Three words should produce three files");
    }

    @Test
    void testProcessWithDuplicateWords() {
        SplitFile step = new SplitFile();
        File file = mock(File.class);

        when(file.getContent()).thenReturn("hello hello world world");

        Set<File> result = new HashSet<>();
        result.add(new File("hello"));
        result.add(new File("world"));

        assertEquals(result.size(), step.process(file).size(),
            "If there are duplicate words, they should be saved ones");
    }

    @Test
    void testProcessWithMultipleWhitespaces() {
        SplitFile step = new SplitFile();
        File file = mock(File.class);

        when(file.getContent()).thenReturn("hello    world\t\ttest\n\nfoo");

        Set<File> result = step.process(file);

        assertEquals(4, result.size(),
            "Should handle multiple spaces, tabs, and newlines");
    }
}
