package todo.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoItems {

    private static List<ToDoItem> toDoItemArrayList = new ArrayList<>();

    private static String uniqueId;

    private static ToDoItems toDoItems = new ToDoItems();

    private ToDoItems() {}

    public static ToDoItems getInstance() {
        return toDoItems;
    }

    public static List<ToDoItem> getToDoItemArrayList() {
        return toDoItemArrayList;
    }

    private static void setToDoItemArrayList(List<ToDoItem> toDoItemArrayList) {
        ToDoItems.toDoItemArrayList = toDoItemArrayList;
    }

    public static String getUniqueId() {
        return uniqueId;
    }

    public static void setUniqueId(String uniqueId) {
        ToDoItems.uniqueId = uniqueId;
    }
}
