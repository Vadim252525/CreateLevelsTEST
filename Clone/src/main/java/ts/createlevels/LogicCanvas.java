package ts.createlevels;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ts.createlevels.Forms.CanvasScene;
import ts.createlevels.Forms.MainScene;

import java.util.ArrayList;

import static ts.createlevels.ProgramSettings.VERSION;

public class LogicCanvas
{
    public static float sizeCell = 30;
    public static boolean isDefaultCell = false, isChoiceColor = false;
    public static ArrayList<String[]> listHistory = new ArrayList<>();
    public static byte indexHistory = 0;
    public static ArrayList<byte[]> masReplace = new ArrayList<>();
    public static ArrayList<Color[]> masColorReplace = new ArrayList<>();
    public static ArrayList<String[]> masIdReplace = new ArrayList<>();

    protected static void addHistory(String[] list)
    {
        listHistory.add(list);
        if(listHistory.size() >= 20)
        {
            listHistory.remove(0);
            masReplace.remove(0);
            masColorReplace.remove(0);
            masIdReplace.remove(0);
        }
        indexHistory = (byte) listHistory.size();
    }

    protected static void exit(Canvas canvas, Stage stage, BorderPane border)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Сохранить холст?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if(alert.getResult() != ButtonType.CANCEL)
        {
            if(alert.getResult() == ButtonType.YES)
                Logic.save(canvas, stage,false,-1,-1,-1,-1);
            border.getChildren().clear();
            stage.setTitle("Создание 2D уровней - " + VERSION);
            listHistory.clear();
            masReplace.clear();
            masColorReplace.clear();
            masIdReplace.clear();
            indexHistory = 0;
            MainScene.open(stage);
        }
    }

    protected static void sizeCellVoid(boolean isMin, Stage stage, BorderPane border, boolean newProject)
    {
        if(sizeCell < 50 && !isMin)
        {
            sizeCell += 10;
            border.getChildren().clear();
            CanvasScene.open(stage, newProject);
        }
        else if(sizeCell > 10 && isMin)
        {
            sizeCell -= 10;
            border.getChildren().clear();
            CanvasScene.open(stage, newProject);
        }
    }

    protected static void buttonBackground(Button defaultCellButton, Button choiceColorButton)
    {
        if(isDefaultCell)
            defaultCellButton.getStyleClass().setAll("custom-buttonPressed");
        else
            defaultCellButton.getStyleClass().setAll("custom-button");
        if(isChoiceColor)
            choiceColorButton.getStyleClass().setAll("custom-buttonPressed");
        else
            choiceColorButton.getStyleClass().setAll("custom-button");
    }

    protected static void historyOne(GraphicsContext gc)
    {
        int x = Integer.parseInt(listHistory.get(indexHistory)[0]);
        int y = Integer.parseInt(listHistory.get(indexHistory)[1]);
        int it = (y * Logic.canvasWidth) + x;
        String st1 = Logic.masCanvasId[it];
        String st2 = String.valueOf(Logic.masCanvasColor[it]);
        Logic.masCanvasId[it] = listHistory.get(indexHistory)[2];
        Logic.masCanvasColor[it] = Color.valueOf(listHistory.get(indexHistory)[3]);
        listHistory.get(indexHistory)[2] = st1;
        listHistory.get(indexHistory)[3] = st2;

        gc.setFill(Logic.masCanvasColor[it]);
        gc.fillRect(x * sizeCell + 1, y * sizeCell + 1, sizeCell - 2, sizeCell - 2);
        gc.setFill(Color.BLACK);
        gc.fillText(Logic.masCanvasId[it], x * sizeCell + 5, y * sizeCell + 15);
    }

    protected static void historyBack(GraphicsContext gc)
    {
        Color colorCellOld = Color.valueOf(listHistory.get(indexHistory)[3]);
        String CP = listHistory.get(indexHistory)[1];
        listHistory.get(indexHistory)[3] = String.valueOf(listHistory.get(indexHistory)[4]);
        listHistory.get(indexHistory)[4] = String.valueOf(colorCellOld);

        if(CP.equals("По цвету")) {
            for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                    int it = (y1 * Logic.canvasWidth) + x1;
                    if(masReplace.get(indexHistory)[it] == 1) {
                        gc.setFill(colorCellOld);
                        gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                        gc.setFill(Color.BLACK);
                        gc.fillText(masIdReplace.get(indexHistory)[it], x1 * sizeCell + 5, y1 * sizeCell + 15);

                        Logic.masCanvasColor[it] = colorCellOld;
                    }
                }
            }
        }
        else if(CP.equals("По id"))
        {
            for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                    int it = (y1 * Logic.canvasWidth) + x1;
                    if(masReplace.get(indexHistory)[it] == 1) {
                        gc.setFill(masColorReplace.get(indexHistory)[it]);
                        gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                        gc.setFill(Color.BLACK);
                        gc.fillText(Logic.masCanvasId[it], x1 * sizeCell + 5, y1 * sizeCell + 15);

                        Logic.masCanvasColor[it] = masColorReplace.get(indexHistory)[it];
                    }
                }
            }
        }
    }

    protected static void historyNext(GraphicsContext gc)
    {
        Color colorCellOld = Color.valueOf(listHistory.get(indexHistory)[3]);
        String CP = listHistory.get(indexHistory)[1];
        listHistory.get(indexHistory)[3] = String.valueOf(listHistory.get(indexHistory)[4]);
        listHistory.get(indexHistory)[4] = String.valueOf(colorCellOld);

        if(CP.equals("По цвету")) {
            for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                    int it = (y1 * Logic.canvasWidth) + x1;
                    if(masReplace.get(indexHistory)[it] == 1) {
                        gc.setFill(colorCellOld);
                        gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                        gc.setFill(Color.BLACK);
                        gc.fillText(masIdReplace.get(indexHistory)[it], x1 * sizeCell + 5, y1 * sizeCell + 15);

                        Logic.masCanvasColor[it] = colorCellOld;
                    }
                }
            }
        }
        else if(CP.equals("По id"))
        {
            for (int y1 = 0; y1 < Logic.canvasHeight; y1++) {
                for (int x1 = 0; x1 < Logic.canvasWidth; x1++) {
                    int it = (y1 * Logic.canvasWidth) + x1;
                    if(masReplace.get(indexHistory)[it] == 1) {
                        gc.setFill(colorCellOld);
                        gc.fillRect(x1 * sizeCell + 1, y1 * sizeCell + 1, sizeCell - 2, sizeCell - 2);
                        gc.setFill(Color.BLACK);
                        gc.fillText(Logic.masCanvasId[it], x1 * sizeCell + 5, y1 * sizeCell + 15);

                        Logic.masCanvasColor[it] = masColorReplace.get(indexHistory)[it];
                    }
                }
            }
        }
    }
}
