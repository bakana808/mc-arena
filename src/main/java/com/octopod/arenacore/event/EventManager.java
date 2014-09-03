package com.octopod.arenacore.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager {

	private final List<Object> listeners = Collections.synchronizedList(new ArrayList<>());
	
	public List<Object> getListeners() {
		return listeners;
	}
	
	public void registerListener(Object listener) {
		listeners.add(listener);
	}
	
	public void unregisterListener(Object listener) {
		listeners.remove(listener);
	}
	
	public void unregisterAll() {
		listeners.clear();
	}

    public void triggerEvent(final Event event) {

        List<EventMethod> listeners = collectListenersOf(event);

        synchronized(listeners) {

            //Run all listeners
            for(final EventMethod eventMethod: listeners) {
                eventMethod.invoke(event);
            }

        }
    }

    private ArrayList<EventMethod> collectListenersOf(Event event) {

        synchronized(listeners) {
            ArrayList<EventMethod> methods = new ArrayList<>();
            for(Object listener: listeners) {

                for(Method method: listener.getClass().getMethods()) {
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    if(annotation != null)
                    {
                        Class<?>[] argTypes = method.getParameterTypes();
                        if(argTypes.length != 1)
                            continue;

                        //Try to use argument type to find Event type.
                        if(!event.getClass().equals(argTypes[0])) {
                            //If the listener is instanceof ListenerIdentifier, getProperty the type
                            if(!ListenerIdentifier.class.isInstance(listener)) {
                                continue;
                            } else {
                                Class<? extends Event> type = ((ListenerIdentifier)listener).getType();
                                if(!argTypes[0].equals(Event.class) || !(type.equals(event.getClass()))) {
                                    continue;
                                }
                            }
                        }

                        methods.add(new EventMethod(listener, method, annotation));
                    }
                }

            }
            Collections.sort(methods);
            return methods;
        }

    }

    private static class EventMethod implements Comparable<EventMethod>
    {
        private Object object;
        private Method method;
        private EventHandler annotation;

        protected EventMethod(Object object, Method method, EventHandler annotation) {
            this.object = object;
            this.method = method;
            this.annotation = annotation;
        }

        public Event invoke(final Event event) {

            try {
                Runnable invokeMethod = new Runnable() {
                    public void run() {
                        try {
                            method.invoke(object, event);
                        } catch (Exception e) {}
                    }
                };

                if(annotation.async()) {
                    new Thread(invokeMethod).start();
                } else {
                    invokeMethod.run();
                }
            } catch (Exception e) {}

            return event;
        }

        public EventHandler handler() {return annotation;}

        @Override
        public int compareTo(EventMethod o) {
            return Integer.compare(annotation.priority().getPriority(), o.annotation.priority().getPriority());
        }
    }

	
}
