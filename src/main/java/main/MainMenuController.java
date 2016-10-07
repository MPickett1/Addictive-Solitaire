package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.function.UnaryOperator;

/**
 * Created by Mike P on 10/3/2016.
 */
public class MainMenuController {

    @FXML
    private void newGame(){
        makeGame(4, new Random().nextLong());
    }

    private void makeGame(int shuffles, long seed){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/gameBoard.fxml"));
            Parent root = loader.load();
            GameBoardController gameBoardController = loader.getController();
            gameBoardController.setGameRules(shuffles, seed);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void customGame(){
        int shuffles;
        long seed;
        UnaryOperator<TextFormatter.Change> filterNumber = (TextFormatter.Change t) -> {
            if (t.isReplaced()) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
                }
            }
            if (t.isAdded()) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            }
            return t;
        };

        Dialog dialog = new Dialog();
        dialog.setTitle("Custome Game");
        dialog.setHeaderText("Enter the number of shuffles.\n Enter the game number you would like to play.");
        VBox vBox = new VBox();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        Label lblShuffle = new Label("Shuffles: ");
        TextField tfShuffle = new TextField();
        tfShuffle.promptTextProperty().setValue("Enter the number of shuffles");
        tfShuffle.setTextFormatter(new TextFormatter<>(filterNumber));
        TextField tfSeed = new TextField();
        Label lblSeed = new Label("Game #: ");
        tfSeed.promptTextProperty().setValue("Enter the game seed");
        tfSeed.setTextFormatter(new TextFormatter<>(filterNumber));

        ButtonType play = new ButtonType("PLAY!", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        tfShuffle.setOnAction(value -> {
            tfSeed.requestFocus();
        });

        hBox1.getChildren().addAll(lblShuffle, tfShuffle);
        hBox2.getChildren().setAll(lblSeed, tfSeed);
        vBox.getChildren().addAll(hBox1, hBox2);
        dialog.getDialogPane().setContent(vBox);
        dialog.getDialogPane().getButtonTypes().addAll(play, cancel);

        Optional result = dialog.showAndWait();
        if(result.get() == play) {
            try {
                shuffles = Integer.parseInt(tfShuffle.getText());
                seed = Long.parseLong(tfSeed.getText());
                makeGame(shuffles, seed);
            }catch(Exception e){
                customGame();
            }
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
