package org.spoutcraft.api.addon;

import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.event.Listener;

public class RegisteredListener {
    private final Listener listener;
    private final Event.Priority priority;
    private final Addon Addon;
    private final EventExecutor executor;

    public RegisteredListener(final Listener AddonListener, final EventExecutor eventExecutor, final Event.Priority eventPriority, final Addon registeredAddon) {
        listener = AddonListener;
        priority = eventPriority;
        Addon = registeredAddon;
        executor = eventExecutor;
    }

    public RegisteredListener(final Listener AddonListener, final Event.Priority eventPriority, final Addon registeredAddon, Event.Type type) {
        listener = AddonListener;
        priority = eventPriority;
        Addon = registeredAddon;
        executor = registeredAddon.getAddonLoader().createExecutor(type, AddonListener);
    }

    /**
     * Gets the listener for this registration
     * @return Registered Listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Gets the Addon for this registration
     * @return Registered Addon
     */
    public Addon getAddon() {
        return Addon;
    }

    /**
     * Gets the priority for this registration
     * @return Registered Priority
     */
    public Event.Priority getPriority() {
        return priority;
    }

    /**
     * Calls the event executor
     * @return Registered Priority
     */
    public void callEvent(Event event) {
        executor.execute(listener, event);
    }
}