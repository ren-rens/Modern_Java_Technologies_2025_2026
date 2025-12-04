package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Comparator;

public class PriorityComparator<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(Event o1, Event o2) {
        int priorityComparison = Integer.compare(o1.getPriority(), o2.getPriority());

        return priorityComparison != 0 ? priorityComparison : o1.getTimestamp().compareTo(o2.getTimestamp());
    }

}
