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
        int ratingChange = 0; // stays like this if dateEvent is null or tension == 0

        if (dateEvent != null && dateEvent.getTensionLevel() != 0) {
            // dateEvent not null can find bonuses
            int bonuses = findBonuses(dateEvent);

            ratingChange = (this.romanceLevel * 7) / dateEvent.getTensionLevel() +
                    Math.floorDiv(this.humorLevel, 3) + bonuses;
        }

        this.rating += ratingChange;
    }

    private int findBonuses(DateEvent dateEvent) {
        int bonuses = 0;

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

        return bonuses;
    }

    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    private int rating;
    private final String favoriteDateLocation;

}
