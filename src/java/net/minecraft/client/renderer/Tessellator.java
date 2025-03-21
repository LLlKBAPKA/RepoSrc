package net.minecraft.client.renderer;

import net.mojang.blaze3d.systems.RenderSystem;
import net.optifine.SmartAnimations;

public class Tessellator
{
    private final BufferBuilder buffer;
    private static final Tessellator INSTANCE = new Tessellator();

    public static Tessellator getInstance()
    {
        RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
        return INSTANCE;
    }

    public Tessellator(int bufferSize)
    {
        this.buffer = new BufferBuilder(bufferSize);
    }

    public Tessellator()
    {
        this(2097152);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        if (this.buffer.animatedSprites != null)
        {
            SmartAnimations.spritesRendered(this.buffer.animatedSprites);
        }

        this.buffer.finishDrawing();
        WorldVertexBufferUploader.draw(this.buffer);
    }

    public BufferBuilder getBuffer()
    {
        return this.buffer;
    }
}
