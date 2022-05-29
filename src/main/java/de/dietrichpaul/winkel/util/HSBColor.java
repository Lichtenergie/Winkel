package de.dietrichpaul.winkel.util;

import java.awt.*;

public class HSBColor {

    private final float hue;
    private final float saturation;
    private final float brightness;
    private final int alpha;

    public HSBColor(Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public HSBColor(int red, int green, int blue, int alpha) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, green, blue, hsb);

        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = alpha;
    }

    public HSBColor(int color) {
        this(new Color(color, true));
    }


    public HSBColor(float hue, float saturation, float brightness, int alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha = alpha;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRGB() {
        int i = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
        Color color = new Color(i);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), this.alpha).getRGB();
    }

    @Override
    public String toString() {
        return "HSBColor{" + "hue=" + hue + ", saturation=" + saturation + ", brightness=" + brightness + ", alpha=" + alpha + '}';
    }

}
