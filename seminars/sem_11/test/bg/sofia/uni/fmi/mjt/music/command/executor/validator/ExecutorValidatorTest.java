package bg.sofia.uni.fmi.mjt.music.command.executor.validator;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecutorValidatorTest {

    @Test
    void testValidateExecutorWithInvalidArgumentsSize() {
        assertEquals(null, ExecutorValidator.validateExecutor(new String[1], 2),
            "When trying to validate arguments with size DIFFERENT than the needed one" +
                "should return NULL");
    }

    @Test
    void testValidateExecutorWithValidArgumentsSize() {
        assertEquals(ExecutorConstants.VALID_STRING, ExecutorValidator.validateExecutor(new String[1], 1),
            "When trying to validate arguments with size SAME as the needed one" +
                "should return VALID STRING DIFFERENT THAN NULL");
    }

}
