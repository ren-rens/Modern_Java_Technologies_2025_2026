package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class NameWorkoutFilter implements WorkoutFilter {
    public NameWorkoutFilter(String keyword, boolean caseSensitive) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Invalid keyword for nameWorkoutFilter!");
        }

        this.keyword = keyword;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(Workout workout) {
        return caseSensitive ? workout.getName().contains(keyword) :
            workout.getName().toLowerCase().contains(keyword.toLowerCase());
    }

    String keyword;
    boolean caseSensitive;
}
