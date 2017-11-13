package io.github.shamrice.neChat.testClient.ui.frames.panels;

import io.github.shamrice.neChat.testClient.state.ApplicationState;

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
    private JTextField resultsTextField;

    public CommandPanel() {
        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(2, 4));
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
                result = ApplicationState
                        .getNeChatRestClient()
                        .getMessages().getLogin();
                break;

            case "Send Message":
                result = ApplicationState
                        .getNeChatRestClient()
                        .sendMessage("test3", "THIS IS A TEST MESSAGE FROM CLIENT")
                        .getMessage();
                break;

            case "Get Buddies":
                result = ApplicationState
                        .getNeChatRestClient()
                        .getBuddies()
                        .toString();
                break;

            case "Add Buddy":
                result = ApplicationState
                        .getNeChatRestClient()
                        .addBuddy("test3")
                        .getMessage();
                break;

            case "Remove Buddy":
                result = ApplicationState
                        .getNeChatRestClient()
                        .removeBuddy("test3")
                        .getMessage();
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

        resultsTextField = new JTextField();
        resultsTextField.setSize(new Dimension(100, 100));

        commandPanel.add(getMessagesButton);
        commandPanel.add(sendMessageButton);
        commandPanel.add(getBuddiesButton);
        commandPanel.add(getMessagesButton);
        commandPanel.add(addBuddyButton);
        commandPanel.add(removeBuddyButton);
        commandPanel.add(resultsTextField);
    }
}
