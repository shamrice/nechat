package io.github.shamrice.neChat.testClient.windows;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * Created by Erik on 10/28/2017.
 */
public class mainWindow extends JFrame {

    private JFrame jFrame = new JFrame("NeChat Test App");

    public mainWindow() {

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setBounds(10, 10, 40, 25);

        JTextField loginTextField = new JTextField();
        loginTextField.setBounds(50, 10, 150, 25);

        JButton testButton = new JButton("Close");
        testButton.setBounds(50, 50, 150, 50);
        

        jFrame.add(loginLabel);
        jFrame.add(loginTextField);

        jFrame.setSize(400, 500);
        jFrame.setLayout(null);
        jFrame.setVisible(true);



    }

}
