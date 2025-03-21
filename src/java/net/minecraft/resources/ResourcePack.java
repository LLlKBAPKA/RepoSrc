package net.minecraft.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class ResourcePack implements IResourcePack {
    private static final Logger LOGGER = LogManager.getLogger();
    public final File file;

    public ResourcePack(File resourcePackFileIn) {
        this.file = resourcePackFileIn;
    }

    private static String getFullPath(ResourcePackType type, ResourceLocation location) {
        return String.format("%s/%s/%s", type.getDirectoryName(), location.getNamespace(), location.getPath());
    }

    protected static String getRelativeString(File file1, File file2) {
        return file1.toURI().relativize(file2.toURI()).getPath();
    }

    public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
        return this.getInputStream(getFullPath(type, location));
    }

    public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
        return this.resourceExists(getFullPath(type, location));
    }

    protected abstract InputStream getInputStream(String resourcePath) throws IOException;

    public InputStream getRootResourceStream(String fileName) throws IOException {
        if (!fileName.contains("/") && !fileName.contains("\\")) {
            return this.getInputStream(fileName);
        } else {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
    }

    protected abstract boolean resourceExists(String resourcePath);

    protected void onIgnoreNonLowercaseNamespace(String namespace) {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", namespace, this.file);
    }

    @Nullable
    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
        Object object;

        try (InputStream inputstream = this.getInputStream("pack.mcmeta")) {
            object = getResourceMetadata(deserializer, inputstream);
        }

        return (T) object;
    }

    @Nullable
    public static <T> T getResourceMetadata(IMetadataSectionSerializer<T> deserializer, InputStream inputStream) {
        JsonObject jsonobject;

        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            jsonobject = JSONUtils.fromJson(bufferedreader);
        } catch (IOException | JsonParseException jsonparseexception1) {
            LOGGER.error("Couldn't load {} metadata", deserializer.getSectionName(), jsonparseexception1);
            return null;
        }

        if (!jsonobject.has(deserializer.getSectionName())) {
            return null;
        } else {
            try {
                return deserializer.deserialize(JSONUtils.getJsonObject(jsonobject, deserializer.getSectionName()));
            } catch (JsonParseException jsonparseexception1) {
                LOGGER.error("Couldn't load {} metadata", deserializer.getSectionName(), jsonparseexception1);
                return null;
            }
        }
    }

    public String getName() {
        return this.file.getName();
    }
}
