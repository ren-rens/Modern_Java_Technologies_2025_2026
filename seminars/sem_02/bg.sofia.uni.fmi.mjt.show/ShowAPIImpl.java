package bg.sofia.uni.fmi.mjt.show;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.elimination.LowestRatingEliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;

public class ShowAPIImpl implements ShowAPI {

    public ShowAPIImpl(Ergenka[] ergenkas, EliminationRule[] defaultEliminationRules) {
        this.ergenkas = ergenkas;

        if (defaultEliminationRules == null || defaultEliminationRules.length == 0) {
            this.defaultEliminationRules = new EliminationRule[] { new LowestRatingEliminationRule() };
        } else {
            this.defaultEliminationRules = defaultEliminationRules;
        }
    }

    @Override
    public Ergenka[] getErgenkas() {
        return ergenkas;
    }

    @Override
    public void playRound(DateEvent dateEvent) {
        if (ergenkas == null) {
            return;  // Do nothing if no ergenkas
        }

        // If dateEvent is null, use a default DateEvent
        if (dateEvent == null) {
            //dateEvent = new DateEvent("Default", 5, 60);
            dateEvent = new DateEvent("", 0, 0);
        }

        for (Ergenka ergenka : this.ergenkas) {
            if (ergenka == null) {
                continue;
            }

            ergenka.reactToDate(dateEvent);
        }
    }

    @Override
    public void eliminateErgenkas(EliminationRule[] eliminationRules) {
        if (ergenkas == null) {
            return;
        }

        if (eliminationRules == null || eliminationRules.length == 0) {
            eliminationRules = this.defaultEliminationRules;
        }

        // If still no rules to apply, return
        if (eliminationRules == null || eliminationRules.length == 0) {
            return;
        }

        // Each rule returns the REMAINING ergenkas after elimination
        for (EliminationRule eliminationRule : eliminationRules) {
            if (eliminationRule == null) {
                continue;
            }

            // The rule returns the remaining ergenkas directly
            this.ergenkas = eliminationRule.eliminateErgenkas(this.ergenkas);

//            if (this.ergenkas == null) {
//                this.ergenkas = new Ergenka[0];
//            }
        }
    }

    @Override
    public void organizeDate(Ergenka ergenka, DateEvent dateEvent) {
        if (ergenka == null) {
            return;
        }

        ergenka.reactToDate(dateEvent);
    }


    private Ergenka[] ergenkas;
    private EliminationRule[] defaultEliminationRules;

}
