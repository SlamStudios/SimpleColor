package org.slamstudios.simplecolor;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating color gradients.
 */
public final class GradientUtil {

    private GradientUtil() {}

    /**
     * Generates a list of colors forming a gradient between start and end colors.
     *
     * @param start the starting color
     * @param end the ending color
     * @param steps the number of steps (characters) in the gradient
     * @return list of colors for each step
     */
    @Nonnull
    public static List<Color> generateGradient(@Nonnull Color start, @Nonnull Color end, int steps) {
        List<Color> gradient = new ArrayList<>(steps);

        if (steps <= 1) {
            gradient.add(start);
            return gradient;
        }

        for (int i = 0; i < steps; i++) {
            float ratio = (float) i / (steps - 1);
            int red = Math.round(start.getRed() + ratio * (end.getRed() - start.getRed()));
            int green = Math.round(start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
            int blue = Math.round(start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
            gradient.add(new Color(clamp(red), clamp(green), clamp(blue)));
        }

        return gradient;
    }

    /**
     * Generates a multi-color gradient through multiple color stops.
     *
     * @param colors the color stops
     * @param steps the total number of steps
     * @return list of colors for each step
     */
    @Nonnull
    public static List<Color> generateMultiGradient(@Nonnull List<Color> colors, int steps) {
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("At least one color is required");
        }
        if (colors.size() == 1) {
            List<Color> result = new ArrayList<>(steps);
            for (int i = 0; i < steps; i++) {
                result.add(colors.getFirst());
            }
            return result;
        }

        List<Color> gradient = new ArrayList<>(steps);
        int segments = colors.size() - 1;
        float stepsPerSegment = (float) steps / segments;

        for (int i = 0; i < steps; i++) {
            float position = i / stepsPerSegment;
            int segmentIndex = Math.min((int) position, segments - 1);
            float segmentRatio = position - segmentIndex;

            Color startColor = colors.get(segmentIndex);
            Color endColor = colors.get(segmentIndex + 1);

            int red = Math.round(startColor.getRed() + segmentRatio * (endColor.getRed() - startColor.getRed()));
            int green = Math.round(startColor.getGreen() + segmentRatio * (endColor.getGreen() - startColor.getGreen()));
            int blue = Math.round(startColor.getBlue() + segmentRatio * (endColor.getBlue() - startColor.getBlue()));
            gradient.add(new Color(clamp(red), clamp(green), clamp(blue)));
        }

        return gradient;
    }

    /**
     * Clamps a value to the valid RGB range (0-255).
     */
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * Interpolates between two colors based on a ratio.
     *
     * @param start the starting color
     * @param end the ending color
     * @param ratio the interpolation ratio (0.0 to 1.0)
     * @return the interpolated color
     */
    @Nonnull
    public static Color interpolate(@Nonnull Color start, @Nonnull Color end, float ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int red = Math.round(start.getRed() + ratio * (end.getRed() - start.getRed()));
        int green = Math.round(start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
        int blue = Math.round(start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
        return new Color(clamp(red), clamp(green), clamp(blue));
    }
}
