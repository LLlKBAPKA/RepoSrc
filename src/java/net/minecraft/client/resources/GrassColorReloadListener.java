package net.minecraft.client.resources;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GrassColors;

import java.io.IOException;

public class GrassColorReloadListener extends ReloadListener<int[]> {
    private static final ResourceLocation GRASS_LOCATION = new ResourceLocation("textures/colormap/grass.png");

    /**
     * Performs any reloading that can be done off-thread, such as file IO
     */
    protected int[] prepare(IResourceManager resourceManagerIn) {
        try {
            return ColorMapLoader.loadColors(resourceManagerIn, GRASS_LOCATION);
        } catch (IOException ioexception) {
            throw new IllegalStateException("Failed to load grass color texture", ioexception);
        }
    }

    protected void apply(int[] objectIn, IResourceManager resourceManagerIn) {
        GrassColors.setGrassBiomeColorizer(objectIn);
    }
}
