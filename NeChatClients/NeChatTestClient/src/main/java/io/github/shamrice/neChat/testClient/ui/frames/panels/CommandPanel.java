package io.github.shamrice.neChat.testClient.ui.frames.panels;

import io.github.shamrice.neChat.testClient.state.ApplicationState;
import io.github.shamrice.neChat.testClient.web.services.requests.StatusResponse;
import io.github.shamrice.neChat.testClient.web.services.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.testClient.web.services.requests.messages.MessagesResponse;

import javax.net.ssl.SSLEngineResult;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erik on 10/30/2017.
 */
public class CommandPanel implements ActionListener {

    private JPanel commandPanel;
    private JButton getMessagesButton;
    private JButton sendMessageButton;
    private JButton getBuddiesButton;
    private JButton addBuddyButton;
    private JButton removeBuddyButton;
    private JButton tempButton;
    private JTextField resultsTextField;
    private JTextField inputTextField;

    public CommandPanel() {
        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(4, 2));
        commandPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Commands"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addWidgets();
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("CommandPanel-EventName: " + eventName);

        String result = "";

        switch(eventName) {

            case "Get Messages":
                MessagesResponse response = (MessagesResponse)
                        ApplicationState
                        .getNeChatRestClient()
                        .getMessages();
                result = response.getLogin();
                break;

            case "Send Message":
                StatusResponse sendMessageStatus = (StatusResponse)
                        ApplicationState
                        .getNeChatRestClient()
                        .sendMessage(inputTextField.getText(), "THIS IS A TEST MESSAGE FROM CLIENT");
                result = sendMessageStatus.getMessage();
                break;

            case "Get Buddies":
                BuddiesResponse buddiesResponse = (BuddiesResponse)
                        ApplicationState
                        .getNeChatRestClient()
                        .getBuddies();
                result = buddiesResponse.getBuddy(1).getLogin();
                break;

            case "Add Buddy":
                StatusResponse addBuddyStatus = (StatusResponse)
                        ApplicationState
                        .getNeChatRestClient()
                        .addBuddy(inputTextField.getText());
                result = addBuddyStatus.getMessage();
                break;

            case "Remove Buddy":
                StatusResponse removeBuddyStatus = (StatusResponse)
                        ApplicationState
                        .getNeChatRestClient()
                        .removeBuddy(inputTextField.getText());
                result = removeBuddyStatus.getMessage();
                break;

            default:
                result = "event not found: " + eventName;
                break;
        }

        ApplicationState.setResult(result);
        resultsTextField.setText(result);

    }

    public JPanel getCommandPanel() {
        return commandPanel;
    }

    private void addWidgets() {
        getMessagesButton = new JButton("Get Messages");
        getMessagesButton.addActionListener(this);

        sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(this);

        getBuddiesButton = new JButton("Get Buddies");
        getBuddiesButton.addActionListener(this);

        addBuddyButton = new JButton("Add Buddy");
        addBuddyButton.addActionListener(this);

        removeBuddyButton = new JButton("Remove Buddy");
        removeBuddyButton.addActionListener(this);

        inputTextField = new JTextField();

        resultsTextField = new JTextField();
        resultsTextField.setSize(new Dimension(100, 100));

        tempButton = new JButton("");

        commandPanel.add(getMessagesButton);
        commandPanel.add(sendMessageButton);
        commandPanel.add(getBuddiesButton);
        commandPanel.add(addBuddyButton);
        commandPanel.add(removeBuddyButton);
        commandPanel.add(tempButton);
        commandPanel.add(inputTextField);
        commandPanel.add(resultsTextField);
    }
}
