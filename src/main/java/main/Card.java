package main;

import javafx.scene.image.Image;

/**
 * Created by Mike P on 10/1/2016.
 */
public class Card {
    public enum Suit{
        H,
        C,
        D,
        S,
        B
    }

    public enum Rank{
        ZERO,
        Ace,
        Two,
        Three,
        Four,
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Jack,
        Queen,
        King
    }

    private Rank rank;
    private Suit suit;
    private Image image;
    private boolean lock = false;

    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        switch(this.rank.ordinal()){
            case 11:
                image = new Image("images/J" + this.suit + ".png");
                break;
            case 12:
                image = new Image("images/Q" + this.suit + ".png");
                break;
            case 13:
                image = new Image("images/K" + this.suit + ".png");
                break;
            case 1:
                image = new Image("images/A" + this.suit + ".png");
                break;
            default:
                image = new Image("images/" + this.rank.ordinal() + this.suit + ".png");
                break;
        }
    }

    public Image getImage(){
        return image;
    }

    public Suit getSuit(){
        return suit;
    }

    public Rank getRank(){
        return rank;
    }

    public void setlock(){
        lock = true;
    }

    public boolean getLock(){
        return lock;
    }

}
