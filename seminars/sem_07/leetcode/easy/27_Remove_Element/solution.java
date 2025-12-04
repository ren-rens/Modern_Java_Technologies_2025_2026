class Solution {
  
    public int removeElement(int[] nums, int val) {
        Set<Integer> set = new HashSet<>();
        int size = nums.length;
        int idx = 0;
        int k = 0;

        for (int i = 0; i < size - idx; i++) {
            if (nums[i] == val) {
                for (int j = i + 1; j < size; j++) {
                    nums[j - 1] = nums[j];
                }
                           
                idx++;
                i--;
            }

            else { 
                k++;
            }
        }

        return k;
    }
  
}
