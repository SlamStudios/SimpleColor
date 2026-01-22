package org.slamstudios.simplecolor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents chat colors with their legacy codes and permissions.
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

    /**
     * Strips all color codes from a string.
     */
    @Nonnull
    public static String stripColor(@Nonnull String input) {
        return input.replaceAll("[" + COLOR_CHAR + ALT_COLOR_CHAR + "][0-9a-fA-Fk-oK-OrR]", "");
    }

    /**
     * Translates alternate color codes to section symbols.
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
}
