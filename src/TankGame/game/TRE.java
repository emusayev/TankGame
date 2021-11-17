/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame.game;


import TankGame.Launcher;
import TankGame.game.PowerUps.LifeUp;
import TankGame.game.PowerUps.PowerUp;
import TankGame.game.PowerUps.SpeedUp;
import TankGame.game.Walls.UnBreakableWall;
import TankGame.game.Walls.BreakableWall;
import TankGame.game.Walls.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


import static javax.imageio.ImageIO.read;
import static TankGame.GameConstants.*;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {
    private BufferedImage world;
    private JFrame jf;
    private Tank t1;
    private Tank t2;
    private BufferedImage lifeupImage;
    private BufferedImage speedupImage;
    private BufferedImage background;
    private BufferedImage gameoverImage;
    BufferedImage UnBreakableWall = null;
    BufferedImage  BreakableWall = null;
    private Launcher lf;
    private long a = 0;
    ArrayList<Wall> walls = new ArrayList<>();
    ArrayList<PowerUp> powerups = new ArrayList<>();
    private static LifeUp life;

    public TRE(Launcher lf){
        this.lf = lf;
    }

    @Override
    public void run(){
     //  try {
           this.resetGame();
           while (true) {
                this.a++;
                this.t1.update(); // updateds both tanks
                this.t2.update();

                t1.checkCrash(t2);  // check tanks whether they collided together
                t2.checkCrash(t1);

                checkWallCollision(t1);  //checking walls between all tanks
                checkWallCollision(t2);

                checkPowerUpCollision(t1); // checking powerups for both tanks
                checkPowerUpCollision(t2);

                this.repaint();   // redraw game
               //sleep for a few milliseconds
               try {
                   Thread.sleep(6);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

            }

    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.a = 0;
        this.t1.setX(200);
        this.t1.setY(200);
        this.t2.setX(1800);
        this.t2.setY(1800);
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() throws IOException {
        this.jf = new JFrame("Tank Game");
        this.world = new BufferedImage(WORLD_WIDTH, WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null;
        BufferedImage t2img = null;


        t1img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
        t2img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank2.png")));
        UnBreakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall2.png")));
        BreakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall1.png")));
        background = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp")));
        speedupImage = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("speed.gif")));
        lifeupImage = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("life.gif")));
        gameoverImage = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("GameOver.png")));

        InputStreamReader isr = new InputStreamReader(TRE.class.getClassLoader().getResourceAsStream("map/map1.txt"));
        BufferedReader mapReader = new BufferedReader(isr);

        String row = mapReader.readLine();
        if(row == null){
            throw new IOException("no new data on file");
        }
        String[] mapInfo = row.split("\t");
        int numCols = Integer.parseInt(mapInfo[0]);
        int numRows = Integer.parseInt(mapInfo[1]);

        for(int curRow = 0; curRow < numRows; curRow++){
            row = mapReader.readLine();
            mapInfo = row.split("\t");
            for(int curCol = 0; curCol < numCols; curCol++){
                switch (mapInfo[curCol]){
                    case "2":
                        TankGame.game.Walls.BreakableWall sq = new BreakableWall(curCol * 30, curRow * 30, BreakableWall) {
                        };
                        this.walls.add(sq);
                        break;
                    case "9":
                        UnBreakableWall sl = new UnBreakableWall(curCol * 30, curRow * 30, UnBreakableWall) {
                        };
                        this.walls.add(sl);
                        break;
                    case "3":
                        LifeUp lifeup = new LifeUp(curCol * 30, curRow * 30, lifeupImage);
                        this.powerups.add(lifeup);
                        break;
                    case "4":
                        SpeedUp speedup = new SpeedUp(curCol * 30, curRow * 30, speedupImage);
                        this.powerups.add(speedup);
                        break;
                }
            }
        }

        t1 = new Tank(200, 200, 0, 0, 0, t1img) {
        };
        t2 = new Tank(1800, 1800, 0, 0, 180, t2img) {
        };
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);

    }

    private void checkWallCollision(Crashes cc){
        for (int i = 0; i < walls.size(); i++){
            cc.checkCrash(walls.get(i));
            walls.get(i).checkCrash(cc);
            if (walls.get(i).isCrashed()){
                walls.remove(i);
            }
        }
    }
    private void checkPowerUpCollision(Crashes cc){
        for (int i = 0; i < powerups.size(); i++) {
            cc.checkCrash(powerups.get(i));
            powerups.get(i).checkCrash(cc);
            if (powerups.get(i).isCrash()) {
                powerups.remove(i);
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g2);
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0,0, WORLD_WIDTH, WORLD_HEIGHT);
        for(int i = 0; i < WORLD_WIDTH; i+= 320){
            for(int j = 0; j < WORLD_HEIGHT; j+= 240)
                buffer.drawImage( background, i, j, 320, 240, this);
        }
        for (int i = 0; i < walls.size(); i++){
            walls.get(i).drawImage(buffer);
        }
        for (int i = 0; i < powerups.size(); i++){
            powerups.get(i).drawImage(buffer);
        }

        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        g2.drawImage(world,0,0,null);

        BufferedImage lHalf = world.getSubimage(Tank.getCamX(t1),Tank.getCamY(t1),GAME_SCREEN_WIDTH/2,GAME_SCREEN_HEIGHT);
        BufferedImage rHalf = world.getSubimage(Tank.getCamX(t2),Tank.getCamY(t2),GAME_SCREEN_WIDTH/2,GAME_SCREEN_HEIGHT);
        BufferedImage mmap = world.getSubimage(0,0, WORLD_WIDTH, WORLD_HEIGHT);
        g2.drawImage(lHalf,0,0,null);
        g2.drawImage(rHalf,GAME_SCREEN_WIDTH/2,0,null);

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 22));
        g2.setColor(Color.BLUE);
        g2.drawString("PLAYER1",GAME_SCREEN_WIDTH/4,875);
        g2.drawString("PLAYER2",GAME_SCREEN_WIDTH - GAME_SCREEN_WIDTH/4,875 );

        g2.setColor(Color.GREEN);
        g2.fillRect(290, 10, 3* t1.getHealth(), 20);
        g2.fillRect(900, 10, 3* t2.getHealth(), 20);

        g2.setColor(Color.MAGENTA);
        g2.drawString("Lives remaining: " + t1.getLives(),60,675);
        g2.drawString("Lives remaining: " + t2.getLives(),500,175);

        g2.scale(.1,.1);
        g2.drawImage(mmap,GAME_SCREEN_WIDTH/2,200,null);

        //re scale here to not print end game image by the scale of minimap
        g2.scale(10,10);
        if(t1.getLives() == 0 ||t2.getLives() == 0){
            g2.drawImage(gameoverImage,0,0,GAME_SCREEN_WIDTH,GAME_SCREEN_HEIGHT,null);

            g2.setFont(new Font("TimesRoman", Font.PLAIN, 22));
            g2.drawString("Game Over !! Please exit the game ", 500,800);


    }
}
}
