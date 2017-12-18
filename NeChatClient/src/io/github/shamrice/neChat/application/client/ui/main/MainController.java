package io.github.shamrice.neChat.application.client.ui.main;

import io.github.shamrice.neChat.application.client.Main;
import io.github.shamrice.neChat.application.client.context.ApplicationContext;
import io.github.shamrice.neChat.application.client.ui.main.buddy.BuddyModel;
import io.github.shamrice.neChat.application.client.ui.util.PaneUtils;
import io.github.shamrice.neChat.application.rest.client.requests.Response;
import io.github.shamrice.neChat.application.rest.client.requests.StatusResponse;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.Buddy;
import io.github.shamrice.neChat.application.rest.client.requests.messages.Message;
import io.github.shamrice.neChat.application.rest.client.requests.messages.MessagesResponse;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Created by Erik on 11/16/2017.
 */
public class MainController {

    @FXML
    private TableView<BuddyModel> buddyModelTableView;

    @FXML
    private TableColumn<BuddyModel, String> buddyModelListTableColumn;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField sendMessageTextField;

    @FXML
    private Button addBuddyButton;

    @FXML
    private Button removeBuddyButton;


    private Main main;
    private Object chatTextAreaLockObj = null;

    public MainController() {}

    @FXML
    private void initialize() {

        buddyModelListTableColumn.setCellValueFactory(cellData -> cellData.getValue().getBuddyLogin());

        buddyModelTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setSelectedBuddy(newValue)
        );
    }

    public void addBuddyButtonClicked() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setGraphic(null);
        textInputDialog.setTitle("Add Buddy");
        textInputDialog.setHeaderText("New buddy name to add:");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Log.get().logMessage(LogLevel.DEBUG, "New buddy to add: " + result.get());

            String buddyToAdd = result.get().replace(" ", "");

            if (!buddyToAdd.toLowerCase().equals(ApplicationContext.get().getCurrentLogin().toLowerCase())) {

                StatusResponse response = (StatusResponse) ApplicationContext.get()
                        .getNeChatRestClient()
                        .addBuddy(buddyToAdd);

                if (response.isSuccess()) {
                    refreshBuddyList();

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Buddy Added",
                            buddyToAdd + " has been added to your buddy list.",
                            null,
                            false
                    );
                } else {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Buddy Add Failure",
                            "Buddy " + buddyToAdd + " was not able to be added.",
                            response.getMessage(),
                            response.isAuthFailure()
                    );
                }
            } else {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Buddy Add Failure",
                        "You cannot add yourself to your buddy list.",
                        null,
                        false
                );
            }
        }
    }

    public void removeBuddyButtonClicked() {

        String buddyToRemove = ApplicationContext.get().getSelectedBuddyLogin();
        if (buddyToRemove != null) {
            StatusResponse response = (StatusResponse) ApplicationContext.get()
                    .getNeChatRestClient()
                    .removeBuddy(buddyToRemove);

            if (response.isSuccess()) {
                //refresh buddy list, select first cell, set selected buddy if any left and refresh the chat area.
                refreshBuddyList();
                buddyModelTableView.getSelectionModel().selectFirst();
                BuddyModel selectedBuddy =buddyModelTableView.getSelectionModel().getSelectedItem();
                if (selectedBuddy != null) {
                    ApplicationContext.get().setSelectedBuddyLogin(
                            selectedBuddy.getBuddyLogin().getValue()
                    );
                } else {
                    ApplicationContext.get().setSelectedBuddyLogin(null);
                }
                refreshChatTextArea();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Buddy Removed");
                alert.setHeaderText(buddyToRemove + " has been removed from your buddy list.");
                alert.show();
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Buddy Removal Error",
                        "Buddy " + buddyToRemove + " was not able to be removed.",
                        response.getMessage(),
                        response.isAuthFailure()
                );
            }
        }
    }


    public void sendMessageKeyboardInput() {
        sendMessageButtonClicked();
    }

    public void sendMessageButtonClicked() {
        String toLogin = ApplicationContext.get().getSelectedBuddyLogin();
        String message = sendMessageTextField.getText();

        if (toLogin != null && message != null && message.length() > 0) {
             StatusResponse response =
                     (StatusResponse) ApplicationContext.get()
                             .getNeChatRestClient().sendMessage(toLogin, message);

            if (response.isSuccess()) {
                refreshChatTextArea();
                chatTextAreaChanged();
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Send Message Error",
                        "Unable to send message to " + toLogin,
                        response.getMessage(),
                        response.isAuthFailure()
                );
            }
        }
        sendMessageTextField.setText("");
    }

    public void chatTextAreaChanged() {
        chatTextArea.setScrollTop(Double.MAX_VALUE);
    }

    public void setMain(Main main) {
        this.main = main;

        refreshBuddyList();
    }

    private void refreshBuddyList() {
        ObservableList<BuddyModel> buddyData = FXCollections.observableArrayList();

        Response response = ApplicationContext.get().getNeChatRestClient().getBuddies();

        if (response != null) {
            BuddiesResponse buddiesResponse = (BuddiesResponse) response;

            for (Buddy buddy : buddiesResponse.getBuddyList()) {
                buddyData.add(new BuddyModel(buddy.getLogin()));
            }

            buddyModelTableView.setItems(buddyData);
        }
    }

    private void setSelectedBuddy(BuddyModel buddy) {
        if (buddy != null) {
            Log.get().logMessage(LogLevel.DEBUG, "Buddy selected = " + buddy.getBuddyLogin());
            ApplicationContext.get().setSelectedBuddyLogin(buddy.getBuddyLogin().getValue());

            refreshChatTextArea();

            Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refreshChatTextArea();
                    chatTextAreaChanged();
                }
            });
            timer.start();
        }
    }

    private void refreshChatTextArea() {
        //HACK - concurrent modification lock. TODO: make rest client itself thread safe.
        if (chatTextAreaLockObj == null) {
            chatTextAreaLockObj = new Object();

            String chatText = "";

            MessagesResponse messagesResponse =
                    (MessagesResponse) ApplicationContext.get()
                            .getNeChatRestClient()
                            .getMessagesWithUser(
                                    ApplicationContext.get().getSelectedBuddyLogin()
                            );

            if (messagesResponse != null) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");

                for (Message message : messagesResponse.getMessageList()) {

                    chatText += "(" + dateFormat.format(message.getCreateDate()) + ") "
                            + message.getFromLogin() + ": "
                            + message.getMessage();
                    chatText += "\n";
                }
            }

            chatTextArea.setText(chatText);
            chatTextArea.appendText(""); //forces the change listener to scroll text area to bottom.
            chatTextAreaLockObj = null;
        } else {
            Log.get().logMessage(LogLevel.INFORMATION, "Attempted to refresh chat area but was blocked by another thread.");
        }

    }

    public void test() {
        Log.get().logMessage(LogLevel.DEBUG, "TEST");
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText, boolean isAuthFailure) {

        Log.get().logMessage(LogLevel.ERROR, "App error: " + headerText + " content: " + contentText);

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();

        if (isAuthFailure) {
            if (new PaneUtils().showLoginPaneAndSignIn()) {
                refreshBuddyList();
                chatTextAreaLockObj = null;
                refreshChatTextArea();
                chatTextAreaChanged();
            } else {
                //user hit cancel button.
                Log.get().logMessage(LogLevel.INFORMATION, "User decided to not sign in again after auth error. Closing.");

                Platform.exit();
                System.exit(0);
            }
        }
    }
}
