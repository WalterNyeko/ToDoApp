package todo;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import todo.helpers.APIConstants;
import todo.helpers.Helpers;
import todo.helpers.TableViewHelpers;
import todo.model.ToDoItem;
import todo.model.ToDoItems;
import todo.model.ToDoList;
import todo.model.ToDoLists;
import todo.service.ToDoListsService;
import todo.service.ToDoListsServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ToDoApp extends Application {
    final double TITLE_FONT_SIZE = 30.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label nameLabel = new Label(APIConstants.NAME_OF_TODO_LIST);
        nameLabel.setPrefWidth(100);
        TextField newToDoList = new TextField();

        Label descLabel = new Label(APIConstants.DESCRIPTION_OF_TODO_LIST);
        descLabel.setPrefWidth(100);
        TextField descToDoList = new TextField();

        Button buttonAddToList = new Button(APIConstants.ADD_TODO_LIST);
        newToDoList.setMinWidth(40);

        Button buttonClearListFields = new Button(APIConstants.CLEAR);
        buttonClearListFields.setMinWidth(40);

        Label title = new Label(APIConstants.HEADING_TO_DO_LIST);
        title.setFont(new Font(TITLE_FONT_SIZE));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.BASELINE_CENTER);
        titleBox.setPadding(new Insets(30, 10, 10, 10));

        HBox upperItems = new HBox();
        upperItems.setSpacing(20);
        upperItems.getChildren()
                .addAll(nameLabel,
                        newToDoList);
        upperItems.setAlignment(Pos.BOTTOM_LEFT);
        upperItems.setPadding(new Insets(10, 10, 10, 80));

        HBox lowerItems = new HBox();
        lowerItems.setSpacing(20);
        lowerItems.getChildren()
                .addAll(descLabel,
                        descToDoList,
                        buttonAddToList,
                        buttonClearListFields);
        lowerItems.setAlignment(Pos.BOTTOM_LEFT);
        lowerItems.setPadding(new Insets(10, 10, 10, 80));

        TableView tableView = new TableView();

        TableColumn<Map, String> titleColumn = new TableColumn<>(APIConstants.TODO_LIST_TITLE_TEXT);
        titleColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_LIST_TITLE));
        titleColumn.setMinWidth(150);

        TableColumn<Map, String> descriptionColumn = new TableColumn<>(APIConstants.TODO_LIST_DESCRIPTION_TEXT);
        descriptionColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_LIST_DESCRIPTION));
        descriptionColumn.setMinWidth(250);

        tableView.getColumns().add(titleColumn);
        tableView.getColumns().add(descriptionColumn);


        TableViewHelpers.updateTableToDoList(tableView);
        TableViewHelpers.setTableAppearance(tableView);

        TableView tableViewItems = new TableView();

        TableColumn<Map, String> idColumn = new TableColumn<>(APIConstants.TODO_ITEM_ID_TEXT);
        idColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_ITEM_ID));
        idColumn.setMinWidth(500);
        idColumn.setVisible(false);

        TableColumn<Map, String> itemDescriptionColumn = new TableColumn<>(APIConstants.TODO_ITEM_DESC_TEXT);
        itemDescriptionColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_ITEM_DESC));
        itemDescriptionColumn.setMinWidth(200);

        TableColumn<Map, String> itemDueDateColumn = new TableColumn<>(APIConstants.TODO_ITEM_DUE_DATE_TEXT);
        itemDueDateColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_ITEM_DUE_DATE));
        itemDueDateColumn.setMinWidth(100);

        TableColumn<Map, String> itemCompletionStatusColumn = new TableColumn<>(APIConstants.TODO_ITEM_STATUS_TEXT);
        itemCompletionStatusColumn.setCellValueFactory(new MapValueFactory<>(APIConstants.TODO_ITEM_STATUS));
        itemCompletionStatusColumn.setMinWidth(100);

        tableViewItems.getColumns().add(idColumn);
        tableViewItems.getColumns().add(itemDescriptionColumn);
        tableViewItems.getColumns().add(itemDueDateColumn);
        tableViewItems.getColumns().add(itemCompletionStatusColumn);

        TableViewHelpers.updateTableToDoItems(tableViewItems, "", null);
        TableViewHelpers.setTableAppearance(tableViewItems);

        buttonAddToList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                ToDoListsService toDoListsService = new ToDoListsServiceImpl();
                if (!newToDoList.getText().strip().isBlank()){
                    ToDoList toDoList = new ToDoList(newToDoList.getText(), descToDoList.getText());
                    if (buttonAddToList.getText().equalsIgnoreCase(APIConstants.EDIT_TODO_LIST)) {
                        toDoListsService.editToDoList(toDoList, ToDoLists.getInstance().getOriginalTitle());
                        TableViewHelpers.updateTableToDoList(tableView);
                        newToDoList.clear();
                        descToDoList.clear();
                        buttonAddToList.setText(APIConstants.ADD_TODO_LIST);
                    }else if (buttonAddToList.getText().equalsIgnoreCase(APIConstants.ADD_TODO_LIST)){
                        toDoListsService.addToDoList(toDoList);
                        TableViewHelpers.updateTableToDoList(tableView);
                        newToDoList.clear();
                        descToDoList.clear();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(APIConstants.MISSING_TITLE);
                    alert.setTitle(APIConstants.MISSING_HEADER_TITLE);
                    alert.showAndWait();
                }



            }
        });
        buttonClearListFields.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newToDoList.setText(null);
                descToDoList.setText(null);
                buttonAddToList.setText(APIConstants.ADD_TODO_LIST);
                buttonAddToList.setDisable(false);
            }
        });

        tableView.setMinWidth(500);
        tableViewItems.setMinWidth(500);
        tableView.setPlaceholder(new Label(APIConstants.NO_TODO_LISTS));
        tableViewItems.setPlaceholder(new Label(APIConstants.NO_TODO_ITEMS));
        /**
         * Get Selected List
         */
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        ObservableList selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<?> change) {
                List<Map<String, String>> toDoLists = (List<Map<String, String>>) change.getList();
                if (buttonAddToList.getText().equalsIgnoreCase(APIConstants.EDIT_TODO_LIST)) {
                    newToDoList.setText(toDoLists.get(0).get(APIConstants.TODO_LIST_TITLE));
                    descToDoList.setText(toDoLists.get(0).get(APIConstants.TODO_LIST_DESCRIPTION));
                    ToDoLists.getInstance().setOriginalTitle(toDoLists.get(0).get(APIConstants.TODO_LIST_TITLE));
                }
            }
        });


        HBox tableBox = new HBox(tableView);
        tableBox.setPadding(new Insets(10, 10, 10, 10));
        tableBox.setAlignment(Pos.BASELINE_CENTER);

        VBox allItems = new VBox();
        allItems.setSpacing(20);
        allItems.getChildren().addAll(upperItems, lowerItems, tableBox);
        allItems.setPadding(new Insets(20, 20, 20, 20));

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        TitledPane toDoListTitlePane = new TitledPane();
        toDoListTitlePane.setCollapsible(true);
        toDoListTitlePane.setContent(allItems);
        toDoListTitlePane.setText(title.getText());
        toDoListTitlePane.setPadding(new Insets(20, 20, 20, 20));
        toDoListTitlePane.setMaxWidth(visualBounds.getWidth() / 2);
        toDoListTitlePane.setMaxHeight(visualBounds.getHeight() - 100);

        TitledPane toDoItemsTitlePane = new TitledPane();
        toDoItemsTitlePane.setCollapsible(true);
        toDoItemsTitlePane.setText(APIConstants.TO_DO_ITEMS_HEADING);
        toDoItemsTitlePane.setPadding(new Insets(20, 20, 20, 20));
        toDoItemsTitlePane.setMaxWidth(visualBounds.getWidth() / 2 - 50);
        toDoItemsTitlePane.setMaxHeight(visualBounds.getHeight() - 100);


        Label labelDueDate = new Label(APIConstants.TODO_ITEM_DUE_DATE_TEXT);
        labelDueDate.setMinWidth(200);
        DatePicker datePicker = new DatePicker();
        datePicker.setMinWidth(200);

        /**
         * Ensure we cannot select past dates as due dates
         */
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) < 0);
                    }

                };
            }

        };
        datePicker.setDayCellFactory(callB);
        /**
         * Ends Here
         */

        Label labelDesc = new Label(APIConstants.TODO_ITEM_DESC_TEXT);
        labelDesc.setMinWidth(200);
        TextField descTextField = new TextField();
        descTextField.setMinWidth(200);

        CheckBox completed = new CheckBox(APIConstants.COMPLETED);
        completed.setVisible(false);

        Button buttonAddItem = new Button(APIConstants.ADD_TODO_ITEM);
        buttonAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (buttonAddItem.getText().equalsIgnoreCase(APIConstants.ADD_TODO_ITEM)) {
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
                }else if (buttonAddItem.getText().equalsIgnoreCase(APIConstants.EDIT_TODO_ITEM)) {
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
                    buttonAddItem.setText(APIConstants.ADD_TODO_ITEM);
                    descTextField.setText(null);
                    datePicker.setValue(null);
                    completed.setVisible(false);
                }
            }
        });

        HBox addItemsBox = new HBox(labelDesc, descTextField, completed);
        HBox dueDateItemsBox = new HBox(labelDueDate, datePicker, buttonAddItem);
        // create a tile pane
        VBox radios = new VBox();
        radios.setSpacing(10);
        radios.setPadding(new Insets(10));
        // create radiobuttons
        RadioButton completedItems = new RadioButton(APIConstants.COMPLETED_ITEMS);
        RadioButton uncompletedItems = new RadioButton(APIConstants.UNCOMPLETED_ITEMS);
        RadioButton allTheItems = new RadioButton(APIConstants.ALL_ITEMS);

        ToggleGroup tg = new ToggleGroup();
        completedItems.setToggleGroup(tg);
        uncompletedItems.setToggleGroup(tg);
        allTheItems.setToggleGroup(tg);

        radios.getChildren().add(completedItems);
        radios.getChildren().add(uncompletedItems);
        radios.getChildren().add(allTheItems);

        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {

                RadioButton rb = (RadioButton)tg.getSelectedToggle();

                if (rb != null) {
                    String text = rb.getText();
                    if (text.equalsIgnoreCase(APIConstants.COMPLETED_ITEMS)) {
                        TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), true);
                    }else if (text.equalsIgnoreCase(APIConstants.UNCOMPLETED_ITEMS)) {
                        TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), false);
                    }else if (text.equalsIgnoreCase(APIConstants.ALL_ITEMS)) {
                        TableViewHelpers.updateTableToDoItems(tableViewItems, newToDoList.getText(), null);
                    }
                }
            }
        });

        Label saveLabel = new Label(APIConstants.SAVE_TO_EXTERNAL_STORAGE);
        ComboBox comboBoxSave = new ComboBox();
        comboBoxSave.setMinWidth(200);
        saveLabel.setMinWidth(100);
        comboBoxSave.getItems().add(APIConstants.SAVE_ALL_ITEMS);
        comboBoxSave.getItems().add(APIConstants.SAVE_LIST_ITEMS);
        HBox saveHbox = new HBox(saveLabel, comboBoxSave);
        saveHbox.setSpacing(10);

        Label loadLabel = new Label(APIConstants.LOAD_FROM_EXTERNAL_STORAGE);
        ComboBox comboBoxLoad = new ComboBox();
        comboBoxLoad.setMinWidth(200);
        loadLabel.setMinWidth(100);
        comboBoxLoad.getItems().add(APIConstants.LOAD_TODO_LIST_ITEMS);
        comboBoxLoad.getItems().add(APIConstants.LOAD_ALL_ITEMS);
        HBox loadHbox = new HBox(loadLabel, comboBoxLoad);
        loadHbox.setSpacing(10);

        comboBoxSave.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String value) {
                saveToExternalStorage(value, primaryStage, newToDoList);
            }
        });

        comboBoxLoad.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
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
        });
        VBox boxSaveAndLoad = new VBox(saveHbox, loadHbox);
        boxSaveAndLoad.setSpacing(10);

        HBox filtersBox = new HBox(radios, boxSaveAndLoad);
        HBox tableViewItemsBox = new HBox(tableViewItems);


        TableViewHelpers.addButtonToTable(
                tableView, tableViewItems,
                newToDoList, descToDoList,
                buttonAddToList, toDoItemsTitlePane,
                allTheItems
        );

        addItemsBox.setSpacing(25);
        dueDateItemsBox.setSpacing(25);
        filtersBox.setSpacing(10);
        addItemsBox.setPadding(new Insets(20));
        dueDateItemsBox.setPadding(new Insets(20));
        tableViewItemsBox.setPadding(new Insets(30));
        filtersBox.setPadding(new Insets(0,0,0, 20));
        VBox vBoxItems = new VBox(addItemsBox, dueDateItemsBox, filtersBox, tableViewItemsBox);

        toDoItemsTitlePane.setContent(vBoxItems);
        HBox bothSections = new HBox(toDoListTitlePane, toDoItemsTitlePane);

        TableViewHelpers.addButtonToItemsTable(tableViewItems, descTextField, datePicker, buttonAddItem, completed);

        Scene scene = new Scene(bothSections, visualBounds.getWidth(), visualBounds.getHeight());

        primaryStage.setTitle("To-Do List Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveToExternalStorage(String value, Stage primaryStage, TextField newToDoList) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        if (value.equalsIgnoreCase(APIConstants.SAVE_ALL_ITEMS)) {
            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                List<ToDoItem> toDoItems = ToDoItems
                        .getInstance().getToDoItemArrayList();
                String json = new Gson().toJson(toDoItems);
                Helpers.saveItemsToFile(json, file);
            }
        }

        if (value.equalsIgnoreCase(APIConstants.SAVE_LIST_ITEMS)) {
            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                List<ToDoItem> toDoItems = ToDoItems
                        .getInstance().getToDoItemArrayList().stream().filter(item -> item.getToDoListTitle().equalsIgnoreCase(newToDoList.getText())).collect(Collectors.toList());
                String json = new Gson().toJson(toDoItems);
                Helpers.saveItemsToFile(json, file);
            }
        }
    }

}