package bg.sofia.uni.fmi.mjt.show.elimination;

import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class PublicVoteEliminationRule implements EliminationRule{
    public PublicVoteEliminationRule(String[] votes) {
        this.votes = votes;
    }

    @Override
    public Ergenka[] eliminateErgenkas(Ergenka[] ergenkas) {
        /*
        votesToErgenkaCount:
        idx -> ergenkas[i] (Ergenka's name)
        count -> the count of the votes for this Ergenka
         */

        int ergenkasSize = ergenkas.length;
        int[] votesToErgenkaCount = new int[ergenkasSize];

        for (String vote : votes) {
            for (int j = 0; j < ergenkasSize; j++) {
                if (vote.equals(ergenkas[j].getName())) {
                    votesToErgenkaCount[j]++;
                    break;
                }
            }
        }

        for (int i = 0; i < ergenkasSize; i++) {
            if (votesToErgenkaCount[i] == 0.5 * ergenkasSize + 1) {
                return new Ergenka[] {ergenkas[i]};
            }
        }

        return new Ergenka[] {};
    }

    private String[] votes;
}
