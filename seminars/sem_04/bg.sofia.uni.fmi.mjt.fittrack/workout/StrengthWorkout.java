package bg.sofia.uni.fmi.mjt.fittrack.workout;

import bg.sofia.uni.fmi.mjt.fittrack.exception.InvalidWorkoutException;

public final class StrengthWorkout implements Workout {
    public StrengthWorkout(String name, int duration, int caloriesBurned, int difficulty) {
        if (name == null || name.isEmpty()) {
            throw new InvalidWorkoutException("Invalid name for workout!");
        }

        if (duration <= 0) {
            throw new InvalidWorkoutException("Invalid duration for workout!");
        }

        if (caloriesBurned <= 0) {
            throw new InvalidWorkoutException("Invalid calories burned for workout!");
        }

        if (difficulty < 1 || difficulty > 5) {
            throw new InvalidWorkoutException("Invalid difficulty for workout!");
        }

        this.name = name;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public WorkoutType getType() {
        return type;
    }

    String name;
    int duration;
    int caloriesBurned;
    int difficulty;
    WorkoutType type;

}
