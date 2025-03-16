package org.excellent.client.managers.module.impl.movement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import org.excellent.client.api.events.orbit.EventHandler;
import org.excellent.client.managers.component.impl.rotation.Rotation;
import org.excellent.client.managers.component.impl.rotation.RotationComponent;
import org.excellent.client.managers.events.player.UpdateEvent;
import org.excellent.client.managers.module.Category;
import org.excellent.client.managers.module.Module;
import org.excellent.client.managers.module.ModuleInfo;
import org.excellent.client.utils.player.MoveUtil;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ModuleInfo(name = "ElytraRecast", category = Category.MOVEMENT)
public class ElytraRecast extends Module {
    int pitch45, pitch90;

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (!mc.player.isElytraFlying() && mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == Items.ELYTRA && MoveUtil.isMoving()) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_FALL_FLYING));
            mc.player.startFallFlying();
        }

        if (mc.player.isElytraFlying()) {
            if (mc.player.isOnGround()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    pitch45 = 15;
                    pitch90 = 0;
                } else {
                    mc.player.jump();
                    pitch90 = 7;
                }
            }

            if (pitch45 > 0) {
                RotationComponent.update(new Rotation(Rotation.cameraYaw(), -45), 360, 360, 0, 5);
                pitch45--;
            } else if (pitch90 > 0) {
                RotationComponent.update(new Rotation(Rotation.cameraYaw(), 90), 360, 360, 0, 5);
            }
        } else {
            pitch90 = 0;
            pitch45 = 0;
        }
    }
}
