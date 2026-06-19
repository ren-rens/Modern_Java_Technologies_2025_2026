class Solution {
    public String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();

        int indexA = a.length() - 1;
        int indexB = b.length() - 1;
        int plusOne = 0;

        while (indexA >= 0 || indexB >= 0 || plusOne != 0) {
            int currSum = plusOne;

            if (indexA >= 0) {
                currSum += a.charAt(indexA--) - '0';
            }

            if (indexB >= 0) {
                currSum += b.charAt(indexB--) - '0';
            }

            result.append(currSum % 2);
            plusOne = currSum / 2;
        }

        return result.reverse().toString();
    }
}
