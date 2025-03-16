package org.excellent.client.managers.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.excellent.client.api.events.CancellableEvent;

@Getter
@Setter
@AllArgsConstructor
public class JumpEvent extends CancellableEvent {
    private float jumpMotion;
    private float yaw;
}