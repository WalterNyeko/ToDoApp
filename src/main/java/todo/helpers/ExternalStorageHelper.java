package todo.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import todo.model.ToDoItem;
import todo.model.ToDoItems;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExternalStorageHelper {
    public static void saveItemsToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ExternalStorageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveToExternalStorage(String value, Stage primaryStage, TextField newToDoList) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if (value.equalsIgnoreCase(ApplicationConstants.SAVE_ALL_ITEMS)) {
            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                List<ToDoItem> toDoItems = ToDoItems
                        .getInstance().getToDoItemArrayList();
                String json = new Gson().toJson(toDoItems);
                ExternalStorageHelper.saveItemsToFile(json, file);
            }
        }

        if (value.equalsIgnoreCase(ApplicationConstants.SAVE_LIST_ITEMS)) {
            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                List<ToDoItem> toDoItems = ToDoItems
                        .getInstance().getToDoItemArrayList().stream().filter(item -> item.getToDoListTitle().equalsIgnoreCase(newToDoList.getText())).collect(Collectors.toList());
                String json = new Gson().toJson(toDoItems);
                ExternalStorageHelper.saveItemsToFile(json, file);
            }
        }
    }

    public static void loadExternalItems(Stage primaryStage, TableView tableViewItems, TextField newToDoList) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        try {
            String list = Files.readAllLines(file.toPath(), Charset.defaultCharset() ).get(0);
            Type collectionType = new TypeToken<List<ToDoItem>>(){}.getType();
            List<ToDoItem> toDoItems = (List<ToDoItem>) new Gson()
                    .fromJson( list , collectionType);
            for (ToDoItem toDoItem : toDoItems) {
                ToDoItems.getInstance().getToDoItemArrayList().add(toDoItem);
            }
            TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
