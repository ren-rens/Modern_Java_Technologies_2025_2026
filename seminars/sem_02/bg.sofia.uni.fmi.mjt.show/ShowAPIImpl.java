package bg.sofia.uni.fmi.mjt.show;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class ShowAPIImpl implements ShowAPI{
    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {
        this.ergenkas = ergenkas;
        this.defaultEliminationRules = defaultEliminationRules;
    }

    @Override
    public Ergenka[] getErgenkas() {
        if (!isValid()) {
            return null;
        }
        return ergenkas;
    }

    @Override
    public void playRound(DateEvent dateEvent) {
        if (!isValid()) {
            return;
        }

        for (Ergenka ergenka : this.ergenkas) {
            ergenka.reactToDate(dateEvent);
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {
        if (!isValid()) {
            return;
        }

        if (eliminationRules == null || eliminationRules.length == 0) {
            eliminationRules = this.defaultEliminationRules;
        }

        for (EliminationRule eliminationRule : eliminationRules) {
            Ergenka[] eliminatedErgenkas = eliminationRule.eliminateErgenkas(this.ergenkas);

            int eliminatedCount = eliminatedErgenkas.length;
            if (eliminatedCount == 0) {
                continue;
            }

            int size = this.ergenkas.length;
            if (eliminatedCount == size) {
                System.out.println(eliminatedCount);

                ergenkas = null;
                ergenkas = new Ergenka[]{};
                continue;
            }

            int idx = 0;
            Ergenka[] remainingErgenkas = new Ergenka[size - eliminatedErgenkas.length];

            for (Ergenka ergenka : this.ergenkas) {
                boolean isEliminated = false;
                for (Ergenka eliminated : eliminatedErgenkas) {
                    if (ergenka.equals(eliminated)) {
                        isEliminated = true;
                        break;
                    }
                }

                if (isEliminated) {
                    continue;
                }


                remainingErgenkas[idx++] = ergenka;

            }

            this.ergenkas = remainingErgenkas;
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        ergenka.reactToDate(dateEvent);
    }

    private boolean isValid() {
        return this.ergenkas != null && this.ergenkas.length != 0;
    }

    private Ergenka[] ergenkas;
    private EliminationRule[] defaultEliminationRules;
}
