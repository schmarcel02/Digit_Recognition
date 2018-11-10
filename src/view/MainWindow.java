package view;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Stage {
    private static MainWindow window;

    public static MainWindow createWindow() {
       window = new MainWindow();
       return window;
    }

    private BorderPane layout;

    public MainWindow() {
        setResizable(false);
        setScene(new Scene(layout()));
        setMenu(new TrainMenu());
        show();
    }

    private void setMenu(Pane menu) {
        menu.prefWidthProperty().bind(widthProperty());
        menu.prefHeightProperty().bind(heightProperty());
        layout.setBottom(menu);
        sizeToScene();
    }

    private BorderPane layout() {
        layout = new BorderPane();
        layout.setTop(createMenuBar());
        return layout;
    }

    private HBox createMenuBar() {
        HBox menuBar = new HBox();
        menuBar.getChildren().addAll(createTrainButton(), createTestButton(), createWriteButton());
        menuBar.setStyle("-fx-background-color: #eeeeee");
        return menuBar;
    }

    private Button createTrainButton() {
        Button trainButton = new Button("Train");
        trainButton.setOnAction(e -> {
            setMenu(new TrainMenu());
        });
        trainButton.setStyle("-fx-background-radius: 0; -fx-background-insets: 0");
        trainButton.styleProperty().bind(Bindings.when(trainButton.hoverProperty())
                .then("-fx-background-color: #e0e0e0")
                .otherwise("-fx-background-color: #eeeeee"));
        return trainButton;
    }

    private Button createTestButton() {
        Button testButton = new Button("Test");
        testButton.setOnAction(e -> {
            setMenu(new TestMenu());
        });
        testButton.setStyle("-fx-background-radius: 0; -fx-background-insets: 0");
        testButton.styleProperty().bind(Bindings.when(testButton.hoverProperty())
                .then("-fx-background-color: #e0e0e0")
                .otherwise("-fx-background-color: #eeeeee"));
        return testButton;
    }

    private Button createWriteButton() {
        Button writeButton = new Button("Write");
        writeButton.setOnAction(e -> {
            setMenu(new WriteMenu());
        });
        writeButton.setStyle("-fx-background-radius: 0; -fx-background-insets: 0");
        writeButton.styleProperty().bind(Bindings.when(writeButton.hoverProperty())
                .then("-fx-background-color: #e0e0e0")
                .otherwise("-fx-background-color: #eeeeee"));
        return writeButton;
    }
}
