package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class PublicVoteEliminationRule implements EliminationRule {
    public PublicVoteEliminationRule(String[] votes) {
        this.votes = votes;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        if (ergenkas == null || ergenkas.length == 0 || votes == null || votes.length == 0) {
            return (ergenkas == null) ? new Ergenka[0] : ergenkas;
        }

        int ergenkasSize = ergenkas.length;

        // Count votes for each ergenka
        int[] votesToErgenkaCount = countVotesForEachErgenka(ergenkas, ergenkasSize);

        // Calculate required votes for elimination (50% + 1)
        int requiredVotes = votes.length / 2 + 1;

        // Find if any ergenka has 50% + 1 votes (to be eliminated)
        int eliminatedIndex = findIdxOfErgenkaToEliminate(votesToErgenkaCount, requiredVotes, ergenkasSize);

        // If no one has 50%+1 votes, return all (no elimination)
        if (eliminatedIndex == -1) {
            return ergenkas;
        }

        // Build array of REMAINING ergenkas (all except the eliminated one)
        return findRemainingErgenkas(ergenkas, ergenkasSize, eliminatedIndex);
    }

    private int[] countVotesForEachErgenka(Ergenka[] ergenkas, int ergenkasSize) {
        int[] votesToErgenkaCount = new int[ergenkasSize];

        for (String vote : votes) {
            if (vote == null) {
                continue;
            }

            for (int j = 0; j < ergenkasSize; j++) {
                if (ergenkas[j] == null) {
                    continue;
                }

                if (vote.equals(ergenkas[j].getName())) {
                    votesToErgenkaCount[j]++;
                    break;
                }
            }
        }

        return votesToErgenkaCount;
    }

    private int findIdxOfErgenkaToEliminate(int[] votesToErgenkaCount, int requiredVotes, int ergenkasSize) {
        int eliminatedIndex = -1;
        for (int i = 0; i < ergenkasSize; i++) {
            if (votesToErgenkaCount[i] >= requiredVotes) {
                eliminatedIndex = i;
                break;
            }
        }

        return eliminatedIndex;
    }

    private Ergenka[] findRemainingErgenkas(Ergenka[] ergenkas, int ergenkasSize, int eliminatedIndex) {
        Ergenka[] remaining = new Ergenka[ergenkasSize - 1];
        int idx = 0;

        for (int i = 0; i < ergenkasSize; i++) {
            if (i != eliminatedIndex && ergenkas[i] != null) {
                remaining[idx++] = ergenkas[i];
            }
        }

        return remaining;
    }

    private String[] votes;
}
