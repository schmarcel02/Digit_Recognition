package view;

import common.DigitImage;
import common.NN;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import loader.DigitImageLoadingService;

import java.io.IOException;
import java.util.List;

public class TestMenu extends BorderPane{
    private static List<DigitImage> images;
    private DigitDisplay display;
    private int imageIndex = 0;

    private boolean training = false;

    TestMenu() {
        try {
            images = new DigitImageLoadingService("/data/t10k-labels.idx1-ubyte", "/data/t10k-images.idx3-ubyte").loadDigitImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinSize(800, 400);
        setMaxSize(800, 400);
        setLayout();
    }

    private void setLayout() {
        setLeft(createDigitDisplay());
        Button b = new Button("Test");
        b.setOnAction(e -> {
            if (!training) {
                training = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*DigitImage image = images.get(imageIndex);
                        while (training) {
                            int right = 0;
                            int wrong = 0;
                            for (int i = 0; i < 1000; i++) {
                                image = images.get(imageIndex);
                                int guess = NN.feedForward(image.imageData);
                                //System.out.println("Label: " + image.label + " Guess: " + guess);
                                if (guess == image.label)
                                    right++;
                                else
                                    wrong++;
                                imageIndex++;
                                if (imageIndex >= 10000)
                                    imageIndex = 0;
                            }
                            System.out.println("Right: " + right + " Wrong: " + wrong);
                        }*/
                        testAll();
                    }
                }).start();
            } else {
                training = false;
            }
        });
        setRight(b);
    }

    private void testAll() {
        int right = 0;
        int wrong = 0;
        for (int i = 0; i < images.size(); i++) {
            DigitImage image = images.get(i);
            int guess = NN.feedForward(image.imageData);
            if (guess == image.label)
                right++;
            else
                wrong++;
        }
        double percentage = (float)right * 100 / (float)images.size();
        System.out.println("Right: " + right + " Wrong: " + wrong + " Percentage: " + percentage);
    }

    private DigitDisplay createDigitDisplay() {
        display = new DigitDisplay(images.get(imageIndex++).imageData);
        display.widthProperty().bind(widthProperty().divide(2));
        display.heightProperty().bind(heightProperty().divide(1));
        ChangeListener<Number> l = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                display.repaint();
            }
        };
        display.widthProperty().addListener(l);
        display.heightProperty().addListener(l);
        display.setManaged(false);
        display.repaint();
        return display;
    }
}
