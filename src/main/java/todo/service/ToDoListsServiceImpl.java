package todo.service;

import todo.model.ToDoItem;
import todo.model.ToDoItems;
import todo.model.ToDoList;
import todo.model.ToDoLists;

import java.util.List;

public class ToDoListsServiceImpl implements ToDoListsService{

    @Override
    public void addToDoList(ToDoList toDoList) {
        List<ToDoList> toDoLists = ToDoLists.getInstance().getToDoLists();
        if (toDoList != null) {
            toDoLists.add(toDoList);
        }
    }

    @Override
    public void editToDoList(ToDoList toDoList, String originalTitle) {
        List<ToDoList> toDoLists = ToDoLists.getInstance().getToDoLists();
        if (toDoList != null) {
            for (int i = 0; i < toDoLists.size(); i++) {
                if (toDoLists.get(i).getTitle().equalsIgnoreCase(originalTitle)) {
                    toDoLists.set(i,toDoList);
                }
            }
        }

        List<ToDoItem> toDoItems = ToDoItems.getInstance().getToDoItemArrayList();

        for (int i = 0; i < toDoItems.size(); i++) {
            if (toDoItems.get(i).getToDoListTitle().equalsIgnoreCase(originalTitle)) {
                ToDoItem toDoItem = toDoItems.get(i);
                toDoItem.setToDoListTitle(toDoList.getTitle());
                toDoItems.set(i,toDoItem);
            }
        }

    }
}
