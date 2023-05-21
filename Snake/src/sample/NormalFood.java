package sample;

import javafx.scene.image.Image;

public class NormalFood extends Food{
    private int score = 1;
    private Image icon = new Image ("/img/ic_cherry.png");
    @Override
    public int getScore (){
        return score;
    }
    public Image getIcon (){
        return icon;
    }
}
