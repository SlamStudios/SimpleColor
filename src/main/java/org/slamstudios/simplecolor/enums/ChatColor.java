package org.slamstudios.simplecolor.enums;

import com.hypixel.hytale.server.core.Message;
import org.slamstudios.simplecolor.ColorParser;
import org.slamstudios.simplecolor.Permissions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents chat colors with their legacy codes and permissions.
 * 
 * Supports:
 * - Legacy colors: ChatColor.RED, ChatColor.BLUE, etc.
 * - Hex colors: ChatColor.hex("#FF5733")
 * - Gradients: ChatColor.gradient("red", "blue", "green")
 * - Rainbow: ChatColor.RAINBOW
 * - Links: ChatColor.link("https://example.com", "Click Here")
 * - Translation: ChatColor.translate(message)
 * 
 * Aliases available:
 * - ChatColor.RED or CC.RED or SimpleColor.RED or SC.RED
 */
public enum ChatColor {
    BLACK('0', "black", new Color(0, 0, 0)),
    DARK_BLUE('1', "dark_blue", new Color(0, 0, 170)),
    DARK_GREEN('2', "dark_green", new Color(0, 170, 0)),
    DARK_AQUA('3', "dark_aqua", new Color(0, 170, 170)),
    DARK_RED('4', "dark_red", new Color(170, 0, 0)),
    DARK_PURPLE('5', "dark_purple", new Color(170, 0, 170)),
    GOLD('6', "gold", new Color(255, 170, 0)),
    GRAY('7', "gray", new Color(170, 170, 170)),
    DARK_GRAY('8', "dark_gray", new Color(85, 85, 85)),
    BLUE('9', "blue", new Color(85, 85, 255)),
    GREEN('a', "green", new Color(85, 255, 85)),
    AQUA('b', "aqua", new Color(85, 255, 255)),
    RED('c', "red", new Color(255, 85, 85)),
    LIGHT_PURPLE('d', "light_purple", new Color(255, 85, 255)),
    YELLOW('e', "yellow", new Color(255, 255, 85)),
    WHITE('f', "white", new Color(255, 255, 255));

    public static final char COLOR_CHAR = '\u00A7';
    public static final char ALT_COLOR_CHAR = '&';
    
    /** Rainbow gradient code */
    public static final String RAINBOW = "&*";

    private static final Map<Character, ChatColor> BY_CODE = new HashMap<>();
    private static final Map<String, ChatColor> BY_NAME = new HashMap<>();

    static {
        for (ChatColor color : values()) {
            BY_CODE.put(color.code, color);
            BY_NAME.put(color.name.toLowerCase(), color);
        }
    }

    private final char code;
    private final String name;
    private final Color color;

    ChatColor(char code, String name, Color color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the legacy color code character.
     */
    public char getCode() {
        return code;
    }

    /**
     * Gets the color name.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the RGB color.
     */
    @Nonnull
    public Color getColor() {
        return color;
    }

    /**
     * Gets the permission node for this color.
     */
    @Nonnull
    public String getPermission() {
        return Permissions.COLOR_PREFIX + name;
    }

    /**
     * Returns the legacy format string (§X).
     */
    @Override
    public String toString() {
        return String.valueOf(COLOR_CHAR) + code;
    }

    /**
     * Gets a ChatColor by its code character.
     */
    @Nullable
    public static ChatColor getByCode(char code) {
        return BY_CODE.get(Character.toLowerCase(code));
    }

    /**
     * Gets a ChatColor by its name.
     */
    @Nullable
    public static ChatColor getByName(@Nonnull String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    // ==================== HEX COLORS ====================

    /**
     * Creates a hex color string from a hex code.
     * 
     * Example: ChatColor.hex("#FF5733") or ChatColor.hex("FF5733")
     * 
     * @param hexCode The hex color code (with or without #)
     * @return Color code string that can be used in messages
     */
    @Nonnull
    public static String hex(@Nonnull String hexCode) {
        String cleanHex = hexCode.startsWith("#") ? hexCode.substring(1) : hexCode;
        if (cleanHex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color: " + hexCode + " (must be 6 characters)");
        }
        
        // Validate hex characters
        if (!cleanHex.matches("[0-9a-fA-F]{6}")) {
            throw new IllegalArgumentException("Invalid hex color: " + hexCode + " (must contain only 0-9, a-f)");
        }
        
        // Format as &#RRGGBB for the parser
        return "&#" + cleanHex;
    }

    /**
     * Parses a color from a hex string (#RRGGBB or RRGGBB).
     */
    @Nonnull
    public static Color parseHex(@Nonnull String hex) {
        String cleanHex = hex.startsWith("#") ? hex.substring(1) : hex;
        if (cleanHex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }
        return new Color(
                Integer.parseInt(cleanHex.substring(0, 2), 16),
                Integer.parseInt(cleanHex.substring(2, 4), 16),
                Integer.parseInt(cleanHex.substring(4, 6), 16)
        );
    }

    /**
     * Converts a Color to hex string.
     */
    @Nonnull
    public static String toHex(@Nonnull Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // ==================== GRADIENTS ====================

    /**
     * Creates a gradient color code from multiple colors.
     * 
     * Examples:
     * - ChatColor.gradient("red", "blue")
     * - ChatColor.gradient("#FF5733", "#3498DB", "#2ECC71")
     * - ChatColor.gradient("blue", "red", "green", "yellow")
     * 
     * @param colors Color names (e.g., "red", "blue") or hex codes (e.g., "#FF5733")
     * @return Gradient color code string
     */
    @Nonnull
    public static String gradient(@Nonnull String... colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("Gradient requires at least 2 colors");
        }
        
        // Build gradient code: &#color1:color2:color3
        StringBuilder gradient = new StringBuilder("&#");
        for (int i = 0; i < colors.length; i++) {
            if (i > 0) gradient.append(":");
            
            String color = colors[i];
            // Check if it's a hex code
            if (color.startsWith("#")) {
                gradient.append(color.substring(1)); // Remove # from hex
            } else {
                // Try to resolve as color name
                ChatColor namedColor = getByName(color);
                if (namedColor != null) {
                    gradient.append(toHex(namedColor.getColor()));
                } else {
                    // Assume it's a hex without #
                    gradient.append(color);
                }
            }
        }
        
        return gradient.toString();
    }

    /**
     * Parses a color from name or hex.
     */
    @Nullable
    public static Color parseColor(@Nonnull String input) {
        ChatColor named = getByName(input);
        if (named != null) {
            return named.getColor();
        }
        try {
            return parseHex(input);
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== LINKS ====================

    /**
     * Creates a clickable link in chat.
     * 
     * Example: ChatColor.link("https://example.com", "Click Here!")
     * 
     * @param url The URL to link to
     * @param text The text to display
     * @return Formatted link string
     */
    @Nonnull
    public static String link(@Nonnull String url, @Nonnull String text) {
        // Format: &(url)[text]
        return "&(" + url + ")[" + text + "]";
    }

    // ==================== TRANSLATION ====================

    /**
     * Translates color codes in a message and returns a Message object.
     * 
     * Supports:
     * - Legacy codes: &a, &c, &f, etc.
     * - Hex colors: &#FF5733
     * - Gradients: &#red:blue or &#FF5733:3498DB
     * - Rainbow: &*
     * - Links: &(url)[text]
     * 
     * Example: ChatColor.translate("&aGreen &cRed &#FF5733Hex")
     * 
     * @param message The message with color codes
     * @return Parsed Message object ready to send
     */
    @Nonnull
    public static Message translate(@Nonnull String message) {
        return ColorParser.parse(message);
    }

    /**
     * Strips all color codes from a string.
     */
    @Nonnull
    public static String stripColor(@Nonnull String input) {
        return input.replaceAll("[" + COLOR_CHAR + ALT_COLOR_CHAR + "][0-9a-fA-Fk-oK-OrR]", "");
    }

    /**
     * Strips all color codes including hex, gradients, and rainbow.
     */
    @Nonnull
    public static String stripAll(@Nonnull String input) {
        return ColorParser.stripAll(input);
    }

    /**
     * Translates alternate color codes to section symbols.
     * 
     * @param altChar The alternate character (usually '&')
     * @param textToTranslate The text to translate
     * @return Translated text with § symbols
     */
    @Nonnull
    public static String translateAlternateColorCodes(char altChar, @Nonnull String textToTranslate) {
        char[] chars = textToTranslate.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == altChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) {
                chars[i] = COLOR_CHAR;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }

    // ==================== CONVENIENCE METHODS ====================

    /**
     * Applies this color to text.
     * 
     * Example: ChatColor.RED.apply("Error message")
     * 
     * @param text The text to colorize
     * @return Colored text string
     */
    @Nonnull
    public String apply(@Nonnull String text) {
        return this.toString() + text;
    }

    /**
     * Combines this color with text and returns a Message.
     * 
     * Example: ChatColor.GREEN.message("Success!")
     * 
     * @param text The text to colorize
     * @return Message object ready to send
     */
    @Nonnull
    public Message message(@Nonnull String text) {
        return translate(this.toString() + text);
    }
}
