import bg.sofia.uni.fmi.mjt.show.ShowAPI;
import bg.sofia.uni.fmi.mjt.show.ShowAPIImpl;
import bg.sofia.uni.fmi.mjt.show.elimination.LowestRatingEliminationRule;
import bg.sofia.uni.fmi.mjt.show.elimination.PublicVoteEliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;
import bg.sofia.uni.fmi.mjt.show.date.DateEvent;
import bg.sofia.uni.fmi.mjt.show.elimination.EliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.HumorousErgenka;
import bg.sofia.uni.fmi.mjt.show.ergenka.RomanticErgenka;

import javax.naming.PartialResultException;

void main() {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    IO.println(String.format("Hello and welcome!"));

    Ergenka humorousErgenka = new HumorousErgenka("Kristina", (short)16, 8, 6, 7);
    Ergenka romanticErgenka = new RomanticErgenka("Nataliya", (short)23, 9, 9, 7, "London");
    Ergenka[] ergenkas = new Ergenka[] {humorousErgenka, romanticErgenka};

    String[] votes = new String[]{"Nataliya", "Nataliya"};
    EliminationRule[] defaultEliminationRules = new EliminationRule[]{new LowestRatingEliminationRule()/*, new PublicVoteEliminationRule(votes)*/};

    ShowAPI api = new ShowAPIImpl(ergenkas, defaultEliminationRules);
    Ergenka[] currErgenkas = api.getErgenkas();
    for (Ergenka curr : currErgenkas) {
        System.out.println(curr.getName());
    }

    DateEvent date = new DateEvent("London", 7, 45);

    api.organizeDate(romanticErgenka, date);

    currErgenkas = api.getErgenkas();

    api.playRound(date);
    currErgenkas = api.getErgenkas();
    for (Ergenka curr : currErgenkas) {
        System.out.println(curr.getName());
        System.out.println(curr.getRating());
    }

    api.eliminateErgenkas(null);
    api.eliminateErgenkas(null);

    currErgenkas = api.getErgenkas();

    if(currErgenkas == null) {
        System.out.println("null ptr");
    }
    else {
        for (Ergenka curr : currErgenkas) {
            System.out.println(curr.getName());
            System.out.println(curr.getRating());
        }
    }

}
