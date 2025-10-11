public class TaskDistributor {
    public static int minDifference(int[] tasks) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }

        // the idea comes from dynamic programming
        int totalSum = 0;
        for (int current : tasks) {
            totalSum += current;

        }
        // we are looking for difference: totalSum - 2 * bestPossibleSum
        int target = totalSum / 2;
        boolean[] possible = new boolean[target + 1]; // is there a subset of tasks that makes sum = idx
        possible[0] = true; // base

        for (int current : tasks) {
            for (int i = target; i >= current; i--) {
                if (possible[i - current]) {
                    possible[i] = true;
                }
            }
        }

        // searching for biggest sum <= totalSum that is possible
        int best = 0;
        for (int i = target; i >= 0; i--) {
            if (possible[i]) {
                best = i;
                break;
            }
        }

        return totalSum - 2 * best;
    }

    public static void main(String[] args) {
        System.out.println(minDifference(new int[]{1, 2, 3, 4, 5}));   // 1
        System.out.println(minDifference(new int[]{10, 20, 15, 5}));  // 0
        System.out.println(minDifference(new int[]{7, 3, 2, 1, 5, 4})); // 0
        System.out.println(minDifference(new int[]{9, 1, 1, 1}));     // 6
        System.out.println(minDifference(new int[]{}));            // 0
        System.out.println(minDifference(new int[]{120}));         // 120
        System.out.println(minDifference(new int[]{30, 30}));       // 0
    }
}
