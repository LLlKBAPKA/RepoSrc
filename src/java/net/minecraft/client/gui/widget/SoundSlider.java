package net.minecraft.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SoundSlider extends GameSettingsSlider {
    private final SoundCategory category;

    public SoundSlider(Minecraft settings, int x, int y, SoundCategory category, int width) {
        super(settings.gameSettings, x, y, width, 20, settings.gameSettings.getSoundLevel(category));
        this.category = category;
        this.onAction();
    }

    protected void onAction() {
        ITextComponent itextcomponent = (float) this.sliderValue == (float) this.getYImage(false) ? DialogTexts.OPTIONS_OFF : new StringTextComponent((int) (this.sliderValue * 100.0D) + "%");
        this.setMessage((new TranslationTextComponent("soundCategory." + this.category.getName())).appendString(": ").append(itextcomponent));
    }

    protected void onChange() {
        this.settings.setSoundLevel(this.category, (float) this.sliderValue);
        this.settings.saveOptions();
    }
}
