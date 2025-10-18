package bg.sofia.uni.fmi.mjt.show.ergenka;

import bg.sofia.uni.fmi.mjt.show.date.DateEvent;

public class HumorousErgenka implements Ergenka{
    public HumorousErgenka(String name, short age, int romanceLevel, int humorLevel, int rating) {        this.name = name;
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
        int bonuses = 0;
        /*
        +4 точки, ако срещата е разумно дълга (>= 30 мин и <= 90 мин)
        -2 точки, ако срещата е прекалено кратка (<30 мин)
        -3 точки, ако срещата е прекалено дълга (>90 мин)
         */

        int duration = dateEvent.getDuration();
        if (duration >= 30 && duration <= 90) {
            bonuses += 4;
        }
        if (duration < 30) {
            bonuses -= 2;
        }
        if (duration > 90) {
            bonuses -= 3;
        }

        int dateTension = dateEvent.getTensionLevel();

        this.rating = (this.humorLevel * 5) / dateTension  + (int)(this.romanceLevel / 3) + bonuses;
    }

    private String name;
    private short age;
    private int romanceLevel;
    private int humorLevel;
    private int rating;
}
