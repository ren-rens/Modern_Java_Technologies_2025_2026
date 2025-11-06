package bg.sofia.uni.fmi.mjt.fittrack.exception;

public class OptimalPlanImpossibleException extends Exception {
    public OptimalPlanImpossibleException(String s) {
        super(s);
    }

    OptimalPlanImpossibleException(String s, Throwable e) {
        super(s, e);
    }
}
