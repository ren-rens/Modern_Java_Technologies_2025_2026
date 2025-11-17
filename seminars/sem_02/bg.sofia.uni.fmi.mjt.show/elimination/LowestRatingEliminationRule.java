package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class LowestRatingEliminationRule implements EliminationRule {

    public LowestRatingEliminationRule() {
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0) {
            return new Ergenka[0];
        }

        int nonNullCount = notNullElements(ergenkas);
        if (nonNullCount == 0) {
            return new Ergenka[0];
        }

        // Find lowest rating
        int lowestRating = findLowestRating(ergenkas);

        // Count how many will REMAIN (those NOT having lowest rating)
        int remainingCount = findRemaningErgenkasCount(ergenkas, lowestRating);
        if (remainingCount == 0) {
            return new Ergenka[0];
        }

        // Build array of REMAINING ergenkas
        return findRemaningErgenkas(ergenkas, lowestRating, remainingCount);
    }

    private int notNullElements(Ergenka[] ergenkas) {
        int nonNullCount = 0;
        for (Ergenka ergenka : ergenkas) {
            if (ergenka != null) {
                nonNullCount++;
            }
        }

        return nonNullCount;
    }

    private int findLowestRating(Ergenka[] ergenkas) {
        int minRating = Integer.MAX_VALUE;

        for (Ergenka ergenka : ergenkas) {
            if (ergenka == null) {
                continue;
            }

            int rating = ergenka.getRating();
            if (rating < minRating) {
                minRating = rating;
            }
        }

        return minRating;
    }

    private int findRemaningErgenkasCount(Ergenka[] ergenkas, int lowestRating) {
        int remainingCount = 0;
        for (Ergenka ergenka : ergenkas) {
            if (ergenka == null) {
                continue;
            } else if (ergenka.getRating() != lowestRating) {
                remainingCount++; // keep higher rated ergenkis
            }
        }

        return remainingCount;
    }

    private Ergenka[] findRemaningErgenkas(Ergenka[] ergenkas, int lowestRating, int remainingCount) {
        Ergenka[] remaining = new Ergenka[remainingCount];
        int idx = 0;

        for (Ergenka ergenka : ergenkas) {
            if (idx == remainingCount) {
                break;
            }

            if (ergenka == null) {
                continue;
            }

            if (ergenka.getRating() > lowestRating) {
                remaining[idx++] = ergenka;
            }
        }

        return remaining;
    }
    
}
