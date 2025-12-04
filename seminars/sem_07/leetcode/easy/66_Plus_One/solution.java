class Solution {
  
    public int[] plusOne(int[] digits) {
        int size = digits.length;
        boolean plusOne = true;

        int[] result = new int[size + 1];

        for (int i = size - 1; i >= 0; i--) {
            if (!plusOne) {
                break;
            }

            if (digits[i] == 9) {
                digits[i] = 0;
                continue;
            }

            digits[i] += 1;
            plusOne = false;
        }

        if (plusOne) {
            // new size
            result = new int[size + 1];
            result[0] = 1;
          
            for (int i = 0; i < size; i++) {
                result[i + 1] = digits[i];
            }
          
        } else {
            result = digits;
        }

        return result;
      
    }
}
