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
        int ratingChange = 0;

        if (dateEvent != null && dateEvent.getTensionLevel() != 0) {
            int bonuses = findBonuses(dateEvent);

            ratingChange = (this.humorLevel * 5) / dateEvent.getTensionLevel() +
                Math.floorDiv(this.romanceLevel, 3) + bonuses;
        }

        this.rating += ratingChange;
    }

    private int findBonuses(DateEvent dateEvent) {
        int bonuses = 0;
        int duration = dateEvent.getDuration();

        if (duration >= 30 && duration <= 90) {
            bonuses += 4;
        } else if (duration < 30) {
            bonuses -= 2;
        } else if (duration > 90) {
            bonuses -= 3;
        }

        return bonuses;
    }

    private final String name;
    private final short age;
    private final int romanceLevel;
    private final int humorLevel;
    private int rating;
    
}
