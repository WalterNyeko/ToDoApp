package todo.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoLists {

    private static ToDoLists instance = new ToDoLists();

    private List<ToDoList> toDoLists = new ArrayList<>();

    private String originalTitle = "";

    private ToDoLists () { }

    public static ToDoLists getInstance() {
        if (instance == null) {
            instance = new ToDoLists();
        }
        return instance;
    }


    public List<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(List<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
}
