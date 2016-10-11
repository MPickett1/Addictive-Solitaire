package main;

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
    private Label elapsedTime, gameNum, score, highScore;

    private GameLogic gameLogic = GameLogic.getInstance();
    private static int card1 = -1;

    @FXML
    public void initialize(){
        gameBoard.getChildren().clear();
        gameLogic.getBoard().clear();
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
                            //TODO: check if it is a valid move.
                            gameLogic.moveCard((Card) gameBoard.getChildren().get(card1), btn);
                            card1 = -1;
                            setBoard();
                            gameLogic.checkMoves();
                            setBoard();
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
}
