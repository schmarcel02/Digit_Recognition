package view;

import common.DigitImage;
import common.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame implements KeyListener {
    private DigitImage currentImage;

    public Frame() {
        addKeyListener(this);
        createAndAddPanel();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void createAndAddPanel() {
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                currentImage.draw(g);
            }
        };
        panel.setPreferredSize(new Dimension(280, 280));
        add(panel);
    }

    public void setCurrentImage(DigitImage image) {
        this.currentImage = image;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Main.nextImage();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
