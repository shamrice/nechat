package io.github.shamrice.neChat.testClient.frames.panels;

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

    public CommandPanel() {
        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(1, 3));
        commandPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Commands"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addWidgets();
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("CommandPanel-EventName: " + eventName);
    }

    public JPanel getCommandPanel() {
        return commandPanel;
    }

    private void addWidgets() {
        getMessagesButton = new JButton("Get Messages");
        getMessagesButton.addActionListener(this);

        getBuddiesButton = new JButton("Get Buddies");
        getBuddiesButton.addActionListener(this);

        commandPanel.add(getMessagesButton);
        commandPanel.add(getBuddiesButton);
    }
}
