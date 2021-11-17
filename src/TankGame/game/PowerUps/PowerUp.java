package TankGame.game.PowerUps;

import TankGame.game.Crashes;

import java.awt.*;

public abstract class PowerUp implements Crashes {

    @Override
    public abstract void checkCrash(Crashes cr);

    public Rectangle getTargetBox() {

        return null;
    }

    public abstract boolean isCrash();

    public abstract void drawImage(Graphics gr);
}
