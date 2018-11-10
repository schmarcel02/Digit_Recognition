package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DigitDisplay extends Canvas{
    private byte[] data;

    private int lastX = -1, lastY = -1;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        repaint();
    }

    public DigitDisplay(byte[] data) {
        this.data = data;
    }

    public DigitDisplay() {
        data = new byte[28 * 28];
        setOnMouseClicked(e -> {
            int px = getPixelX(e.getX());
            int py = getPixelY(e.getY());
            draw(px, py);
        });
        setOnMouseDragged(e -> {
            int px = getPixelX(e.getX());
            int py = getPixelY(e.getY());
            draw(px, py);
        });
    }

    private int getPixelX(double x) {
        double pWidth = getWidth() / 28;
        return (int) Math.floor(x/pWidth);
    }

    private int getPixelY(double y) {
        double pHeight = getHeight() / 28;
        return (int) Math.floor(y/pHeight);
    }

    public void repaint() {
        double canWidth = getWidth();
        double canHeight = getHeight();
        double pWidth = canWidth / 28;
        double pHeight = canHeight / 28;

        Platform.runLater(() -> {
            GraphicsContext g = getGraphicsContext2D();
            g.clearRect(0, 0, canWidth, canHeight);
            for (int y = 0; y < 28; y++) {
                int pY = (int) Math.round(y * pHeight);
                int nPY = (int) Math.round((y + 1) * pHeight);
                int height = nPY - pY;

                for (int x = 0; x < 28; x++) {
                    int pX = (int) Math.round(x * pWidth);
                    int nPX = (int) Math.round((x + 1) * pWidth);
                    int width = nPX - pX;

                    g.setFill(new Color(0, 0, 0, (data[y * 28 + x] & 0xFF)/255f));
                    g.fillRect(pX, pY, width, height);
                }
                g.setFill(Color.BLACK);
                g.strokeLine(0, 0, getWidth(), 0);
                g.strokeLine(0, 0, 0, getHeight());
                g.strokeLine(getWidth(), getHeight(), getWidth(), 0);
                g.strokeLine(getWidth(), getHeight(), 0, getHeight());
            }
        });
    }

    private void draw(int px, int py) {
        if (px == lastX && py == lastY)
            return;
        lastX = px;
        lastY = py;
        drawOnPixel(px, py, 255);
        drawOnPixel(px + 1, py, 64);
        drawOnPixel(px - 1, py, 64);
        drawOnPixel(px, py + 1, 64);
        drawOnPixel(px, py - 1, 64);
        repaint();
    }

    private void drawOnPixel(int px, int py, int strength) {
        if (px >= 28 || px < 0 || py >= 28 || py < 0)
            return;
        int index = py * 28 + px;
        int temp = data[index] & 0xFF;
        temp += strength;
        if (temp > 255)
            temp = 255;
        data[index] = (byte)(temp & 0xFF);
    }
}
