package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Comparator;

public class PriorityComparator<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(Event o1, Event o2) {
        return Integer.compare(o1.getPriority(), o2.getPriority());
    }

}
