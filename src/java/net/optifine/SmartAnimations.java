package net.optifine;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.optifine.shaders.Shaders;

import java.util.BitSet;

public class SmartAnimations {
    private static boolean active;
    private static final BitSet spritesRendered = new BitSet();
    private static final BitSet texturesRendered = new BitSet();

    public static boolean isActive() {
        return active && !Shaders.isShadowPass;
    }

    public static void update() {
        active = Config.getGameSettings().ofSmartAnimations;
    }

    public static void spriteRendered(TextureAtlasSprite sprite) {
        if (sprite.isTerrain()) {
            int i = sprite.getAnimationIndex();

            if (i >= 0) {
                spritesRendered.set(i);
            }
        }
    }

    public static void spritesRendered(BitSet animationIndexes) {
        if (animationIndexes != null) {
            spritesRendered.or(animationIndexes);
        }
    }

    public static boolean isSpriteRendered(TextureAtlasSprite sprite) {
        if (!sprite.isTerrain()) {
            return true;
        } else {
            int i = sprite.getAnimationIndex();
            return i >= 0 && spritesRendered.get(i);
        }
    }

    public static void resetSpritesRendered(AtlasTexture atlasTexture) {
        if (atlasTexture.isTerrain()) {
            spritesRendered.clear();
        }
    }

    public static void textureRendered(int textureId) {
        if (textureId >= 0) {
            texturesRendered.set(textureId);
        }
    }

    public static boolean isTextureRendered(int texId) {
        return texId >= 0 && texturesRendered.get(texId);
    }

    public static void resetTexturesRendered() {
        texturesRendered.clear();
    }
}
