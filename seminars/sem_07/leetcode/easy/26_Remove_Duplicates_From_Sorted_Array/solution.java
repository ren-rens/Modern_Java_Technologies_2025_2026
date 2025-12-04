class Solution {
    public int removeDuplicates(int[] nums) {
        int size = nums.length;
        Set<Integer> set = new HashSet<>();
        int idx = 0;

        for (int i = 0; i < size - idx; i++) {
            if (set.contains(nums[i])) {
                // should be deleted
                
                i--;
                idx++;
                // shift all
                for (int j = i + 1; j < size; j++) {
                    nums[j - 1] = nums[j];
                }
            }
            else {
                set.add(nums[i]);
            }
        }

        return set.size();
    }
}
