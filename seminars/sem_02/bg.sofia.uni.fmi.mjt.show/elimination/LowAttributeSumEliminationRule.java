package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowAttributeSumEliminationRule implements EliminationRule{
    public LowAttributeSumEliminationRule(int threshold) {
        this.threshold = threshold;
    }
    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        Ergenka[] eliminateErgenkas = new Ergenka[ergenkas.length];
        int idx = 0;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka.getHumorLevel() + ergenka.getRomanceLevel() < this.threshold) {
                eliminateErgenkas[idx++] = ergenka;
            }
        }

        return eliminateErgenkas;
    }

    private int threshold;
}
