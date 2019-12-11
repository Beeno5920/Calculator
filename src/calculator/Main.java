package calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class Main extends Application {
    private Label inputDisplay;
    private Label outputDisplay;
    private Button[] buttons;
    private String[] buttonLables = {
            "7", "8", "9", "Del", "AC",
            "4", "5", "6", "*", "/",
            "1", "2", "3", "+", "-",
            "0", ".", "Ans", "="
    };
    private final int width = 400;
    private final int height = 500;

    @Override
    public void start(Stage primaryStage) throws Exception{
        int nCol = 5;
        int nRow = 4;

        GridPane displayLayout = new GridPane();
        ColumnConstraints[] disColumns = new ColumnConstraints[nCol];
        for (int i = 0; i < 2; i++) {
            disColumns[i] = new ColumnConstraints();
            disColumns[i].setHgrow(Priority.ALWAYS);
            disColumns[i].setFillWidth(true);
            displayLayout.getColumnConstraints().add(disColumns[i]);
        }
        inputDisplay = new Label("0");
        inputDisplay.setFont(new Font(30));
        inputDisplay.setStyle("-fx-background-color:ORANGE");
        inputDisplay.setPrefSize(width, 50);
        inputDisplay.setAlignment(Pos.TOP_LEFT);
        inputDisplay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        outputDisplay = new Label("0");
        outputDisplay.setFont(new Font(30));
        outputDisplay.setStyle("-fx-background-color:ORANGE");
        outputDisplay.setPrefSize(width, 50);
        outputDisplay.setAlignment(Pos.BOTTOM_RIGHT);
        outputDisplay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        displayLayout.add(inputDisplay, 0, 0);
        displayLayout.add(outputDisplay, 0, 1);

        GridPane buttonLayout = new GridPane();
        buttonLayout.setPadding(new Insets(15, 0, 15, 0));
        buttonLayout.setVgap(5);
        buttonLayout.setHgap(5);
        ColumnConstraints[] columns = new ColumnConstraints[nCol];
        for (int i = 0; i < nCol; i++) {
            columns[i] = new ColumnConstraints();
            columns[i].setHgrow(Priority.ALWAYS);
            columns[i].setFillWidth(true);
            buttonLayout.getColumnConstraints().add(columns[i]);
        }

        buttons = new Button[buttonLables.length];
        Calculator calculator = new Calculator(inputDisplay, outputDisplay);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(buttonLables[i]);
            buttons[i].setOnAction(calculator);
            buttons[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttons[i].setPrefSize(width / nCol, height / nRow);
            if (buttonLables[i].equals("Del") || buttonLables[i].equals("AC"))
                buttons[i].setStyle("-fx-background-color:ORANGE");
            else
                buttons[i].setStyle("-fx-background-color:GREY");
            buttons[i].setFont(new Font(20));
            buttonLayout.add(buttons[i], i % nCol, i / nCol);
        }
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:BLACK");
        root.setPadding(new Insets(15, 15, 15, 15));
        root.setTop(displayLayout);
        root.setCenter(buttonLayout);

        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setTitle("Calculator App");
        primaryStage.show();
    }

    public void setInputDisplay (String text) {
        this.inputDisplay.setText(text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
