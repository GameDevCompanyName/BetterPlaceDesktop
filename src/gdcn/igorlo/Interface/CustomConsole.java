package gdcn.igorlo.Interface;

import gdcn.igorlo.Constants.Booleans;
import gdcn.igorlo.Constants.Colors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
public class CustomConsole extends StackPane {

    private ScrollPane scrollPane;
    private VBox textBox;

    public CustomConsole(){

        textBox = new VBox();
        TextFlow allText = new TextFlow();
        scrollPane = new ScrollPane();

        Message.setParentNode(textBox);
        Message.loadFonts();
        Message.allText = allText;
        textBox.getChildren().add(allText);

        if (Booleans.DEBUG)
            this.setStyle("-fx-border-color: red");
        this.setAlignment(Pos.TOP_LEFT);

        scrollPane.setContent(textBox);
        if (Booleans.DEBUG)
            scrollPane.setStyle("-fx-border-color: green");

        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;\n" +
                "-fx-background: transparent;");
        scrollPane.prefHeightProperty().bind(this.heightProperty());
        scrollPane.prefWidthProperty().bind(this.widthProperty());
        scrollPane.setFitToWidth(true);

        textBox.maxWidthProperty().bind(scrollPane.widthProperty());
        textBox.prefWidthProperty().bind(scrollPane.widthProperty());


        this.getChildren().addAll(scrollPane);

    }

    private void slowScrollToBottom() {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(scrollPane.vvalueProperty(), 1.0)));
        animation.play();
    }

    public void userTextAppend(String name, String text, String color){

        Message message = new Message(name, color, text);
        message.prefWidthProperty().bind(scrollPane.widthProperty());
        textBox.getChildren().add(message);

        slowScrollToBottom();

    }

    public Node getBox() {
        return this;
    }

    public void cleanMessageHistory() {
        textBox.getChildren().clear();
    }

    public void systemMsg(String text) {
        userTextAppend("SYSTEM", text, Colors.SYSTEM_MSG_NAME);
    }

}
