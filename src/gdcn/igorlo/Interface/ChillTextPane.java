package gdcn.igorlo.Interface;

import gdcn.igorlo.Constants.Booleans;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class ChillTextPane extends StackPane {

    MyTextArea textArea;

    public ChillTextPane(Pane pane, Chat chat){


        Font font = new Font("Courier New", 14);
        this.setAlignment(Pos.BOTTOM_LEFT);
        this.prefWidthProperty().bind(pane.widthProperty());
        this.maxWidthProperty().bind(pane.widthProperty());

        textArea = new MyTextArea(pane, chat);
        if (Booleans.DEBUG){
            textArea.setStyle("-fx-border-color: red");
            this.setStyle("-fx-border-color: GREEN");
        }
        textArea.setFont(font);

        this.getChildren().addAll(textArea);
        //this.getChildren().addAll(textArea);

    }

    public String getText() {
        return textArea.getText();
    }

    public void clear() {
        textArea.setText("");
    }

}