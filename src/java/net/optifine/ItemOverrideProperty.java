package net.optifine;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class ItemOverrideProperty {
    private final ResourceLocation location;
    private final float[] values;

    public ItemOverrideProperty(ResourceLocation location, float[] values) {
        this.location = location;
        this.values = values.clone();
        Arrays.sort(this.values);
    }

    public Integer getValueIndex(ItemStack stack, ClientWorld world, LivingEntity entity) {
        Item item = stack.getItem();
        IItemPropertyGetter iitempropertygetter = ItemModelsProperties.func_239417_a_(item, this.location);

        if (iitempropertygetter == null) {
            return null;
        } else {
            float f = iitempropertygetter.call(stack, world, entity);
            int i = Arrays.binarySearch(this.values, f);
            return i;
        }
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public float[] getValues() {
        return this.values;
    }

    public String toString() {
        return "location: " + this.location + ", values: [" + Config.arrayToString(this.values) + "]";
    }
}
