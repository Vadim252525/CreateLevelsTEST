package ts.createlevels;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramSettings
{
    public static final int WIDTH = 1920, HEIGHT = 1080;
    public static final String VERSION = "1.0.0";
    public static boolean isCanvasOpen = false, isWindowEvent = false;
    public static void createDirectory(String path)
    {
        try
        {
            Files.createDirectory(Paths.get(path));
        }
        catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    public static BackgroundFill backFill()
    {
        return new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#2b2b2b")),
                        new Stop(1, Color.BLACK)),
                CornerRadii.EMPTY,
                Insets.EMPTY);
    }
    public static ImageView icon(String name)
    {
        ImageView iconFile = new ImageView("file:imageIcon/" + name + ".png");
        iconFile.setFitHeight(24);
        iconFile.setFitWidth(24);
        return iconFile;
    }
    public static Label label(String text)
    {
        Label textL = new Label(text);
        textL.setFont(Font.font("Arial", FontWeight.BOLD, 16f));
        textL.setTextFill(Color.WHITE);
        textL.setPadding(new Insets(7,0,0,0));
        return textL;
    }
}
