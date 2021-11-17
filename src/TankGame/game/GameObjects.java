package TankGame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObjects {
    protected int x;
    protected int y;
    protected int vx;
    protected int vy;
    protected BufferedImage image;
    private Rectangle TargetBox;


    public GameObjects(int x, int y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.image = img;
        this.TargetBox = new Rectangle(this.x, this.y, img.getWidth(), img.getHeight());
    }

    public void drawImage(Graphics gr){
        Graphics2D g2d = (Graphics2D) gr;
        g2d.drawImage(image, x, y, null);
    }


}
