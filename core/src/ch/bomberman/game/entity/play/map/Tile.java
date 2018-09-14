package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.entity.play.misc.MapDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class Tile extends MapDrawable {
    //TODO width and height separate?
    public static final float TILE_SIZE = 6;

    Tile(Texture texture, Vector2 tileIndex) {
        super(tileIndex, texture, TILE_SIZE, TILE_SIZE);
    }

    public abstract boolean isTraversable();

    public abstract boolean canContainObject();
}
