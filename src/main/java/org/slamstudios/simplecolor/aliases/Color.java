package org.slamstudios.simplecolor.aliases;

import com.hypixel.hytale.server.core.Message;
import org.slamstudios.simplecolor.enums.ChatColor;

import javax.annotation.Nonnull;

/**
 * Alias for ChatColor - provides access via Color prefix.
 * 
 * Usage:
 * - Color.RED, Color.BLUE, Color.GREEN
 * - Color.hex("#FF5733")
 * - Color.gradient("red", "blue")
 * - Color.RAINBOW
 * - Color.link("url", "text")
 * - Color.translate(message)
 * 
 * Note: This is a color alias class.
 * For the plugin instance, use SimpleColor.getInstance()
 */
public class Color {
    
    // Color constants
    public static final ChatColor BLACK = ChatColor.BLACK;
    public static final ChatColor DARK_BLUE = ChatColor.DARK_BLUE;
    public static final ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
    public static final ChatColor DARK_AQUA = ChatColor.DARK_AQUA;
    public static final ChatColor DARK_RED = ChatColor.DARK_RED;
    public static final ChatColor DARK_PURPLE = ChatColor.DARK_PURPLE;
    public static final ChatColor GOLD = ChatColor.GOLD;
    public static final ChatColor GRAY = ChatColor.GRAY;
    public static final ChatColor DARK_GRAY = ChatColor.DARK_GRAY;
    public static final ChatColor BLUE = ChatColor.BLUE;
    public static final ChatColor GREEN = ChatColor.GREEN;
    public static final ChatColor AQUA = ChatColor.AQUA;
    public static final ChatColor RED = ChatColor.RED;
    public static final ChatColor LIGHT_PURPLE = ChatColor.LIGHT_PURPLE;
    public static final ChatColor YELLOW = ChatColor.YELLOW;
    public static final ChatColor WHITE = ChatColor.WHITE;
    
    // Special codes
    public static final String RAINBOW = ChatColor.RAINBOW;
    
    // Prevent instantiation
    private Color() {}
    
    // Delegate all methods to ChatColor
    
    @Nonnull
    public static String hex(@Nonnull String hexCode) {
        return ChatColor.hex(hexCode);
    }
    
    @Nonnull
    public static String gradient(@Nonnull String... colors) {
        return ChatColor.gradient(colors);
    }
    
    @Nonnull
    public static String link(@Nonnull String url, @Nonnull String text) {
        return ChatColor.link(url, text);
    }
    
    @Nonnull
    public static Message translate(@Nonnull String message) {
        return ChatColor.translate(message);
    }
    
    @Nonnull
    public static String stripColor(@Nonnull String input) {
        return ChatColor.stripColor(input);
    }
    
    @Nonnull
    public static String stripAll(@Nonnull String input) {
        return ChatColor.stripAll(input);
    }
}
