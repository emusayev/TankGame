package TankGame.game.Walls;

import TankGame.game.Crashes;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class UnBreakableWall extends Wall {
    int x,y;
    BufferedImage widthImage;
    private Rectangle TargetBox;

    public UnBreakableWall(int x, int y, BufferedImage widthImage){
        this.x = x;
        this.y = y;
        this.widthImage = widthImage;
        this.TargetBox = new Rectangle(x,y,widthImage.getWidth(),widthImage.getHeight());
    }

    public void drawImage(Graphics gr){
        Graphics2D g2 = (Graphics2D) gr;
        g2.drawImage(this.widthImage,x,y,null);
    }

    @Override
    public void checkCrash(Crashes cr) {

    }
    @Override
    public boolean isCrashed() {

        return false;
    }

    @Override
    public Rectangle getTargetBox() {

        return new Rectangle(x,y,widthImage.getWidth(),widthImage.getHeight());
    }
}
