package io.github.shamrice.neChat.testClient.ui.frames.panels;

import io.github.shamrice.neChat.testClient.state.ApplicationState;
import io.github.shamrice.neChat.testClient.web.services.NeChatRestClient;
import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;

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
    private JButton getBuddiesButton;
    private JTextField resultsTextField;

    public CommandPanel() {
        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(2, 3));
        commandPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Commands"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addWidgets();
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("CommandPanel-EventName: " + eventName);
        System.out.println("Login: " + ApplicationState.getLogin());
        System.out.println("Password: " + ApplicationState.getPassword());
        System.out.println("Auth_Token: " + ApplicationState.getAuthToken());

        String result = ApplicationState
                .getNeChatRestClient()
                .getBuddies(
                        ApplicationState.getLogin(),
                        ApplicationState.getPassword(),
                        ApplicationState.getAuthToken()
                );
        ApplicationState.setResult(result);
        resultsTextField.setText(result);
    }

    public JPanel getCommandPanel() {
        return commandPanel;
    }

    private void addWidgets() {
        getMessagesButton = new JButton("Get Messages");
        getMessagesButton.addActionListener(this);

        getBuddiesButton = new JButton("Get Buddies");
        getBuddiesButton.addActionListener(this);

        resultsTextField = new JTextField();
        resultsTextField.setSize(new Dimension(100, 100));

        commandPanel.add(getMessagesButton);
        commandPanel.add(getBuddiesButton);
        commandPanel.add(resultsTextField);
    }
}
