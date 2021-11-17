
package TankGame.game;

import TankGame.game.Crashes;
import TankGame.game.GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Missile extends GameObjects implements Crashes {
    private int x, y, vx, vy;
    private int angle;
    private static BufferedImage bulletImage;
    private boolean isCrashed = false;
    Rectangle hitBox;


    static{
        try{
            bulletImage = read(new File("resources/Weapon.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Missile( int x, int y, int angle) {
        super(x, y, bulletImage);
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x, y, bulletImage.getWidth(), bulletImage.getHeight());

    }

    public void update(){
        this.vx = (int) Math.round(3*Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(3*Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        getTargetBox();
    }

    public void drawImage(Graphics g){
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bulletImage,rotation, null);
        /*g2d.drawRect(x,y,this.bulletImage.getWidth(),this.bulletImage.getHeight());*/
    }

    public boolean isCrashed() {
        return isCrashed;
    }

    @Override
    public Rectangle getTargetBox () {
        return new Rectangle(x,y,bulletImage.getWidth(), bulletImage.getHeight());
    }

    @Override
    public void checkCrash(Crashes cr) {
        if(this.getTargetBox().intersects(cr.getTargetBox())){
            isCrashed = true;
        }
    }


}