package io.github.shamrice.neChat.testClient;

import io.github.shamrice.neChat.testClient.state.ApplicationState;
import io.github.shamrice.neChat.testClient.ui.frames.MainApplicationFrame;
import io.github.shamrice.neChat.testClient.web.services.NeChatRestClient;
import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.configuration.ConfigurationBuilder;

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

        NeChatRestClient neChatRestClient = new NeChatRestClient(ConfigurationBuilder.build());
        ApplicationState.setNeChatRestClient(neChatRestClient);

        JFrame.setDefaultLookAndFeelDecorated(true);
        MainApplicationFrame mainApplicationFrame = new MainApplicationFrame();
    }
}
