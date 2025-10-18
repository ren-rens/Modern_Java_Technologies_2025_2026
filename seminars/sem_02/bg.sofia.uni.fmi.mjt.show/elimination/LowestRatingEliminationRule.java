package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowestRatingEliminationRule implements EliminationRule{
    public LowestRatingEliminationRule() {
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        int minRating = Integer.MAX_VALUE;
        int size = ergenkas.length;

        int[] ergenkaIndex = new int[size];
        for (int j = 0; j < size; j++) {
            ergenkaIndex[j] = -1;
        }

        int idx = 0;

        for (int i = 0; i < size; i++) {
            int currRating = ergenkas[i].getRating();
            if (currRating < minRating) {
                minRating = currRating;
                for (int j = 0; j < idx; j++) {
                    ergenkaIndex[j] = -1;
                }

                idx = 0;
                ergenkaIndex[idx++] = i;
            }
            else if (currRating == minRating) {
                ergenkaIndex[idx++] = i;
            }
        }

        Ergenka[] eliminateErgenkas = new Ergenka[idx];
        idx = 0;

        for (int i = 0; i < size; i++) {
            if (ergenkaIndex[i] == -1) {
                break;
            }
            eliminateErgenkas[idx++] = ergenkas[ergenkaIndex[i]];
        }

        return eliminateErgenkas;
    }
}
