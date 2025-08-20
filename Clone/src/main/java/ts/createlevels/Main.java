package ts.createlevels;

import javafx.application.Application;
import javafx.stage.Stage;
import ts.createlevels.Forms.MainScene;

import java.io.File;

import static ts.createlevels.ProgramSettings.*;

public class Main extends Application
{
    @Override
    public void start(Stage stage)
    {
        if(!new File("save").exists()) createDirectory("save");

        stage.setTitle("Создание 2D уровней - " + VERSION);
        stage.setMaximized(true);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.show();
        MainScene.open(stage);
    }
    public static void main(String[] args)
    {
        launch();
    }
}