package todo.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UIComponentsHelper {
    public static void clearTextFieldsAndChangeButtonText(TextField newToDoList, TextField descToDoList, Button buttonAddToList) {
        newToDoList.setText(null);
        descToDoList.setText(null);
        buttonAddToList.setText(ApplicationConstants.ADD_TODO_LIST);
        buttonAddToList.setDisable(false);
    }

    public static void showAlert(Alert.AlertType alertType, String headerText, String titleText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setTitle(titleText);
        alert.showAndWait();
    }
}
