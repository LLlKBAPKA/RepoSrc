package org.excellent.client.api.events.orbit;

import org.excellent.client.Excellent;
import org.excellent.client.api.events.orbit.listeners.ArrayBackedImmutableList;
import org.excellent.client.api.events.orbit.listeners.IListener;
import org.excellent.client.api.events.orbit.listeners.LambdaListener;
import org.excellent.client.api.events.orbit.listeners.ListOperations;
import org.excellent.client.managers.other.notification.NotificationType;
import org.excellent.client.utils.render.color.ColorFormatting;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Norbit's reimplementation of Orbit's {@link IEventBus}.
 */
public class EventBus implements IEventBus {
    private record LambdaFactoryInfo(String packagePrefix,
                                     LambdaListener.Factory factory) {
    }

    private final Map<Object, List<IListener>> listenerCache;
    private final Map<Class<?>, List<IListener>> staticListenerCache;

    private final Map<Class<?>, List<IListener>> listenerMap;
    private final Supplier<List<IListener>> listenerListFactory;

    private final List<LambdaFactoryInfo> lambdaFactoryInfos = new CopyOnWriteArrayList<>();

    public EventBus(Map<Object, List<IListener>> listenerCache, Map<Class<?>, List<IListener>> staticListenerCache, Map<Class<?>, List<IListener>> listenerMap, Supplier<List<IListener>> listenerListFactory) {
        this.listenerCache = listenerCache;
        this.staticListenerCache = staticListenerCache;
        this.listenerMap = listenerMap;
        this.listenerListFactory = listenerListFactory;
    }

    public static EventBus threadSafe() {
        return new EventBus(Collections.synchronizedMap(new IdentityHashMap<>()), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), CopyOnWriteArrayList::new);
    }

    public static EventBus threadUnsafe() {
        return new EventBus(new IdentityHashMap<>(), new IdentityHashMap<>(), new IdentityHashMap<>(), CopyOnWriteArrayList::new);
    }

    @Override
    public void registerLambdaFactory(String packagePrefix, LambdaListener.Factory factory) {
        lambdaFactoryInfos.add(new LambdaFactoryInfo(packagePrefix, factory));
    }

    @Override
    public boolean isListening(Class<?> eventClass) {
        List<IListener> listeners = listenerMap.get(eventClass);
        return listeners != null && !listeners.isEmpty();
    }

    /**
     * @return whether the {@link IListener} is currently subscribed to the event bus.
     * @since 1.2.0
     */
    public boolean isSubscribed(IListener listener) {
        List<IListener> listeners = listenerMap.get(listener.getTarget());
        return listeners != null && ListOperations.contains(listeners, listener);
    }

    /**
     * @return whether the {@link Object} currently has its listeners subscribed to the event bus.
     * @since 1.2.0
     */
    public boolean isSubscribed(Object object) {
        return listenerCache.containsKey(object);
    }

    /**
     * @return whether the {@link Class} currently has all of its immediate (ignoring static listeners inherited from
     * superclasses) static listeners subscribed to the event bus.
     * @since 1.2.0
     */
    public boolean isSubscribed(Class<?> staticListener) {
        List<IListener> listeners = staticListenerCache.get(staticListener);
        for (IListener listener : listeners) {
            if (listener instanceof LambdaListener lambdaListener) {
                if (lambdaListener.owner != staticListener)
                    break; // getListeners(List<IListener>, Class<?>, Object, boolean) implicitly orders based on inheritance
                if (!this.isSubscribed(listener)) return true;
            }
        }
        return true;
    }

    /**
     * @return whether the {@link Class} or any of its superclasses have *any* of its static listeners subscribed to the event bus.
     * @since 1.2.0
     */
    public boolean areAnySubscribed(Class<?> staticListener) {
        List<IListener> listeners = staticListenerCache.get(staticListener);
        if (listeners == null) return false;
        for (IListener listener : listeners) {
            if (this.isSubscribed(listener)) return true;
        }
        return false;
    }

    /**
     * @return whether the {@link Class} or any of its superclasses have *all* of its static listeners subscribed to the event bus.
     * @since 1.2.0
     */
    public boolean areAllSubscribed(Class<?> staticListener) {
        List<IListener> listeners = staticListenerCache.get(staticListener);
        if (listeners == null) return false;
        for (IListener listener : listeners) {
            if (!this.isSubscribed(listener)) return false;
        }
        return true;
    }

    @Override
    public <T> T post(T event) {
        List<IListener> listeners = listenerMap.get(event.getClass());

        try {
            if (listeners != null) {
                for (IListener listener : listeners) {
                    listener.call(event);
                }
            }
        } catch (Exception e) {
            System.err.println("1 ТРЕВОГА БЛЯАОЛ ВАЫВАЛ ЫОВАЫВЛА!!!!");
            e.printStackTrace();
            if (Excellent.devMode())
                Excellent.inst().notificationManager().register(ColorFormatting.getColor(255, 0, 0) + "Чекни консоль.", NotificationType.INFO, 3000);
        }

        return event;
    }

    @Override
    public <T extends ICancellable> T post(T event) {
        List<IListener> listeners = listenerMap.get(event.getClass());

        try {
            if (listeners != null) {
                event.setCancelled(false);

                for (IListener listener : listeners) {
                    listener.call(event);
                    if (event.isCancelled()) break;
                }
            }
        } catch (Exception e) {
            System.err.println("2 ТРЕВОГА БЛЯАОЛ ВАЫВАЛ ЫОВАЫВЛА!!!!");
            e.printStackTrace();
            if (Excellent.devMode())
                Excellent.inst().notificationManager().register(ColorFormatting.getColor(255, 0, 0) + "Чекни консоль.", NotificationType.INFO, 3000);
        }
        return event;
    }

    @Override
    public void subscribe(Object object) {
        if (listenerCache.containsKey(object)) return;
        subscribe(getInstanceListeners(object), false);
    }

    @Override
    public void subscribe(Class<?> klass) {
        List<IListener> listeners = getStaticListeners(klass);
        subscribe(listeners, true);
    }

    @Override
    public void subscribe(IListener listener) {
        subscribe(listener, true);
    }

    private void subscribe(List<IListener> listeners, boolean check) {
        for (IListener listener : listeners) subscribe(listener, check);
    }

    private void subscribe(IListener listener, boolean check) {
        List<IListener> listeners = listenerMap.computeIfAbsent(listener.getTarget(), klass -> listenerListFactory.get());
        if (!check || !ListOperations.contains(listeners, listener)) ListOperations.insert(listeners, listener);
    }

    @Override
    public void unsubscribe(Object object) {
        List<IListener> listeners = listenerCache.get(object);
        if (listeners != null) {
            unsubscribe(listeners);
            listenerCache.remove(object);
        }
    }

    @Override
    public void unsubscribe(Class<?> klass) {
        List<IListener> listeners = staticListenerCache.get(klass);
        if (listeners != null) unsubscribe(listeners);
    }

    @Override
    public void unsubscribe(IListener listener) {
        List<IListener> listeners = listenerMap.get(listener.getTarget());
        if (listeners != null) ListOperations.remove(listeners, listener);
    }

    private void unsubscribe(List<IListener> listeners) {
        for (IListener listener : listeners) unsubscribe(listener);
    }

    private List<IListener> getStaticListeners(Class<?> klass) {
        return staticListenerCache.computeIfAbsent(klass, o -> getListeners(o, null, true));
    }

    private List<IListener> getInstanceListeners(Object object) {
        return listenerCache.computeIfAbsent(object, o -> getListeners(o.getClass(), o, false));
    }

    private List<IListener> getListeners(Class<?> klass, Object object, boolean staticOnly) {
        List<IListener> listeners = new ArrayList<>();
        getListeners(listeners, klass, object, staticOnly);
        return new ArrayBackedImmutableList<>(listeners);
    }

    private void getListeners(List<IListener> listeners, Class<?> klass, Object object, boolean staticOnly) {
        while (klass != null) {
            LambdaListener.Factory factory = null;

            for (var method : klass.getDeclaredMethods()) {
                if (isValid(method, staticOnly)) {
                    if (LambdaListener.requireLambdaFactoryRegistration() && factory == null)
                        factory = getLambdaFactory(klass); // Lazy-loaded
                    listeners.add(new LambdaListener(factory, klass, object, method));
                }
            }

            klass = klass.getSuperclass();
        }
    }

    private boolean isValid(Method method, boolean staticOnly) {
        if (staticOnly && !Modifier.isStatic(method.getModifiers())) return false;
        if (!method.isAnnotationPresent(EventHandler.class)) return false;
        if (method.getReturnType() != void.class) return false;
        if (method.getParameterCount() != 1) return false;

        return !method.getParameters()[0].getType().isPrimitive();
    }

    private LambdaListener.Factory getLambdaFactory(Class<?> klass) {
        for (LambdaFactoryInfo info : lambdaFactoryInfos) {
            if (klass.getName().startsWith(info.packagePrefix)) return info.factory;
        }

        throw new NoLambdaFactoryException(klass);
    }
}
