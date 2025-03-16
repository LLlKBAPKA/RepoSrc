package org.excellent.client.api.events;

import org.excellent.client.api.events.orbit.ICancellable;

public class CancellableEvent extends Event implements ICancellable {
    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void cancel() {
        ICancellable.super.cancel();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
