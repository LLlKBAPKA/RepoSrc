package org.excellent.client.managers.other.notification;

import org.excellent.client.Excellent;
import org.excellent.client.api.events.orbit.EventHandler;
import org.excellent.client.api.events.orbit.EventPriority;
import org.excellent.client.api.interfaces.IMinecraft;
import org.excellent.client.managers.events.render.Render2DEvent;
import org.excellent.client.managers.other.notification.impl.ErrorNotification;
import org.excellent.client.managers.other.notification.impl.InfoNotification;
import org.excellent.client.managers.other.notification.impl.WarnNotification;

import java.util.ArrayList;

public final class NotificationManager extends ArrayList<Notification> implements IMinecraft {

    public void init() {
        Excellent.eventHandler().subscribe(this);
    }

    public void register(final String content, final NotificationType type, long delay) {
        Notification notification = null;
        int index = this.size();
        switch (type) {
            case WARN -> notification = new WarnNotification(content, delay, index);
            case ERROR -> notification = new ErrorNotification(content, delay, index);
            case INFO -> notification = new InfoNotification(content, delay, index);
        }
        if (notification == null) return;
        this.add(notification);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(Render2DEvent event) {
        if (this.isEmpty()) return;
        this.removeIf(Notification::hasExpired);
        int i = 0;
        for (Notification notification : this) {
            notification.render(event.getMatrix(), i);
            i++;
        }
    }

}
