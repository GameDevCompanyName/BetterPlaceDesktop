package gdcn.igorlo;

import gdcn.igorlo.Constants.*;
import gdcn.igorlo.Interface.Chat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane mainPane = new Pane();
        Color mainColor = Color.web(Colors.DEFAULT_BACKGROUND);
        Scene mainScene = new Scene(mainPane, mainColor);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle(Strings.WINDOW_TITLE);
        Chat chat = new Chat(primaryStage, mainScene, mainPane);
        primaryStage.show();
        chat.start();

    }

}
