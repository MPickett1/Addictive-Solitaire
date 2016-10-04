package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

/**
 * Created by Mike P on 10/2/2016.
 */
public class GameLogic {
    private ObservableList<Card> board = FXCollections.observableArrayList();
    private GameLogic _instance = new GameLogic();

    public GameLogic(){
        //TODO: Have an arrayList of the
        //TODO: Init a game board, then shuffle them.
    }

    public GameLogic getInstance(){
        return _instance;
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

    private void checkBoard(){
        for(int i = 0; i < 4; i++){
            for(int j = (i*13)+1; j < board.size(); j++) {
                if(!board.get(j-1).getLock()) {
                    if (board.get(j - 1).getSuit() != board.get(j).getSuit())
                        break;
                    else if (board.get(j - 1).getRank().ordinal() != board.get(j).getRank().ordinal() - 1)
                        break;
                    else {
                        board.get(j-1).setlock();
                        board.get(j).setlock();
                    }
                }
            }
        }
    }

}
