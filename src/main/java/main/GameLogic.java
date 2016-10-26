package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Mike P on 10/2/2016.
 */
public class GameLogic {
    private List<Card> board = new ArrayList<>();
    private static GameLogic _instance = new GameLogic();
    private int shuffles;
    private Random seed = new Random();
    private LocalTime start = LocalTime.MIN, end = LocalTime.MIN;
    private Timeline timeline;
    private int score = 0, highScore = -1, testScore = 0;

    public void setRules(int shuffles, long seed){
        this.shuffles = shuffles + 1;
        this.seed.setSeed(seed);
    }

    public void shuffle(){
        Collections.shuffle(board, seed);
        shuffles--;

    }

    public int getShuffles(){
        return shuffles;
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

    public void setBoard(List<Card> cards){
        board.clear();
        board.addAll(cards);
    }

    public List<Card> getBoard(){
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
        int i = 4;
        for(Card card: board){
            if(card.getRank() == Card.Rank.ZERO) {
                card.setLock(false);
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
                }
            }
        }
        boolean win = checkBoard();

        if(i == 0){
            System.out.println("No more moves.");
            if(shuffles == 0){
                showDialog("lose");
                return;
            }else if(win){
                System.out.println("You Win.");
                showDialog("win");
                return;
            }else {
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
                timeline.stop();
                alert.setTitle("Winner!");
                alert.setHeaderText("Congratulations!");
                if(isHighScore()){
                    alert.setContentText("YOU WIN!\n" + "NEW HIGHSCORE!\n" + getScore() + "\n"
                            + timeElapsed(end).format(DateTimeFormatter.ofPattern("HH:m:ss")));
                }else {
                    alert.setContentText("YOU WIN!\n" + "Score: " + getScore() + "\n"
                            + timeElapsed(end).format(DateTimeFormatter.ofPattern("HH:m:ss")));
                }
                break;
            case "lose":
                timeline.stop();
                alert.setTitle("Loser!");
                alert.setHeaderText("You have run out of shuffles!");
                if(isHighScore()){
                    alert.setContentText("YOU LOSE!\n" + "NEW HIGHSCORE!\n" + getScore());
                }else {
                    if(isHighScore()){
                        alert.setContentText("YOU LOSE!\n" + "Score: " + getScore());
                    }
                }
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

    private boolean isHighScore(){
        if(score >= highScore)
            return true;
        else
            return false;
    }

    public int getScore(){
        testScore = 0;
        board.forEach(card -> {
            if (card.getLock() && !card.getRank().equals(Card.Rank.ZERO))
                testScore += 10;
        });
        if(testScore > score) {
            score = testScore;
        }
        return score;
    }

    private void setEndTime(){
        end = LocalTime.now();
    }

    public void setStartTime(Label elapsedTime){
        start = LocalTime.now();
        timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> {
                    elapsedTime.setText(timeElapsed(LocalTime.now()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
        timeline.statusProperty().addListener(listener -> {
            if(timeline.getStatus().equals(Timeline.Status.STOPPED))
                setEndTime();
                timeElapsed(end);
        });
    }

    public LocalTime timeElapsed(LocalTime time){

        long hour = start.until(time, ChronoUnit.HOURS);
        time = time.minusHours(hour);
        long min = start.until(time, ChronoUnit.MINUTES);
        time = time.minusMinutes(min);
        long sec = start.until(time, ChronoUnit.SECONDS);

        return time.withHour((int) hour).withMinute((int) min).withSecond((int) sec).withNano(0);
    }

}
