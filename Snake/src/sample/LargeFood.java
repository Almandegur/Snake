package sample;

import javafx.scene.image.Image;
import java.util.List;
import java.awt.Point;

public class LargeFood extends Food{
    private int score = 3;
    private Image icon = new Image ("/img/ic_apple.png");
    @Override
    public int getScore (){
        return score;
    }
    public Image getIcon (){
        return icon;
    }
}
