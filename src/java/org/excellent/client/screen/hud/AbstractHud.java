package org.excellent.client.screen.hud;

import org.excellent.client.managers.module.impl.client.Theme;
import org.excellent.client.utils.animation.Animation;
import org.excellent.client.utils.animation.util.Easings;
import org.excellent.client.utils.render.color.ColorUtil;

public abstract class AbstractHud {
    Animation animation = new Animation();

    public void update(float value) {
        animation.update();
        animation.run(value, 0.25, Easings.SINE_OUT, true);
    }

    public float animValue() {
        return animation.get();
    }

    public int clientColor() {
        return ColorUtil.multAlpha(theme().clientColor(), animValue());
    }

    public int textColor() {
        return ColorUtil.multAlpha(theme().textColor(), animValue());
    }

    public int textAccentColor() {
        return ColorUtil.multAlpha(theme().textAccentColor(), animValue());
    }

    public int iconColor() {
        return ColorUtil.multAlpha(theme().iconColor(), animValue());
    }

    public Theme theme() {
        return Theme.getInstance();
    }
}
