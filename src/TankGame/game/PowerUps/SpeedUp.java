package TankGame.game.PowerUps;

import TankGame.game.Crashes;
import TankGame.game.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeedUp extends PowerUp{
    int x,y ;
    private Rectangle TargetBox;
    BufferedImage image;
    private boolean crashed = false;

    public SpeedUp(int x, int y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.image = img;
        this.TargetBox = new Rectangle(x,y,img.getWidth(),img.getHeight());
    }

    @Override
    public void checkCrash(Crashes cr) {
        if(cr instanceof Tank){
            if(this.getTargetBox().intersects(cr.getTargetBox())){
                crashed = true;
                ((Tank) cr).speedBoost();
            }
        }
    }

    @Override
    public Rectangle getTargetBox() {

        return new Rectangle(x,y,image.getWidth(),image.getHeight());
    }

    @Override
    public boolean isCrash() {
        return false;
    }

    @Override
    public void drawImage(Graphics gr) {
        Graphics2D g2 = (Graphics2D) gr;
        g2.drawImage(this.image,x,y,null);
    }
}
