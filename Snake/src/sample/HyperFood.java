package sample;

import javafx.scene.image.Image;

public class HyperFood extends Food{
    private int score = 0;
    public Image icon = new Image ("/img/ic_pomegranate.png");
    @Override
    public int getScore (){
        return score;
    }
    public Image getIcon (){
        return icon;
    }
    public void apply (){
        Window.wallCollision = false;
    }
}
