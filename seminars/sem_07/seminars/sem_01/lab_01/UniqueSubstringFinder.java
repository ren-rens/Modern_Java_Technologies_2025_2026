public class UniqueSubstringFinder {
    static int MAX_SIZE = 26;
    public static String longestUniqueSubstring(String s) {
        int stringSize = s.length();
        if (s == null || stringSize == 0) {
            return "";
        }

        int[] occurance = new int[MAX_SIZE]; //a->0 , b->1, c->2, ...

        // with sliding window
        int start = 0;
        int maxLen = 0;
        int maxStart = 0;

        for (int i = 0; i < stringSize; i ++) {
            char current = s.charAt(i);
            int currentIndex = current - 'a';

            // case: the current symbol has occured; have to slide the window until the symbol
            while (occurance[currentIndex] == 1) {
                occurance[s.charAt(start) - 'a'] = 0;
                start++;
            }

            // mark the current symbol as occured
            occurance[currentIndex] = 1;

            if (i - start + 1 > maxLen) {
                maxLen = i - start + 1;
                maxStart = start;
            }
        }

        return s.substring(maxStart, maxStart + maxLen);
    }
    public static void main(String[] args) {
        System.out.println("Unique Substring Finder:");
        System.out.println(longestUniqueSubstring("x"));
    }
}
