package common;

import java.awt.*;

public class DigitImage {
    private static final int width = 28, height = 28;
    public int label;
    public byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public DigitImage(int label, byte[] imageData) {
        this.label = label;
        this.imageData = imageData;
    }

    public void draw(Graphics g) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(0, 0, 0, imageData[y*width+x] & 0xFF);
                g.setColor(color);
                g.fillRect(x * 10, y * 10, 10, 10);
            }
        }
    }
}
