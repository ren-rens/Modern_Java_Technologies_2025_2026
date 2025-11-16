package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class HumorousErgenka implements Ergenka {
    public HumorousErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {
        this.name = name;
        this.age = age;
        this.romanceLevel = romanceLevel;
        this.humorLevel = humorLevel;
        this.rating = rating;
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
            return;  // Do nothing if dateEvent is null
        }

        int bonuses = 0;

        int duration = dateEvent.getDuration();
        if (duration >= 30 && duration <= 90) {
            bonuses += 4;
        } else if (duration < 30) {
            bonuses -= 2;
        } else {
            bonuses -= 3;
        }

        int dateTension = dateEvent.getTensionLevel();

        // Protect against division by zero
        int ratingChange;
        if (dateTension == 0) {
            //ratingChange = 0;
            return;
        } else {
            ratingChange = (this.humorLevel * 5) / dateTension +
                Math.floorDiv(this.romanceLevel, 3) + bonuses;
        }

        this.rating += ratingChange;
    }

    private String name;
    private short age;
    private int romanceLevel;
    private int humorLevel;
    private int rating;
}
