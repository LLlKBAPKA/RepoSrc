package org.excellent.client.managers.component.impl.rotation;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.MathHelper;
import org.excellent.client.api.events.orbit.EventHandler;
import org.excellent.client.managers.component.Component;
import org.excellent.client.managers.events.player.LookEvent;
import org.excellent.client.managers.events.player.RotationEvent;

public class FreeLookComponent extends Component {

    @Getter
    @Setter
    private static boolean active;
    @Getter
    @Setter
    private static float freeYaw, freePitch;

    @EventHandler
    public void onEvent(LookEvent event) {
        if (active) {
            rotateTowards(event.getYaw(), event.getPitch());
            event.cancel();
        }
    }

    @EventHandler
    public void onEvent(RotationEvent event) {
        if (active) {
            event.setYaw(freeYaw);
            event.setPitch(freePitch);
        } else {
            freeYaw = event.getYaw();
            freePitch = event.getPitch();
        }
    }

    private void rotateTowards(double targetYaw, double targetPitch) {
        freePitch = MathHelper.clamp((float) (freePitch + targetPitch * 0.15D), -90.0F, 90.0F);
        freeYaw = (float) (freeYaw + targetYaw * 0.15D);
    }
}
