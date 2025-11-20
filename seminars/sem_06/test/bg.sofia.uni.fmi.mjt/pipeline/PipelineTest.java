package bg.sofia.uni.fmi.mjt.pipeline;

import bg.sofia.uni.fmi.mjt.pipeline.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PipelineTest {

    @Test
    void testStartWithNullInitialedStage() {
        assertThrows(IllegalArgumentException.class, () -> Pipeline.start(null),
            "When given null initialStage to start in Pipeline should throw IllegalArgumentException");
    }

    @Test
    void testStartWithInitialStageValid() {
        Stage<String, String> stage = mock();

        assertNotNull(Pipeline.start(stage),
            "When given valid initial stage to start (not null), should return the initial list of stages");
    }

    @Test
    void testAddStageWithNullStage() {
        Stage<String, Integer> s1 = mock();
        Pipeline<String, Integer> p = Pipeline.start(s1);

        assertThrows(IllegalArgumentException.class, () -> p.addStage(null),
            "When given null stage to addStage in Pipeline should throw IllegalArgumentException");
    }

    @Test
    void testAddStageWithValidStage() {
        Stage<Object, Object> s1 = mock();
        Stage<Object, Object> s2 = mock();

        Pipeline<Object, Object> p = Pipeline.start(s1);

        Pipeline<Object, Object> pAdd = p.addStage(s2);

        assertNotNull(pAdd,
            "When given valid stage to addStage in Pipeline should return this pipeline instance cast to a pipeline producing {@code NEW_O}");

    }

    @Test
    void testCacheClearedWhenAddingStage() {
        Stage<String, Integer> s1 = mock();
        Stage<Integer, String> s2 = mock();

        when(s1.execute("input")).thenReturn(10);
        when(s2.execute(10)).thenReturn("output");

        Pipeline<String, Integer> p1 = Pipeline.start(s1);

        // fill cache
        p1.execute("input");
        verify(s1, times(1)).execute("input");

        // adding new stage should clear cache
        Pipeline<String, String> p2 = p1.addStage(s2);

        // should be added again because tha cache is empty
        p2.execute("input");
        verify(s1, times(2)).execute("input");  // should be called again
        verify(s2, times(1)).execute(10);
    }

    @Test
    void testExecuteWithNullInput() {
        Stage<String, Integer> s1 = mock();
        Pipeline<String, Integer> p = Pipeline.start(s1);

        assertThrows(IllegalArgumentException.class, () -> p.execute(null),
            "When given null input to execute in Pipeline should throw IllegalArgumentException");
    }

    @Test
    void testExecuteWithOneStageInput() {
        Stage<String, Integer> s1 = mock();
        String input = "step1";
        int output = 1;

        when(s1.execute(input)).thenReturn(output);

        Pipeline<String, Integer> p = Pipeline.start(s1);

        assertEquals(output, p.execute(input),
            "When given one stage input to execute in Pipeline should return the output produced by the stage of the pipeline");

        //verify(s1).execute(input);
    }

    @Test
    void testExecuteWithMultipleStagesInput() {
        Stage<String, Integer> s1 = mock();
        Stage<Integer, String> s2 = mock();

        when(s1.execute("step1")).thenReturn(1);
        when(s2.execute(1)).thenReturn("step2");

        Pipeline<String, Integer> p1 = Pipeline.start(s1);
        Pipeline<String, String> p2 = p1.addStage(s2);

        assertEquals("step2", p2.execute("step1"),
            "When given multiple stages to execute should return the output produced by the last stage of the pipeline");

        verify(s1).execute("step1");
        verify(s2).execute(1);
    }

    @Test
    void testExecuteWithUsingCache() {
        Stage<String, Integer> s1 = mock();
        String input1 = "step1";
        int output1 = 1;

        when(s1.execute(input1)).thenReturn(output1);

        Pipeline<String, Integer> p = Pipeline.start(s1);

        assertEquals(output1, p.execute(input1));
        assertEquals(output1, p.execute(input1),
            "If the same input has been processed before, the cached" +
                " output is returned instead of re-executing the stages."); // cached

        verify(s1, times(1)).execute(input1);
    }

}
