package net.minecraft.client.renderer.entity.model;

import net.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.HandSide;

public interface IHasArm
{
    void translateHand(HandSide sideIn, MatrixStack matrixStackIn);
}
