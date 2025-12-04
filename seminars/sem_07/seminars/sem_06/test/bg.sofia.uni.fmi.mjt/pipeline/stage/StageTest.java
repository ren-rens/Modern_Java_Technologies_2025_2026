package bg.sofia.uni.fmi.mjt.pipeline.stage;

import bg.sofia.uni.fmi.mjt.file.step.CountFiles;
import bg.sofia.uni.fmi.mjt.pipeline.step.Step;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StageTest {

    @Test
    void testStartWithNullInitialStep() {
        assertThrows(IllegalArgumentException.class, () -> Stage.start(null),
            "When giving null initialStep to Stage.start, should throw IllegalArgumentException");
    }

    @Test
    void testStartWithValidStepInitial() {
        CountFiles step = new CountFiles();

        Stage<?, ?> stage = Stage.start(step);

        assertNotNull(stage, "Stage.start should return a non-null stage instance");
    }

    @Test
    void testAddStepWithNull() {
        Stage<Object, Object> stage = Stage.start(mock());

        assertThrows(IllegalArgumentException.class, () -> stage.addStep(null),
            "When giving null step to Stage.addStep, should throw IllegalArgumentException");
    }

    @Test
    void testAddStepWithNotNull() {
        // Create initial stage: String -> Integer (length)
        Step<String, Integer> initialStep = mock();
        when(initialStep.process("input")).thenReturn(1);

        Stage<String, Integer> stage = Stage.start(initialStep);

        // Add a second step: Integer -> String (convert to string)
        Step<Integer, String> secondStep = mock();
        when(secondStep.process(1)).thenReturn("output");
        Stage<String, String> newStage = stage.addStep(secondStep);
        assertNotNull(newStage, "Adding a valid step should return a non-null stage");
    }

    @Test
    void testExecuteWithNull() {
        Stage<String, String> stage = Stage.start(mock());

        assertThrows(IllegalArgumentException.class, () -> stage.execute(null),
            "When executing with input null, should throw IllegalArgumentException");
    }

    @Test
    void testExecuteWithValidStepsAndInput() {
        Step<String, Integer> step1 = mock();
        Step<Integer, String> step2 = mock();

        String input = "step1";
        String output = "step2";
        when(step1.process(input)).thenReturn(1);
        when(step2.process(1)).thenReturn(output);

        Stage<Object, Object> stage = new Stage<>(List.of(step1, step2));

        assertEquals(output, stage.execute(input),
            "When given valid steps and input to execute, " +
                "should return the output produced by the last step of this stage");

        verify(step1).process(input);
        verify(step2).process(1);
    }

    @Test
    void testExecuteWithSingleStep() {
        Step<String, Integer> step = mock();
        when(step.process("A")).thenReturn(10);

        Stage<String, Integer> stage = Stage.start(step);

        assertEquals(10, stage.execute("A"));
        verify(step).process("A");
    }

}
