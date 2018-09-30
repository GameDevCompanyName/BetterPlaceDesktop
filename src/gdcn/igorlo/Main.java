package gdcn.igorlo;

import gdcn.igorlo.Constants.*;
import gdcn.igorlo.Interface.Chat;
import gdcn.igorlo.Interface.Message;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Connector.application = this;
        StackPane mainPane = new StackPane();
        Scene mainScene = new Scene(mainPane, Color.web("#000000"));
        primaryStage.setScene(mainScene);
        primaryStage.setTitle(Strings.WINDOW_TITLE);
        Chat chat = new Chat(primaryStage, mainScene, mainPane);
        primaryStage.show();
        chat.start();
    }

}
