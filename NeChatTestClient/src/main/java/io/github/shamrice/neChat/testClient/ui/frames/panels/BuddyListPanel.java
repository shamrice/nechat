package io.github.shamrice.neChat.testClient.ui.frames.panels;

import io.github.shamrice.neChat.testClient.state.ApplicationState;
import io.github.shamrice.neChat.testClient.web.services.requests.buddies.Buddy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Erik on 11/13/2017.
 */
public class BuddyListPanel implements ActionListener {

    private JPanel buddyListPanel;
    private JButton refreshButton;
    private JList<String> buddyJList;

    public BuddyListPanel() {
        buddyListPanel =  new JPanel();
        buddyListPanel.setLayout(new GridLayout(2, 1));
        buddyListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Buddy List"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        addWidgets();
    }

    public JPanel getBuddyListPanel() {
        return buddyListPanel;
    }

    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        System.out.println("CommandPanel-EventName: " + eventName);

        String result = "";

        switch (eventName) {
            case "Refresh":
               setUpBuddyList();
                break;
        }

    }

    private void addWidgets() {
        buddyJList = new JList<>();
        buddyJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        buddyJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    java.util.List<String> selectedValueList = buddyJList.getSelectedValuesList();
                    System.out.println("Selected buddy=" + selectedValueList);
                }
            }
        });

        setUpBuddyList();

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);

        buddyListPanel.add(refreshButton);
        buddyListPanel.add(buddyJList);
    }

    private void setUpBuddyList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Buddy buddy : ApplicationState.getNeChatRestClient().getResponseCache().getBuddyList()) {
            listModel.addElement(buddy.getLogin());
        }

        buddyJList.setModel(listModel);


    }
}
