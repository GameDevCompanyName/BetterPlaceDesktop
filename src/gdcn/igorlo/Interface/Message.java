package gdcn.igorlo.Interface;

import gdcn.igorlo.Connector;
import gdcn.igorlo.Constants.Booleans;
import gdcn.igorlo.Constants.Times;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class Message extends VBox {

    private static Pane parent;

    private static Font commonFont;
    private static Font boldFont;
    private static Font nameFont;

    private TextFlow msg = new TextFlow();

    private String senderName;
    private String senderColor;
    private MessageType type;

    private Color nameColor;

    Message(String senderName, String senderColor, String text){

        this.type = MessageType.COMMON_MESSAGE;
        this.senderColor = senderColor;
        colorize(senderColor);

        this.senderName = senderName;

        if (Booleans.DEBUG){
            this.setStyle("-fx-border-color: #FF765B");
        }

        this.build(text);

    }

    public static void setParentNode(Pane parentNode) {
        parent = parentNode;
    }

    public static void loadFonts(){

        try {
            commonFont = Font.loadFont(new FileInputStream(new File("Resources/Fonts/UbuntuMono-R.ttf")), 14);
            boldFont = Font.loadFont(new FileInputStream(new File("Resources/Fonts/UbuntuMono-B.ttf")), 14);
            nameFont = boldFont;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        commonFont = new Font("Courier New", 14);
//        nameFont = new Font("Courier New Bold", 14);

    }

    public static Font getCommonFont() {
        return commonFont;
    }

    private List<Text> buildText(String text){

        Font font = commonFont;

        List<Text> texts = new LinkedList<>();

        String[] parsedText = text.split(" ");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < parsedText.length; i++){
            String word = parsedText[i];
            if (word.contains("www.") || word.contains("https://") || word.contains("http://")){

                Hyperlink link = new Hyperlink(word, Connector.application);
                link.setFont(font);
                link.makeSmooth(Times.LINK_CHANGE_TIME);
                texts.add(link);

            } else
                    buffer.append(word);

            if (i != parsedText.length - 1)
                buffer.append(" ");
        }

        if (buffer.length() != 0){
            Text bufferedText = new Text();
            if (type == MessageType.COMMON_MESSAGE)
                bufferedText.setStyle("-fx-fill: White;");
            if (type == MessageType.SERVER_MESSAGE)
                bufferedText.setStyle("-fx-fill: White");
            bufferedText.setText(buffer.toString());
            bufferedText.setFont(font);
            texts.add(bufferedText);
        }

        return texts;

    }

    public void addText(String text){

        msg.getChildren().addAll(buildText(text));

    }

    public void build(String text){

        Text nickname = new Text(senderName + ": ");
        nickname.setFont(nameFont);
        nickname.setFill(nameColor);
        DropShadow dropShadow = new DropShadow();
        nickname.setEffect(dropShadow);

        LinkedList<Text> allText = new LinkedList<>();
        allText.addFirst(nickname);
        allText.addAll(buildText(text));
        msg.getChildren().addAll(allText);

        this.prefWidthProperty().bind(parent.widthProperty());
        this.setMaxWidth(Control.USE_PREF_SIZE);
        this.getChildren().add(msg);

    }


    private void colorize(String senderColor) {

        nameColor = Color.web(senderColor);

    }

    public String getSenderName() {
        return senderName;
    }

    public enum MessageType{
        SERVER_MESSAGE, COMMON_MESSAGE, CLIENT_MESSAGE
    }

}
