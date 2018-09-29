package gdcn.igorlo.Interface;

import gdcn.igorlo.Connector;
import gdcn.igorlo.Constants.Booleans;
import gdcn.igorlo.Constants.Colors;
import gdcn.igorlo.Utilities.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Random;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;

public class Chat {

    private Stage mainStage;
    private Scene mainScene;
    private String userName = "You";
    private Pane mainPane;
    private ChillTextPane inputField;
    private CustomConsole console;
    private String userColor = Colors.DEFAULT_MSG_NAME;
    private long startTimeSeconds;

    public Chat(Stage mainStage, Scene mainScene, Pane mainPane) {
        this.mainStage = mainStage;
        this.mainScene = mainScene;
        this.mainPane = mainPane;
        startTimeSeconds = System.currentTimeMillis()/1000;
        Connector.setChat(this);
    }

    public void flushTextFromField() {
        String text = inputField.getText();
        if (text.isEmpty())
            return;
        console.userTextAppend(userName, text, userColor);
        if (text.charAt(0) == '/'){
            String[] splited = text.split(" ");
            if (splited[0].matches("/[АПХапх]+") && splited[0].length() > 3){
                lose();
                inputField.clear();
                return;
            }
            switch (splited[0].toLowerCase()){
                case "/connect":
                    connection(splited);
                    break;
                case "/login":
                    login(splited);
                    break;
                case "/echo":
                    echo(text);
                    break;
                case "/suicide":
                    suicide(splited);
                    break;
                case "/name":
                    name(splited);
                    break;
                case "/color":
                    color(splited);
                    break;
                case "/disconnect":
                    disconnect(splited);
                    break;
                case "/date":
                    chatDate(splited);
                    break;
                case "/exit":
                    exit(splited);
                    break;
                case "/time":
                    time(splited);
                    break;
                case "/howlong":
                    howLong(splited);
                    break;
                case "/joji":
                    joji(splited);
                    break;
                case "/yeah":
                    systemMsg("right");
                    break;
                case "/random":
                    chatRandom(splited);
                    break;
                default:
                    unknownCommand();
            }
            inputField.clear();
            return;
        }
        if (Connector.loggedIn){
            Connector.sendMessage(text);
        }
        inputField.clear();

    }

    private void lose() {
        systemMsg("You laugh = you lose");
    }

    private void unknownCommand() {
        systemMsg("Неизвестная команда.");
    }

    private void chatRandom(String[] splited) {
        if (splited.length > 3){
            unknownParameters();
            return;
        }
        switch (splited.length){
            case 1:
                systemMsg(String.valueOf(randomTrueOrFalse()));
                break;
            case 2:
                if (!splited[1].matches("[0-9]+")){
                    error();
                    return;
                }
                int upperBound = Integer.parseInt(splited[1]);
                systemMsg(String.valueOf(randomIntFromTo(0, upperBound)));
                break;
            case 3:
                if (!splited[1].matches("[0-9]+") || !splited[2].matches("[0-9]+")){
                    error();
                    return;
                }
                int upBound = Integer.parseInt(splited[2]);
                int lowBound = Integer.parseInt(splited[1]);
                if (lowBound > upBound){
                    error();
                    return;
                }
                systemMsg(String.valueOf(randomIntFromTo(lowBound, upBound)));
                break;
            default:
                error();
                break;
        }
    }

    private void error() {
        systemMsg("Ошибка.");
    }

    private int randomIntFromTo(int lower, int upper) {
        Random random = new Random();
        return lower + random.nextInt(upper - lower + 1);
    }

    private boolean randomTrueOrFalse() {
        Random random = new Random();
        return random.nextBoolean();
    }

    private void howLong(String[] splited) {
        if (splited.length > 1){
            unknownParameters();
            return;
        }
        systemMsg("Вы провели здесь " + ((System.currentTimeMillis()/1000) - startTimeSeconds) + " сек.");
    }

    private void joji(String[] splited) {
        if (splited.length > 1){
            unknownParameters();
            return;
        }
        systemMsg(Utils.getRandomJojiLine());
    }

    private void time(String[] splited) {
        if (splited.length > 1){
            unknownParameters();
            return;
        }
        systemMsg(new Date().toString());
    }

    private void unknownParameters() {
        systemMsg("Неизвестные параметры.");
    }

    private void exit(String[] splited) {
        Connector.dropAllTheConnection();
        System.exit(0);
    }

    private void chatDate(String[] splited) {
        if (splited.length > 1){
            unknownParameters();
            return;
        }
        systemMsg(date());
    }

    private void disconnect(String[] splited) {
        systemMsg("Отключаюсь.");
        Connector.dropAllTheConnection();
    }

    private void color(String[] splited) {
        systemMsg("Your color: " + userColor);
    }

    private void name(String[] splited) {
        systemMsg("Your name: " + userName);
    }

    private void suicide(String[] splited) {
        systemMsg("R.I.P.:\n" +
                "       LIL PEEP\n" +
                "       XXXTENTACION\n" +
                "       2PAC\n" +
                "       ROMA LSP\n" +
                "       B.I.G.\n" +
                "       Filthy Frank, anime hunter");
    }

    private void echo(String text) {
        systemMsg(text.substring(6));
    }

    private void login(String[] splited) {
        if (!Connector.connected){
            systemMsg("Нельзя залогиниться, нет соединения с сервером.");
            return;
        }
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

    public void serverMessage(String message) {
        console.userTextAppend("Server", message, Colors.SERVER_MSG_NAME);
    }

}
