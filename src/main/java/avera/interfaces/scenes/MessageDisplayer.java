package avera.interfaces.scenes;

import javafx.scene.control.Alert;

public class ErrorDisplayer
{
    public static void displayError(String titleMessage, String headerMessage, Alert.AlertType typeAlert, String contextMessage)
    {
        Alert alert = new Alert(typeAlert);
        alert.setTitle(titleMessage);
        alert.setHeaderText(headerMessage);
        alert.setContentText(contextMessage);
        alert.showAndWait();
    }
}
