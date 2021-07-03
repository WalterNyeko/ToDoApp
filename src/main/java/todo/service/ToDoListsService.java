package todo.service;

import javafx.scene.control.*;
import todo.model.ToDoList;

public interface ToDoListsService {
    void addToDoList(ToDoList toDoList);
    void editToDoList(ToDoList toDoList, String originalTitle);
    void addToDoItem(Button buttonAddItem, TextField descTextField,
                     DatePicker datePicker, TextField newToDoList,
                     TableView tableViewItems, CheckBox completed);
}
