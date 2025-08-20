package ts.createlevels.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ts.createlevels.Logic;

import java.util.ArrayList;
import java.util.Objects;

import static ts.createlevels.ProgramSettings.*;
//Главное лобби
public class MainScene
{
    public static void open(Stage stage)
    {
        isCanvasOpen = false;
        //Кнопки
        Button createButton = new Button("Создать");
        createButton.getStyleClass().setAll("custom-button");

        Button openButton = new Button("Открыть");
        openButton.getStyleClass().setAll("custom-button");

        //Текст
        Label textInfo = new Label();
        textInfo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        textInfo.setTextFill(Color.WHITE);
        textInfo.setPadding(new Insets(20));

        //Позиционирование
        BorderPane stack = new BorderPane();
        AnchorPane topPanel = new AnchorPane();

        //Лево
        HBox buttonBox = new HBox(createButton, openButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        //Середина
        HBox textBox = new HBox(textInfo);
        textBox.setAlignment(Pos.CENTER);
        textBox.setMaxWidth(WIDTH);

        //Добавление и отступы
        AnchorPane.setLeftAnchor(buttonBox, 20.0);
        AnchorPane.setTopAnchor(buttonBox, 15.0);
        AnchorPane.setLeftAnchor(textBox, 0.0);
        AnchorPane.setRightAnchor(textBox, 0.0);
        topPanel.getChildren().addAll(textBox, buttonBox);
        stack.setTop(topPanel);

        //Добавление сохранений (фото/текст)
        GridPane imageBox = new GridPane();
        imageBox.setHgap(70);
        imageBox.setVgap(100);
        int count = Logic.lastSavedCount();

        byte a = 0, b = 0;
        ArrayList<String[]> list = Logic.lastSavedOpen();
        assert list != null;
        ImageView view;
        if(list.get(0)[1] != null)
        {
            textInfo.setText("Возвращайтесь к своим проектами или создайте новый!");
            for (byte i = 0; i < count;i++)
            {
                Button deleteButton = new Button("", icon("delete"));
                deleteButton.getStyleClass().setAll("custom-button");

                Label text = new Label("Название: " + list.get(i)[0] + "\nДата сохранения: " + list.get(i)[1]);
                text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                text.setTextFill(Color.WHITE);

                view = new ImageView(new Image("file:" + list.get(i)[2]));
                view.setPreserveRatio(false);
                view.setFitWidth(500);
                view.setFitHeight(250);
                view.setStyle("-fx-effect: dropshadow(gaussian, black, 50, 0, 0, 0);");

                VBox contentBox = new VBox(view, text);

                HBox mainBox = new HBox(contentBox, deleteButton);
                mainBox.setAlignment(Pos.TOP_RIGHT);
                mainBox.setSpacing(10);

                imageBox.add(mainBox, a, b);

                //Добавление событий на фото
                byte finalI = i;
                view.setOnMouseClicked(e ->
                    Logic.openFileSave(stage, stack,"save/" + list.get(finalI)[0] + "/" + list.get(finalI)[0] + ".save"));
                ImageView finalView = view;
                view.setOnMouseEntered(e ->
                        {
                            finalView.setEffect(new Glow(0.9));
                            finalView.setStyle("-fx-effect: dropshadow(gaussian, gray, 50, 0, 0, 0);");
                        });
                view.setOnMouseExited(e ->
                        {
                            finalView.setEffect(new Glow());
                            finalView.setStyle("-fx-effect: dropshadow(gaussian, black, 50, 0, 0, 0);");
                        });

                //Добавление события на удаление сохранения
                deleteButton.setOnAction(e ->
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы точно хотите удалить холст => " +  list.get(finalI)[0] +
                            "?\nЕго нельзя будет восстановить!", ButtonType.YES, ButtonType.CANCEL);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES)
                    {
                        stack.getChildren().clear();
                        Logic.lastSavedDelete(finalI,"save/" + list.get(finalI)[0]);
                        MainScene.open(stage);
                    }
                });

                if(++a == 3)
                {
                    a = 0;
                    b++;
                }
            }
            imageBox.setPadding(new Insets(90));
            stack.setCenter(imageBox);
        }
        else
            textInfo.setText("У вас пока нету проектов! Создайте новый!");

        //Создание сцены
        Scene scene = new Scene(stack);
        stack.setBackground(new Background(backFill()));
        scene.getStylesheets().add(Objects.requireNonNull(MainScene.class.getResource("/styleButton.css")).toExternalForm());
        stage.setScene(scene);

        //Добавление событий
        createButton.setOnAction(e -> SuperstructureScene.open(stage));

        openButton.setOnAction(e -> Logic.openFileSave(stage, stack, null));

        scene.setOnKeyPressed(e -> {
            if(e.isShiftDown() && e.getCode() == KeyCode.F10)
                Logic.openFileSave(stage, stack, null);
            else if(e.isControlDown() && e.getCode() == KeyCode.N)
                SuperstructureScene.open(stage);
        });
    }
}
