package org.excellent.client.managers.module.impl.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.excellent.client.api.events.orbit.EventHandler;
import org.excellent.client.managers.events.player.MotionEvent;
import org.excellent.client.managers.events.player.UpdateEvent;
import org.excellent.client.managers.module.Category;
import org.excellent.client.managers.module.Module;
import org.excellent.client.managers.module.ModuleInfo;
import org.excellent.client.managers.module.settings.impl.BooleanSetting;
import org.excellent.client.managers.module.settings.impl.MultiBooleanSetting;
import org.excellent.client.utils.other.Instance;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ModuleInfo(name = "NoDelay", category = Category.PLAYER)
public class NoDelay extends Module {
    public static NoDelay getInstance() {
        return Instance.get(NoDelay.class);
    }

    private final MultiBooleanSetting elements = new MultiBooleanSetting(this, "Элементы",
            BooleanSetting.of("No Jump Delay", true),
            BooleanSetting.of("No Place Delay", false)
    );

    @EventHandler
    public void onEvent(UpdateEvent event) {
        if (elements.getValue("No Place Delay")) {
            mc.setRightClickDelayTimer(0);
        }
    }

    @EventHandler
    public void onEvent(MotionEvent event) {
        if (elements.getValue("No Jump Delay") && mc.player.onGroundTicks > 0) {
            mc.player.setJumpTicks(0);
        }
    }
}