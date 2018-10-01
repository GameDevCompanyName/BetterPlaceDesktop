package gdcn.igorlo.Interface;

import gdcn.igorlo.Connector;
import gdcn.igorlo.Constants.Booleans;
import gdcn.igorlo.Constants.Colors;
import gdcn.igorlo.Constants.INFO;
import gdcn.igorlo.Constants.Times;
import gdcn.igorlo.Utilities.Utils;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static jdk.nashorn.internal.runtime.Version.version;

public class Chat {

    private Stage mainStage;
    private Scene mainScene;
    private String userName = "You";
    private Pane mainPane;
    private ChillTextPane inputField;
    private CustomConsole console;
    private String userColor = Colors.DEFAULT_MSG_NAME;
    private VBox GUI;
    private long startTimeSeconds;

    private List<String> cmdHistory = new ArrayList<>();
    private int historyPosition = 0;

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
                clearAndRemember(text);
                return;
            }
            switch (splited[0].toLowerCase()){
                case "/help":
                    help();
                    break;
                case "/server":
                    sendServerCommand(text);
                    break;
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
                case "/changecolor":
                    changeColor(splited);
                    break;
                case "/version":
                    chatVersion();
                    break;
                case "/about":
                    about();
                    break;
                case "/testdrive":
                    testDrive(splited);
                    break;
                case "/clear":
                    clear();
                    break;
                case "/fightofthecentury":
                    fightOfTheCentury();
                    break;
                default:
                    unknownCommand();
            }
            clearAndRemember(text);
            return;
        }
        if (Connector.loggedIn){
            Connector.sendMessage(text);
        }
        clearAndRemember(text);

    }

    private void clearAndRemember(String text) {
        cmdHistory.add(text);
        inputField.clear();
        historyPosition = cmdHistory.size();
    }

    private void fightOfTheCentury() {
        systemMsg("Они встретились на пустыре в девяткино...");
        systemMsg("\tБыла полночь...");
        systemMsg("\t\tОни НАБРОСИЛИСЬ ДРУГ НА ДРУГА");
        systemMsg("Кто же победит??");
        systemMsg(".");
        systemMsg(".");
        systemMsg(".");
        systemMsg("\tпобедил...");
        if (randomTrueOrFalse()){
            systemMsg("ПОБЕДИЛ ГААГ!");
        } else {
            systemMsg("ПОБЕДИЛ ВРРВ!");
        }
    }

    private void clear() {
        Message.allText.getChildren().clear();
    }

    private void testDrive(String[] splited) {
        if (splited.length != 2) {
            unknownParameters();
            return;
        }
        if (!splited[1].matches("[0-9]+")){
            error();
            systemMsg("Неверный формат параметра");
            return;
        }
        int quantity = Integer.parseInt(splited[1]);
        Random random = new Random();
        for (int i = 0; i < quantity; i++){
            systemMsg(String.valueOf(random.nextLong()));
        }
    }

    private void about() {
        systemMsg(INFO.ABOUT);
    }

    private void chatVersion() {
        systemMsg("Текущая версия: " + INFO.VERSION);
    }

    private void help() {
        systemMsg("Полный список команд:" +
                "\n\n\t" + "/connect (IP) (PORT)" +
                "\n\t" + "/help" +
                "\n\t" + "/login [LOGIN] [PASS]" +
                "\n\t" + "/echo [TEXT]" +
                "\n\t" + "/name" +
                "\n\t" + "/color" +
                "\n\t" + "/time" +
                "\n\t" + "/date" +
                "\n\t" + "/joji" +
                "\n\t" + "/suicide" +
                "\n\t" + "/random (FROM) (TO)" +
                "\n\t" + "/disconnect" +
                "\n\t" + "/exit" +
                "\n\t" + "/howlong" +
                "\n\t" + "/yeah" +
                "\n\t" + "/changecolor [ITEM] [#HEX]" +
                "\n\t" + "/server [command to server]" +
                "\n\t" + "/version" +
                "\n\t" + "/about" +
                "\n\t" + "/testdrive [N]" +
                "\n\t" + "/clear" +
                "\n\t" + "/fightofthecentury" +
                "\n\n\t" + "() - опциональные параметры" +
                "\n\t" + "[] - обязательные параметры"
        );
    }

    private void setBackground(String colorHex){
        FadeTransition fadeTransition = new FadeTransition(
                Duration.seconds(Times.COLOR_CHANGE),
                mainPane
        );
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> {
            mainPane.setStyle("-fx-background-color: " + colorHex + ";");
            FadeTransition fadeInTransition = new FadeTransition(
                    Duration.seconds(Times.COLOR_CHANGE),
                    mainPane
            );
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);
            fadeInTransition.play();
        });
        fadeTransition.play();
    }

    private void sendServerCommand(String text) {
        Connector.sendServerCommand(text);
    }

    private void changeColor(String[] splited) {
        if (splited.length == 1){
            systemMsg("Вы можете изменить цвет для:\n\t" +
                    "background\n\t" +
                    "msgtext\n\t" +
                    "inputtext\n\t" +
                    "systemname");
        }
        if (splited.length != 3){
            unknownParameters();
            return;
        }
        if (!splited[2].matches("#[0-9aAbBcCdDeEfF]{6}")){
            error();
            systemMsg("Второй аргумент команды должен иметь вид HEX\n\t#FFFFFF (например)");
            return;
        }
        switch (splited[1]){
            case "background":
                setBackground(splited[2]);
                break;
            case "msgtext":
                setMsgTextColor(splited[2]);
                break;
            case "systemname":
                setSystemNameColor(splited[2]);
                break;
            case "inputtext":
                setInpuTextColor(splited[2]);
                break;
            default:
                systemMsg("Вы можете изменить цвет только для:\n\t" +
                        "background\n\t" +
                        "msgtext\n\t" +
                        "inputtext\n\t" +
                        "systemname");
        }
    }

    private void setInpuTextColor(String colorHex) {
        inputField.getTextArea().setStyle("-fx-background-color: rgba(60,60,70,0.25);" +
                "-fx-text-fill: " + colorHex + ";");
    }

    private void setSystemNameColor(String colorHex) {
        Colors.SYSTEM_MSG_NAME = colorHex;
    }

    private void setMsgTextColor(String colorHex) {
        Colors.DEFAULT_MSG_TEXT = colorHex;
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
        if (splited.length != 1 && splited.length != 3){
            systemMsg("Неизвестные параметры");
            systemMsg("Используйте /connect для стандартного подключения");
            systemMsg("Используйте /connect [IP] [PORT] для явного указания данных сервера");
            return;
        }
        if (splited.length == 1){
            Connector.createConnectionIfNONE();
            return;
        }
        if (splited.length == 3){
            if (!splited[1].matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+")){
                systemMsg("Некорректный IP");
                return;
            }
            if (!splited[2].matches("[0-9]+")){
                systemMsg("Некорректный порт");
                return;
            }
            Integer port = Integer.parseInt(splited[2]);
            Connector.createConnectionIfNONE(splited[1], port);
        }
    }

    public void systemMsg(String text) {
        console.systemMsg(text);
    }

    public void start(){

        if (Booleans.DEBUG){
            mainPane.setStyle("-fx-border-color: red");
        }

        setBackground(Colors.DEFAULT_BACKGROUND);
        CustomConsole console = new CustomConsole();
        this.console = console;
        ChillTextPane textPane = new ChillTextPane(mainPane, this);
        this.inputField = textPane;


        GUI = new VBox();
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
        console.userTextAppend("Server", message, Colors.SYSTEM_MSG_NAME);
    }

    public void backToStory() {
        if (historyPosition <= 0)
            return;
        String cmd = cmdHistory.get(--historyPosition);
        inputField.getTextArea().setText(cmd);
        inputField.getTextArea().positionCaret(cmd.length());
    }

    public void downToStory() {
        if (historyPosition >= cmdHistory.size()-1)
            return;
        String cmd = cmdHistory.get(++historyPosition);
        inputField.getTextArea().setText(cmd);
        inputField.getTextArea().positionCaret(cmd.length());
    }
}
