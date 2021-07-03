package todo.service;

import javafx.scene.control.*;
import todo.helpers.ApplicationConstants;
import todo.helpers.TableViewHelpers;
import todo.helpers.UIComponentsHelper;
import todo.model.ToDoItem;
import todo.model.ToDoItems;
import todo.model.ToDoList;
import todo.model.ToDoLists;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ToDoListsServiceImpl implements ToDoListsService{

    @Override
    public void addToDoList(ToDoList toDoList) {
        List<ToDoList> toDoLists = ToDoLists.getInstance().getToDoLists();
        if (toDoList != null) {
            List<ToDoList> theFilteredList = toDoLists.stream().filter(list -> list.getTitle().equalsIgnoreCase(toDoList.getTitle())).collect(Collectors.toList());
            if (theFilteredList.isEmpty()) {
                toDoLists.add(toDoList);
            }else {
                UIComponentsHelper.showAlert(Alert.AlertType.INFORMATION,
                        ApplicationConstants.PROVIDE_VALID_TITLE,
                        ApplicationConstants.TITLE_DUPLICATION);
            }
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

    @Override
    public void addToDoItem(Button buttonAddItem, TextField descTextField,
                            DatePicker datePicker, TextField newToDoList,
                            TableView tableViewItems, CheckBox completed) {
        if (buttonAddItem.getText().equalsIgnoreCase(ApplicationConstants.ADD_TODO_ITEM)) {
            int leftLimit = 48;
            int rightLimit = 122;
            int targetStringLength = 10;
            Random random = new Random();
            String generatedRandomString = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            ToDoItem toDoItem = new ToDoItem(
                    generatedRandomString,
                    descTextField.getText(),
                    datePicker.getValue().toString(),
                    false,
                    newToDoList.getText());
            ToDoItems.getInstance()
                    .getToDoItemArrayList().add(toDoItem);
            TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), null);
            descTextField.clear();
            datePicker.setValue(null);
        }else if (buttonAddItem.getText().equalsIgnoreCase(ApplicationConstants.EDIT_TODO_ITEM)) {
            String id = ToDoItems.getInstance().getUniqueId();
            List<ToDoItem> toDoItems = ToDoItems.getInstance()
                    .getToDoItemArrayList()
                    .stream().filter(item -> item.getId()
                            .equalsIgnoreCase(id))
                    .collect(Collectors.toList());
            ToDoItem toDoItem = toDoItems.get(0);
            toDoItem.setDueDate(datePicker.getValue().toString());
            toDoItem.setDescription(descTextField.getText());
            toDoItem.setComplete(completed.isSelected()? true : false);
            TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), null);
            buttonAddItem.setText(ApplicationConstants.ADD_TODO_ITEM);
            descTextField.setText(null);
            datePicker.setValue(null);
            completed.setVisible(false);
        }
    }
}
