package gdcn.igorlo.Network;

import gdcn.igorlo.Connector;
import javafx.application.Platform;

public class ClientMethods {

    public static void clientVersionRequestReceived(){
        //TODO
    }

    public static void loginWrongErrorReceived(){
        Connector.passWrongError();
    }

    public static void loginAlreadyErrorReceived(){
        Connector.userAlreadyOnline();
    }

    public static void loginSuccessReceived(){
        Connector.loginSuccess();
    }


    public static void userRegistrationSuccessReceived(){
        //TODO
    }

    public static void userColorReceived(String login, String color){
        Connector.userColorRecieved(login, color);
    }

    public static void userMessageReceived(String login, String message, String color){
        Connector.displayMessage(login, message, color);
    }

    public static void userActionReceived(String login, String action){
        //TODO
    }

    public static void serverMessageReceived(String message){
        Connector.displayServerMessage(message);
    }

    public static void serverEventReceived(String event){
        //TODO
    }


    public static void userDisconnectReceived(String reason){

    }

    public static void serverUserLoginReceived(String login){
        Connector.userConnectedRecieved(login);
    }

    public static void serverUserDisconnectReceived(String login){
        Connector.userDisconnectedRecieved(login);
    }

    public static void serverPingRequest() {
        Connector.sendPong();
    }

}