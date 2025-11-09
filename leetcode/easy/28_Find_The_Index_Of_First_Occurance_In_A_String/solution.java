class Solution {
  
    public int strStr(String haystack, String needle) {
        int haystackSize = haystack.length();
        int needleSize = needle.length();

        if (haystackSize < needleSize) {
            return -1;
        }

        // Only iterate until there's enough room for needle
        for (int i = 0; i <= haystackSize - needleSize; i++) {
            boolean isFound = true;
            
            for (int j = 0; j < needleSize; j++) {
                if (needle.charAt(j) != haystack.charAt(i + j)) {
                    isFound = false;
                    break;
                }
            }

            if (isFound) {
                return i;
            }
        }

        return -1;
    }
  
}
