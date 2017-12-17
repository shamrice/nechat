package io.github.shamrice.neChat.application.client.ui;

import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import javafx.scene.control.Alert;

/**
 * Created by Erik on 11/27/2017.
 */
public class RootLayoutController {

    public void closeMenuItemClicked() {
        Log.get().logMessage(LogLevel.DEBUG, "Brutal quit.");
        System.exit(0);
    }

    public void aboutMenuItemClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("About");
        alert.setTitle("About");
        alert.setContentText("Some boring information about this program and what it's all about. Just filling" +
                "some space here for the time being and seeing what it looks like with a decent " +
                "length of text in this window. Blah blah blah");
        alert.showAndWait();
    }
}
