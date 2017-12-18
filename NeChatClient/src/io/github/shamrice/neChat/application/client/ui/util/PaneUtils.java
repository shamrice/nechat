package io.github.shamrice.neChat.application.client.ui.util;

import io.github.shamrice.neChat.application.client.Main;
import io.github.shamrice.neChat.application.client.ui.login.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Erik on 12/18/2017.
 */
public class PaneUtils {

    public boolean showLoginPaneAndSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("ui/login/Login.fxml"));
            AnchorPane loginPane = loader.load();

            LoginController loginController = loader.getController();

            Scene scene = new Scene(loginPane);
            Stage loginStage = new Stage();
            loginStage.setTitle("NeChat - Sign In");

            loginStage.setScene(scene);
            loginStage.showAndWait();

            return loginController.isAuthenticated();

        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }

        return false;
    }
}
