package io.github.shamrice.neChat.testClient.frames.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erik on 10/29/2017.
 */
public class LoginPanel implements ActionListener {
    private JPanel loginPanel;
    private JLabel loginLabel;
    private JTextField loginTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JLabel tokenLabel;
    private JTextField tokenTextField;
    private JButton getTokenButton;

    public LoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Authentication"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addWidgets();
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public String getAuthToken() {
        return tokenTextField.getText();
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("EventName: " + eventName);
        tokenTextField.setText("TEST-TOKEN");

    }

    private void addWidgets() {
        loginLabel = new JLabel("Login:", SwingConstants.LEFT);
        loginTextField = new JTextField(10);

        passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
        passwordTextField = new JPasswordField(10);

        tokenLabel = new JLabel("Auth-Token:", SwingConstants.LEFT);
        tokenTextField = new JTextField(10);
        tokenTextField.setEnabled(false);

        getTokenButton = new JButton("Get Auth Token");
        getTokenButton.addActionListener(this);

        loginPanel.add(loginLabel);
        loginPanel.add(loginTextField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordTextField);
        loginPanel.add(tokenLabel);
        loginPanel.add(tokenTextField);
        loginPanel.add(getTokenButton);
    }


}
