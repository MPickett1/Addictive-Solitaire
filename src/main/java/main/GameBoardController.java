package main;

import com.couchbase.lite.Database;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by Mike P on 10/4/2016.
 */
public class GameBoardController {
    @FXML
    private BorderPane root;
    @FXML
    private GridPane gameBoard;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label elapsedTime, gameNum, score;

    private GameLogic gameLogic = GameLogic.getInstance();
    private static int card1 = -1;
    private long seed;
    private CouchBaseLite couchBaseLite = CouchBaseLite.getInstance();

    @FXML
    public void initialize(){
        gameBoard.getChildren().clear();
        gameLogic.getBoard().clear();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                value -> {
                    score.setText("Score: " + gameLogic.getScore());
                }),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(1);

        for(int i = 1; i <= 4; i++){
            for(int j = 1; j <= 13; j++){
                Card card = new Card(Card.Rank.values()[j], Card.Suit.values()[i]);
                gameLogic.addCardToBoard(card);
                gameLogic.replaceAces();

                Card btn = gameLogic.getBoard().get(gameLogic.getBoard().size()-1);
                btn.selectedProperty().addListener(event -> {
                    if(btn.getLock()) {
                        btn.selectedProperty().setValue(false);
                    }else if(btn.isSelected()){

                        if(card1 == -1){
                            card1 = gameBoard.getChildren().indexOf(btn);
                        }else{
                            gameLogic.moveCard((Card) gameBoard.getChildren().get(card1), btn);
                            card1 = -1;
                            setBoard();
                            gameLogic.checkMoves();
                            setBoard();
                            timeline.playFromStart();
                            for(Node node : gameBoard.getChildren()){
                                if(node.getClass().getSimpleName().equals("Card")){
                                    ((Card) node).selectedProperty().setValue(false);
                                }
                            }
                        }
                    }else {
                        card1 = -1;
                    }
                });
            }
        }
        gameLogic.setStartTime(elapsedTime);
    }

    public void setGameRules(int shuffles, long seed){
        this.seed = seed;
        gameNum.setText("Game #: " + seed);
        gameLogic.setRules(shuffles, seed);
        gameLogic.shuffle();
        setBoard();
    }

    private void setBoard(){
        gameBoard.getChildren().clear();
        int x = 0;
        for (int y = 0; y < 52; y++){
            if(y%13 == 0 && y != 0)
                x++;
            gameBoard.add(gameLogic.getBoard().get(y), y%13, x);
        }
    }

    @FXML
    public void saveGame(){
        Database db = couchBaseLite.getDatabase("saves");
        SaveState save = new SaveState(gameLogic.getBoard(), gameLogic.getShuffles(), seed, gameLogic.getScore());
        save.setName("Test");
        System.out.println(save.getBoard());
        System.out.println(save.getScore());
        System.out.println(save.getSeed());
        System.out.println(save.getShuffles());

        System.out.println(db);
        couchBaseLite.update(db, save);
    }
}
