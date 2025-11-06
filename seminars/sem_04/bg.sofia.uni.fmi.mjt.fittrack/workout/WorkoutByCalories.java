package bg.sofia.uni.fmi.mjt.fittrack.workout;

import java.util.Comparator;

public class WorkoutByCalories implements Comparator<Workout> {
    @Override
    public int compare(Workout o1, Workout o2) {
        return Integer.compare(o2.getCaloriesBurned(), o1.getCaloriesBurned());
    }
}
