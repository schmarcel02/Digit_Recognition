package common;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Frame;
import loader.DigitImageLoadingService;
import view.MainWindow;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private static List<DigitImage> images;
    private static int current = 0;
    private static Frame frame;
    public static void main(String[] args) {
        launch(args);
    }
    public static void nextImage() {
        frame.setCurrentImage(images.get(++current));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow.createWindow();
    }
}
