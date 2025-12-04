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

public void print(ShowAPI api) {
    System.out.println("Ergenkis in the show right now:");
    Ergenka[] currErgenkas = api.getErgenkas();
    for (Ergenka curr : currErgenkas) {
        if (curr == null) {
            System.out.println("Null ergenka");
            continue;
        }

        System.out.println(curr.getName());
        System.out.println(curr.getRating());
    }
}

void main() {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    IO.println(String.format("Hello and welcome, ergenkas!\nGood luck!"));

    Ergenka humorousErgenka = new HumorousErgenka("Kristina", (short)16, 0, 0, 2);
    Ergenka romanticErgenka = new RomanticErgenka("Nataliya", (short)23, 9, 9, 7, "London");
    Ergenka[] ergenkas = new Ergenka[] {humorousErgenka, romanticErgenka, null};

    String[] votes = new String[]{"Nataliya", "Nataliya"};
    EliminationRule[] defaultEliminationRules = new EliminationRule[]{new LowestRatingEliminationRule()/*, new PublicVoteEliminationRule(votes)*/};

    ShowAPI api = new ShowAPIImpl(ergenkas, defaultEliminationRules);
    print(api);

    api.organizeDate(humorousErgenka, new DateEvent("", 30, 121));

    //api.eliminateErgenkas(null);
    print(api);

}
