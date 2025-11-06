package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

public class CaloriesWorkoutFilter implements WorkoutFilter {
    public CaloriesWorkoutFilter(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Invalid min and max given to DurationWorkoutFilter!");
        }

        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matches(Workout workout) {
        return min <= workout.getCaloriesBurned() && workout.getCaloriesBurned() <= max;
    }

    int min;
    int max;
}
