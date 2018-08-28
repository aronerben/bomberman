package ch.bomberman.game.util;

import ch.bomberman.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class CoordinateSystemHelper {

    //TODO careful with camera/viewport later
    public static Vector2 getNormalizedMousePosition() {
        return new Vector2(Gdx.input.getX(), Main.HEIGHT - Gdx.input.getY());
    }

    //TODO method for centralising sprites onscreen
}
