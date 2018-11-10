package view;

import common.DigitImage;
import common.NN;
import common.TrainingManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import loader.DigitImageLoadingService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TrainMenu extends BorderPane{
    private static List<DigitImage> images;
    private DigitDisplay display;
    private int imageIndex = 0;
    private int activeThreads = 0;

    private boolean training = false;

    TrainMenu() {
        try {
            images = new DigitImageLoadingService("/data/train-labels.idx1-ubyte", "/data/train-images.idx3-ubyte").loadDigitImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinSize(800, 400);
        setMaxSize(800, 400);
        setLayout();
    }

    private void setLayout() {
        setLeft(createDigitDisplay());
        TextField eta = new TextField();
        Button start = new Button("start");
        start.setOnAction(e -> {
            if (!training) {
                training = true;
                new Thread(() -> {
                    while (training) {
                        TrainingManager manager = new TrainingManager(images);
                        manager.trainAll();
                        manager.waitForCompletion();
                    }
                }).start();
            } else {
                training = false;
            }
            /*if (!training) {
                training = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DigitImage image = images.get(imageIndex);
                        while (training) {
                            for (int i = 0; i < 1000; i++) {
                                image = images.get(imageIndex);
                                NN.train(image.imageData, image.label);
                                imageIndex++;
                                if (imageIndex >= 60000)
                                    imageIndex = 0;
                            }
                            display.setData(image.imageData);
                            int guess = NN.feedForward(image.imageData);
                            System.out.println("Label: " + image.label + " Guess: " + guess + (image.label == guess ? " Rite" : " Wong on so many levels"));
                        }
                    }
                }).start();
            } else {
                training = false;
            }*/
            //trainAll();
        });
        eta.setOnAction(e -> {
            NN.eta = Double.valueOf(eta.getText());
            System.out.println("Eta is now: " + NN.eta);
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(start, eta);
        setRight(vBox);
    }

    private void trainAll() {
        for (int i = 0; i < images.size() / 1000; i++) {
            waitWhileMoreThanXThreads(3);
            trainBunch(i * 1000, (i+1) * 1000);
        }
        waitWhileMoreThanXThreads(0);
        System.out.println("finished training");
    }

    private void trainBunch(int startIn, int endEx) {
        Thread t = new Thread(() -> {
            DigitImage image = null;
            for (int k = startIn; k < endEx; k++) {
                image = images.get(k);
                NN.train(image.imageData, image.label);
            }
            if (image != null) {
                int guess = NN.feedForward(image.imageData);
                System.out.println("Label: " + image.label + " Guess: " + guess + (image.label == guess ? " Rite" : " Wong on so many levels"));
            }
            activeThreads--;
        });
        t.start();
        activeThreads++;
    }

    private void waitWhileMoreThanXThreads(int amount) {
        while (activeThreads > amount) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private DigitDisplay createDigitDisplay() {
        display = new DigitDisplay(images.get(imageIndex).imageData);
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
