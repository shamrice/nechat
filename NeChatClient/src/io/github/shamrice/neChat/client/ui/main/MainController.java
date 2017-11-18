package io.github.shamrice.neChat.client.ui.main;

import io.github.shamrice.neChat.client.Main;
import io.github.shamrice.neChat.client.context.ApplicationContext;
import io.github.shamrice.neChat.client.ui.main.buddy.BuddyModel;
import io.github.shamrice.neChat.web.services.requests.StatusResponse;
import io.github.shamrice.neChat.web.services.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.web.services.requests.buddies.Buddy;
import io.github.shamrice.neChat.web.services.requests.messages.Message;
import io.github.shamrice.neChat.web.services.requests.messages.MessagesResponse;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
    private Object chatTextAreaLockObj = null;

    public MainController() {}

    @FXML
    private void initialize() {

        buddyModelListTableColumn.setCellValueFactory(cellData -> cellData.getValue().getBuddyLogin());

        buddyModelTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setSelectedBuddy(newValue)
        );
/*
        chatTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chatTextArea.setScrollTop(Double.MAX_VALUE);
            }
        });
        */
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
                JOptionPane.showMessageDialog(null, "Error sending message: " + response.getMessage(), "ERROR", 1);
            }
        }
        sendMessageTextField.setText("");
    }

    public void chatTextAreaChanged() {
        chatTextArea.setScrollTop(Double.MAX_VALUE);
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

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshChatTextArea();
                chatTextAreaChanged();
            }
        });
        timer.start();


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


            //System.out.println(chatText);
            chatTextArea.setText(chatText);
            chatTextArea.appendText(""); //forces the change listener to scroll text area to bottom.
            chatTextAreaLockObj = null;
        }

    }

    public void test() {
        System.out.println("TEST");
    }
}
