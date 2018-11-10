package view;

import common.NN;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class WriteMenu extends BorderPane{
    private DigitDisplay display;

    WriteMenu() {
        setMinSize(800, 400);
        setMaxSize(800, 400);
        setLayout();
    }

    private void setLayout() {
        setLeft(createDigitDisplay());
        Button b = new Button("test");
        b.setOnAction(e -> {
            System.out.println("Guess: " + NN.feedForward(display.getData()));
        });
        setRight(b);
    }

    private DigitDisplay createDigitDisplay() {
        display = new DigitDisplay();
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
