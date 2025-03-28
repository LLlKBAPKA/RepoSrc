package net.minecraft.client.resources;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;

import java.io.IOException;

public class FoliageColorReloadListener extends ReloadListener<int[]> {
    private static final ResourceLocation FOLIAGE_LOCATION = new ResourceLocation("textures/colormap/foliage.png");

    /**
     * Performs any reloading that can be done off-thread, such as file IO
     */
    protected int[] prepare(IResourceManager resourceManagerIn) {
        try {
            return ColorMapLoader.loadColors(resourceManagerIn, FOLIAGE_LOCATION);
        } catch (IOException ioexception) {
            throw new IllegalStateException("Failed to load foliage color texture", ioexception);
        }
    }

    protected void apply(int[] objectIn, IResourceManager resourceManagerIn) {
        FoliageColors.setFoliageBiomeColorizer(objectIn);
    }
}
