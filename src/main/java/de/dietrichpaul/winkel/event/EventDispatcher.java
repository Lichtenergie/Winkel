package de.dietrichpaul.winkel.event;

import de.dietrichpaul.winkel.util.MathUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class EventDispatcher {

    private static final Function<Class<?>,  Map<Object, Subscription<?>>> MAPPING_FUNCTION = key -> new ConcurrentHashMap<>();
    private static final Comparator<Subscription<?>> PRIORITY_ORDER = Comparator.comparingInt(subscription -> MathUtil.conjugate(subscription.getPrioritySupplier().getAsInt()));

    private Map<Class<?>, Map<Object, Subscription<?>>> subscriptions = new HashMap<>();

    public <L> void subscribe(Class<L> listenerType, L listener) {
        subscribe(listenerType, new Subscription<L>(listener));
    }

    public <L> void subscribe(Class<L> listenerType, L listener, int priority) {
        subscribe(listenerType, new Subscription<L>(listener, priority));
    }

    public <L> void subscribe(Class<L> listenerType, L listener, IntSupplier priority) {
        subscribe(listenerType, new Subscription<L>(listener, priority));
    }

    public <L> void subscribe(Class<L> listenerType, Subscription<L> subscription) {
        this.subscriptions.computeIfAbsent(listenerType, MAPPING_FUNCTION).put(subscription.getListenerType(), subscription);
    }

    public <L> void unsubscribe(Class<L> listenerType, L listener) {
        this.subscriptions.get(listenerType).remove(listener);
    }

    @SuppressWarnings("unchecked")
    public <L, E extends AbstractEvent<L>> E post(E event) {
        if (event.isAbort())
            return event;

        Map<Object, Subscription<?>> subscriptions = this.subscriptions.get(event.getListenerType());
        if (subscriptions == null || subscriptions.isEmpty())
            return event;

        List<Subscription<?>> subscriptionList = new ArrayList<>(subscriptions.values());

        subscriptionList.sort(PRIORITY_ORDER);

        for (Subscription<?> subscription : subscriptionList) {
            event.getEventExecutor().execute((L) subscription.getListenerType());
            if (event.isAbort())
                return event;
        }

        return event;
    }

}
