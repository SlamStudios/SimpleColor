package org.slamstudios.simplecolor;

/**
 * Permission constants for SimpleColor.
 */
public final class Permissions {

    private Permissions() {}

    /**
     * Base permission prefix.
     */
    public static final String PREFIX = "simplecolor.";

    /**
     * Permission prefix for colors.
     */
    public static final String COLOR_PREFIX = PREFIX + "color.";

    /**
     * Permission prefix for formats.
     */
    public static final String FORMAT_PREFIX = PREFIX + "format.";

    /**
     * Permission for all colors.
     */
    public static final String ALL_COLORS = COLOR_PREFIX + "*";

    /**
     * Permission for all formats.
     */
    public static final String ALL_FORMATS = FORMAT_PREFIX + "*";

    /**
     * Permission for hex colors.
     */
    public static final String HEX = COLOR_PREFIX + "hex";

    /**
     * Permission for gradient colors.
     */
    public static final String GRADIENT = COLOR_PREFIX + "gradient";

    /**
     * Permission for rainbow gradient.
     */
    public static final String RAINBOW = COLOR_PREFIX + "rainbow";

    /**
     * Permission for links.
     */
    public static final String LINK = PREFIX + "link";

    /**
     * Permission to bypass all permission checks.
     */
    public static final String BYPASS = PREFIX + "bypass";

    // Individual color permissions
    public static final String COLOR_BLACK = COLOR_PREFIX + "black";
    public static final String COLOR_DARK_BLUE = COLOR_PREFIX + "dark_blue";
    public static final String COLOR_DARK_GREEN = COLOR_PREFIX + "dark_green";
    public static final String COLOR_DARK_AQUA = COLOR_PREFIX + "dark_aqua";
    public static final String COLOR_DARK_RED = COLOR_PREFIX + "dark_red";
    public static final String COLOR_DARK_PURPLE = COLOR_PREFIX + "dark_purple";
    public static final String COLOR_GOLD = COLOR_PREFIX + "gold";
    public static final String COLOR_GRAY = COLOR_PREFIX + "gray";
    public static final String COLOR_DARK_GRAY = COLOR_PREFIX + "dark_gray";
    public static final String COLOR_BLUE = COLOR_PREFIX + "blue";
    public static final String COLOR_GREEN = COLOR_PREFIX + "green";
    public static final String COLOR_AQUA = COLOR_PREFIX + "aqua";
    public static final String COLOR_RED = COLOR_PREFIX + "red";
    public static final String COLOR_LIGHT_PURPLE = COLOR_PREFIX + "light_purple";
    public static final String COLOR_YELLOW = COLOR_PREFIX + "yellow";
    public static final String COLOR_WHITE = COLOR_PREFIX + "white";

    // Individual format permissions
    public static final String FORMAT_BOLD = FORMAT_PREFIX + "bold";
    public static final String FORMAT_ITALIC = FORMAT_PREFIX + "italic";
    public static final String FORMAT_UNDERLINE = FORMAT_PREFIX + "underline";
    public static final String FORMAT_MONOSPACE = FORMAT_PREFIX + "monospace";
    public static final String FORMAT_RESET = FORMAT_PREFIX + "reset";
}
