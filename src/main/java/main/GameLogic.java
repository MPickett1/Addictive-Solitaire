package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Mike P on 10/2/2016.
 */
public class GameLogic {
    private ObservableList<Card> board = FXCollections.observableArrayList();
    private static GameLogic _instance = new GameLogic();
    private int shuffles;
    private Random seed = new Random();

    public void setRules(int shuffles, long seed){
        this.shuffles = shuffles;
        this.seed.setSeed(seed);
    }

    public void shuffle(){

        Collections.shuffle(board, seed);
    }
    public static GameLogic getInstance(){
        return _instance;
    }

    public void addCardToBoard(Card card){
        this.board.add(card);
    }

    public void replaceAces(){
        for(Card card : board){
            if(card.getRank() == Card.Rank.Ace){
                board.set(board.indexOf(card), new Card(Card.Rank.ZERO, Card.Suit.B));
            }
        }
    }

    public void setBoard(ArrayList cards){
        board.setAll(cards);
    }

    public ObservableList<Card> getBoard(){
        return board;
    }

    private void moveCard(int index1, int index2){
        Card temp = board.get(index1);
        board.set(index1, board.get(index2));
        board.set(index2, temp);
    }

    public void moveCard(Card card1, Card card2){
        int index = board.indexOf(card1);
        int index2 = board.indexOf(card2);

        if(card1.getRank() == Card.Rank.ZERO) {
            if (index == 0 || index == 13 || index == 26 || index == 39) {
                if (card2.getRank() == Card.Rank.Two) {
                    moveCard(index, index2);
                }else if (board.get(index + 1).getSuit() == card2.getSuit()) {
                    if (board.get(index + 1).getRank().ordinal() == card2.getRank().ordinal() + 1) {
                        moveCard(index, index2);
                    }
                }
            } else if (index == 51 || index == 12 || index == 25 || index == 38) {
                if (board.get(index - 1).getSuit() == card2.getSuit()) {
                    if (board.get(index - 1).getRank().ordinal() == card2.getRank().ordinal() - 1) {
                        moveCard(index, index2);
                    }
                }
            } else {
                if (board.get(index - 1).getRank() != Card.Rank.King) {
                    if (board.get(index - 1).getRank() == Card.Rank.ZERO && !board.get(index - 1).getLock()) {
                        if (board.get(index + 1).getSuit() == card2.getSuit()) {
                            if (board.get(index + 1).getRank().ordinal() == card2.getRank().ordinal() + 1) {
                                moveCard(index, index2);
                            }
                        }
                    } else {
                        if (board.get(index - 1).getSuit() == card2.getSuit()) {
                            if (board.get(index - 1).getRank().ordinal() == card2.getRank().ordinal() - 1) {
                                moveCard(index, index2);
                                return;
                            }
                        }
                        if (board.get(index + 1).getSuit() == card2.getSuit()) {
                            if (board.get(index + 1).getRank().ordinal() == card2.getRank().ordinal() + 1) {
                                moveCard(index, index2);
                            }
                        }
                    }
                }
            }
        }else if(card2.getRank() == Card.Rank.ZERO){
            moveCard(card2, card1);
        }
    }

    public void checkMoves(){
        //TODO: Check if there are any moves left.
        int i = 4;
        for(Card card: board){
            if(card.getRank() == Card.Rank.ZERO) {
                int index = board.indexOf(card);
                if(index == 0 || index == 13 || index == 26 || index == 39)
                    break;
                if(board.get(index-1).getRank() == Card.Rank.King){
                    card.setLock(true);
                    i--;
                }else if(board.get(index-1).getRank() == Card.Rank.ZERO){
                    if(board.get(index-1).getLock()) {
                        card.setLock(true);
                        i--;
                    }
                }else{
                    card.setLock(false);
                }
            }
        }
        boolean win = checkBoard();

        if(i == 0){
            System.out.println("No more moves.");
            if(win){
                System.out.println("You Win.");
            }else {
                shuffles--;
                ObservableList<Card> locked = FXCollections.observableArrayList();
                for(Card card: board){
                    if(card.getLock() && card.getRank() != Card.Rank.ZERO){
                        locked.add(card);
                    }else{
                        locked.add(null);
                    }
                }

                board.removeAll(locked);

                showDialog("shuffle");
                shuffle();
                for(Card card : locked){
                    if( card != null){
                        board.add(locked.indexOf(card), card);
                    }
                }

                for(Card card : board){
                    if(card.getRank() == Card.Rank.ZERO){
                        card.setLock(false);
                    }
                }
                checkMoves();
            }

        }
    }

    private void showDialog(String type){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        switch(type){
            case "shuffle":
                alert.setTitle("Shuffling...");
                alert.setHeaderText("You have run out of moves!");
                alert.setContentText("Now Shuffling.");
                break;
            case "win":
                alert.setTitle("Winner!");
                alert.setHeaderText("You have run out of moves!");
                alert.setContentText("YOU WIN!");
                break;
            case "lose":
                break;
        }

        alert.showAndWait();
    }
    public boolean checkBoard(){
        for(int i = 0; i < 4; i++){
            for(int j = ((i*13)+1); j < board.size(); j++) {
                if((j == 14 || j == 27 || j == 40 || j == 1)){
                    if (board.get(j-1).getRank() == Card.Rank.Two){
                        board.get(j - 1).setLock(true);
                    } else {
                        break;
                    }
                }
                if (board.get(j - 1).getSuit() != board.get(j).getSuit()) {
                    break;
                }else if (board.get(j - 1).getRank().ordinal() != board.get(j).getRank().ordinal() - 1) {
                    break;
                }else {
                    board.get(j-1).setLock(true);
                    board.get(j).setLock(true);
                }
            }
        }

        for(Card card : board){
            if(!card.getLock()) {
                return false;
            }
        }
        return true;
    }

}
