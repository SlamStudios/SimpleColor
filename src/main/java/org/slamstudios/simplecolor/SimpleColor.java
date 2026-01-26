package org.slamstudios.simplecolor;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.slamstudios.simplecolor.listeners.ChatListener;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * SimpleColor Plugin - Chat color library for Hytale.
 *
 * Provides Spigot/Minecraft-style chat color codes with support for:
 * - Legacy color codes (&0-9, &a-f)
 * - Format codes (&l, &o, &n, &m, &r)
 * - Hex colors (&#RRGGBB)
 * - Multi-color gradients (&#color1:color2:color3:...)
 * - Rainbow gradient (&*)
 * - Clickable links (&(url)[text])
 *
 * Permissions:
 * - simplecolor.bypass - Bypass all permission checks
 * - simplecolor.color.* - All colors
 * - simplecolor.color.<n> - Individual color (black, red, etc.)
 * - simplecolor.color.hex - Hex colors
 * - simplecolor.color.gradient - Gradient colors
 * - simplecolor.color.rainbow - Rainbow gradient
 * - simplecolor.format.* - All formats
 * - simplecolor.format.<n> - Individual format (bold, italic, etc.)
 * - simplecolor.link - Clickable links
 */
public class SimpleColor extends JavaPlugin {

    private static SimpleColor instance;
    private static SimpleColorConfig config;

    public SimpleColor(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;
        getLogger().at(Level.INFO).log("SimpleColor is setting up...");

        // Load config
        Path configPath = getDataDirectory().resolve("config.json");
        config = SimpleColorConfig.load(configPath);
        config.save(configPath); // Save to create file with defaults if not exists

        registerListeners();
    }

    @Override
    protected void start() {
        getLogger().at(Level.INFO).log("SimpleColor v1.0.0 started!");
        getLogger().at(Level.INFO).log("Color codes: &0-9, &a-f | Formats: &l, &o, &n, &m, &r");
        getLogger().at(Level.INFO).log("Hex: &#RRGGBB | Gradients: &#c1:c2:c3 | Rainbow: &*");
        getLogger().at(Level.INFO).log("Links: &(url)[text]");
        getLogger().at(Level.INFO).log("Chat parsing: " + (config.isChatParsingEnabled() ? "enabled" : "disabled"));
        getLogger().at(Level.INFO).log("Chat format: " + config.getChatFormat());
    }

    @Override
    protected void shutdown() {
        getLogger().at(Level.INFO).log("SimpleColor shutting down...");
        // Save config on shutdown
        if (config != null) {
            config.save(getDataDirectory().resolve("config.json"));
        }
        instance = null;
        config = null;
    }

    public static SimpleColor getInstance() {
        return instance;
    }

    @Nonnull
    public static SimpleColorConfig getConfig() {
        return config;
    }

    /**
     * Reloads the configuration from disk.
     */
    public void reloadConfig() {
        Path configPath = getDataDirectory().resolve("config.json");
        config = SimpleColorConfig.load(configPath);
        getLogger().at(Level.INFO).log("SimpleColor config reloaded.");
    }

    @Nonnull
    public static Message parse(@Nonnull String input) {
        return ColorParser.parse(input);
    }

    @Nonnull
    public static Message parse(@Nonnull String input, @Nonnull Player player) {
        return ColorParser.parse(input, player);
    }

    @Nonnull
    public static String strip(@Nonnull String input) {
        return ColorParser.stripAll(input);
    }

    public void registerListeners() {
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, ChatListener::onPlayerChat);
    }
}
