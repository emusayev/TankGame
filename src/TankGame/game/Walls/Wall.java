package TankGame.game.Walls;

import TankGame.game.Crashes;

import java.awt.*;

public abstract class Wall implements Crashes {

    public abstract void drawImage(Graphics gr);

    @Override
    public Rectangle getTargetBox() {

        return null;
    }
    public abstract boolean isCrashed();
}
