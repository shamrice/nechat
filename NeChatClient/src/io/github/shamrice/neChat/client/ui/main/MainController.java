package io.github.shamrice.neChat.client.ui.main;

import io.github.shamrice.neChat.client.Main;
import io.github.shamrice.neChat.client.context.ApplicationContext;
import io.github.shamrice.neChat.client.ui.main.buddy.BuddyModel;
import io.github.shamrice.neChat.web.services.requests.StatusResponse;
import io.github.shamrice.neChat.web.services.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.web.services.requests.buddies.Buddy;
import io.github.shamrice.neChat.web.services.requests.messages.Message;
import io.github.shamrice.neChat.web.services.requests.messages.MessagesResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

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

    private Main main;

    public MainController() {}

    @FXML
    private void initialize() {

        buddyModelListTableColumn.setCellValueFactory(cellData -> cellData.getValue().getBuddyLogin());

        buddyModelTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setSelectedBuddy(newValue)
        );
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
            } else {
                JOptionPane.showMessageDialog(null, "Error sending message: " + response.getMessage(), "ERROR", 1);
            }
        }
        sendMessageTextField.setText("");
    }

    public void setMain(Main main) {
        this.main = main;

        ObservableList<BuddyModel> buddyData = FXCollections.observableArrayList();

        BuddiesResponse buddiesResponse = (BuddiesResponse) ApplicationContext.get().getNeChatRestClient().getBuddies();

        for (Buddy buddy : buddiesResponse.getBuddyList()) {
            buddyData.add(new BuddyModel(buddy.getLogin()));
        }

        buddyModelTableView.setItems(buddyData);

    }

    private void setSelectedBuddy(BuddyModel buddy) {
        System.out.println("Buddy selected = " + buddy.getBuddyLogin());
        ApplicationContext.get().setSelectedBuddyLogin(buddy.getBuddyLogin().getValue());

        refreshChatTextArea();
    }

    private void refreshChatTextArea() {

        String chatText = "";

        MessagesResponse messagesResponse =
                (MessagesResponse) ApplicationContext.get()
                        .getNeChatRestClient()
                        .getChronologicalMessageHistoryWithUser(
                                ApplicationContext.get().getSelectedBuddyLogin()
                        );

        if (messagesResponse != null) {
            for (Message message : messagesResponse.getMessageList()) {
                //System.out.println("To Login: " + message.getLogin() + " From: " + message.getFromLogin() + " Message: " + message.getMessage());
                chatText += message.getFromLogin() + ": " + message.getMessage();
                chatText += "\n";
            }
        }

        //System.out.println(chatText);
        chatTextArea.setText(chatText);
    }

    public void test() {
        System.out.println("TEST");
    }
}
