//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
package bg.sofia.uni.fmi.mjt.fittrack.workout;

import bg.sofia.uni.fmi.mjt.fittrack.exception.InvalidWorkoutException;

public abstract class Workout {
    Workout(String name, int duration, int caloriesBurned, int difficulty) {
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

    String name;
    int duration;
    int caloriesBurned;
    int difficulty;
}

 */

import bg.sofia.uni.fmi.mjt.fittrack.FitPlanner;
import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.CardioWorkout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.StrengthWorkout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.YogaSession;

import java.awt.desktop.SystemEventListener;

void main() throws OptimalPlanImpossibleException {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    List<Workout> workouts = Arrays.asList(
        new CardioWorkout("HIIT", 30, 400, 4),
        new StrengthWorkout("Upper Body", 45, 350, 3),
        new YogaSession("Morning Flow", 20, 150, 2),
        new CardioWorkout("Cycling", 60, 600, 5),
        new StrengthWorkout("Leg Day", 30, 250, 2),
        new YogaSession("Evening Relax", 15, 100, 1)
    );

    FitPlanner planner = new FitPlanner(workouts);
    List<Workout> plan = planner.generateOptimalWeeklyPlan(120);

    for (Workout w : plan) {
        System.out.println(
            w.getName() + " " +
                w.getDuration() + " " +
                w.getCaloriesBurned() + " " +
                " " + w.getDifficulty()
        );
    }

    // CardioWorkout[name=Cycling, duration=60, caloriesBurned=600, difficulty=5]
    // CardioWorkout[name=HIIT, duration=30, caloriesBurned=400, difficulty=4]
    // StrengthWorkout[name=Leg Day, duration=30, caloriesBurned=250, difficulty=2]
}
