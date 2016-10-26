package main;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike P on 10/25/2016.
 */
public class GameSlotsController {
    @FXML
    private Label gameQuery;

    @FXML
    private HBox hbox1, hbox2, hbox3;
    @FXML
    private Button btnBack;

    private CouchBaseLite couchBaseLite = CouchBaseLite.getInstance();
    private Database db = couchBaseLite.getDatabase("saves");
    private List<String> docNames = new ArrayList<>();
    private ObservableList<HBox> hBoxes = FXCollections.observableArrayList();
    private SaveState saveState;
    public GameSlotsController(){}

    @FXML
    public void initialize(){
        hBoxes.addAll(hbox1, hbox2, hbox3);
        try {
            QueryEnumerator q = couchBaseLite.getDatabase("saves").createAllDocumentsQuery().run();
            for(QueryRow res : q){
                docNames.add(res.getDocument().getProperties().get("name").toString());
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        docNames.forEach(consumer -> {
            GameSlot gameSlot = new GameSlot(consumer);
            hBoxes.get(docNames.indexOf(consumer)).getChildren().clear();
            hBoxes.get(docNames.indexOf(consumer)).getChildren().add(gameSlot);
        });
    }

    public void loadGame() {
        gameQuery.setText("Load Game");
        hBoxes.forEach(consumer -> {
            ((Button) consumer.getChildren().get(0)).setOnAction(event -> {
                try {
                    if ((((Button) consumer.getChildren().get(0)).getGraphic() != null)) {
                        saveState = couchBaseLite.loadGame(((GameSlot) consumer.getChildren().get(0)).getNameBeforeChange(), db);
                        saveState.setName(((GameSlot) consumer.getChildren().get(0)).getTextFieldText());
                        makeGame(saveState);
                    }
                } catch (CouchbaseLiteException | NullPointerException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void saveGame(SaveState save){
        gameQuery.setText("Save Game");
        hBoxes.forEach(consumer -> {
            if((((Button) consumer.getChildren().get(0)).getGraphic() == null)){
                consumer.getChildren().set(0, new GameSlot("New Save"));
            }
            ((Button) consumer.getChildren().get(0)).setOnAction(event -> {
                try {
                    System.out.println("Save");
                    if(couchBaseLite.loadGame(save.getName(), db) == null){
                        System.out.println("Here");
                        save.setName(((GameSlot) consumer.getChildren().get(0)).getTextFieldText());
                        couchBaseLite.update(db, new SaveState(save));
                    }else {
                        save.setName(((GameSlot) consumer.getChildren().get(0)).getTextFieldText());
                        couchBaseLite.update(db, save);
                    }

                } catch (NullPointerException | CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                closeWindow();
                System.exit(0);
            });
        });
    }

    private void makeGame(SaveState saveState){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/gameBoard.fxml"));
            Parent root = loader.load();
            GameBoardController gameBoardController = loader.getController();
            gameBoardController.setGameRules(saveState);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeWindow(){
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }
}
