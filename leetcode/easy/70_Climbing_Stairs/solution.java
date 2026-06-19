class Solution {
  
    public int climbStairs(int n) {
        int[] buff = new int[46];

        return climbStairsBuff(n, buff);
    }

    private int climbStairsBuff(int n, int[] buff) {
        if (n < 0) {
            return 0;
        }

        if (n == 0 || n == 1 || n == 2) {
            buff[n] = n;

            return n;
        }

        if (buff[n] != 0) {
            return buff[n];
        }

        buff[n - 1] = climbStairsBuff(n - 1, buff);
        buff[n - 2] = climbStairsBuff(n - 2, buff);

        return buff[n - 1] + buff[n - 2];
    }
  
}
