package com.safjnest.scene;

import lombok.Value;

@Value
public class Color {
    public static final float MAX = 1;
    public static final Color BLACK = new Color(0, 0, 0);

    float r;
    float g;
    float b;

    public Color divide(float n) {
        return new Color(r / n, g / n, b / n);
    }

    public Color times(Color other) {
        return new Color(r * other.r, g * other.g, b * other.b);
    }

    public Color times(float n) {
        return new Color(r * n, g * n, b * n);
    }

    public Color plus(Color other) {
        return new Color(r + other.r, g + other.g, b + other.b);
    }

    public Color clamp() {
        return new Color(r < 0 ? 0 : r > MAX ? MAX : r, g < 0 ? 0 : g > MAX ? MAX : g, b < 0 ? 0 : b > MAX ? MAX : b);
    }
}
