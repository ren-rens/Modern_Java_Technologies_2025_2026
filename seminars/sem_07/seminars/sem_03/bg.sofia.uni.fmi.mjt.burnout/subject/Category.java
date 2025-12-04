package bg.sofia.uni.fmi.mjt.burnout.subject;

public enum Category {

    MATH(0),
    PROGRAMMING(1),
    THEORY(2),
    PRACTICAL(3);

    private final int idx;

    Category(int i) {
        idx = i;
    }

    public int getIdx() {
        return idx;
    }

}
