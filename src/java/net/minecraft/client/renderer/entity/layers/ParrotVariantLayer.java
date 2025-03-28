package net.minecraft.client.renderer.entity.layers;

import net.mojang.blaze3d.matrix.MatrixStack;
import net.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.model.ParrotModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.optifine.Config;
import net.optifine.shaders.Shaders;

public class ParrotVariantLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>>
{
    private final ParrotModel parrotModel = new ParrotModel();

    public ParrotVariantLayer(IEntityRenderer<T, PlayerModel<T>> rendererIn)
    {
        super(rendererIn);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
        this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
    }

    private void renderParrot(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, boolean leftShoulderIn)
    {
        CompoundNBT compoundnbt = leftShoulderIn ? entitylivingbaseIn.getLeftShoulderEntity() : entitylivingbaseIn.getRightShoulderEntity();
        EntityType.byKey(compoundnbt.getString("id")).filter((p_lambda$renderParrot$0_0_) ->
        {
            return p_lambda$renderParrot$0_0_ == EntityType.PARROT;
        }).ifPresent((p_lambda$renderParrot$1_11_) ->
        {
            Entity entity = Config.getRenderGlobal().renderedEntity;

            if (entitylivingbaseIn instanceof AbstractClientPlayerEntity)
            {
                AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity)entitylivingbaseIn;
                Entity entity1 = leftShoulderIn ? abstractclientplayerentity.entityShoulderLeft : abstractclientplayerentity.entityShoulderRight;

                if (entity1 != null)
                {
                    Config.getRenderGlobal().renderedEntity = entity1;

                    if (Config.isShaders())
                    {
                        Shaders.nextEntity(entity1);
                    }
                }
            }

            matrixStackIn.push();
            matrixStackIn.translate(leftShoulderIn ? (double)0.4F : (double) - 0.4F, entitylivingbaseIn.isCrouching() ? (double) - 1.3F : -1.5D, 0.0D);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.parrotModel.getRenderType(ParrotRenderer.PARROT_TEXTURES[compoundnbt.getInt("Variant")]));
            this.parrotModel.renderOnShoulder(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, limbSwing, limbSwingAmount, netHeadYaw, headPitch, entitylivingbaseIn.ticksExisted);
            matrixStackIn.pop();
            Config.getRenderGlobal().renderedEntity = entity;

            if (Config.isShaders())
            {
                Shaders.nextEntity(entity);
            }
        });
    }
}
