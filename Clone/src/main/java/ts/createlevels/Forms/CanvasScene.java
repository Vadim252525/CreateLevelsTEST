package ts.createlevels.Forms;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ts.createlevels.Logic;
import ts.createlevels.LogicCanvas;

import java.util.Objects;

import static ts.createlevels.ProgramSettings.*;
//Холст для создания уровней
public class CanvasScene extends LogicCanvas
{
    public static void open(Stage stage, boolean newProject)
    {
        isCanvasOpen = true;
        //Создание панелей
        BorderPane border = new BorderPane();
        AnchorPane anchor = new AnchorPane();

        //Содание холста
        Canvas canvas;
        if(sizeCell * Logic.canvasWidth <= WIDTH) canvas = new Canvas(WIDTH,HEIGHT * 2);
        else canvas = new Canvas(sizeCell * Logic.canvasWidth,sizeCell * Logic.canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        //Создание кнопок
        Button exitButton = new Button("Выйти");
        exitButton.getStyleClass().setAll("custom-button");

        Button saveButton = new Button("Сохранить");
        saveButton.getStyleClass().setAll("custom-button");

        Button exsportButton = new Button("Экспорт");
        exsportButton.getStyleClass().setAll("custom-button");

        Button backButton = new Button("", icon("back"));
        backButton.getStyleClass().setAll("custom-button");

        Button nextButton = new Button("", icon("next"));
        nextButton.getStyleClass().setAll("custom-button");

        Button increaseButton = new Button("", icon("increase"));
        increaseButton.getStyleClass().setAll("custom-button");

        Button decreaseButton = new Button("", icon("decrease"));
        decreaseButton.getStyleClass().setAll("custom-button");

        Button defaultCellButton = new Button("", icon("defaultCell"));
        defaultCellButton.getStyleClass().setAll("custom-button");

        Button choiceColorButton = new Button("", icon("choiceColor"));
        choiceColorButton.getStyleClass().setAll("custom-button");

        //Создание прочих элементов
        ColorPicker colorButton = new ColorPicker(Logic.replacementColor);
        colorButton.getStyleClass().setAll("custom-button");

        TextField textFReplacement = new TextField(Logic.replacementClick);
        textFReplacement.getStyleClass().setAll("custom-button");
        textFReplacement.setPrefWidth(50);

        ComboBox<String> comboPouring = new ComboBox<>();
        comboPouring.getItems().addAll("Кисть", "По цвету", "По id");
        comboPouring.setValue("Кисть");
        comboPouring.getStyleClass().setAll("custom-combo");

        //Создание текста
        Label textCords = label("Координаты:");
        Label textReplacement = label("Замена:");
        Label textPouring = label("Заливка:");

        //Создание контейнеров
        HBox buttonBox = new HBox(exitButton, saveButton, exsportButton, textCords);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.TOP_LEFT);

        HBox buttonCanvasBox = new HBox(increaseButton, decreaseButton, backButton, nextButton, colorButton, defaultCellButton,
                choiceColorButton, textReplacement, textFReplacement, textPouring, comboPouring);
        buttonCanvasBox.setSpacing(20);
        buttonCanvasBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane canvasBox = new ScrollPane(canvas);
        canvasBox.setPrefViewportHeight(HEIGHT - 200);

        anchor.getChildren().addAll(buttonBox, buttonCanvasBox);

        //Позиционирование элементов
        AnchorPane.setLeftAnchor(buttonBox, 15.0);
        AnchorPane.setTopAnchor(buttonBox, 15.0);
        AnchorPane.setLeftAnchor(buttonCanvasBox, 15.0);
        AnchorPane.setTopAnchor(buttonCanvasBox, 60.0);
        border.setBottom(canvasBox);
        border.setTop(anchor);

        // Отрисовка сетки и ID
        if(newProject)
        {
            Logic.masCanvasId = new String[Logic.canvasWidth * Logic.canvasHeight];
            Logic.masCanvasColor = new Color[Logic.canvasWidth * Logic.canvasHeight];

        }
        for (int y = 0; y < Logic.canvasHeight; y++) {
            for (int x = 0; x < Logic.canvasWidth; x++) {
                int it = (y * Logic.canvasWidth) + x;
                if(newProject) gc.setFill(Logic.pouringColor);
                else gc.setFill(Logic.masCanvasColor[it]);
                gc.fillRect(x * sizeCell, y * sizeCell, sizeCell, sizeCell);

                // Рисуем границу
                gc.setStroke(Color.BLACK);
                gc.strokeRect(x * sizeCell, y * sizeCell, sizeCell, sizeCell);

                // Выводим ID
                gc.setFill(Color.BLACK);
                gc.setFont(javafx.scene.text.Font.font(12));
                if(newProject)
                {
                    gc.fillText(Logic.replacement, x * sizeCell + 5, y * sizeCell + 15);
                    Logic.masCanvasId[it] = Logic.replacement;
                    Logic.masCanvasColor[it] = Logic.pouringColor;
                }
                else
                {
                    gc.fillText(Logic.masCanvasId[it], x * sizeCell + 5, y * sizeCell + 15);
                }
            }
        }

        // Обработка кликов для изменения цвета
        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / sizeCell);
            int y = (int) (e.getY() / sizeCell);

            if (x >= 0 && x < Logic.canvasWidth && y >= 0 && y < Logic.canvasHeight) {
                if(!isChoiceColor && comboPouring.getValue().equals("Кисть"))
                {
                    if(!isDefaultCell) gc.setFill(Logic.replacementColor);
                    else gc.setFill(Color.WHITE);
                    gc.fillRect(x * sizeCell + 1, y * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                    // Перерисовываем ID поверх нового цвета
                    gc.setFill(Color.BLACK);
                }
                if(!isDefaultCell && !isChoiceColor && comboPouring.getValue().equals("Кисть"))
                {
                    addHistory(new String[] {
                            String.valueOf(x),
                            String.valueOf(y),
                            Logic.masCanvasId[(y * Logic.canvasWidth) + x],
                            Logic.masCanvasColor[(y * Logic.canvasWidth) + x].toString()
                    });
                    masReplace.add(null);
                    masColorReplace.add(null);
                    masIdReplace.add(null);
                    gc.fillText(Logic.replacementClick, x * sizeCell + 5, y * sizeCell + 15);
                    Logic.masCanvasId[(y * Logic.canvasWidth) + x] = Logic.replacementClick;
                    Logic.masCanvasColor[(y * Logic.canvasWidth) + x] = Logic.replacementColor;
                }
                else if(isChoiceColor) //Выбор цвета
                {
                    Logic.replacementColor = Logic.masCanvasColor[(y * Logic.canvasWidth) + x];
                    colorButton.setValue(Logic.replacementColor);
                }
                else if(!comboPouring.getValue().equals("Кисть")) //Заливка
                {
                    Color[] masColor = new Color[Logic.canvasWidth * Logic.canvasHeight];
                    String[] masId = new String[Logic.canvasWidth * Logic.canvasHeight];
                    Color colorCell = Logic.masCanvasColor[(y * Logic.canvasWidth) + x];
                    String strCell = Logic.masCanvasId[(y * Logic.canvasWidth) + x];
                    byte[] masIndex = new byte[Logic.canvasWidth * Logic.canvasHeight];
                    if(comboPouring.getValue().equals("По цвету")) {
                        for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                            for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                                int it = (y1 * Logic.canvasWidth) + x1;
                                if(Logic.masCanvasColor[it].equals(colorCell)) {
                                    masIndex[it] = 1;
                                    gc.setFill(Logic.replacementColor);
                                    gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                                    gc.setFill(Color.BLACK);
                                    gc.fillText(Logic.masCanvasId[it], x1 * sizeCell + 5, y1 * sizeCell + 15);
                                    masId[it] = Logic.masCanvasId[it];
                                    masColor[it] = Logic.masCanvasColor[it];
                                    Logic.masCanvasColor[it] = Logic.replacementColor;
                                }
                                else masIndex[(y1 * Logic.canvasWidth) + x1] = 0;
                            }
                        }
                    }
                    else if(comboPouring.getValue().equals("По id"))
                    {
                        for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                            for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                                int it = (y1 * Logic.canvasWidth) + x1;
                                if(Logic.masCanvasId[it].equals(strCell)) {
                                    masIndex[it] = 1;
                                    gc.setFill(Logic.replacementColor);
                                    gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                                    gc.setFill(Color.BLACK);
                                    gc.fillText(Logic.masCanvasId[it], x1 * sizeCell + 5, y1 * sizeCell + 15);
                                    masColor[it] = Logic.masCanvasColor[it];
                                    masId[it] = Logic.masCanvasId[it];
                                    Logic.masCanvasColor[it] = Logic.replacementColor;
                                }
                                else masIndex[(y1 * Logic.canvasWidth) + x1] = 0;
                            }
                        }
                    }
                    addHistory(new String[] {
                            "-1",
                            comboPouring.getValue(),
                            strCell,
                            colorCell.toString(),
                            Logic.replacementColor.toString()
                    });
                    masReplace.add(masIndex);
                    masColorReplace.add(masColor);
                    masIdReplace.add(masId);
                }
                else //Обнуление ячейки
                {
                    addHistory(new String[] {
                            String.valueOf(x),
                            String.valueOf(y),
                            Logic.masCanvasId[(y * Logic.canvasWidth) + x],
                            Logic.masCanvasColor[(y * Logic.canvasWidth) + x].toString()
                    });
                    masReplace.add(null);
                    masColorReplace.add(null);
                    masIdReplace.add(null);
                    gc.fillText(Logic.replacement, x * sizeCell + 5, y * sizeCell + 15);
                    Logic.masCanvasId[(y * Logic.canvasWidth) + x] = Logic.replacement;
                    Logic.masCanvasColor[(y * Logic.canvasWidth) + x] = Color.WHITE;
                }
            }
        });
        canvas.setOnMouseMoved(e -> //Координаты
        {
            int x = (int) (e.getX() / sizeCell);
            int y = (int) (e.getY() / sizeCell);
            if(x < Logic.canvasWidth && y < Logic.canvasHeight)
                textCords.setText("Координаты: х(" + x + ") у(" + y + ")");
        });

        //Создание сцены
        Scene scene = new Scene(border);
        scene.getStylesheets().addAll(
                Objects.requireNonNull(MainScene.class.getResource("/styleButton.css")).toExternalForm(),
                Objects.requireNonNull(MainScene.class.getResource("/styleComboBox.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Проект - " + Logic.nameProject);
        border.setBackground(new Background(backFill()));

        //Добавление событий
        saveButton.setOnAction(e -> Logic.save(canvas, stage, false,-1,-1,-1,-1));
        colorButton.setOnAction(e -> Logic.replacementColor = colorButton.getValue());
        exitButton.setOnAction(e -> exit(canvas, stage, border));
        exsportButton.setOnAction(e -> CreateScene.open(stage, canvas));
        increaseButton.setOnAction(e -> sizeCellVoid(false, stage, border, newProject));
        decreaseButton.setOnAction(e -> sizeCellVoid(true, stage, border, newProject));
        defaultCellButton.setOnAction(e ->
        {
            isDefaultCell = !isDefaultCell;
            isChoiceColor = false;
            buttonBackground(defaultCellButton, choiceColorButton);
        });
        choiceColorButton.setOnAction(e ->
        {
            isChoiceColor = !isChoiceColor;
            isDefaultCell = false;
            buttonBackground(defaultCellButton, choiceColorButton);
        });
        textFReplacement.textProperty().addListener((observable, oldValue, newValue) -> Logic.replacementClick = textFReplacement.getText());
        backButton.setOnAction(e ->
        {
            if(indexHistory > 0)
            {
                indexHistory--;
                if(!Objects.equals(listHistory.get(indexHistory)[0], "-1")) historyOne(gc);
                else historyBack(gc);
            }
        });
        nextButton.setOnAction(e ->
        {
            if(indexHistory < listHistory.size())
            {
                if(!Objects.equals(listHistory.get(indexHistory)[0], "-1")) historyOne(gc);
                else historyNext(gc);
                indexHistory++;
            }
        });

        //Добавление сочетаний клавиш
        scene.setOnKeyPressed(e -> {
            if(e.isShiftDown() && e.getCode() == KeyCode.ESCAPE)
                exit(canvas, stage, border);

            else if(e.isControlDown() && e.getCode() == KeyCode.S)
                Logic.save(canvas, stage, false,-1,-1,-1,-1);

            else if(e.isShiftDown() && e.getCode() == KeyCode.F5)
                CreateScene.open(stage, canvas);

            else if(e.isControlDown() && e.getCode() == KeyCode.X) {
                isChoiceColor = !isChoiceColor;
                isDefaultCell = false;
                buttonBackground(defaultCellButton, choiceColorButton);
            }

            else if(e.isControlDown() && e.getCode() == KeyCode.C) {
                isDefaultCell = !isDefaultCell;
                isChoiceColor = false;
                buttonBackground(defaultCellButton, choiceColorButton);
            }

            else if(e.isControlDown() && e.getCode() == KeyCode.Z) {
                if(indexHistory > 0)
                {
                    indexHistory--;
                    if(!Objects.equals(listHistory.get(indexHistory)[0], "-1")) historyOne(gc);
                    else historyBack(gc);
                }
            }

            else if(e.isControlDown() && e.getCode() == KeyCode.Y) {
                if(indexHistory < listHistory.size())
                {
                    if(!Objects.equals(listHistory.get(indexHistory)[0], "-1")) historyOne(gc);
                    else historyNext(gc);
                    indexHistory++;
                }
            }

            else if(e.isControlDown() && e.getCode() == KeyCode.EQUALS)
                sizeCellVoid(false, stage, border, newProject);

            else if(e.isControlDown() && e.getCode() == KeyCode.MINUS)
                sizeCellVoid(true, stage, border, newProject);
        });

        //Добавление события на закрытие окна
        if(!isWindowEvent)
        {
            isWindowEvent = true;
            stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event ->
            {
                if(isCanvasOpen)
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Сохранить холст?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES)
                        Logic.save(canvas, stage,false,-1,-1,-1,-1);
                }
            });
        }
    }
}
