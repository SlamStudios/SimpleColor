package org.slamstudios.simplecolor.enums;

import org.slamstudios.simplecolor.Permissions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents chat format codes with their permissions.
 */
public enum ChatFormat {
    BOLD('l', "bold"),
    ITALIC('o', "italic"),
    UNDERLINE('n', "underline"),
    MONOSPACE('m', "monospace"),
    RESET('r', "reset");

    public static final char FORMAT_CHAR = '\u00A7';
    public static final char ALT_FORMAT_CHAR = '&';

    private static final Map<Character, ChatFormat> BY_CODE = new HashMap<>();
    private static final Map<String, ChatFormat> BY_NAME = new HashMap<>();

    static {
        for (ChatFormat format : values()) {
            BY_CODE.put(format.code, format);
            BY_NAME.put(format.name.toLowerCase(), format);
        }
    }

    private final char code;
    private final String name;

    ChatFormat(char code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the format code character.
     */
    public char getCode() {
        return code;
    }

    /**
     * Gets the format name.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the permission node for this format.
     */
    @Nonnull
    public String getPermission() {
        return Permissions.FORMAT_PREFIX + name;
    }

    /**
     * Returns the legacy format string (§X).
     */
    @Override
    public String toString() {
        return String.valueOf(FORMAT_CHAR) + code;
    }

    /**
     * Gets a ChatFormat by its code character.
     */
    @Nullable
    public static ChatFormat getByCode(char code) {
        return BY_CODE.get(Character.toLowerCase(code));
    }

    /**
     * Gets a ChatFormat by its name.
     */
    @Nullable
    public static ChatFormat getByName(@Nonnull String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    /**
     * Checks if a character is a valid format code.
     */
    public static boolean isFormatCode(char code) {
        return BY_CODE.containsKey(Character.toLowerCase(code));
    }
}
