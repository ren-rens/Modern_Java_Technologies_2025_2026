package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountFilesTest {

    @Test
    void testProcessWithInputNull() {
        CountFiles step = new CountFiles();

        assertThrows(IllegalArgumentException.class, () -> step.process(null),
            "When given null input collection to process in CountFiles it should throw IllegalArgumentException");
    }

    @Test
    void testProcessWithInputNotNull() {
        CountFiles step = new CountFiles();
        Collection<File> input = mock();
        int size = 1;

        when(input.size()).thenReturn(size);

        assertEquals(size, step.process(input),
            "When given existing input collection to process in CountFiles should return its size");
    }

}
