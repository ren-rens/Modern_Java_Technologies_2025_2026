class Solution {
  
    public int lengthOfLastWord(String s) {
        int lastWord = 0;
        int currWord = 0;
        int size = s.length();

        for (int i = 0; i < size; i++) {
            if (s.charAt(i) == ' ') {
                lastWord = (currWord != 0 ? currWord : lastWord);
                currWord = 0;

                continue;
            }

            // the symbol is not a whitespace -> it is a symbol of a word
            currWord++;
        }

        lastWord = (currWord != 0 ? currWord : lastWord);
        return lastWord;
    }
  
}
