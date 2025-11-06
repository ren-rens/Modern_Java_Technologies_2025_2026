package bg.sofia.uni.fmi.mjt.fittrack.exception;

public class InvalidWorkoutException extends RuntimeException {
    public InvalidWorkoutException(String s) {
        super(s);
    }

    public InvalidWorkoutException(String s, Throwable e) {
        super(s, e);
    }
}
