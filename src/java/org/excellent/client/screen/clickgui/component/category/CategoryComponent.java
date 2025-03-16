package org.excellent.client.screen.clickgui.component.category;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.mojang.blaze3d.matrix.MatrixStack;
import org.excellent.client.Excellent;
import org.excellent.client.managers.module.Category;
import org.excellent.client.managers.module.Module;
import org.excellent.client.managers.module.impl.client.Theme;
import org.excellent.client.screen.clickgui.ClickGuiScreen;
import org.excellent.client.screen.clickgui.component.WindowComponent;
import org.excellent.client.screen.clickgui.component.module.ModuleComponent;
import org.excellent.client.utils.math.Mathf;
import org.excellent.client.utils.other.Instance;
import org.excellent.client.utils.render.color.ColorUtil;
import org.excellent.client.utils.render.draw.RenderUtil;
import org.excellent.client.utils.render.draw.Round;
import org.excellent.client.utils.render.font.Fonts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class CategoryComponent extends WindowComponent {
    private final Category category;
    private final List<ModuleComponent> moduleComponents = new ArrayList<>();
    private float moduleHeight = 0;
    private final float margin = 5;

    public CategoryComponent(Category category, ClickGuiScreen clickGui) {
        this.category = category;
        this.moduleComponents.addAll(getModules().stream().map(module -> new ModuleComponent(module, clickGui)).toList());
        size.set(clickGui.categoryWidth(), clickGui.categoryHeight());
    }

    private List<Module> getModules() {
        List<Module> modulesList = Instance.get(category);
        modulesList.sort(Comparator.comparing(module -> -font.getWidth(module.getName(), moduleFontSize)));
        return modulesList;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        moduleComponents.forEach(component -> component.resize(minecraft, width, height));
    }

    @Override
    public void init() {
        moduleComponents.forEach(ModuleComponent::init);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Theme theme = Theme.getInstance();
//        theme.drawClientRect(matrix, position.x, position.y, size.x, size.y + moduleHeight, alphaPC(), 4);

        float cx = (float) Mathf.step(position.x, 0.5);
        float cy = (float) Mathf.step(position.y, 0.5);
        float cwidth = (float) Mathf.step(size.x, 0.5);
        float cheight = (float) Mathf.step(size.y + moduleHeight, 0.5);
        float cradius = 4;

        RenderUtil.Shadow.drawShadow(matrix, cx - cradius / 2, cy - cradius / 2, cwidth + cradius, cheight + cradius, 10, ColorUtil.replAlpha(theme.shadowColor(), (float) Math.pow(alphaPC(), 3)));
        RenderUtil.Rounded.smooth(matrix, cx, cy, cwidth, cheight, ColorUtil.replAlpha(backgroundColor(), alphaPC()), Round.of(cradius));

        Fonts.CLICKGUI.draw(matrix, category.getIcon(), position.x + 5, position.y + 5, ColorUtil.multAlpha(theme.iconColor(), alphaPC()), categoryFontSize);
        font.drawCenter(matrix, category.getName(), position.x + (size.x / 2F), position.y + (size.y / 2F) - (categoryFontSize / 2F), ColorUtil.multAlpha(theme.textColor(), alphaPC()), categoryFontSize);

        float offset = 0;
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.position().set(position.x, position.y + size.y + offset);
            component.render(matrix, mouseX, mouseY, partialTicks);
            offset += (float) (component.size().y + (component.expandAnimation().getValue() * component.getSettingHeight()));
        }
        moduleHeight = offset;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.keyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (ModuleComponent component : moduleComponents) {
            if (Excellent.inst().clickGui().searchCheck(component.getModule().getName())) continue;
            component.charTyped(codePoint, modifiers);
        }
        return false;
    }

    @Override
    public void onClose() {
        moduleComponents.forEach(ModuleComponent::onClose);
    }
}