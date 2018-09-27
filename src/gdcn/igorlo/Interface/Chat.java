package gdcn.igorlo.Interface;

import gdcn.igorlo.Connector;
import gdcn.igorlo.Constants.Booleans;
import gdcn.igorlo.Constants.Colors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Chat {

    private Stage mainStage;
    private Scene mainScene;
    private String userName = "You";
    private Pane mainPane;
    private ChillTextPane inputField;
    private CustomConsole console;
    private String userColor = Colors.DEFAULT_MSG_NAME;

    public Chat(Stage mainStage, Scene mainScene, Pane mainPane) {
        this.mainStage = mainStage;
        this.mainScene = mainScene;
        this.mainPane = mainPane;
        Connector.setChat(this);
    }

    public void flushTextFromField() {
        String text = inputField.getText();
        console.userTextAppend(userName, text, userColor);
        if (text.charAt(0) == '/'){
            String[] splited = text.split(" ");
            switch (splited[0].toLowerCase()){
                case "/connect":
                    connection(splited);
                    break;
                case "/login":
                    login(splited);
                    break;
                default:
                    systemMsg("Неизвестная команда");
            }
            inputField.clear();
            return;
        }
        if (Connector.loggedIn){
            Connector.sendMessage(text);
        }
        inputField.clear();

    }

    private void login(String[] splited) {
        if (splited.length != 3){
            systemMsg("Формат комманды: /login [name] [pass]");
            return;
        }
        Connector.sendLogInAttempt(splited[1], splited[2]);
        userName = splited[1];
    }

    private void connection(String[] splited) {
        if (splited.length != 1){
            systemMsg("Неизвестные параметры");
            return;
        }
        Connector.createConnectionIfNONE();
    }

    public void systemMsg(String text) {
        console.systemMsg(text);
    }

    public void start(){

        if (Booleans.DEBUG){
            mainPane.setStyle("-fx-border-color: red");
        }

        CustomConsole console = new CustomConsole();
        this.console = console;
        ChillTextPane textPane = new ChillTextPane(mainPane, this);
        this.inputField = textPane;

        VBox GUI = new VBox();
        GUI.setBackground(new Background(new BackgroundFill(Color.web(Colors.DEFAULT_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        GUI.maxHeightProperty().bind(mainPane.heightProperty());
        GUI.maxWidthProperty().bind(mainPane.widthProperty());
        GUI.setAlignment(Pos.TOP_LEFT);
        GUI.getChildren().addAll(console, textPane);

        console.prefHeightProperty().bind(mainPane.heightProperty().subtract(textPane.getHeight()));
        console.maxWidthProperty().bind(mainPane.widthProperty());

        mainPane.getChildren().add(GUI);

    }

    public void userMessage(String login, String message, String color) {
        console.userTextAppend(login, message, color);
    }

    public void colorRecieved(String login, String color) {
        if (login.equals(userName))
            userColor = color;
    }

}
