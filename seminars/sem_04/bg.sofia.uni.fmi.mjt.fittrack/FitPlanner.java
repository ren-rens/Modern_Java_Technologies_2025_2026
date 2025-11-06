package bg.sofia.uni.fmi.mjt.fittrack;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.CardioWorkout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutByCalories;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutByDifficulty;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;
import bg.sofia.uni.fmi.mjt.fittrack.workout.filter.WorkoutFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class FitPlanner implements FitPlannerAPI {
    public FitPlanner(Collection<Workout> availableWorkouts) {
        if (availableWorkouts == null) {
            throw new IllegalArgumentException("Invalid input for availableWorkouts in constructor of FitPlanner!");
        }

        this.availableWorkouts = availableWorkouts;
    }

    @Override
    public List<Workout> findWorkoutsByFilters(List<WorkoutFilter> filters) {
        if (availableWorkouts.isEmpty()) {
            return List.of();
        }

        if (filters == null) {
            throw new IllegalArgumentException("Invalid filters given to FirPlanner!");
        }

        Set<Workout> rightWorkouts = new HashSet<>(availableWorkouts);

        for (WorkoutFilter filter : filters) {
            for (Workout workout : availableWorkouts) {
                if (!filter.matches(workout)) {
                    // remove from set if in set
                    rightWorkouts.remove(workout);
                }
            }
        }

        return List.copyOf(rightWorkouts);
    }

    @Override
    public List<Workout> generateOptimalWeeklyPlan(int totalMinutes) throws OptimalPlanImpossibleException {
        if (availableWorkouts.isEmpty()) {
            return List.of();
        }

        if (totalMinutes < 0) {
            throw new IllegalArgumentException("Invalid input for totalMinutes in generateOptimalWeeklyPlan!");
        }

        if (totalMinutes == 0) {
            return List.of();
        }

        boolean[][] usedWorkouts = findProfitInCalories(totalMinutes);
        Set<Workout> currWorkouts = findWorkoutsByProfit(usedWorkouts, totalMinutes, availableWorkouts.size());

        Set<Workout> result = new TreeSet<>(new WorkoutByDifficulty());
        result.addAll(currWorkouts);

        return List.copyOf(result);
    }

    private boolean[][] findProfitInCalories(int totalMinutes) {
        // profit is the burned calories
        // weight is the duration
        int size = availableWorkouts.size();
        int[] calories = new int[size];
        int[] duration = new int[size];
        int idx = 0;

        for (Workout workout : availableWorkouts) {
            calories[idx] = workout.getCaloriesBurned();
            duration[idx++] = workout.getDuration();
        }

        return knapsack(totalMinutes, calories, duration, size);
    }

    private boolean[][] knapsack(int totalMinutes, int[] val, int[] wt, int size) {
        // Initializing dp array
        int[] dp = new int[totalMinutes + 1];
        boolean[][] used = new boolean[size][totalMinutes + 1];

        // Taking first i elements
        for (int i = 1; i <= size; i++) {

            // Starting from back, so that we also have data of
            // previous computation of i-1 items
            for (int j = totalMinutes; j >= wt[i - 1]; j--) {
                if (dp[j - wt[i - 1]] + val[i - 1] > dp[j]) {
                    dp[j] = dp[j - wt[i - 1]] + val[i - 1];
                    used[i - 1][j] = true;
                }
            }
        }

        return used;
        //dp[totalMinutes];
    }

    private Set<Workout> findWorkoutsByProfit(boolean[][] used, int totalMinutes, int size)
        throws OptimalPlanImpossibleException {
        Set<Workout> result = new HashSet<>();
        List<Workout> workoutList = new ArrayList<>(availableWorkouts);

        int j = totalMinutes;
        for (int i = size - 1; i >= 0; i--) {
            if (used[i][j]) {
                Workout w = workoutList.get(i);
                result.add(w);

                j -= w.getDuration();
            }
        }

        if (result.isEmpty()) {
            throw new OptimalPlanImpossibleException("No valid plan fits the time limit.");
        }

        return result;
    }

    @Override
    public Map<WorkoutType, List<Workout>> getWorkoutsGroupedByType() {
        if (availableWorkouts.isEmpty()) {
            return Map.of();
        }

        Map<WorkoutType, List<Workout>> mp = new EnumMap<>(WorkoutType.class);

        for (Workout workout : availableWorkouts) {
            WorkoutType type = workout.getType();
            List<Workout> curr = mp.get(type);

            if (curr.contains(workout)) {
                continue;
            }

            curr.add(workout);
            mp.put(type, curr);
        }

        return Map.copyOf(mp);
    }

    @Override
    public List<Workout> getWorkoutsSortedByCalories() {
        if (availableWorkouts.isEmpty()) {
            return List.of();
        }

        Set<Workout> byCalories = new TreeSet<>(new WorkoutByCalories());
        byCalories.addAll(availableWorkouts);

        return List.copyOf(byCalories);
    }

    @Override
    public List<Workout> getWorkoutsSortedByDifficulty() {
        if (availableWorkouts.isEmpty()) {
            return List.of();
        }

        Set<Workout> s = new TreeSet<>(new WorkoutByDifficulty());
        s.addAll(availableWorkouts);
        return List.copyOf(s);
    }

    @Override
    public Set<Workout> getUnmodifiableWorkoutSet() {
        return availableWorkouts.isEmpty() ? Set.of() : Set.copyOf(availableWorkouts);
    }

    Collection<Workout> availableWorkouts;
}
