package net.minecraft.server;

import net.minecraft.util.SharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ServerEula {
    private static final Logger LOG = LogManager.getLogger();
    private final Path eulaFile;
    private final boolean acceptedEULA;

    public ServerEula(Path file) {
        this.eulaFile = file;
        this.acceptedEULA = SharedConstants.developmentMode || this.loadEulaStatus();
    }

    private boolean loadEulaStatus() {
        try (InputStream inputstream = Files.newInputStream(this.eulaFile)) {
            Properties properties = new Properties();
            properties.load(inputstream);
            return Boolean.parseBoolean(properties.getProperty("eula", "false"));
        } catch (Exception exception) {
            LOG.warn("Failed to load {}", this.eulaFile);
            this.createEULAFile();
            return false;
        }
    }

    public boolean hasAcceptedEULA() {
        return this.acceptedEULA;
    }

    private void createEULAFile() {
        if (!SharedConstants.developmentMode) {
            try (OutputStream outputstream = Files.newOutputStream(this.eulaFile)) {
                Properties properties = new Properties();
                properties.setProperty("eula", "false");
                properties.store(outputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
            } catch (Exception exception) {
                LOG.warn("Failed to save {}", this.eulaFile, exception);
            }
        }
    }
}
