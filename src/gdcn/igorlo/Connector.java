package gdcn.igorlo;

import gdcn.igorlo.Interface.Chat;
import gdcn.igorlo.Network.*;
import gdcn.igorlo.Constants.*;

import javafx.application.Application;
import javafx.application.Platform;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Connector {

    public static boolean connected = false;
    public static boolean loggedIn = false;
    public static String name = "null";
    public static String myColor = "null";

    public static Application application;
    public static Chat chat;

    public static Channel channel;
    public static ChannelFuture future;
    public static ChannelFactory factory;
    public static ClientBootstrap bootstrap;

    private static void send(String msg) {

        if (Booleans.DEBUG)
            System.out.println("ОТПРАВЛЯЮ --- " + msg);
        if (connected && channel.isOpen()){
            try {
                channel.write(msg).await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setChat(Chat chat) {
        Connector.chat = chat;
    }

    public static void resetConnection(){
        dropAllTheConnection();
        createConnectionIfNONE();
    }

    public static void createConnectionIfNONE(){

        factory = new NioClientSocketChannelFactory(
                Executors.newFixedThreadPool(1),
                Executors.newFixedThreadPool(4)
        );
        bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(() -> Channels.pipeline(new Handler()));

        if (channel == null && connected == false){
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(INFO.DEFAULT_IP, INFO.DEFAULT_PORT));
            try {
                channel = future.await().getChannel();
                connected = true;
            } catch (InterruptedException e) {
                if (Booleans.DEBUG)
                    System.out.println("Не удалось создать подключение");
                chat.systemMsg("Не удалось создать подключение");
                e.printStackTrace();
            }
        }

    }

    public static void sendLogInAttempt(String login, String pass){
        name = login;
        send(ClientMessage.loginAttemptSend(login, pass));
    }

    public static void sendMessage(String text){
        send(ClientMessage.messageSend(text));
    }

    public static void sendDisconnect(String reason){
        send(ClientMessage.disconnectSend(reason));
    }

    public static void dropAllTheConnection(){

        sendDisconnect("Завершил сессию.");
        connected = false;

        if (bootstrap != null)
            Platform.runLater(() -> bootstrap.releaseExternalResources());
        if (factory != null)
            Platform.runLater(() -> bootstrap.releaseExternalResources());
        if (channel != null && channel.isOpen())
            channel.close();
        if (future != null)
            future.cancel();


        future = null;
        channel = null;
        bootstrap = null;
        factory = null;

    }

    public static void passWrongError() {
        chat.systemMsg("Неверный пароль");
    }

    public static void userAlreadyOnline() {
        chat.systemMsg("Такой пользователь уже онлайн");
    }

    public static void loginSuccess() {
        loggedIn = true;
        chat.systemMsg("Успешно залогинен");
    }

    public static void userColorRecieved(String login, String color) {
        chat.colorRecieved(login, color);
    }

    public static void displayMessage(String login, String message, String color) {
        chat.userMessage(login, message, color);
    }

    public static void displayServerMessage(String message) {
        chat.serverMessage(message);
    }

    public static void userConnectedRecieved(String login) {
        chat.systemMsg("Пользователь " + login + " подключился");
    }

    public static void userDisconnectedRecieved(String login) {
        chat.systemMsg("Пользователь " + login + " вышел");
    }

    public static void sendPong() {
        send(ClientMessage.userPong());
    }

    public static void connectionLost() {
        connected = false;
        chat.systemMsg("Соединение разорвано");
        dropAllTheConnection();
    }

    public static void exit() {
        dropAllTheConnection();
        factory.releaseExternalResources();
    }

    public static void exeptionCaught(ExceptionEvent e) {
        chat.systemMsg("Произошла ошибка: " + e.getCause().getMessage());
    }

    public static void connectionSuccess() {
        chat.systemMsg("Подключение установлено");
        connected = true;
    }

    public static void sendServerCommand(String text) {
        send(ClientMessage.commandSend(text));
    }

    public static void createConnectionIfNONE(String ip, Integer port) {
        factory = new NioClientSocketChannelFactory(
                Executors.newFixedThreadPool(1),
                Executors.newFixedThreadPool(4)
        );
        bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(() -> Channels.pipeline(new Handler()));

        if (channel == null && !connected){
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
            try {
                channel = future.await().getChannel();
                connected = true;
            } catch (InterruptedException e) {
                if (Booleans.DEBUG)
                    System.out.println("Не удалось создать подключение");
                chat.systemMsg("Не удалось создать подключение");
                e.printStackTrace();
            }
        }
    }
}
