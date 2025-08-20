package ts.createlevels;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ts.createlevels.Forms.CanvasScene;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

import static ts.createlevels.ProgramSettings.*;

public class Logic
{
    public static Color pouringColor = Color.WHITE, replacementColor = Color.RED;
    public static short canvasWidth = 10, canvasHeight = 10;
    public static String replacement = "-1", nameProject = "", replacementClick = "0";
    public static String[] masCanvasId;
    public static Color[] masCanvasColor;

    public static void save(Canvas canvas, Stage stage, boolean IsExport,int x1,int x2,int y1,int y2)
    {
        try
        {
            if(!new File("save").exists()) createDirectory("save");
            if(!new File("save/" + nameProject).exists()) createDirectory("save/" + nameProject);
            lastSavedPrint();

            String path = "save/" + nameProject + "/" + nameProject +".save";
            try (PrintWriter writer = new PrintWriter(path))
            {
                for (int y = 0; y < canvasHeight; y++)
                {
                    for (int x = 0; x < canvasWidth; x++)
                    {
                        writer.print(masCanvasId[(y * Logic.canvasWidth) + x] + ",");
                    }
                    writer.println();
                }
                writer.print("*");
                for (int y = 0; y < canvasHeight; y++)
                {
                    for (int x = 0; x < canvasWidth; x++)
                    {
                        writer.print(masCanvasColor[(y * canvasWidth) + x] + ",");
                    }
                    writer.println();
                }
                writer.print("*");
                writer.println();
                writer.print("Width=" + canvasWidth + "\n*Height=" + canvasHeight + "\n*RepId=" + replacementClick + "\n*RepColor=" + replacementColor);
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
            WritableImage image = new WritableImage (
                    (int)canvas.getWidth(),
                    (int)canvas.getHeight()
            );

            stage.setMaximized(false);
            canvas.snapshot(null, image);
            File file = new File(path + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            stage.setMaximized(true);

            if(IsExport)
            {
                DirectoryChooser dir = new DirectoryChooser();
                dir.setTitle("Экспорт уровня");
                dir.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                File selectDir = dir.showDialog(stage);
                if(selectDir != null)
                {
                    int b = x1, b2 = y1;
                    path = selectDir.toPath() + "/level" + ".txt";
                    try (PrintWriter writer = new PrintWriter(path))
                    {
                        for(int i = canvasWidth * y1 + x1;i < canvasWidth * canvasHeight;i++)
                        {
                            writer.print(masCanvasId[i] + ",");
                            if(b == x2)
                            {
                                b2++;
                                i += canvasWidth - x2 + x1 - 1;
                                b = x1 - 1;
                                writer.println();
                                if(b2 == y2 + 1) break;
                            }
                            b++;
                        }
                    }
                    catch (Exception e)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static void openFileSave(Stage stage, BorderPane stack, String path)
    {
        FileChooser file = new FileChooser();
        file.setTitle("Выберите файл сохранения");
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл сохранения", "*.save"));
        file.setInitialDirectory(new File("save/"));

        File open;
        if(path == null) open = file.showOpenDialog(stage);
        else open = new File(path);
        if(open != null)
        {
            try
            {
                String[] mas = Files.readString(open.toPath()).split("\\*");
                nameProject = open.getName().split(".save")[0];
                canvasWidth = Short.parseShort(mas[2].split("=")[1].trim());
                canvasHeight = Short.parseShort(mas[3].split("=")[1].trim());
                replacementClick = mas[4].split("=")[1].trim();
                replacementColor = Color.web(mas[5].split("=")[1].trim().replace("0x", "#"));

                int masLength = mas[0].split(",").length;
                String[] masStr = mas[0].split(",");
                String[] masColor = mas[1].split(",");
                masCanvasId = new String[masLength];
                masCanvasColor = new Color[masLength];
                for (short i = 0;i < masLength - 1;i++)
                {
                    masCanvasId[i] = masStr[i].trim();;
                    masCanvasColor[i] = Color.web(masColor[i].trim().replace("0x", "#"));
                }
                stack.getChildren().clear();
                CanvasScene.open(stage, false);
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    //Работа с сохранениями
    public static ArrayList<String[]> lastSavedOpen()
    {
        if(new File("saved.txt").exists() && lastSavedCount() > 0)
        {
            ArrayList<String[]> dataList = new ArrayList<>();
            try
            {
                File file = new File("saved.txt");
                String[] mas = Files.readString(file.toPath()).split("\\*");
                String[] masTime = new String[mas.length];

                for(byte i = 0;i < mas.length;i++)
                {
                    File dir = new File("save/" + mas[i] + "/");
                    File[] dirList = dir.listFiles();
                    assert dirList != null;
                    for(File fileI : dirList)
                    {
                        if(fileI.isFile() && fileI.getName().toLowerCase().endsWith(".png"))
                        {
                            Path path = fileI.toPath();
                            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                            long timeCreate = attributes.creationTime().toMillis();
                            Date date = new Date(timeCreate);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            masTime[i] = format.format(date);
                        }
                    }
                }
                for(byte i = 0;i < mas.length;i++)
                {
                    dataList.add(new String[] {mas[i], masTime[i], "save/" + mas[i] + "/" + mas[i] + ".save.png"});
                }
                return dataList;
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
        return null;
    }

    public static void lastSavedPrint()
    {
        if(!new File("saved.txt").exists())
        {
            try
            {
                Files.write(Paths.get("saved.txt"), "".getBytes());
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
        boolean next = true;
        int count = lastSavedCount();
        byte masLength = 0;
        if(count >= 6) masLength = 1;
        StringBuilder str = new StringBuilder();
        str.append(Logic.nameProject).append("*");
        try
        {
            File file = new File("saved.txt");
            String[] mas = Files.readString(file.toPath()).split("\\*");

            for (String ma : mas)
            {
                if (ma.contains(Logic.nameProject))
                {
                    next = false;
                    break;
                }
            }
            if(next)
            {
                for(byte i = 0;i < mas.length - masLength;i++)
                {
                    str.append(mas[i]).append("*");
                }
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

        if(next)
        {
            try(PrintWriter writer = new PrintWriter(new FileWriter("saved.txt", false)))
            {
                writer.print(str);
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public static void lastSavedDelete(byte id, String path)
    {
        StringBuilder str = new StringBuilder();
        try
        {
            File file = new File("saved.txt");
            String[] mas = Files.readString(file.toPath()).split("\\*");
            for(byte i = 0;i < mas.length;i++)
            {
                if(i != id) str.append(mas[i]).append("*");
                else mas[i] = null;
            }
            try(PrintWriter writer = new PrintWriter(new FileWriter("saved.txt", false)))
            {
                writer.print(str);
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }

            Path directory = Paths.get(path);
            Files.walk(directory)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path1 -> {
                        try {
                            Files.delete(path1);
                        }
                        catch (IOException ignored) {}
                    });

            File file2 = new File("save/");
            File[] files = file2.listFiles();
            assert files != null;
            int fileCount = files.length;
            file = new File("saved.txt");
            mas = Files.readString(file.toPath()).split("\\*");
            boolean foundUnique = false;
            String uniqueName = null;
            if(fileCount > 5)
            {
                for (File f : files)
                {
                    String fileName = f.getName();
                    boolean isUnique = true;

                    for (String ma : mas)
                    {
                        if (ma.equals(fileName))
                        {
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique)
                    {
                        foundUnique = true;
                        uniqueName = fileName;
                        break;
                    }
                }
                if (foundUnique)
                {
                    Logic.nameProject = uniqueName;
                    lastSavedPrint();
                }
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static int lastSavedCount()
    {
        int count = 0;
        try
        {
            File file = new File("saved.txt");
            count = Files.readString(file.toPath()).split("\\*").length;
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        return count;
    }
}
