package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Mike P on 10/3/2016.
 */
public class MainMenuController {
    @FXML
    private StackPane root;
    @FXML
    private VBox bgContainer, buttonContainer;
    @FXML
    private ImageView background;
    @FXML
    private Button newGame, loadGame, settings;


    @FXML
    private void initialize(){
        //TODO:INIT the stage.
    }

    @FXML
    private void newGame(){
        try {
            Parent root = new FXMLLoader(getClass().getClassLoader().getResource("fxml/gameBoard.fxml")).load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest(event ->{
                Stage stage1 = new Stage();
                try {
                    stage1.setScene(new Scene(new FXMLLoader(getClass().getClassLoader().getResource("fxml/mainMenu.fxml")).load()));
                    stage1.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadGame(){
        //TODO: load a previous game
    }

    @FXML
    private void loadSettings(){
        //TODO: load the settings scene
    }

}
