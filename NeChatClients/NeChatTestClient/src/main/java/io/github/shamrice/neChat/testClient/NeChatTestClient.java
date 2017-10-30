package io.github.shamrice.neChat.testClient;

import io.github.shamrice.neChat.testClient.frames.MainApplicationFrame;

import javax.swing.*;

/**
 * Created by Erik on 10/28/2017.
 */
public class NeChatTestClient {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        MainApplicationFrame mainApplicationFrame = new MainApplicationFrame();
    }
}
