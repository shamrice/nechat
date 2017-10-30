package io.github.shamrice.neChat.testClient.frames;

import io.github.shamrice.neChat.testClient.frames.panels.CommandPanel;
import io.github.shamrice.neChat.testClient.frames.panels.LoginPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erik on 10/29/2017.
 */
public class MainApplicationFrame implements ActionListener {

    private JFrame mainApplicationFrame;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private CommandPanel commandPanel;


    public MainApplicationFrame() {
        mainApplicationFrame = new JFrame("NeChat Test Client");
        mainApplicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainApplicationFrame.setSize(new Dimension(200, 400));

        loginPanel = new LoginPanel();
        commandPanel = new CommandPanel();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(loginPanel.getLoginPanel());
        mainPanel.add(commandPanel.getCommandPanel());

        mainApplicationFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        mainApplicationFrame.pack();
        mainApplicationFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("EventName: " + eventName);

    }

    private void addWidgets() {

    }


}
