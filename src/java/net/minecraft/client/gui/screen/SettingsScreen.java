package net.minecraft.client.gui.screen;

import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.IBidiTooltip;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SettingsScreen extends Screen {
    protected final Screen parentScreen;
    protected final GameSettings gameSettings;

    public SettingsScreen(Screen previousScreen, GameSettings gameSettingsObj, ITextComponent textComponent) {
        super(textComponent);
        this.parentScreen = previousScreen;
        this.gameSettings = gameSettingsObj;
    }

    public void onClose() {
        this.minecraft.gameSettings.saveOptions();
    }

    public void closeScreen() {
        this.minecraft.displayScreen(this.parentScreen);
    }

    @Nullable
    public static List<IReorderingProcessor> func_243293_a(OptionsRowList p_243293_0_, int p_243293_1_, int p_243293_2_) {
        Optional<Widget> optional = p_243293_0_.func_238518_c_(p_243293_1_, p_243293_2_);

        if (optional.isPresent() && optional.get() instanceof IBidiTooltip) {
            Optional<List<IReorderingProcessor>> optional1 = ((IBidiTooltip) optional.get()).func_241867_d();
            return optional1.orElse(null);
        } else {
            return null;
        }
    }
}
