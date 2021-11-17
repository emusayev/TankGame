package TankGame.game;



import TankGame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static TankGame.GameConstants.*;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObjects implements Crashes {


    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int health = 100;
    private int life = 3;

    private int R = 2;
    private float rotationspeed = 2.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    private Rectangle TargetBox;
    public ArrayList<Missile> ammunation;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super(x,y, img);
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.img = img;
        ammunation = new ArrayList<>();
        this.TargetBox = new Rectangle(this.x,this.y,this.img.getWidth(), this.img.getHeight());

    }

    public void gainLife(){this.life = this.life + 1;}

    public void takeDamage(int damage){
        if(health - damage <= 0){
            health = 0;
            loseLife();
        }
        else{
            health = health - damage;
        }
    }

    public void loseLife() {
        if (life == 0){
            health = 0;
        }
        else{
            life = life - 1;
            resetHealth(100);
        }
    }

    public void resetHealth(int value) {
        if(health + value >= 100){
            health = 100;
        }
        else{
            health = health + value;
        }
    }

    public void speedBoost(){
        this.R += 5;
        this.rotationspeed = 4;
    }

    public int getHealth(){return health;}

    public int getLives(){return life;}

    void setX(int x){ this.x = x; }

    public int getX(){return x;}

    void setY(int y) { this. y = y;}

    public int getY() {return y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShot(){this.ShootPressed = true;}

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShot(){this.ShootPressed = false;}

    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if(this.ShootPressed){
            this.shoot();
            ShootPressed = false;
        }
        for (int i = 0; i < ammunation.size(); i++){
            if(ammunation.get(i).isCrashed()) {
                ammunation.remove(i);
            }else ammunation.get(i).update();
        }
    }

    private void shoot() {
        Missile missile = new Missile(x,y,angle);
        ammunation.add(missile);
    }

    private void rotateLeft() {
        this.angle -= this.rotationspeed;
    }

    private void rotateRight() {
        this.angle += this.rotationspeed;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= WORLD_WIDTH - 88) {
            x = WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.WORLD_HEIGHT - 80) {
            y = GameConstants.WORLD_HEIGHT- 80;
        }
    }

    public static int getCamX(Tank tank){
        int x = tank.getX();
        if (x < GAME_SCREEN_WIDTH/4) x = GAME_SCREEN_WIDTH / 4;
        if (x > WORLD_WIDTH - GAME_SCREEN_WIDTH/4) x = WORLD_WIDTH - GAME_SCREEN_WIDTH/4;
        return x - GAME_SCREEN_WIDTH/4;
    }
    public static int getCamY(Tank tank){
        int y = tank.getY();
        if (y < GAME_SCREEN_HEIGHT/2 ) y = GAME_SCREEN_HEIGHT/2;
        if (y > WORLD_HEIGHT - GAME_SCREEN_HEIGHT/2) y = WORLD_HEIGHT - GAME_SCREEN_HEIGHT/2;
        return y - GAME_SCREEN_HEIGHT/2;
    }


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        for (int i = 0; i < ammunation.size(); i++){
            ammunation.get(i).drawImage(g2d);
        }
    }

    @Override
    public Rectangle getTargetBox() {

        return new Rectangle(this.x,this.y,img.getWidth(),img.getHeight());
    }

    @Override
    public void checkCrash(Crashes cr) {
        if(this.getTargetBox().intersects(cr.getTargetBox())){
            if (cr instanceof Missile){
                takeDamage(25);
            } else {
                Rectangle crash = this.getTargetBox().intersection(cr.getTargetBox());
                if (crash.height > crash.width && this.x < crash.x) {
                    x = x - crash.width / 2;
                } else if (crash.height > crash.width && this.x > cr.getTargetBox().x) {
                    x = x + crash.width / 2;
                } else if (crash.height < crash.width && this.y < crash.y) {
                    y = y - crash.height / 2;
                } else if (crash.height < crash.width && this.y > cr.getTargetBox().y) {
                    y = y + crash.height / 2;
                }
            }
        }
        for (Missile missile : ammunation){
            missile.checkCrash(cr);
            cr.checkCrash(missile);
        }
    }

}
