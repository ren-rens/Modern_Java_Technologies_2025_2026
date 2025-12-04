package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {

    public EventBusImpl() {
        this.subscriptions = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    /**
     * Subscribes the given subscriber to the given event type.
     * If the same subscriber is already subscribed to the given event type, the method
     * should do nothing (no duplicate subscriptions).
     *
     * @param eventType  the type of event to subscribe to
     * @param subscriber the subscriber to subscribe
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the subscriber is null
     */
    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Invalid evenType OR subscriber in EventBusImpl subscribe!");
        }

        Collection<Subscriber<?>> subs = subscriptions.computeIfAbsent(eventType, k -> new HashSet<>());

        if (subs.contains(subscriber)) {
            return;
        }

        subs.add(subscriber);
    }

    /**
     * Unsubscribes the given subscriber from the given event type.
     *
     * @param eventType  the type of event to unsubscribe from
     * @param subscriber the subscriber to unsubscribe
     * @throws IllegalArgumentException     if the event type is null
     * @throws IllegalArgumentException     if the subscriber is null
     * @throws MissingSubscriptionException if the subscriber is not subscribed to the event type
     */
    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Invalid evenType OR subscriber in EventBusImpl unsubscribe!");
        }

        // if subscriber not in event -> MissingSubscriptionException
        Collection<Subscriber<?>> subs = subscriptions.get(eventType);
        if (subs == null || !subs.contains(subscriber)) {
            throw new MissingSubscriptionException("Invalid subscriber give to unsubscribe event!");
        }

        subs.remove(subscriber);
        subscriptions.replace(eventType, subs);
    }

    /**
     * Publishes the given event to all subscribers of the event type.
     *
     * @param event the event to publish
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Invalid event to publish in EventBusImpl!");
        }

        Class<T> eventType = (Class<T>) event.getClass();

        for (Map.Entry<Class<? extends Event<?>>, Collection<Subscriber<?>>> entry : subscriptions.entrySet()) {
            if (!entry.getKey().equals(eventType)) {
                continue;
            }

            for (Subscriber<?> subscriber : entry.getValue()) {
                @SuppressWarnings("unchecked")
                // the subscriber is subscribed for this type of event -> it should be safe to cast
                Subscriber<T> typedSubscriber = (Subscriber<T>) subscriber;

                typedSubscriber.onEvent(event);
            }
        }

        addEventToLogs(event, eventType);
    }

    private <T extends Event<?>> void addEventToLogs(T event, Class<T> eventType) {
        Collection<Event<?>> events = eventLogs.get(eventType);

        if (events == null) {
            events = new ArrayList<>();
            eventLogs.put(eventType, events);
        }

        if (!events.contains(event)) {
            events.add(event);
        }
    }

    /**
     * Clears all subscribers and event logs.
     */
    @Override
    public void clear() {
        subscriptions.clear();
        eventLogs.clear();
    }

    /**
     * Returns all events of the given event type that occurred between the given timestamps. If
     * {@code from} and {@code to} are equal the returned collection is empty.
     * <p> {@code from} - inclusive, {@code to} - exclusive. </p>
     *
     * @param eventType the type of event to get
     * @param from      the start timestamp (inclusive)
     * @param to        the end timestamp (exclusive)
     * @return an unmodifiable collection of events of the given event type that occurred between
     * the given timestamps
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the start timestamp is null
     * @throws IllegalArgumentException if the end timestamp is null
     */
    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Invalid arguments given to getEventLogs!");
        }

        if (from.equals(to)) {
            return List.of();
        }

        Collection<Event<?>> events = eventLogs.get(eventType);

        if (events == null) {
            return List.of();
        }

        // for adding the events between from and to
        List<Event<?>> filteredEvents = new ArrayList<>();

        for (Event<?> event : events) {
            Instant timestamp = event.getTimestamp();

            if (!timestamp.isBefore(from) && timestamp.isBefore(to)) {
                filteredEvents.add(event);
            }
        }

        filteredEvents.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp())); // sort by timestamp

        return Collections.unmodifiableCollection(filteredEvents);
    }

    /**
     * Returns all subscribers for the given event type in an unmodifiable collection. If there are
     * no subscribers for the event type, the method returns an empty unmodifiable collection.
     *
     * @param eventType the type of event to get subscribers for
     * @return an unmodifiable collection of subscribers for the given event type
     * @throws IllegalArgumentException if the event type is null
     */
    @SuppressWarnings("checkstyle:Indentation")
    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Invalid event type in getSubscribersForEvent!");
        }

        Collection<Subscriber<?>> subs = subscriptions.get(eventType);

        return subs == null ? Collections.emptySet() : Collections.unmodifiableCollection(new HashSet<>(subs));
    }

    private Map<Class<? extends Event<?>>, Collection<Subscriber<?>>> subscriptions; // type of event -> subscriber
    private Map<Class<? extends Event<?>>, Collection<Event<?>>> eventLogs; // published events

}
