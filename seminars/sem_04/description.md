# Колекции и Чист код
## FitTrack – Оптимален седмичен план 🏋️‍♂️
Добре дошли във FitTrack — приложението за всички, които искат да изгорят калории, без да жертват цялото си свободно време. Ще създадем фитнес приложение, което помага да изберем най-добрите тренировки за седмицата, така че да постигнем максимален ефект за ограничено време (и минимални страдания).

### 🧩 FitPlanner
В пакета ```bg.sofia.uni.fmi.mjt.fittrack``` създайте публичен клас ```FitPlanner```, който има публичен конструктор ```FitPlanner(Collection<Workout> availableWorkouts)``` и имплементира интерфейса ```FitPlannerAPI```:
```
package bg.sofia.uni.fmi.mjt.fittrack;

import bg.sofia.uni.fmi.mjt.fittrack.exception.OptimalPlanImpossibleException;
import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;
import bg.sofia.uni.fmi.mjt.fittrack.workout.WorkoutType;
import bg.sofia.uni.fmi.mjt.fittrack.workout.filter.WorkoutFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for the core functionality of the FitTrack Planner.
 * Enables searching, rating, and optimal planning of workouts.
 */
public interface FitPlannerAPI {

    /**
     * Returns a list of workouts that match all provided filters.
     *
     * @param filters a list of filters to be applied.
     * @return a list of workouts that satisfy all filters.
     * @throws IllegalArgumentException if filters is null.
     */
    List<Workout> findWorkoutsByFilters(List<WorkoutFilter> filters);

    /**
     * Generates an optimal weekly workout plan that maximizes burned calories
     * while fitting within the specified total time limit.
     *
     * @param totalMinutes total available time (in minutes) for workouts during the week
     * @return a list of optimally selected workouts, sorted by calories, then by difficulty, in descending order.
     *         Returns an empty list if totalMinutes is 0.
     * @throws OptimalPlanImpossibleException if a valid plan cannot be generated (e.g., all workouts exceed the time limit)
     * @throws IllegalArgumentException       if totalMinutes is negative
     */
    List<Workout> generateOptimalWeeklyPlan(int totalMinutes) throws OptimalPlanImpossibleException;

    /**
     * Groups all available workouts by type.
     *
     * @return an unmodifiable Map where the key is WorkoutType and the value is a list of workouts of that type.
     */
    Map<WorkoutType, List<Workout>> getWorkoutsGroupedByType();

    /**
     * Returns a list of all workouts, sorted by burned calories in descending order.
     *
     * @return an unmodifiable list of workouts sorted by calories in descending order.
     */
    List<Workout> getWorkoutsSortedByCalories();

    /**
     * Returns a list of all workouts, sorted by difficulty in ascending order.
     *
     * @return an unmodifiable list of workouts sorted by difficulty in ascending order.
     */
    List<Workout> getWorkoutsSortedByDifficulty();

    /**
     * Returns an unmodifiable set of all available workouts.
     *
     * @return an unmodifiable Set containing all workouts.
     */
    Set<Workout> getUnmodifiableWorkoutSet();

}
```

### 🏃‍♀️Workouts
Поддържат се три типа тренировки: ```CardioWorkout```, ```StrengthWorkout``` и ```YogaSession```. Всички имат публичен конструктор със списък аргументи ```(String name, int duration, int caloriesBurned, int difficulty)```.

#### Валидация на аргументи:
* ```name``` не трябва да е ```null``` или празен/само whitespace
* ```duration``` трябва да е положително число (> 0)
* ```caloriesBurned``` трябва да е положително число (> 0)
* ```difficulty``` трябва да е в интервала [1, 5] (1 – лесна, 5 – “няма да си усещаш краката”)

При невалиден параметър се хвърля ```InvalidWorkoutException```, което е ```RuntimeException```.

Всички видове тренировки имплементират интерфейса ```Workout```:
```
package bg.sofia.uni.fmi.mjt.fittrack.workout;

public sealed interface Workout permits CardioWorkout, StrengthWorkout, YogaSession {

    /**
     * Returns the name of the workout.
     *
     * @return the workout name.
     */
    String getName();

    /**
     * Returns the duration of the workout in minutes.
     *
     * @return the duration in minutes.
     */
    int getDuration();

    /**
     * Returns the number of calories burned by performing the workout.
     *
     * @return the calories burned.
     */
    int getCaloriesBurned();

    /**
     * Returns the difficulty of the workout (1 - easy, 5 - very hard).
     *
     * @return the difficulty.
     */
    int getDifficulty();

    /**
     * Returns the type of the workout.
     *
     * @return the workout type.
     */
    WorkoutType getType();

}
```

Видът тренировка се описва от следния enum:
```
package bg.sofia.uni.fmi.mjt.fittrack.workout;

public enum WorkoutType {
    CARDIO, STRENGTH, YOGA
}
```

### 🧪 WorkoutFilter
Тренировките могат да се филтрират по различни критерии. Всички филтри имплементират интерфейса ```WorkoutFilter```:
```
package bg.sofia.uni.fmi.mjt.fittrack.workout.filter;

import bg.sofia.uni.fmi.mjt.fittrack.workout.Workout;

/**
 * Interface for filtering workouts according to various criteria.
 */
public interface WorkoutFilter {

    /**
     * Checks whether a given workout matches the filter's conditions.
     *
     * @param workout the workout to check.
     * @return true if the workout matches the filter, false otherwise.
     */
    boolean matches(Workout workout);

}
```

#### 🧩 Имплементирайте следните филтри в пакета 
```
bg.sofia.uni.fmi.mjt.fittrack.workout.filter
```
1. ```NameWorkoutFilter```
  *   Аргументи: ```keyword```, ```caseSensitive```.
  *   Сравнява за частично или пълно съвпадение на името на тренировката (substring matching).
  *   Ако ```keyword``` е ```null``` или празен стринг – хвърли ```IllegalArgumentException```.
```
public NameWorkoutFilter(String keyword, boolean caseSensitive)
```

2. DurationWorkoutFilter
  *   Аргументи: ```min```, ```max``` - продължителност в минути.
  *   Проверява дали ```min``` <= ```duration``` <= ```max```.
  *   Ако ```min``` > ```max```, ```min``` < 0 или ```max``` < 0, хвърли ```IllegalArgumentException```.
```
public DurationWorkoutFilter(int min, int max)
```

3. CaloriesWorkoutFilter
  *  Същата логика като горния, но по изгорени калории.
```
public CaloriesWorkoutFilter(int min, int max)
```

4. TypeWorkoutFilter
  *  Филтрира по тип (```CARDIO```, ```STRENGTH```, ```YOGA```).
  *  Ако типът е ```null```, хвърли ```IllegalArgumentException```.
```
public TypeWorkoutFilter(WorkoutType type)
```

## Подсказки
👉 За генерирането на оптималния седмичен план може да използвате класическия ```0/1 Knapsack``` алгоритъм, базиран на динамично програмиране. Всеки тип тренировка може да бъде избран най-много веднъж. Целта е да се избере подмножество от тренировки, така че общото време да не надвишава лимита, а изгорените калории да са максимални. Не е необходимо да се включват задължително всички типове тренировки – целта е максимални изгорени калории в рамките на фиксирано време.

```0/1 Knapsack``` алгоритъмът е един от важните и широко използвани алгоритми в компютърните науки и има множество приложения. С него се решават задачи като:
* Планиране на ресурси: Как да изберем най-добрите задачи/проекти/тренировки, които да се поберат в ограничен ресурс (време, бюджет, капацитет).
* Оптимизация на портфолио: Как да изберем акции/инвестиции, които максимизират печалбата при ограничен бюджет.

## Гранични случаи
| Метод / Конструктор | Случай | Очакван резултат |
|----------------------|--------|------------------|
| `FitPlanner(Collection<Workout> availableWorkouts)` | `availableWorkouts` е `null` | `IllegalArgumentException` |
| `FitPlanner(Collection<Workout> availableWorkouts)` | `availableWorkouts` е празен | всички останали методи връщат празни колекции |
| `generateOptimalWeeklyPlan(int totalMinutes)` | `totalMinutes < 0` | `IllegalArgumentException` |
| `generateOptimalWeeklyPlan(int totalMinutes)` | `totalMinutes == 0` | празен списък |
| `generateOptimalWeeklyPlan(int totalMinutes)` | няма комбинация под лимита | `OptimalPlanImpossibleException` |
| `generateOptimalWeeklyPlan(int totalMinutes)` | има няколко оптимални плана | върни произволен от тях (всички се считат за коректни) |

## Пример
```
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
    System.out.println(w);
}

// CardioWorkout[name=Cycling, duration=60, caloriesBurned=600, difficulty=5]
// CardioWorkout[name=HIIT, duration=30, caloriesBurned=400, difficulty=4]
// StrengthWorkout[name=Leg Day, duration=30, caloriesBurned=250, difficulty=2]
```

> Warning
> Планът е сортиран по изгорени калории, след това по трудност — и двете в низходящ ред.

## Пакети
Спазвайте имената на пакетите на всички по-горе описани типове, тъй като в противен случай, решението ви няма да може да бъде автоматично тествано.
```
src
└── bg.sofia.uni.fmi.mjt.fittrack
    ├── exception
    │      ├── InvalidWorkoutException.java 
    │      └── OptimalPlanImpossibleException.java 
    ├── workout
    │      ├── filter   
    │      │      ├── CaloriesWorkoutFilter.java
    │      │      ├── DurationWorkoutFilter.java
    │      │      ├── NameWorkoutFilter.java
    │      │      ├── TypeWorkoutFilter.java
    │      │      └── WorkoutFilter.java
    │      ├── CardioWorkout.java
    │      ├── StrengthWorkout.java  
    │      ├── Workout.java  
    │      ├── WorkoutType.java  
    │      └── YogaSession.java          
    ├── FitPlanner.java
    ├── FitPlannerAPI.java
    └── (...)
```

> ⚠️ **Warning**
>
> **Не променяйте по никакъв начин интерфейсите, дадени в условието.**  
> Използването на **Java Stream API** и/или **lambdas** **не е разрешено**.  
> Задачата трябва да се реши **с помощта на знанията от курса до момента**.
