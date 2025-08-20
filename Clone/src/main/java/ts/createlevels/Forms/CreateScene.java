package ts.createlevels.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ts.createlevels.Logic;

import java.util.Objects;

import static ts.createlevels.ProgramSettings.*;
//Экспорт уровня
public class CreateScene
{
    public static void open(Stage stage, Canvas canvas)
    {
        //Кнопки
        Button exportButton = new Button("Экспорт");
        exportButton.getStyleClass().setAll("custom-button");

        Button backButton = new Button("Отмена");
        backButton.getStyleClass().setAll("custom-button");

        //ТекстБокс
        TextField textFx1 = new TextField("0");
        textFx1.getStyleClass().setAll("custom-button");
        textFx1.setPrefWidth(150);

        TextField textFy1 = new TextField("0");
        textFy1.getStyleClass().setAll("custom-button");
        textFy1.setPrefWidth(150);

        TextField textFx2 = new TextField("0");
        textFx2.getStyleClass().setAll("custom-button");
        textFx2.setPrefWidth(150);

        TextField textFy2 = new TextField("0");
        textFy2.getStyleClass().setAll("custom-button");
        textFy2.setPrefWidth(150);

        //Текст
        Label textXY = new Label("x1 | y1\n\n\nx2 | y2");
        textXY.setFont(Font.font("Arial", FontWeight.BOLD, 15.325f));
        textXY.setTextFill(Color.WHITE);
        textXY.setPadding(new Insets(20,0,0,0));

        //Позиционирование
        BorderPane stack = new BorderPane();

        //Лево
        VBox textLeftBox = new VBox(textFx1, textFx2);
        textLeftBox.setSpacing(20);
        textLeftBox.setAlignment(Pos.TOP_LEFT);
        textLeftBox.setPadding(new Insets(20,0,0,20));

        //Право
        VBox textRightBox = new VBox(textFy1, textFy2);
        textRightBox.setSpacing(20);
        textRightBox.setAlignment(Pos.TOP_RIGHT);
        textRightBox.setPadding(new Insets(20,20,0,0));

        //Низ
        HBox buttonBottomBox = new HBox(exportButton, backButton);
        buttonBottomBox.setAlignment(Pos.CENTER);
        buttonBottomBox.setMaxWidth(WIDTH);
        buttonBottomBox.setSpacing(20);
        buttonBottomBox.setPadding(new Insets(20, 20, 20, 20));

        stack.setRight(textRightBox);
        stack.setLeft(textLeftBox);
        stack.setBottom(buttonBottomBox);
        stack.setCenter(textXY);

        //Создание сцены
        Scene scene = new Scene(stack);
        stack.setBackground(new Background(backFill()));
        scene.getStylesheets().add(Objects.requireNonNull(MainScene.class.getResource("/styleButton.css")).toExternalForm());
        Stage stageSuper = new Stage();
        stageSuper.initModality(Modality.APPLICATION_MODAL);
        stageSuper.initOwner(stage);
        stageSuper.setScene(scene);
        stageSuper.setTitle("Экспорт уровня");
        stageSuper.setWidth(450);
        stageSuper.setHeight(230);
        stageSuper.setResizable(false);
        stageSuper.show();

        //Добавление событий
        backButton.setOnAction(e -> stageSuper.close());

        exportButton.setOnAction(e ->
        {
            Logic.save(canvas, stage,true,
                    Integer.parseInt(textFx1.getText()),
                    Integer.parseInt(textFx2.getText()),
                    Integer.parseInt(textFy1.getText()),
                    Integer.parseInt(textFy2.getText()));
            stageSuper.close();
        });

        scene.setOnKeyPressed(e -> {
            if(e.isControlDown() && e.getCode() == KeyCode.S) {
                Logic.save(canvas, stage,true,
                        Integer.parseInt(textFx1.getText()),
                        Integer.parseInt(textFx2.getText()),
                        Integer.parseInt(textFy1.getText()),
                        Integer.parseInt(textFy2.getText()));
                stageSuper.close();
            }
            else if(e.isShiftDown() && e.getCode() == KeyCode.ESCAPE)
                stageSuper.close();
        });
    }
}
