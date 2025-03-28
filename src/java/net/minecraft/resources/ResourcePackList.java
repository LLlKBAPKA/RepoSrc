package net.minecraft.resources;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcePackList implements AutoCloseable {
    private final Set<IPackFinder> packFinders;
    private Map<String, ResourcePackInfo> packNameToInfo = ImmutableMap.of();
    private List<ResourcePackInfo> enabled = ImmutableList.of();
    private final ResourcePackInfo.IFactory packInfoFactory;

    public ResourcePackList(ResourcePackInfo.IFactory iFactory, IPackFinder... iPackFinders) {
        this.packInfoFactory = iFactory;
        this.packFinders = ImmutableSet.copyOf(iPackFinders);
    }

    public ResourcePackList(IPackFinder... p_i241886_1_) {
        this(ResourcePackInfo::new, p_i241886_1_);
    }

    public void reloadPacksFromFinders() {
        List<String> list = this.enabled.stream().map(ResourcePackInfo::getName).collect(ImmutableList.toImmutableList());
        this.close();
        this.packNameToInfo = this.func_232624_g_();
        this.enabled = this.func_232618_b_(list);
    }

    private Map<String, ResourcePackInfo> func_232624_g_() {
        Map<String, ResourcePackInfo> map = Maps.newTreeMap();

        for (IPackFinder ipackfinder : this.packFinders) {
            ipackfinder.findPacks((p_232615_1_) ->
            {
                map.put(p_232615_1_.getName(), p_232615_1_);
            }, this.packInfoFactory);
        }

        return ImmutableMap.copyOf(map);
    }

    public void setEnabledPacks(Collection<String> p_198985_1_) {
        this.enabled = this.func_232618_b_(p_198985_1_);
    }

    private List<ResourcePackInfo> func_232618_b_(Collection<String> p_232618_1_) {
        List<ResourcePackInfo> list = this.func_232620_c_(p_232618_1_).collect(Collectors.toList());

        for (ResourcePackInfo resourcepackinfo : this.packNameToInfo.values()) {
            if (resourcepackinfo.isAlwaysEnabled() && !list.contains(resourcepackinfo)) {
                resourcepackinfo.getPriority().insert(list, resourcepackinfo, Functions.identity(), false);
            }
        }

        return ImmutableList.copyOf(list);
    }

    private Stream<ResourcePackInfo> func_232620_c_(Collection<String> p_232620_1_) {
        return p_232620_1_.stream().map(this.packNameToInfo::get).filter(Objects::nonNull);
    }

    public Collection<String> func_232616_b_() {
        return this.packNameToInfo.keySet();
    }

    public Collection<ResourcePackInfo> getAllPacks() {
        return this.packNameToInfo.values();
    }

    public Collection<String> func_232621_d_() {
        return this.enabled.stream().map(ResourcePackInfo::getName).collect(ImmutableSet.toImmutableSet());
    }

    public Collection<ResourcePackInfo> getEnabledPacks() {
        return this.enabled;
    }

    @Nullable
    public ResourcePackInfo getPackInfo(String name) {
        return this.packNameToInfo.get(name);
    }

    public void close() {
        this.packNameToInfo.values().forEach(ResourcePackInfo::close);
    }

    public boolean contains(String name) {
        return this.packNameToInfo.containsKey(name);
    }

    public List<IResourcePack> getResourcePacks() {
        return this.enabled.stream().map(ResourcePackInfo::getResourcePack).collect(ImmutableList.toImmutableList());
    }
}
