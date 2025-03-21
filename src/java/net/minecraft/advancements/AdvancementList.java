package net.minecraft.advancements;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

public class AdvancementList {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, Advancement> advancements = Maps.newHashMap();
    private final Set<Advancement> roots = Sets.newLinkedHashSet();
    private final Set<Advancement> nonRoots = Sets.newLinkedHashSet();
    private AdvancementList.IListener listener;

    private void remove(Advancement advancementIn) {
        for (Advancement advancement : advancementIn.getChildren()) {
            this.remove(advancement);
        }

        LOGGER.info("Forgot about advancement {}", advancementIn.getId());
        this.advancements.remove(advancementIn.getId());

        if (advancementIn.getParent() == null) {
            this.roots.remove(advancementIn);

            if (this.listener != null) {
                this.listener.rootAdvancementRemoved(advancementIn);
            }
        } else {
            this.nonRoots.remove(advancementIn);

            if (this.listener != null) {
                this.listener.nonRootAdvancementRemoved(advancementIn);
            }
        }
    }

    public void removeAll(Set<ResourceLocation> ids) {
        for (ResourceLocation resourcelocation : ids) {
            Advancement advancement = this.advancements.get(resourcelocation);

            if (advancement == null) {
                LOGGER.warn("Told to remove advancement {} but I don't know what that is", resourcelocation);
            } else {
                this.remove(advancement);
            }
        }
    }

    public void loadAdvancements(Map<ResourceLocation, Advancement.Builder> advancementsIn) {
        Function<ResourceLocation, Advancement> function = Functions.forMap(this.advancements, null);

        while (!advancementsIn.isEmpty()) {
            boolean flag = false;
            Iterator<Entry<ResourceLocation, Advancement.Builder>> iterator = advancementsIn.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<ResourceLocation, Advancement.Builder> entry = iterator.next();
                ResourceLocation resourcelocation = entry.getKey();
                Advancement.Builder advancement$builder = entry.getValue();

                if (advancement$builder.resolveParent(function)) {
                    Advancement advancement = advancement$builder.build(resourcelocation);
                    this.advancements.put(resourcelocation, advancement);
                    flag = true;
                    iterator.remove();

                    if (advancement.getParent() == null) {
                        this.roots.add(advancement);

                        if (this.listener != null) {
                            this.listener.rootAdvancementAdded(advancement);
                        }
                    } else {
                        this.nonRoots.add(advancement);

                        if (this.listener != null) {
                            this.listener.nonRootAdvancementAdded(advancement);
                        }
                    }
                }
            }

            if (!flag) {
                for (Entry<ResourceLocation, Advancement.Builder> entry1 : advancementsIn.entrySet()) {
                    LOGGER.error("Couldn't load advancement {}: {}", entry1.getKey(), entry1.getValue());
                }

                break;
            }
        }

        LOGGER.info("Loaded {} advancements", this.advancements.size());
    }

    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.nonRoots.clear();

        if (this.listener != null) {
            this.listener.advancementsCleared();
        }
    }

    public Iterable<Advancement> getRoots() {
        return this.roots;
    }

    public Collection<Advancement> getAll() {
        return this.advancements.values();
    }

    @Nullable
    public Advancement getAdvancement(ResourceLocation id) {
        return this.advancements.get(id);
    }

    public void setListener(@Nullable AdvancementList.IListener listenerIn) {
        this.listener = listenerIn;

        if (listenerIn != null) {
            for (Advancement advancement : this.roots) {
                listenerIn.rootAdvancementAdded(advancement);
            }

            for (Advancement advancement1 : this.nonRoots) {
                listenerIn.nonRootAdvancementAdded(advancement1);
            }
        }
    }

    public interface IListener {
        void rootAdvancementAdded(Advancement advancementIn);

        void rootAdvancementRemoved(Advancement advancementIn);

        void nonRootAdvancementAdded(Advancement advancementIn);

        void nonRootAdvancementRemoved(Advancement advancementIn);

        void advancementsCleared();
    }
}
