package net.optifine.util;


import net.minecraft.client.renderer.entity.layers.MooshroomMushroomLayer;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.mojang.blaze3d.platform.GlStateManager;
import net.optifine.*;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.Shaders;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class TextureUtils {
    private static final String texGrassTop = "grass_block_top";
    private static final String texGrassSide = "grass_block_side";
    private static final String texGrassSideOverlay = "grass_block_side_overlay";
    private static final String texSnow = "snow";
    private static final String texGrassSideSnowed = "grass_block_snow";
    private static final String texMyceliumSide = "mycelium_side";
    private static final String texMyceliumTop = "mycelium_top";
    private static final String texWaterStill = "water_still";
    private static final String texWaterFlow = "water_flow";
    private static final String texLavaStill = "lava_still";
    private static final String texLavaFlow = "lava_flow";
    private static final String texFireLayer0 = "fire_0";
    private static final String texFireLayer1 = "fire_1";
    private static final String texSoulFireLayer0 = "soul_fire_0";
    private static final String texSoulFireLayer1 = "soul_fire_1";
    private static final String texCampFire = "campfire_fire";
    private static final String texCampFireLogLit = "campfire_log_lit";
    private static final String texSoulCampFire = "soul_campfire_fire";
    private static final String texSoulCampFireLogLit = "soul_campfire_log_lit";
    private static final String texPortal = "nether_portal";
    private static final String texGlass = "glass";
    private static final String texGlassPaneTop = "glass_pane_top";
    public static TextureAtlasSprite iconGrassTop;
    public static TextureAtlasSprite iconGrassSide;
    public static TextureAtlasSprite iconGrassSideOverlay;
    public static TextureAtlasSprite iconSnow;
    public static TextureAtlasSprite iconGrassSideSnowed;
    public static TextureAtlasSprite iconMyceliumSide;
    public static TextureAtlasSprite iconMyceliumTop;
    public static TextureAtlasSprite iconWaterStill;
    public static TextureAtlasSprite iconWaterFlow;
    public static TextureAtlasSprite iconLavaStill;
    public static TextureAtlasSprite iconLavaFlow;
    public static TextureAtlasSprite iconFireLayer0;
    public static TextureAtlasSprite iconFireLayer1;
    public static TextureAtlasSprite iconSoulFireLayer0;
    public static TextureAtlasSprite iconSoulFireLayer1;
    public static TextureAtlasSprite iconCampFire;
    public static TextureAtlasSprite iconCampFireLogLit;
    public static TextureAtlasSprite iconSoulCampFire;
    public static TextureAtlasSprite iconSoulCampFireLogLit;
    public static TextureAtlasSprite iconPortal;
    public static TextureAtlasSprite iconGlass;
    public static TextureAtlasSprite iconGlassPaneTop;
    public static final String SPRITE_PREFIX_BLOCKS = "minecraft:block/";
    public static final String SPRITE_PREFIX_ITEMS = "minecraft:item/";
    public static final ResourceLocation LOCATION_SPRITE_EMPTY = new ResourceLocation("optifine/ctm/default/empty");
    public static final ResourceLocation LOCATION_TEXTURE_EMPTY = new ResourceLocation("optifine/ctm/default/empty.png");
    private static final IntBuffer staticBuffer = Config.createDirectIntBuffer(256);
    private static int glMaximumTextureSize = -1;
    private static final Map<Integer, String> mapTextureAllocations = new HashMap<>();

    public static void update() {
        AtlasTexture atlastexture = getTextureMapBlocks();

        if (atlastexture != null) {
            String s = "minecraft:block/";
            iconGrassTop = getSpriteCheck(atlastexture, s + "grass_block_top");
            iconGrassSide = getSpriteCheck(atlastexture, s + "grass_block_side");
            iconGrassSideOverlay = getSpriteCheck(atlastexture, s + "grass_block_side_overlay");
            iconSnow = getSpriteCheck(atlastexture, s + "snow");
            iconGrassSideSnowed = getSpriteCheck(atlastexture, s + "grass_block_snow");
            iconMyceliumSide = getSpriteCheck(atlastexture, s + "mycelium_side");
            iconMyceliumTop = getSpriteCheck(atlastexture, s + "mycelium_top");
            iconWaterStill = getSpriteCheck(atlastexture, s + "water_still");
            iconWaterFlow = getSpriteCheck(atlastexture, s + "water_flow");
            iconLavaStill = getSpriteCheck(atlastexture, s + "lava_still");
            iconLavaFlow = getSpriteCheck(atlastexture, s + "lava_flow");
            iconFireLayer0 = getSpriteCheck(atlastexture, s + "fire_0");
            iconFireLayer1 = getSpriteCheck(atlastexture, s + "fire_1");
            iconSoulFireLayer0 = getSpriteCheck(atlastexture, s + "soul_fire_0");
            iconSoulFireLayer1 = getSpriteCheck(atlastexture, s + "soul_fire_1");
            iconCampFire = getSpriteCheck(atlastexture, s + "campfire_fire");
            iconCampFireLogLit = getSpriteCheck(atlastexture, s + "campfire_log_lit");
            iconSoulCampFire = getSpriteCheck(atlastexture, s + "soul_campfire_fire");
            iconSoulCampFireLogLit = getSpriteCheck(atlastexture, s + "soul_campfire_log_lit");
            iconPortal = getSpriteCheck(atlastexture, s + "nether_portal");
            iconGlass = getSpriteCheck(atlastexture, s + "glass");
            iconGlassPaneTop = getSpriteCheck(atlastexture, s + "glass_pane_top");
            String s1 = "minecraft:item/";
        }
    }

    public static TextureAtlasSprite getSpriteCheck(AtlasTexture textureMap, String name) {
        TextureAtlasSprite textureatlassprite = textureMap.getUploadedSprite(name);

        if (textureatlassprite instanceof MissingTextureSprite) {
            Config.warn("Sprite not found: " + name);
        }

        return textureatlassprite;
    }

    public static BufferedImage fixTextureDimensions(String name, BufferedImage bi) {
        if (name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) {
            int i = bi.getWidth();
            int j = bi.getHeight();

            if (i == j * 2) {
                BufferedImage bufferedimage = new BufferedImage(i, j * 2, 2);
                Graphics2D graphics2d = bufferedimage.createGraphics();
                graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2d.drawImage(bi, 0, 0, i, j, null);
                return bufferedimage;
            }
        }

        return bi;
    }

    public static int ceilPowerOfTwo(int val) {
        int i;

        for (i = 1; i < val; i *= 2) {
        }

        return i;
    }

    public static int getPowerOfTwo(int val) {
        int i = 1;
        int j;

        for (j = 0; i < val; ++j) {
            i *= 2;
        }

        return j;
    }

    public static int twoToPower(int power) {
        int i = 1;

        for (int j = 0; j < power; ++j) {
            i *= 2;
        }

        return i;
    }

    public static Texture getTexture(ResourceLocation loc) {
        Texture texture = Config.getTextureManager().getTexture(loc);

        if (texture != null) {
            return texture;
        } else if (!Config.hasResource(loc)) {
            return null;
        } else {
            texture = new SimpleTexture(loc);
            Config.getTextureManager().loadTexture(loc, texture);
            return texture;
        }
    }

    public static void resourcesReloaded(IResourceManager rm) {
        if (getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            update();
            NaturalTextures.update();
            BetterGrass.update();
            BetterSnow.update();
            TextureAnimations.update();
            CustomColors.update();
            CustomSky.update();
            RandomEntities.update();
            CustomItems.updateModels();
            CustomEntityModels.update();
            Shaders.resourcesReloaded();
            Lang.resourcesReloaded();
            Config.updateTexturePackClouds();
            SmartLeaves.updateLeavesModels();
            CustomPanorama.update();
            CustomGuis.update();
            MooshroomMushroomLayer.update();
            CustomLoadingScreens.update();
            CustomBlockLayers.update();
            Config.getTextureManager().tick();
            Config.dbg("Disable Forge light pipeline");
            ReflectorForge.setForgeLightPipelineEnabled(false);
        }
    }

    public static AtlasTexture getTextureMapBlocks() {
        return Config.getTextureMap();
    }


    public static void registerResourceListener() {
        IResourceManager iresourcemanager = Config.getResourceManager();

        if (iresourcemanager instanceof IReloadableResourceManager ireloadableresourcemanager) {
            ReloadListener<?> reloadlistener = new ReloadListener<>() {
                protected Object prepare(IResourceManager resourceManagerIn) {
                    return null;
                }

                protected void apply(Object objectIn, IResourceManager resourceManagerIn) {
                }
            };
            ireloadableresourcemanager.addReloadListener(reloadlistener);
            IResourceManagerReloadListener iresourcemanagerreloadlistener = TextureUtils::resourcesReloaded;
            ireloadableresourcemanager.addReloadListener(iresourcemanagerreloadlistener);
        }
    }

    public static void registerTickableTextures() {
        TickableTexture tickabletexture = new TickableTexture() {
            public void tick() {
                TextureAnimations.updateAnimations();
            }

            public void loadTexture(IResourceManager var1) {
            }

            public int getGlTextureId() {
                return 0;
            }

            public void restoreLastBlurMipmap() {
            }

            public MultiTexID getMultiTexID() {
                return null;
            }
        };
        ResourceLocation resourcelocation = new ResourceLocation("optifine/tickable_textures");
        Config.getTextureManager().loadTexture(resourcelocation, tickabletexture);
    }

    public static void registerCustomModels(ModelBakery modelBakery) {
        CustomItems.update();
        CustomItems.loadModels(modelBakery);
    }

    public static void registerCustomSprites(AtlasTexture textureMap) {
        if (textureMap.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            ConnectedTextures.updateIcons(textureMap);
            CustomItems.updateIcons(textureMap);
            BetterGrass.updateIcons(textureMap);
        }
    }

    public static void refreshCustomSprites(AtlasTexture textureMap) {
        if (textureMap.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            ConnectedTextures.refreshIcons(textureMap);
            CustomItems.refreshIcons(textureMap);
            BetterGrass.refreshIcons(textureMap);
        }

        EmissiveTextures.refreshIcons(textureMap);
    }

    public static ResourceLocation fixResourceLocation(ResourceLocation loc, String basePath) {
        if (!loc.getNamespace().equals("minecraft")) {
            return loc;
        } else {
            String s = loc.getPath();
            String s1 = fixResourcePath(s, basePath);

            if (!s1.equals(s)) {
                loc = new ResourceLocation(loc.getNamespace(), s1);
            }

            return loc;
        }
    }

    public static String fixResourcePath(String path, String basePath) {
        String s = "assets/minecraft/";

        if (path.startsWith(s)) {
            return path.substring(s.length());
        } else if (path.startsWith("./")) {
            path = path.substring(2);

            if (!basePath.endsWith("/")) {
                basePath = basePath + "/";
            }

            return basePath + path;
        } else {
            if (path.startsWith("/~")) {
                path = path.substring(1);
            }

            String s1 = "optifine/";

            if (path.startsWith("~/")) {
                path = path.substring(2);
                return s1 + path;
            } else {
                return path.startsWith("/") ? s1 + path.substring(1) : path;
            }
        }
    }

    public static String getBasePath(String path) {
        int i = path.lastIndexOf(47);
        return i < 0 ? "" : path.substring(0, i);
    }

    public static void applyAnisotropicLevel() {
        if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float f = GL11.glGetFloat(34047);
            float f1 = (float) Config.getAnisotropicFilterLevel();
            f1 = Math.min(f1, f);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, f1);
        }
    }

    public static void bindTexture(int glTexId) {
        GlStateManager.bindTexture(glTexId);
    }

    public static boolean isPowerOfTwo(int x) {
        int i = MathHelper.smallestEncompassingPowerOfTwo(x);
        return i == x;
    }

    public static NativeImage scaleImage(NativeImage ni, int w2) {
        BufferedImage bufferedimage = toBufferedImage(ni);
        BufferedImage bufferedimage1 = scaleImage(bufferedimage, w2);
        return toNativeImage(bufferedimage1);
    }

    public static BufferedImage toBufferedImage(NativeImage ni) {
        int i = ni.getWidth();
        int j = ni.getHeight();
        int[] aint = new int[i * j];
        ni.getBufferRGBA().get(aint);
        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        bufferedimage.setRGB(0, 0, i, j, aint, 0, i);
        return bufferedimage;
    }

    public static NativeImage toNativeImage(BufferedImage bi) {
        int i = bi.getWidth();
        int j = bi.getHeight();
        int[] aint = new int[i * j];
        bi.getRGB(0, 0, i, j, aint, 0, i);
        NativeImage nativeimage = new NativeImage(i, j, false);
        nativeimage.getBufferRGBA().put(aint);
        return nativeimage;
    }

    public static BufferedImage scaleImage(BufferedImage bi, int w2) {
        int i = bi.getWidth();
        int j = bi.getHeight();
        int k = j * w2 / i;
        BufferedImage bufferedimage = new BufferedImage(w2, k, 2);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;

        if (w2 < i || w2 % i != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }

        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(bi, 0, 0, w2, k, null);
        return bufferedimage;
    }

    public static int scaleToGrid(int size, int sizeGrid) {
        if (size == sizeGrid) {
            return size;
        } else {
            int i;

            for (i = size / sizeGrid * sizeGrid; i < size; i += sizeGrid) {
            }

            return i;
        }
    }

    public static int scaleToMin(int size, int sizeMin) {
        if (size >= sizeMin) {
            return size;
        } else {
            int i;

            for (i = sizeMin / size * size; i < sizeMin; i += size) {
            }

            return i;
        }
    }

    public static Dimension getImageSize(InputStream in, String suffix) {
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(suffix);

        while (true) {
            if (iterator.hasNext()) {
                ImageReader imagereader = iterator.next();
                Dimension dimension;

                try {
                    ImageInputStream imageinputstream = ImageIO.createImageInputStream(in);
                    imagereader.setInput(imageinputstream);
                    int i = imagereader.getWidth(imagereader.getMinIndex());
                    int j = imagereader.getHeight(imagereader.getMinIndex());
                    dimension = new Dimension(i, j);
                } catch (IOException ioexception) {
                    continue;
                } finally {
                    imagereader.dispose();
                }

                return dimension;
            }

            return null;
        }
    }

    public static void dbgMipmaps(TextureAtlasSprite textureatlassprite) {
        NativeImage[] anativeimage = textureatlassprite.getMipmapImages();

        for (int i = 0; i < anativeimage.length; ++i) {
            NativeImage nativeimage = anativeimage[i];

            if (nativeimage == null) {
                Config.dbg(i + ": " + null);
            } else {
                Config.dbg(i + ": " + nativeimage.getWidth() * nativeimage.getHeight());
            }
        }
    }

    public static void saveGlTexture(String name, int textureId, int mipmapLevels, int width, int height) {
        bindTexture(textureId);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        name = StrUtils.removeSuffix(name, ".png");
        File file1 = new File(name);
        File file2 = file1.getParentFile();

        if (file2 != null) {
            file2.mkdirs();
        }

        for (int i = 0; i < 16; ++i) {
            String s = name + "_" + i + ".png";
            File file3 = new File(s);
            file3.delete();
        }

        for (int l = 0; l <= mipmapLevels; ++l) {
            File file4 = new File(name + "_" + l + ".png");
            int i1 = width >> l;
            int j = height >> l;
            int k = i1 * j;
            IntBuffer intbuffer = BufferUtils.createIntBuffer(k);
            int[] aint = new int[k];
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, l, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, intbuffer);
            intbuffer.get(aint);
            BufferedImage bufferedimage = new BufferedImage(i1, j, 2);
            bufferedimage.setRGB(0, 0, i1, j, aint, 0, i1);

            try {
                ImageIO.write(bufferedimage, "png", file4);
                Config.dbg("Exported: " + file4);
            } catch (Exception exception) {
                Config.warn("Error writing: " + file4);
                Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }

    public static int getGLMaximumTextureSize() {
        if (glMaximumTextureSize < 0) {
            glMaximumTextureSize = detectGLMaximumTextureSize();
        }

        return glMaximumTextureSize;
    }

    private static int detectGLMaximumTextureSize() {
        for (int i = 65536; i > 0; i >>= 1) {
            GlStateManager.texImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            int j = GL11.glGetError();
            int k = GlStateManager.getTexLevelParameter(32868, 0, 4096);

            if (k != 0) {
                return i;
            }
        }

        return 0;
    }

    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException {
        if (imageStream == null) {
            return null;
        } else {
            BufferedImage bufferedimage1;

            try {
                bufferedimage1 = ImageIO.read(imageStream);
            } finally {
                IOUtils.closeQuietly(imageStream);
            }

            return bufferedimage1;
        }
    }

    public static int toAbgr(int argb) {
        int i = argb >> 24 & 255;
        int j = argb >> 16 & 255;
        int k = argb >> 8 & 255;
        int l = argb & 255;
        return i << 24 | l << 16 | k << 8 | j;
    }

    public static void resetDataUnpacking() {
        GlStateManager.pixelStore(3314, 0);
        GlStateManager.pixelStore(3316, 0);
        GlStateManager.pixelStore(3315, 0);
        GlStateManager.pixelStore(3317, 4);
    }

    public static String getStackTrace(Throwable t) {
        CharArrayWriter chararraywriter = new CharArrayWriter();
        t.printStackTrace(new PrintWriter(chararraywriter));
        return chararraywriter.toString();
    }

    public static void debugTextureGenerated(int id) {
        mapTextureAllocations.put(id, getStackTrace(new Throwable("StackTrace")));
        Config.dbg("Textures: " + mapTextureAllocations.size());
    }

    public static void debugTextureDeleted(int id) {
        mapTextureAllocations.remove(id);
        Config.dbg("Textures: " + mapTextureAllocations.size());
    }
}
