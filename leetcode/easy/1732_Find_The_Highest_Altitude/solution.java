class Solution {

    public int largestAltitude(int[] gain) {
        try {
            if (!isValid(gain)) {
                return 0;
            }
            
            int[] altitudes = getAllAltitudes(gain);
            return getHighestAltitude(altitudes);
        } catch(IllegalArgumentException e) {
            return 0;
        }
        
    }

    private boolean isValid(int[] gain) {
        return gain.length >= 1 && gain.length <= 100;
    }

    private int[] getAllAltitudes(int[] gain) {
        int size = gain.length;
        int[] altitudes = new int[size + 1];
        altitudes[0] = 0;

        for (int i = 0; i < size; i++) {
            if (gain[i] < -100 || gain[i] > 100) {
                throw new IllegalArgumentException("Gain must be between -100 and 100");
            }

            altitudes[i + 1] = altitudes[i] + gain[i];
        }

        return altitudes;
    }

    private int getHighestAltitude(int[] altitudes) {
        int highest = altitudes[0]; // always zero
        int size = altitudes.length;

        for (int i = 1; i < size; i++) {
            if (altitudes[i] <= highest) {
                continue;
            }
                        
            highest = altitudes[i];
        }

        return highest;
    }
}
