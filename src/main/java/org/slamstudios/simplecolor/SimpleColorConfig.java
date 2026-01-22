package org.slamstudios.simplecolor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration for SimpleColor plugin.
 */
public class SimpleColorConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean chatParsingEnabled = true;
    private String chatFormat = "{player}: {message}";

    public SimpleColorConfig() {}

    /**
     * Whether chat color parsing is enabled.
     */
    public boolean isChatParsingEnabled() {
        return chatParsingEnabled;
    }

    /**
     * Sets whether chat color parsing is enabled.
     */
    public void setChatParsingEnabled(boolean enabled) {
        this.chatParsingEnabled = enabled;
    }

    /**
     * Gets the chat format. Supports placeholders:
     * - {player} - Player name
     * - {message} - The message content (will be color-parsed if enabled)
     */
    @Nonnull
    public String getChatFormat() {
        return chatFormat;
    }

    /**
     * Sets the chat format.
     */
    public void setChatFormat(@Nonnull String format) {
        this.chatFormat = format;
    }

    /**
     * Loads config from a JSON file, or creates default if not exists.
     */
    @Nonnull
    public static SimpleColorConfig load(@Nonnull Path configPath) {
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                SimpleColorConfig config = GSON.fromJson(json, SimpleColorConfig.class);
                return config != null ? config : new SimpleColorConfig();
            } catch (IOException e) {
                return new SimpleColorConfig();
            }
        }
        return new SimpleColorConfig();
    }

    /**
     * Saves config to a JSON file.
     */
    public void save(@Nonnull Path configPath) {
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(this));
        } catch (IOException e) {
            // Log error if needed
        }
    }
}
