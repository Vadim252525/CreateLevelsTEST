package ts.createlevels.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ts.createlevels.Logic;

import java.io.File;
import java.util.Objects;

import static ts.createlevels.ProgramSettings.*;
//Создание холста
public class SuperstructureScene
{
    public static void open(Stage stage)
    {
        //Кнопки
        Button createButton = new Button("Создать");
        createButton.getStyleClass().setAll("custom-button");

        Button backButton = new Button("Назад");
        backButton.getStyleClass().setAll("custom-button");

        ColorPicker colorButton = new ColorPicker(Color.WHITE);
        colorButton.getStyleClass().setAll("custom-button");

        //ТекстБокс
        TextField textFName = new TextField("new Project");
        textFName.getStyleClass().setAll("custom-button");
        textFName.setPrefWidth(300);

        TextField textFWidth = new TextField("10");
        textFWidth.getStyleClass().setAll("custom-button");
        textFWidth.setPrefWidth(300);

        TextField textFHeight = new TextField("10");
        textFHeight.getStyleClass().setAll("custom-button");
        textFHeight.setPrefWidth(300);

        TextField textFReplacement = new TextField("-1");
        textFReplacement.getStyleClass().setAll("custom-button");
        textFReplacement.setPrefWidth(150);

        //Текст
        Label textWH = new Label("Название\n\n\nШирина\n\n\nВысота\n\n\nЗаливка ячеек\n\n\nЗамена");
        textWH.setFont(Font.font("Arial", FontWeight.BOLD, 15.325f));
        textWH.setTextFill(Color.WHITE);

        //Позиционирование
        BorderPane stack = new BorderPane();
        AnchorPane topPanel = new AnchorPane();

        //Лево
        VBox buttonBox = new VBox(textFName, textFWidth, textFHeight, colorButton, textFReplacement);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        //Право
        VBox textRightBox = new VBox(textWH);
        textRightBox.setSpacing(20);
        textRightBox.setAlignment(Pos.TOP_RIGHT);

        //Низ
        HBox buttonBottomBox = new HBox(createButton, backButton);
        buttonBottomBox.setAlignment(Pos.CENTER);
        buttonBottomBox.setMaxWidth(WIDTH);
        buttonBottomBox.setSpacing(20);
        buttonBottomBox.setPadding(new Insets(20, 20, 40, 20));

        //Добавление и отступы
        AnchorPane.setLeftAnchor(buttonBox, 20.0);
        AnchorPane.setTopAnchor(buttonBox, 20.0);

        AnchorPane.setTopAnchor(textRightBox, 30.0);
        AnchorPane.setLeftAnchor(textRightBox, 10.0);
        AnchorPane.setRightAnchor(textRightBox, 30.0);

        topPanel.getChildren().addAll(textRightBox, buttonBox);
        stack.setTop(topPanel);
        stack.setBottom(buttonBottomBox);

        //Создание сцены
        Scene scene = new Scene(stack);

        stack.setBackground(new Background(backFill()));
        scene.getStylesheets().add(Objects.requireNonNull(MainScene.class.getResource("/styleButton.css")).toExternalForm());
        Stage stageSuper = new Stage();
        stageSuper.initModality(Modality.APPLICATION_MODAL);
        stageSuper.initOwner(stage);
        stageSuper.setScene(scene);
        stageSuper.setTitle("Надстройка холста");
        stageSuper.setWidth(500);
        stageSuper.setHeight(390);
        stageSuper.setResizable(false);
        stageSuper.show();

        //Добавление событий
        colorButton.setOnAction(e -> Logic.pouringColor = colorButton.getValue());

        createButton.setOnAction(e ->
        {
            Logic.nameProject = textFName.getText().trim();
            boolean examinationBool = false;
            StringBuilder errorText = new StringBuilder("Ошибка:\n");
            try
            {
                Logic.canvasWidth = Short.parseShort(textFWidth.getText());
                Logic.canvasHeight = Short.parseShort(textFHeight.getText());
            }
            catch (Exception ex)
            {
                examinationBool = true;
                errorText.append("=> В размеры должны вводится только числа/цифры!\n");
            }
            try //Проверка на правильность данных
            {
                File nameDir = new File("save/");
                String[] nameDirMas = nameDir.list();
                String[] masForbiddenSymbols = new String[] {".", "?", "/", "\\", "|", "*", ":", "\"", "<", ">"};

                assert nameDirMas != null;

                for(String sim : masForbiddenSymbols)
                {
                    if (Logic.nameProject.contains(sim))
                    {
                        examinationBool = true;
                        errorText.append("=> Такие символы как . ? / \\ | * : \" < > нельзя использовать!\n");
                        break;
                    }
                }

                for (String str : nameDirMas)
                {
                    if (Objects.equals(Logic.nameProject, str))
                    {
                        examinationBool = true;
                        errorText.append("=> \"").append(Logic.nameProject).append("\" такое название уже есть!\n");
                        break;
                    }
                }
                if(!examinationBool)
                {
                    if(Logic.canvasWidth <= 0 || Logic.canvasWidth > 100 || Logic.canvasHeight <= 0 || Logic.canvasHeight > 100)
                    {
                        examinationBool = true;
                        errorText.append("=> Размер уровня должен быть от 1 до 100!\n");
                    }
                }
            }
            catch (Exception ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
            if(!examinationBool)
            {
                Logic.replacement = textFReplacement.getText();
                stageSuper.close();
                stack.getChildren().clear();
                CanvasScene.open(stage,true);
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, errorText.toString(), ButtonType.OK);
                alert.showAndWait();
            }
        });
        backButton.setOnAction(e -> stageSuper.close());

        scene.setOnKeyPressed(e -> {
            if(e.isShiftDown() && e.getCode() == KeyCode.ESCAPE)
                stageSuper.close();
        });
    }
}
