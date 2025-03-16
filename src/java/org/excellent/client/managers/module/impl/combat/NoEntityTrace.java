package org.excellent.client.managers.module.impl.combat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.LivingEntity;
import org.excellent.client.api.events.orbit.EventHandler;
import org.excellent.client.managers.component.impl.target.TargetComponent;
import org.excellent.client.managers.events.other.EntityRayTraceEvent;
import org.excellent.client.managers.module.Category;
import org.excellent.client.managers.module.Module;
import org.excellent.client.managers.module.ModuleInfo;
import org.excellent.client.utils.other.Instance;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ModuleInfo(name = "NoEntityTrace", category = Category.COMBAT)
public class NoEntityTrace extends Module {
    public static NoEntityTrace getInstance() {
        return Instance.get(NoEntityTrace.class);
    }

    @EventHandler
    public void onEvent(EntityRayTraceEvent event) {
        if (event.getEntity() instanceof LivingEntity living && TargetComponent.getTargets(6, true).contains(living)) {
            event.cancel();
        }
    }
}