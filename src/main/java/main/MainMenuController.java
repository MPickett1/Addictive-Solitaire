package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
       //TODO: load a new game
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
