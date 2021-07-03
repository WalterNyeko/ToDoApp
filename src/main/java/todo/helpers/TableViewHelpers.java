package todo.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import todo.model.ToDoItem;
import todo.model.ToDoItems;
import todo.model.ToDoList;
import todo.model.ToDoLists;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableViewHelpers {
    public static void updateTableToDoList(TableView tableView) {
        List<ToDoList> toDoLists = ToDoLists.getInstance().getToDoLists();
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (toDoLists != null) {
            for (ToDoList toDoList : toDoLists) {
                if (toDoList != null) {
                    Map<String, Object> mapOfItem = new HashMap<>();
                    mapOfItem.put(APIConstants.TODO_LIST_TITLE, toDoList.getTitle());
                    mapOfItem.put(APIConstants.TODO_LIST_DESCRIPTION , toDoList.getDescription());
                    items.add(mapOfItem);
                }
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }

    public static void updateTableToDoItems(TableView tableView, String toDoItemTitle, Boolean isComplete) {
        List<ToDoItem> toDoItems = new ArrayList<>();
        if (isComplete == null) {
            toDoItems = ToDoItems.getInstance()
                    .getToDoItemArrayList()
                    .stream().filter(item -> item.getToDoListTitle()
                            .equalsIgnoreCase(toDoItemTitle))
                    .collect(Collectors.toList());
        }
        if (isComplete != null && isComplete) {
            toDoItems = ToDoItems.getInstance()
                    .getToDoItemArrayList()
                    .stream().filter(item -> item.getToDoListTitle()
                            .equalsIgnoreCase(toDoItemTitle) && item.isComplete())
                    .collect(Collectors.toList());
        }
        if (isComplete != null && !isComplete) {
            toDoItems = ToDoItems.getInstance()
                    .getToDoItemArrayList()
                    .stream().filter(item -> item.getToDoListTitle()
                            .equalsIgnoreCase(toDoItemTitle) && !item.isComplete())
                    .collect(Collectors.toList());
        }
        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();
        if (toDoItems != null) {
            for (ToDoItem toDoItem : toDoItems) {
                if (toDoItem != null) {
                    Map<String, Object> mapOfItem = new HashMap<>();

                    mapOfItem.put(APIConstants.TODO_ITEM_ID, toDoItem.getId());
                    mapOfItem.put(APIConstants.TODO_ITEM_DESC, toDoItem.getDescription());
                    mapOfItem.put(APIConstants.TODO_ITEM_DUE_DATE , toDoItem.getDueDate());
                    mapOfItem.put(APIConstants.TODO_ITEM_STATUS , toDoItem.isComplete()? APIConstants.COMPLETED : APIConstants.UNCOMPLETED);
                    items.add(mapOfItem);
                }
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(items);
        }
    }


    public static void addButtonToTable(TableView tableView,
                                        TableView tableViewItems,
                                        TextField title,
                                        TextField description,
                                        Button flexibleBtn,
                                        TitledPane titledPane,
                                        RadioButton allItems) {
        TableColumn<ToDoList, Void> colBtn = new TableColumn(APIConstants.ACTION_COLUMN_TEXT);
        colBtn.setMinWidth(200);
        Callback<TableColumn<ToDoList, Void>, TableCell<ToDoList, Void>> cellFactory = new Callback<TableColumn<ToDoList, Void>, TableCell<ToDoList, Void>>() {
            @Override
            public TableCell<ToDoList, Void> call(final TableColumn<ToDoList, Void> param) {
                final TableCell<ToDoList, Void> cell = new TableCell<ToDoList, Void>() {

                    private final Button btnEdit = new Button(APIConstants.EDIT_BTN_TEXT);
                    private final Button btnDelete = new Button(APIConstants.DELETE_BTN_TEXT);
                    private final Button btnView = new Button(APIConstants.VIEW_BTN_TEXT);
                    HBox hBox = new HBox(btnEdit, btnDelete, btnView);
                    {
                        hBox.setSpacing(10);
                        btnEdit.setOnAction((ActionEvent event) -> {
                            flexibleBtn.setDisable(false);
                            Map<String, String> toDoList = (Map<String, String>) getTableView().getItems().get(getIndex());
                            title.setText(toDoList.get(APIConstants.TODO_LIST_TITLE));
                            description.setText(toDoList.get(APIConstants.TODO_LIST_DESCRIPTION));
                            flexibleBtn.setText(APIConstants.EDIT_TODO_LIST);
                            ToDoLists.getInstance().setOriginalTitle(toDoList.get(APIConstants.TODO_LIST_TITLE));
                        });

                        btnDelete.setOnAction((ActionEvent event) -> {
                            flexibleBtn.setDisable(true);
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    APIConstants.ARE_YOU_SURE_TO_DELETE,
                                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                            alert.setTitle(APIConstants.DELETE_TITLE);
                            alert.setHeaderText(APIConstants.DELETE_HEADER_TEXT);
                            alert.showAndWait();
                            if (alert.getResult() == ButtonType.YES) {
                                Map<String, String> toDoList = (Map<String, String>) getTableView().getItems().get(getIndex());
                                List<ToDoList> toDoLists = ToDoLists.getInstance().getToDoLists();
                                for(int j = 0; j < toDoLists.size(); j++) {
                                    ToDoList obj = toDoLists.get(j);
                                    if(obj.getTitle().equalsIgnoreCase(toDoList.get(APIConstants.TODO_LIST_TITLE))){
                                        toDoLists.remove(j);
                                        break;
                                    }
                                }
                                updateTableToDoList(tableView);
                            }
                        });

                        btnView.setOnAction((ActionEvent event) -> {
                            Map<String, String> toDoList = (Map<String, String>) getTableView().getItems().get(getIndex());
                            title.setText(toDoList.get(APIConstants.TODO_LIST_TITLE));
                            description.setText(toDoList.get(APIConstants.TODO_LIST_DESCRIPTION));
                            updateTableToDoItems(tableViewItems, toDoList.get(APIConstants.TODO_LIST_TITLE), null);
                            titledPane.setText("Items For To-Do List: "+ toDoList.get(APIConstants.TODO_LIST_TITLE));
                            allItems.setSelected(true);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableView.getColumns().add(colBtn);
    }

    public static void setTableAppearance(TableView tableView) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefWidth(750);
        tableView.setPrefHeight(450);
    }

    public static void addButtonToItemsTable(
                                        TableView tableViewItems,
                                        TextField description,
                                        DatePicker dueDate,
                                        Button flexibleBtn,
                                        CheckBox completed) {
        TableColumn<ToDoItem, Void> colBtn = new TableColumn(APIConstants.ACTION_COLUMN_TEXT);
        colBtn.setMinWidth(120);
        Callback<TableColumn<ToDoItem, Void>, TableCell<ToDoItem, Void>> cellFactory = new Callback<TableColumn<ToDoItem, Void>, TableCell<ToDoItem, Void>>() {
            @Override
            public TableCell<ToDoItem, Void> call(final TableColumn<ToDoItem, Void> param) {
                final TableCell<ToDoItem, Void> cell = new TableCell<ToDoItem, Void>() {

                    private final Button btnEdit = new Button(APIConstants.EDIT_BTN_TEXT);
                    private final Button btnDelete = new Button(APIConstants.DELETE_BTN_TEXT);
                    HBox hBox = new HBox(btnEdit, btnDelete);
                    {
                        hBox.setSpacing(10);
                        btnEdit.setOnAction((ActionEvent event) -> {
                            flexibleBtn.setDisable(false);
                            Map<String, String> toDoItem = (Map<String, String>) getTableView().getItems().get(getIndex());
                            description.setText(toDoItem.get(APIConstants.TODO_ITEM_DESC));
                            dueDate.setValue(LocalDate.parse(toDoItem.get(APIConstants.TODO_ITEM_DUE_DATE)));
                            flexibleBtn.setText(APIConstants.EDIT_TODO_ITEM);
                            ToDoItems.getInstance().setUniqueId(toDoItem.get(APIConstants.TODO_ITEM_ID));
                            completed.setVisible(true);
                            completed.setSelected(toDoItem.get(APIConstants.TODO_ITEM_STATUS).equalsIgnoreCase(APIConstants.COMPLETED)? true : false);
                        });

                        btnDelete.setOnAction((ActionEvent event) -> {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    APIConstants.ARE_YOU_SURE_TO_DELETE_ITEM,
                                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                            alert.setTitle(APIConstants.DELETE_TITLE);
                            alert.setHeaderText(APIConstants.DELETE_HEADER_TEXT);
                            alert.showAndWait();
                            String title = "";
                            if (alert.getResult() == ButtonType.YES) {
                                Map<String, String> toDoItem = (Map<String, String>) getTableView().getItems().get(getIndex());
                                List<ToDoItem> toDoItemList = ToDoItems.getInstance().getToDoItemArrayList();
                                for(int j = 0; j < toDoItemList.size(); j++) {
                                    ToDoItem obj = toDoItemList.get(j);
                                    if(obj.getId().equalsIgnoreCase(toDoItem.get(APIConstants.TODO_ITEM_ID))){
                                        toDoItemList.remove(j);
                                        title = obj.getToDoListTitle();
                                        break;
                                    }
                                }
                                updateTableToDoItems(tableViewItems, title, null);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableViewItems.getColumns().add(colBtn);
    }

}


