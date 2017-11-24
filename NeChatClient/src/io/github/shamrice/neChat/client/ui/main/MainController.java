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
            System.out.println("New buddy to add: " + result.get());
            String buddyToAdd = result.get().replace(" ", "");

            if (!buddyToAdd.toLowerCase().equals(ApplicationContext.get().getCurrentLogin().toLowerCase())) {

                StatusResponse response = (StatusResponse) ApplicationContext.get()
                        .getNeChatRestClient()
                        .addBuddy(buddyToAdd);

                if (response.isSuccess()) {
                    refreshBuddyList();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Buddy Added");
                    alert.setHeaderText(buddyToAdd + " has been added to your buddy list.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Buddy Add Failure");
                    alert.setHeaderText("Buddy " + buddyToAdd + " was not able to be added.");
                    alert.setContentText(response.getMessage());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Buddy Add Failure");
                alert.setHeaderText("You cannot add yourself to your buddy list.");
                alert.show();
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Buddy Removal Error");
                alert.setHeaderText("Buddy " + buddyToRemove + " was not able to be removed.");
                alert.setContentText(response.getMessage());
                alert.show();
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Send Message Error");
                alert.setHeaderText("Unable to send message to " + toLogin);
                alert.setContentText(response.getMessage());
                alert.show();
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

        BuddiesResponse buddiesResponse = (BuddiesResponse) ApplicationContext.get().getNeChatRestClient().getBuddies();

        for (Buddy buddy : buddiesResponse.getBuddyList()) {
            buddyData.add(new BuddyModel(buddy.getLogin()));
        }

        buddyModelTableView.setItems(buddyData);
    }

    private void setSelectedBuddy(BuddyModel buddy) {
        if (buddy != null) {
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
