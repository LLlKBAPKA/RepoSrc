package org.excellent.client.managers.events.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import org.excellent.client.api.events.Event;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class KeyboardReleaseEvent extends Event {
    @Getter
    private static final KeyboardReleaseEvent instance = new KeyboardReleaseEvent();
    private int key;
    private Screen screen;

    public void set(int key, Screen screen) {
        this.key = key;
        this.screen = screen;
    }

    public boolean isKey(int key) {
        return this.key == key;
    }
}
