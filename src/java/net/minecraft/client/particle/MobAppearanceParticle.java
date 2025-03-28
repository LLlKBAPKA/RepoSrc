package net.minecraft.client.particle;

import net.mojang.blaze3d.matrix.MatrixStack;
import net.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.entity.model.GuardianModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class MobAppearanceParticle extends Particle
{
    private final Model model = new GuardianModel();
    private final RenderType renderType = RenderType.getEntityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_TEXTURE);

    private MobAppearanceParticle(ClientWorld world, double x, double y, double z)
    {
        super(world, x, y, z);
        this.particleGravity = 0.0F;
        this.maxAge = 30;
    }

    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.CUSTOM;
    }

    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        float f = ((float)this.age + partialTicks) / (float)this.maxAge;
        float f1 = 0.05F + 0.5F * MathHelper.sin(f * (float)Math.PI);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(renderInfo.getRotation());
        matrixstack.rotate(Vector3f.XP.rotationDegrees(150.0F * f - 60.0F));
        matrixstack.scale(-1.0F, -1.0F, 1.0F);
        matrixstack.translate(0.0D, (double) - 1.101F, 1.5D);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(this.renderType);
        this.model.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
        irendertypebuffer$impl.finish();
    }

    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new MobAppearanceParticle(worldIn, x, y, z);
        }
    }
}
