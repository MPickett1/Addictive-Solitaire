package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

/**
 * Created by Mike P on 10/2/2016.
 */
public class GameLogic {
    private ObservableList<Card> board = FXCollections.observableArrayList();
    private static GameLogic _instance = new GameLogic();

    public GameLogic(){
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
                if (card2.getRank() == Card.Rank.Two)
                    moveCard(index, index2);
                if (board.get(index + 1).getSuit() == card2.getSuit()) {
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
                if (board.get(index - 1).getRank() != Card.Rank.King && board.get(index - 1).getRank() != Card.Rank.ZERO) {
                    if (board.get(index - 1).getSuit() == card2.getSuit()) {
                        if (board.get(index - 1).getRank().ordinal() == card2.getRank().ordinal() - 1) {
                            moveCard(index, index2);
                        }
                    }
                    if (board.get(index + 1).getSuit() == card2.getSuit()) {
                        if (board.get(index + 1).getRank().ordinal() == card2.getRank().ordinal() + 1) {
                            moveCard(index, index2);
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
    }

    public boolean checkBoard(){
        for(int i = 0; i < 4; i++){
            System.out.println("i = " + i);
            for(int j = ((i*13)+1); j < board.size(); j++) {
                if((j == 12 || j == 25 || j == 38 || j == 51) && (board.get(j-1).getLock() && board.get(j).getRank() == Card.Rank.ZERO)){
                    board.get(j).setLock();
                    break;
                }
                if (board.get(j - 1).getSuit() != board.get(j).getSuit()) {
                    System.out.println("2 " + (board.get(j - 1).getSuit() != board.get(j).getSuit()));
                    break;
                }else if (board.get(j - 1).getRank().ordinal() != board.get(j).getRank().ordinal() - 1) {
                    System.out.println("3 " + (board.get(j - 1).getRank().ordinal() != board.get(j).getRank().ordinal() - 1));
                    break;
                }else {
                    System.out.println("4 " + j);
                    board.get(j-1).setLock();
                    board.get(j).setLock();
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
