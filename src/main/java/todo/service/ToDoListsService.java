package todo.service;

import todo.model.ToDoList;

public interface ToDoListsService {
    void addToDoList(ToDoList toDoList);
    void editToDoList(ToDoList toDoList, String originalTitle);
}
