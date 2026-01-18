package bg.sofia.uni.fmi.mjt.music.command.executor.validator;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;

public class ExecutorValidator {

    public static String validateExecutor(String[] arguments, int neededSize) {
        if (arguments.length != neededSize) {
            return null;
        }

        return ExecutorConstants.VALID_STRING;
    }

}
