package juuxel.adorn.util;

public final class Colors {
    public static final int BLACK = 0xFF_000000;
    public static final int WHITE = 0xFF_FFFFFF;
    public static final int SCREEN_TEXT = 0xFF_404040;
    public static final int TRANSPARENT = 0x00_000000;

    public static int color(int rgb) {
        return color(rgb, 0xFF);
    }

    public static int color(int rgb, int alpha) {
        return alpha << 24 | rgb;
    }

    public static int color(int rgb, float alpha) {
        return color(rgb, (int) (alpha * 255f));
    }

    public static int color(float red, float green, float blue) {
        return color(red, green, blue, 1f);
    }

    public static int color(float red, float green, float blue, float alpha) {
        return color((int) (red * 255f), (int) (green * 255f), (int) (blue * 255f), (int) (alpha * 255f));
    }

    public static int color(int red, int green, int blue, int alpha) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
    }

    public static float redOf(int argb) {
        return ((argb >> 16) & 0xFF) / 255f;
    }

    public static float greenOf(int argb) {
        return ((argb >> 8) & 0xFF) / 255f;
    }

    public static float blueOf(int argb) {
        return (argb & 0xFF) / 255f;
    }

    public static float alphaOf(int argb) {
        return ((argb >> 24) & 0xFF) / 255f;
    }
}
