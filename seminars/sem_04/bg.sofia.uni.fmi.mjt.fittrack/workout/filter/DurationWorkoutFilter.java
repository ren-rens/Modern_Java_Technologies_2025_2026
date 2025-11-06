package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class DurationWorkoutFilter implements WorkoutFilter {
    public DurationWorkoutFilter(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Invalid min and max given to DurationWorkoutFilter!");
        }

        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(Workout workout) {
        return min <= workout.getDuration() && workout.getDuration() <= max;
    }

    int min;
    int max;
}
