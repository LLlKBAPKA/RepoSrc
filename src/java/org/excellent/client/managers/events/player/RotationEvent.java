package org.excellent.client.managers.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.excellent.client.api.events.Event;

@Getter
@Setter
@AllArgsConstructor
public class RotationEvent extends Event {
    private float yaw, pitch;
    private float partialTicks;
}