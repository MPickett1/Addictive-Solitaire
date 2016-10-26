package main;

import com.couchbase.lite.CouchbaseLiteException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * Created by Mike P on 10/25/2016.
 */
class GameSlot extends Button {
    private TextField tf = new TextField();
    private CouchBaseLite couchBaseLite = CouchBaseLite.getInstance();
    private String name;
    private SaveState saveState = new SaveState();

    public GameSlot(String text) {
        name = text;
        tf.setText(name);
        setText("");
        setGraphic(tf);

        UnaryOperator<TextFormatter.Change> filterSize = (TextFormatter.Change t) -> {
            if(t.getControlNewText().length() > 10){
                t.setText("");
            }
            return t;
        };
        tf.setTextFormatter(new TextFormatter<>(filterSize));
        tf.setMaxWidth(75);
        tf.setAlignment(Pos.CENTER);
        tf.setOnAction(ae -> {
            try {
                saveState = couchBaseLite.loadGame(name, couchBaseLite.getDatabase("saves"));
                if(saveState != null){
                    name = saveState.getName();
                    couchBaseLite.update(couchBaseLite.getDatabase("saves"), saveState);
                }
            } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        });

        setPrefSize(250, 75);
    }

    public String getNameBeforeChange(){
        return name;
    }

    public String getTextFieldText(){
        return tf.getText();
    }
}