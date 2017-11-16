package io.github.shamrice.neChat.client;

import io.github.shamrice.neChat.client.context.ApplicationContext;
import io.github.shamrice.neChat.client.ui.main.MainController;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.authorization.AuthorizationResponse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{

        if (!setUpRestClient()) {
            System.exit(-1);
        }

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("NeChat");

        initRootLayout();
        showMainPane();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("ui/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
    }

    private void showMainPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("ui/main/Main.fxml"));
            AnchorPane mainPane = (AnchorPane) loader.load();

            rootLayout.setCenter(mainPane);

            MainController mainController = loader.getController();
            mainController.setMain(this);

        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean setUpRestClient() {
        ApplicationContext.get().getNeChatRestClient().setUserCredentials(
                new UserCredentials("test", "password")
        );

        AuthorizationResponse response = (AuthorizationResponse) ApplicationContext.get().getNeChatRestClient().getAuthToken();
        if (response.getAuthToken().length() > 0) {
            return true;
        } else {
            System.out.println("Unable to log in with credentials.");
            return false;
        }
    }
}
