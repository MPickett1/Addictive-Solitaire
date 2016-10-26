package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.TransformerException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike P on 10/14/2016.
 */
public class SaveState{
    private final static String jp_id = "_id";
    private final static String jp_name = "name";
    private final static String jp_board = "board";
    private final static String jp_shuffles = "shuffles";
    private final static String jp_seed = "seed";
    private final static String jp_score = "score";

    protected String id = null;
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<ArrayList<String>> board = new SimpleObjectProperty<>(new ArrayList<>());
    private IntegerProperty shuffles = new SimpleIntegerProperty();
    private LongProperty seed = new SimpleLongProperty();
    private IntegerProperty score = new SimpleIntegerProperty();

    public SaveState(){}

    public SaveState(List<Card> board, int shuffles, long seed, int score) {
        setBoard(board);
        setShuffles(shuffles);
        setSeed(seed);
        setScore(score);
    }

    public SaveState(SaveState save){
        setBoard(save.getBoardAsCards());
        setShuffles(save.getShuffles());
        setSeed(save.getSeed());
        setScore(save.getScore());
        setName(save.getName());
        setId(null);
    }

    @JsonProperty(jp_id)
    public String getId() {
        return id;
    }

    @JsonProperty(jp_id)
    public void setId(String id){
        this.id = id;
    }

    @JsonIgnore
    public ArrayList<String> decompressBoard(List<Card> board){
        ArrayList<String> cards = new ArrayList<>();

        for(Card c : board){
            cards.add(c.getRank().name() + ":" + c.getSuit().name());
        }
        return cards;
    }

    @JsonIgnore
    public List<Card> recompressBoard(ArrayList<String> board){
        List<Card> resBoard = new ArrayList<>();

        board.forEach(consumer -> {
            int colonIndex = consumer.indexOf(":");
            resBoard.add(new Card(consumer.substring(0,colonIndex), consumer.substring(colonIndex+1)));
        });

        return resBoard;
    }

    @JsonProperty(jp_name)
    public void setName(String name){
        this.name.set(name);
    }

    @JsonProperty(jp_name)
    public String getName(){
        return name.get();
    }

    @JsonIgnore
    public StringProperty nameProperty(){
        return name;
    }

    @JsonProperty(jp_board)
    public void setBoard(ArrayList<String> board){
        this.board.set(board);
    }

    @JsonIgnore
    public void setBoard(List<Card> board){
        this.board.setValue(decompressBoard(board));
    }

    @JsonProperty(jp_board)
    public ArrayList<String> getBoard(){
        return board.get();
    }

    @JsonIgnore
    public List<Card> getBoardAsCards(){
        return recompressBoard(board.get());
    }

    @JsonIgnore
    public ObjectProperty<ArrayList<String>> boardProperty(){
        return board;
    }

    @JsonProperty(jp_shuffles)
    public void setShuffles(int shuffles){
        this.shuffles.set(shuffles);
    }

    @JsonProperty(jp_shuffles)
    public int getShuffles(){
        return shuffles.get();
    }

    @JsonIgnore
    public IntegerProperty shufflesProperty(){
        return shuffles;
    }

    @JsonProperty(jp_seed)
    public void setSeed(long seed){
        this.seed.set(seed);
    }

    @JsonProperty(jp_seed)
    public long getSeed(){
        return seed.get();
    }

    @JsonIgnore
    public LongProperty seedProperty(){
        return seed;
    }

    @JsonProperty(jp_score)
    public void setScore(int score){
        this.score.set(score);
    }

    @JsonProperty(jp_score)
    public int getScore(){
        return score.get();
    }

    @JsonIgnore
    public IntegerProperty scoreProperty(){
        return score;
    }
}
