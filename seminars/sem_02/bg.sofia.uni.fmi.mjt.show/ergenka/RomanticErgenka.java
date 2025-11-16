package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class RomanticErgenka implements Ergenka {

    public RomanticErgenka(String name, short age, int romanceLevel, int humorLevel, int rating,
                           String favoriteDateLocation) {
        this.name = name;
        this.age = age;
        this.romanceLevel = romanceLevel;
        this.humorLevel = humorLevel;
        this.rating = rating;
        this.favoriteDateLocation = favoriteDateLocation;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public short getAge() {
        return this.age;
    }

    @Override
    public int getRomanceLevel() {
        return this.romanceLevel;
    }

    @Override
    public int getHumorLevel() {
        return this.humorLevel;
    }

    @Override
    public int getRating() {
        return this.rating;
    }

    @Override
    public void reactToDate(DateEvent dateEvent) {
        if (dateEvent == null) {
            return;
        }

        int bonuses = 0;
        /*
        +5 точки, ако локацията на срещата е любима на участника (case insensitive)
        -3 точки, ако срещата е прекалено кратка (<30 мин)
        -2 точки, ако срещата е прекалено дълга (>120 мин)
         */

        String location = dateEvent.getLocation();
        if (location != null && this.favoriteDateLocation != null &&
            location.equalsIgnoreCase(this.favoriteDateLocation)) {
            bonuses += 5;
        }

        int duration = dateEvent.getDuration();
        if (duration < 30) {
            bonuses -= 3;
        }
        else if (duration > 120) {
            bonuses -= 2;
        }

        int dateTension = dateEvent.getTensionLevel();

        int ratingChange;
        if (dateTension == 0) {
            return;
            //ratingChange = 0;  // not sure
        } else {
            ratingChange = (this.romanceLevel * 7) / dateTension +
                Math.floorDiv(this.humorLevel, 3) + bonuses;
        }

        this.rating += ratingChange;
    }

    private String name;
    private short age;
    private int romanceLevel;
    private int humorLevel;
    private int rating;
    private String favoriteDateLocation;

}
