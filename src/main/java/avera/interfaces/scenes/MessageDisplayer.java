package avera.interfaces.scenes;

import javafx.scene.control.Alert;

public class MessageDisplayer
{
    public static void displayMessage (String titleMessage, String headerMessage, Alert.AlertType typeAlert, String contextMessage)
    {
        Alert alert = new Alert(typeAlert);
        alert.setTitle(titleMessage);
        alert.setHeaderText(headerMessage);
        alert.setContentText(contextMessage);
        alert.showAndWait();
    }
}
