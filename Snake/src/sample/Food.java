package sample;

import javafx.scene.image.Image;

import java.util.List;
import java.awt.Point;

public class Food {
    public int x, y;
    private int score;
    private Image icon;
    public Food (){
        x = (int) (Math.random() * Window.COLS);
        y = (int) (Math.random() * Window.ROWS);
        int foodType = (int)(Window.FOODS_IMAGE.length * Math.random());
        icon = new Image (Window.FOODS_IMAGE[foodType]);
    }
    public int getScore (){
        return score;
    }
    public Image getIcon (){
        return icon;
    }

    public void apply() {}
}
