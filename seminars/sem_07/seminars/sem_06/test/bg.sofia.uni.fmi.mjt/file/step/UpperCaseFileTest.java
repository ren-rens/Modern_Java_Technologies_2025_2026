package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpperCaseFileTest {

    private UpperCaseFile step = new UpperCaseFile();

    @Test
    void testProcessInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> step.process(null),
            "If file input given to process is null, should throw IllegalArgumentException");
    }

    @Test
    void testProcessInputContentIsNull() {
        File file = mock();
        when(file.getContent()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> step.process(file),
            "If file input given to process has null content, should throw IllegalArgumentException");

        verify(file).getContent();
    }

    @Test
    void testProcessInputContentIsEmpty() {
        File file = mock();
        when(file.getContent()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> step.process(file),
            "If file input given to process has empty content, should throw IllegalArgumentException");

        verify(file, times(2)).getContent();
    }

    @Test
    void testProcessInputContentHasNoUppercase() {
        File file = mock();
        String content = "content with no upper case symbols";
        when(file.getContent()).thenReturn(content);

        assertEquals(content.toUpperCase(), step.process(file).getContent(),
            "If file input given to process has content with no upper case symbols, should return the content in uppercase");
    }

    @Test
    void testProcessInputContentIsAllUppercase() {
        File file = mock();
        String content = "CONTENT WITH ALL UPPER CASE SYMBOLS";
        when(file.getContent()).thenReturn(content);

        assertEquals(content, step.process(file).getContent(),
            "If file input given to process has content with ALL upper case symbols, should return the content in uppercase");

    }

    @Test
    void testProcessInputContentIsSomeUppercase() {
        File file = mock();
        String content = "ConTEnt wITh sOME Upper CASE SymBolS";
        when(file.getContent()).thenReturn(content);

        assertEquals(content.toUpperCase(), step.process(file).getContent(),
            "If file input given to process has content with some upper case symbols, should return the content in uppercase");

    }

}
