package gdcn.igorlo.Interface;

import javafx.geometry.Bounds;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class MyTextArea extends TextField {

    Pane mainPane;

    public MyTextArea(Pane mainPane, Chat chat) {

        this.mainPane = mainPane;

        this.setMaxHeight(100);
        this.maxWidthProperty().bind(mainPane.widthProperty().subtract(3));

        KeyCombination keyComb = new KeyCodeCombination(KeyCode.ENTER,
                KeyCombination.CONTROL_DOWN);

        setEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (keyComb.match(event)) {
                appendText("\n");
                event.consume();
            } else {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    chat.flushTextFromField();
                    event.consume();
                }
            }
        });

    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        ScrollBar scrollBarv = (ScrollBar) this.lookup(".scroll-bar:vertical");
        if (scrollBarv != null) {
            ((ScrollPane) scrollBarv.getParent()).setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
        ScrollBar scrollBarh = (ScrollBar) this.lookup(".scroll-bar:horizontal");
        if (scrollBarh != null) {
            ((ScrollPane) scrollBarh.getParent()).setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
    }



}