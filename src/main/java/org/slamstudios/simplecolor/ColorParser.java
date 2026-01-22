package org.slamstudios.simplecolor;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses color codes, gradients, and links from text.
 *
 * Supported formats:
 * - &X or §X for legacy color codes (0-9, a-f)
 * - &l, &o, &n, &m, &r for formats (bold, italic, underline, monospace, reset)
 * - &#RRGGBB for hex colors
 * - &#color1:color2:color3:... for multi-color gradients (applies to following text until next color code)
 * - &* for rainbow gradient (applies to following text until next color code)
 * - &(<url>)[text] for clickable links
 */
public final class ColorParser {

    private ColorParser() {}

    // Rainbow colors
    private static final List<Color> RAINBOW_COLORS = Arrays.asList(
            new Color(255, 0, 0),     // Red
            new Color(255, 127, 0),   // Orange
            new Color(255, 255, 0),   // Yellow
            new Color(0, 255, 0),     // Green
            new Color(0, 255, 255),   // Cyan
            new Color(0, 0, 255),     // Blue
            new Color(139, 0, 255)    // Violet
    );

    // Pattern for hex colors: &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("&?#([0-9a-fA-F]{6})");

    // Pattern for multi-color gradients: &#color1:color2:color3:... (applies to following text)
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("&#((?:[0-9a-fA-F]{6}|[a-zA-Z_]+)(?::(?:[0-9a-fA-F]{6}|[a-zA-Z_]+))+)");

    // Pattern for rainbow: &* (applies to following text until next color code)
    private static final char RAINBOW_CODE = '*';

    // Pattern for links: &(<url>)[text]
    private static final Pattern LINK_PATTERN = Pattern.compile("&\\(([^)]+)\\)\\[([^\\]]+)]");

    // Pattern for legacy color codes: &X or §X
    private static final Pattern LEGACY_PATTERN = Pattern.compile("[&§]([0-9a-fA-FklmnoKLMNOrR*])");

    /**
     * Parses a string and returns a formatted Message.
     * This method does not check permissions.
     *
     * @param input the input string with color codes
     * @return the formatted Message
     */
    @Nonnull
    public static Message parse(@Nonnull String input) {
        return parse(input, null);
    }

    /**
     * Parses a string and returns a formatted Message.
     * Checks permissions if a player is provided.
     *
     * @param input the input string with color codes
     * @param player the player to check permissions for (or null to skip checks)
     * @return the formatted Message
     */
    @Nonnull
    public static Message parse(@Nonnull String input, @Nullable Player player) {
        List<MessageSegment> segments = new ArrayList<>();
        String remaining = input;

        // Process links first
        remaining = processLinks(remaining, segments, player);

        // Process gradients
        remaining = processGradients(remaining, segments, player);

        // Process remaining text with colors and formats
        processColorsAndFormats(remaining, segments, player);

        return buildMessage(segments);
    }

    /**
     * Processes link patterns in the input.
     */
    private static String processLinks(String input, List<MessageSegment> segments, @Nullable Player player) {
        if (player != null && !hasPermission(player, Permissions.LINK)) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        Matcher matcher = LINK_PATTERN.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            result.append(input, lastEnd, matcher.start());

            String url = matcher.group(1);
            String text = matcher.group(2);

            // Parse colors in the link text
            MessageSegment linkSegment = new MessageSegment(text);
            linkSegment.link = url;
            segments.add(new MessageSegment(result.toString()));
            result.setLength(0);
            segments.add(linkSegment);

            lastEnd = matcher.end();
        }

        result.append(input.substring(lastEnd));
        return result.toString();
    }

    /**
     * Processes gradient patterns in the input - now just passes through since gradients
     * are handled inline in processColorsAndFormats like rainbow mode.
     */
    private static String processGradients(String input, List<MessageSegment> segments, @Nullable Player player) {
        // Gradients are now handled inline in processColorsAndFormats
        return input;
    }

    /**
     * Processes standard color codes and format codes.
     */
    private static void processColorsAndFormats(String input, List<MessageSegment> segments, @Nullable Player player) {
        StringBuilder currentText = new StringBuilder();
        Color currentColor = null;
        boolean bold = false;
        boolean italic = false;
        boolean underline = false;
        boolean monospace = false;
        boolean rainbowMode = false;
        boolean gradientMode = false;
        List<Color> gradientColors = null;
        StringBuilder gradientText = new StringBuilder();
        StringBuilder rainbowText = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            // Check for color/format codes
            if ((c == '&' || c == '§') && i + 1 < input.length()) {
                char next = input.charAt(i + 1);

                // Check for rainbow code: &*
                if (next == RAINBOW_CODE) {
                    if (player == null || hasPermission(player, Permissions.RAINBOW)) {
                        // Flush current text first
                        flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
                        currentText.setLength(0);
                        rainbowText.setLength(0);
                        gradientText.setLength(0);
                        rainbowMode = true;
                        gradientMode = false;
                        gradientColors = null;
                        currentColor = null;
                    }
                    i += 2;
                    continue;
                }

                // Check for gradient: &#hex1:hex2:... or &#name1:name2:...
                if (next == '#' && i + 2 < input.length()) {
                    Matcher gradMatcher = GRADIENT_PATTERN.matcher(input.substring(i));
                    if (gradMatcher.lookingAt()) {
                        if (player == null || hasPermission(player, Permissions.GRADIENT)) {
                            String colorsStr = gradMatcher.group(1);
                            String[] colorParts = colorsStr.split(":");
                            List<Color> colors = new ArrayList<>();
                            boolean valid = true;

                            for (String colorPart : colorParts) {
                                Color color = ChatColor.parseColor(colorPart);
                                if (color != null) {
                                    colors.add(color);
                                } else {
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid && colors.size() >= 2) {
                                // Flush current text
                                flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
                                currentText.setLength(0);
                                rainbowText.setLength(0);
                                gradientText.setLength(0);
                                rainbowMode = false;
                                gradientMode = true;
                                gradientColors = colors;
                                currentColor = null;
                                i += gradMatcher.end();
                                continue;
                            }
                        }
                    }

                    // Check for simple hex color: &#RRGGBB (not a gradient)
                    if (i + 8 <= input.length()) {
                        String hexPart = input.substring(i + 2, i + 8);
                        if (hexPart.matches("[0-9a-fA-F]{6}") && (i + 8 >= input.length() || input.charAt(i + 8) != ':')) {
                            if (player == null || hasPermission(player, Permissions.HEX)) {
                                // Flush text
                                flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
                                currentText.setLength(0);
                                rainbowText.setLength(0);
                                gradientText.setLength(0);
                                rainbowMode = false;
                                gradientMode = false;
                                gradientColors = null;
                                currentColor = ChatColor.parseHex(hexPart);
                            }
                            i += 8;
                            continue;
                        }
                    }
                }

                // Check for legacy color code
                ChatColor color = ChatColor.getByCode(next);
                if (color != null) {
                    if (player == null || hasPermission(player, Permissions.ALL_COLORS) || hasPermission(player, color.getPermission())) {
                        // Flush text
                        flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
                        currentText.setLength(0);
                        rainbowText.setLength(0);
                        gradientText.setLength(0);
                        rainbowMode = false;
                        gradientMode = false;
                        gradientColors = null;
                        currentColor = color.getColor();
                    }
                    i += 2;
                    continue;
                }

                // Check for format code
                ChatFormat format = ChatFormat.getByCode(next);
                if (format != null) {
                    if (player == null || hasPermission(player, Permissions.ALL_FORMATS) || hasPermission(player, format.getPermission())) {
                        // Flush text
                        flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
                        currentText.setLength(0);
                        rainbowText.setLength(0);
                        gradientText.setLength(0);

                        switch (format) {
                            case BOLD -> bold = true;
                            case ITALIC -> italic = true;
                            case UNDERLINE -> underline = true;
                            case MONOSPACE -> monospace = true;
                            case RESET -> {
                                rainbowMode = false;
                                gradientMode = false;
                                gradientColors = null;
                                currentColor = null;
                                bold = false;
                                italic = false;
                                underline = false;
                                monospace = false;
                            }
                        }
                    }
                    i += 2;
                    continue;
                }
            }

            // Append character to appropriate buffer
            if (rainbowMode) {
                rainbowText.append(c);
            } else if (gradientMode) {
                gradientText.append(c);
            } else {
                currentText.append(c);
            }
            i++;
        }

        // Flush remaining text
        flushText(segments, currentText, rainbowText, gradientText, rainbowMode, gradientMode, gradientColors, currentColor, bold, italic, underline, monospace);
    }

    /**
     * Flushes accumulated text to segments.
     */
    private static void flushText(List<MessageSegment> segments, StringBuilder currentText, StringBuilder rainbowText,
                                   StringBuilder gradientText, boolean rainbowMode, boolean gradientMode,
                                   List<Color> gradientColors, Color currentColor, boolean bold, boolean italic,
                                   boolean underline, boolean monospace) {
        if (rainbowMode && !rainbowText.isEmpty()) {
            // Generate rainbow gradient for the text
            String text = rainbowText.toString();
            List<Color> colors = GradientUtil.generateMultiGradient(RAINBOW_COLORS, text.length());
            for (int j = 0; j < text.length(); j++) {
                MessageSegment seg = new MessageSegment(String.valueOf(text.charAt(j)));
                seg.color = colors.get(j);
                seg.bold = bold;
                seg.italic = italic;
                seg.underline = underline;
                seg.monospace = monospace;
                segments.add(seg);
            }
        } else if (gradientMode && !gradientText.isEmpty() && gradientColors != null) {
            // Generate custom gradient for the text
            String text = gradientText.toString();
            List<Color> colors = GradientUtil.generateMultiGradient(gradientColors, text.length());
            for (int j = 0; j < text.length(); j++) {
                MessageSegment seg = new MessageSegment(String.valueOf(text.charAt(j)));
                seg.color = colors.get(j);
                seg.bold = bold;
                seg.italic = italic;
                seg.underline = underline;
                seg.monospace = monospace;
                segments.add(seg);
            }
        } else if (!currentText.isEmpty()) {
            MessageSegment seg = new MessageSegment(currentText.toString());
            seg.color = currentColor;
            seg.bold = bold;
            seg.italic = italic;
            seg.underline = underline;
            seg.monospace = monospace;
            segments.add(seg);
        }
    }

    /**
     * Builds the final Message from segments.
     */
    private static Message buildMessage(List<MessageSegment> segments) {
        if (segments.isEmpty()) {
            return Message.raw("");
        }

        Message result = null;

        for (MessageSegment segment : segments) {
            if (segment.text.isEmpty() && segment.link == null) {
                continue;
            }

            Message msg = Message.raw(segment.text);

            if (segment.color != null) {
                msg = msg.color(segment.color);
            }
            if (segment.bold) {
                msg = msg.bold(true);
            }
            if (segment.italic) {
                msg = msg.italic(true);
            }
            if (segment.monospace) {
                msg = msg.monospace(true);
            }
            if (segment.link != null) {
                msg = msg.link(segment.link);
            }

            if (result == null) {
                result = msg;
            } else {
                result = result.insert(msg);
            }
        }

        return result != null ? result : Message.raw("");
    }

    /**
     * Checks if a player has a permission.
     */
    private static boolean hasPermission(@Nonnull Player player, @Nonnull String permission) {
        // Check bypass permission first
        if (player.hasPermission(Permissions.BYPASS)) {
            return true;
        }

        // Check wildcard permissions
        if (permission.startsWith(Permissions.COLOR_PREFIX) && player.hasPermission(Permissions.ALL_COLORS)) {
            return true;
        }
        if (permission.startsWith(Permissions.FORMAT_PREFIX) && player.hasPermission(Permissions.ALL_FORMATS)) {
            return true;
        }

        return player.hasPermission(permission);
    }

    /**
     * Strips all color and format codes from a string.
     *
     * @param input the input string
     * @return the stripped string
     */
    @Nonnull
    public static String stripAll(@Nonnull String input) {
        String result = input;

        // Remove links
        result = LINK_PATTERN.matcher(result).replaceAll("$2");

        // Remove gradients (now without braces)
        result = GRADIENT_PATTERN.matcher(result).replaceAll("");

        // Remove hex codes
        result = HEX_PATTERN.matcher(result).replaceAll("");

        // Remove legacy codes
        result = LEGACY_PATTERN.matcher(result).replaceAll("");

        return result;
    }

    /**
     * Internal class representing a message segment.
     */
    private static class MessageSegment {
        String text;
        Color color;
        boolean bold;
        boolean italic;
        boolean underline;
        boolean monospace;
        String link;

        MessageSegment(String text) {
            this.text = text;
        }
    }
}
