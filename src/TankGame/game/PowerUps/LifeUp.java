package TankGame.game.PowerUps;

import TankGame.game.Crashes;
import TankGame.game.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LifeUp extends PowerUp{
    int x,y;
    private Rectangle TargetBox;
    BufferedImage image;
    private boolean crashed = false;

    public LifeUp(int x, int y, BufferedImage image){
        this.x = x;
        this.y = y;
        this.image = image;
        this.TargetBox = new Rectangle(x,y,image.getWidth(),image.getHeight());
    }

    @Override
    public void checkCrash(Crashes cr) {
        if (cr instanceof Tank){
            if(this.getTargetBox().intersects(cr.getTargetBox())){
                crashed = true;
                ((Tank) cr).gainLife();
                ((Tank) cr).resetHealth(100);
            }
        }
    }

    @Override
    public Rectangle getTargetBox() {

        return new Rectangle(x,y,image.getWidth(),image.getHeight());
    }

    @Override
    public boolean isCrash() {
        return crashed;
    }

    @Override
    public void drawImage(Graphics gr) {
        Graphics2D g2 = (Graphics2D) gr;
        g2.drawImage(this.image,x,y,null);
    }


}
