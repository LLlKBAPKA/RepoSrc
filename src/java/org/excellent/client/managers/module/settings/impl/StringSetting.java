package org.excellent.client.managers.module.settings.impl;


import lombok.Getter;
import org.excellent.client.managers.module.Module;
import org.excellent.client.managers.module.settings.Setting;

import java.util.function.Supplier;

@Getter
public class StringSetting extends Setting<String> {

    private final boolean onlyNumber;
    private String cachedValue;

    public StringSetting(Module parent, String name, String value) {
        super(parent, name, value);
        this.onlyNumber = false;
        cachedValue = value;
    }

    public StringSetting(Module parent, String name, String value, boolean onlyNumber) {
        super(parent, name, value);
        this.onlyNumber = onlyNumber;
        cachedValue = value;
    }

    @Override
    public StringSetting set(String value) {
        StringSetting set = (StringSetting) super.set(value);
        this.cachedValue = super.getValue();
        return set;
    }

    @Override
    public StringSetting setVisible(Supplier<Boolean> value) {
        return (StringSetting) super.setVisible(value);
    }

    @Override
    public StringSetting onAction(Runnable action) {
        return (StringSetting) super.onAction(() -> {
            action.run();
            this.cachedValue = super.getValue();
        });
    }

    @Override
    public StringSetting onSetVisible(Runnable action) {
        return (StringSetting) super.onSetVisible(action);
    }

    @Override
    public String getValue() {
        if (cachedValue == null) {
            cachedValue = super.getValue();
        }
        return cachedValue;
    }
}
