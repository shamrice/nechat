package io.github.shamrice.neChat.application.client.ui.login;

import io.github.shamrice.neChat.application.client.context.ApplicationContext;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;
import io.github.shamrice.neChat.application.rest.client.requests.authorization.AuthorizationResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Erik on 11/26/2017.
 */
public class LoginController {

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button cancelButton;

    private boolean isAuthenticated;

    public LoginController() {
        this.isAuthenticated = false;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @FXML
    public void initialize() {

    }

    public void signInButtonClicked() {

        /* set by config builder from config for testing
        ApplicationContext.get().getNeChatRestClient().setUserCredentials(
                new UserCredentials("test", "password")
        );
        */

        /*
        if (ApplicationContext.get().getCurrentLogin() == null || ApplicationContext.get().getCurrentLogin().isEmpty()) {
            //TODO : Display dialog window to prompt for username / password.
            System.out.println("No login in config.");

        }
*/

        String login = loginTextField.getText();
        String password = passwordField.getText();

        if (login != null && !login.isEmpty() && password != null && !password.isEmpty()) {
            ApplicationContext.get().getNeChatRestClient().setUserCredentials(
                    new UserCredentials(login, password)
            );

            try {
                AuthorizationResponse response = (AuthorizationResponse) ApplicationContext.get().getNeChatRestClient().getAuthToken();
                if (response.getAuthToken().length() > 0) {
                    System.out.println("logged in and received token: " + response.getAuthToken());
                    isAuthenticated = true;
                    closeStage();

                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Login", "Invalid credentials supplied. Please try again.", null);
                    System.out.println("Unable to log in with credentials.");

                }
            } catch (NullPointerException nullPointerExc) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Failed to sign in",
                        "Failed to sign in with given credentials. Please try again.",
                        null
                );

                System.out.println("Null response we encountered getting token. Failing.");
                nullPointerExc.printStackTrace();

            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Login", "Please enter a login and password", null);
        }
    }

    public void cancelButtonClicked() {
        closeStage();
    }

    private void showAlert(Alert.AlertType alertType, String titleText, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        if (titleText != null && !titleText.isEmpty()) {
            alert.setTitle(titleText);
        }
        if (headerText != null && !headerText.isEmpty()) {
            alert.setHeaderText(headerText);
        }
        if (contentText != null && !contentText.isEmpty()) {
            alert.setContentText(contentText);
        }

        alert.showAndWait();
    }

    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
