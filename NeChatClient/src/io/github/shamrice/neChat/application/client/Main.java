package io.github.shamrice.neChat.application.client;

import io.github.shamrice.neChat.application.client.ui.login.LoginController;
import io.github.shamrice.neChat.application.client.ui.main.MainController;
import io.github.shamrice.neChat.application.client.ui.util.PaneUtils;
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

        //TODO : login pane should only show up if saved u/p fails to authenticate.
        //TODO : login pane should also have a "create account" option.

        if (new PaneUtils().showLoginPaneAndSignIn()) {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("NeChat");
            initRootLayout();
            showMainPane();
        }
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

}
