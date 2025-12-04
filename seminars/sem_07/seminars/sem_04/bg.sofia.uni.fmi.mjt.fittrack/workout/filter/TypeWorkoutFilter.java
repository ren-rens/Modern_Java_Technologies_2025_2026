package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;

public class TypeWorkoutFilter implements WorkoutFilter {
    public TypeWorkoutFilter(WorkoutType type) {
        if (type == null) {
            throw new IllegalArgumentException("Invalid workout type in TypeWorkoutFilter");
        }

        this.type = type;
    }

    @Override
    public boolean matches(Workout workout) {
        return type.equals(workout.getType());
    }

    WorkoutType type;
}
