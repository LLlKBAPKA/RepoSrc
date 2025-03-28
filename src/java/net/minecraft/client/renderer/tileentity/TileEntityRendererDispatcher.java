package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import net.mojang.blaze3d.matrix.MatrixStack;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.optifine.EmissiveTextures;

public class TileEntityRendererDispatcher
{
    public final Map < TileEntityType<?>, TileEntityRenderer<? >> renderers = Maps.newHashMap();
    public static final TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    private final BufferBuilder fixedRenderBuffer = new BufferBuilder(256);
    private FontRenderer fontRenderer;
    public TextureManager textureManager;
    public World world;
    public ActiveRenderInfo renderInfo;
    public RayTraceResult cameraHitResult;
    public static TileEntity tileEntityRendered;

    private TileEntityRendererDispatcher()
    {
        this.register(TileEntityType.SIGN, new SignTileEntityRenderer(this));
        this.register(TileEntityType.MOB_SPAWNER, new MobSpawnerTileEntityRenderer(this));
        this.register(TileEntityType.PISTON, new PistonTileEntityRenderer(this));
        this.register(TileEntityType.CHEST, new ChestTileEntityRenderer<>(this));
        this.register(TileEntityType.ENDER_CHEST, new ChestTileEntityRenderer<>(this));
        this.register(TileEntityType.TRAPPED_CHEST, new ChestTileEntityRenderer<>(this));
        this.register(TileEntityType.ENCHANTING_TABLE, new EnchantmentTableTileEntityRenderer(this));
        this.register(TileEntityType.LECTERN, new LecternTileEntityRenderer(this));
        this.register(TileEntityType.END_PORTAL, new EndPortalTileEntityRenderer<>(this));
        this.register(TileEntityType.END_GATEWAY, new EndGatewayTileEntityRenderer(this));
        this.register(TileEntityType.BEACON, new BeaconTileEntityRenderer(this));
        this.register(TileEntityType.SKULL, new SkullTileEntityRenderer(this));
        this.register(TileEntityType.BANNER, new BannerTileEntityRenderer(this));
        this.register(TileEntityType.STRUCTURE_BLOCK, new StructureTileEntityRenderer(this));
        this.register(TileEntityType.SHULKER_BOX, new ShulkerBoxTileEntityRenderer(new ShulkerModel(), this));
        this.register(TileEntityType.BED, new BedTileEntityRenderer(this));
        this.register(TileEntityType.CONDUIT, new ConduitTileEntityRenderer(this));
        this.register(TileEntityType.BELL, new BellTileEntityRenderer(this));
        this.register(TileEntityType.CAMPFIRE, new CampfireTileEntityRenderer(this));
    }

    private <E extends TileEntity> void register(TileEntityType<E> typeIn, TileEntityRenderer<E> rendererIn)
    {
        this.renderers.put(typeIn, rendererIn);
    }

    @Nullable
    public <E extends TileEntity> TileEntityRenderer<E> getRenderer(E tileEntityIn)
    {
        return (TileEntityRenderer<E>)this.renderers.get(tileEntityIn.getType());
    }

    public void prepare(World worldIn, TextureManager textureManagerIn, FontRenderer fontRendererIn, ActiveRenderInfo activeRenderInfoIn, RayTraceResult rayTraceResultIn)
    {
        if (this.world != worldIn)
        {
            this.setWorld(worldIn);
        }

        this.textureManager = textureManagerIn;
        this.renderInfo = activeRenderInfoIn;
        this.fontRenderer = fontRendererIn;
        this.cameraHitResult = rayTraceResultIn;
    }

    public <E extends TileEntity> void renderTileEntity(E tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn)
    {
        if (Vector3d.copyCentered(tileEntityIn.getPos()).isWithinDistanceOf(this.renderInfo.getProjectedView(), tileEntityIn.getMaxRenderDistanceSquared()))
        {
            TileEntityRenderer<E> tileentityrenderer = this.getRenderer(tileEntityIn);

            if (tileentityrenderer != null && tileEntityIn.hasWorld() && tileEntityIn.getType().isValidBlock(tileEntityIn.getBlockState().getBlock()))
            {
                runCrashReportable(tileEntityIn, () ->
                {
                    render(tileentityrenderer, tileEntityIn, partialTicks, matrixStackIn, bufferIn);
                });
            }
        }
    }

    private static <T extends TileEntity> void render(TileEntityRenderer<T> rendererIn, T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn)
    {
        World world = tileEntityIn.getWorld();
        int i;

        if (world != null)
        {
            i = WorldRenderer.getCombinedLight(world, tileEntityIn.getPos());
        }
        else
        {
            i = 15728880;
        }

        tileEntityRendered = tileEntityIn;

        if (EmissiveTextures.isActive())
        {
            EmissiveTextures.beginRender();
        }

        rendererIn.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, i, OverlayTexture.NO_OVERLAY);

        if (EmissiveTextures.isActive())
        {
            if (EmissiveTextures.hasEmissive())
            {
                EmissiveTextures.beginRenderEmissive();
                rendererIn.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, LightTexture.MAX_BRIGHTNESS, OverlayTexture.NO_OVERLAY);
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }

        tileEntityRendered = null;
    }

    public <E extends TileEntity> boolean renderItem(E tileEntityIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        TileEntityRenderer<E> tileentityrenderer = this.getRenderer(tileEntityIn);

        if (tileentityrenderer == null)
        {
            return true;
        }
        else
        {
            runCrashReportable(tileEntityIn, () ->
            {
                tileEntityRendered = tileEntityIn;
                tileentityrenderer.render(tileEntityIn, 0.0F, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                tileEntityRendered = null;
            });
            return false;
        }
    }

    private static void runCrashReportable(TileEntity tileEntityIn, Runnable runnableIn)
    {
        try
        {
            runnableIn.run();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
            tileEntityIn.addInfoToCrashReport(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    public void setWorld(@Nullable World worldIn)
    {
        this.world = worldIn;

        if (worldIn == null)
        {
            this.renderInfo = null;
        }
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }

    public TileEntityRenderer getRenderer(TileEntityType p_getRenderer_1_)
    {
        return this.renderers.get(p_getRenderer_1_);
    }

    public synchronized <T extends TileEntity> void setSpecialRendererInternal(TileEntityType<T> p_setSpecialRendererInternal_1_, TileEntityRenderer <? super T > p_setSpecialRendererInternal_2_)
    {
        this.renderers.put(p_setSpecialRendererInternal_1_, p_setSpecialRendererInternal_2_);
    }
}
