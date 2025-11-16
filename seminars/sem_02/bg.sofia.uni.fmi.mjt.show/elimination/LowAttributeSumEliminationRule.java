package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowAttributeSumEliminationRule implements EliminationRule {

    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0) {
            return (ergenkas == null) ? new Ergenka[0] : ergenkas;
        }

        // Count how many will REMAIN (those meeting the threshold)
        int remainingCount = countOfRemainingErgenkas(ergenkas);

        // Build array of REMAINING ergenkas
        return findRemainingErgenkas(ergenkas, remainingCount);
    }

    private int countOfRemainingErgenkas(Ergenka[] ergenkas) {
        int remainingCount = 0;
        for (Ergenka ergenka : ergenkas) {
            if (ergenka == null) {
                continue;
            }

            if (ergenka.getHumorLevel() + ergenka.getRomanceLevel() >= this.threshold) {
                remainingCount++;
            }
        }

        return  remainingCount;
    }

    private Ergenka[] findRemainingErgenkas(Ergenka[] ergenkas, int remainingCount) {
        Ergenka[] remaining = new Ergenka[remainingCount];
        int idx = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka == null) {
                continue;
            }

            if (ergenka.getHumorLevel() + ergenka.getRomanceLevel() >= this.threshold) {
                remaining[idx++] = ergenka;
            }
        }

        return remaining;
    }

    private int threshold;

}
