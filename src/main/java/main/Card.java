package main;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Mike P on 10/1/2016.
 */
public class Card extends ToggleButton{
    public enum Suit{
        B,
        H,
        C,
        D,
        S
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
            case 0:
                image = new Image("images/Blue_Back.png");
                this.suit = Suit.B;
                break;
            default:
                image = new Image("images/" + this.rank.ordinal() + this.suit + ".png");
                break;
        }
        this.lock = false;
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(70);
        imageView.setFitHeight(144);
        setGraphic(imageView);
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

    public void setLock(){
        lock = true;
    }

    public boolean getLock(){
        return lock;
    }

}
