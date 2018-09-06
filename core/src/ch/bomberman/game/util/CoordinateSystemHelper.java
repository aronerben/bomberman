package ch.bomberman.game.util;

import ch.bomberman.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;
import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;

public class CoordinateSystemHelper {

    //TODO careful with camera/viewport later, remove this?
    public static Vector2 getNormalizedMousePosition() {
        return new Vector2(Gdx.input.getX(), Main.VIRTUAL_HEIGHT - Gdx.input.getY());
    }

    //TODO method for centering stuff on screen
    public static Vector2 getCenterCoord(float width, float height) {
        return new Vector2(Main.VIRTUAL_WIDTH / 2 - width / 2, Main.VIRTUAL_HEIGHT / 2 - height / 2);
    }

    public static Vector2 tileIndexToVirtualUnits(float j, float i) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        return new Vector2(j * TILE_SIZE + offset.x, i * TILE_SIZE + offset.y);
    }

    public static Vector2 virtualUnitsToTileIndex(float x, float y) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        return new Vector2((int) ((x - offset.x) / TILE_SIZE), (int) ((y - offset.y) / TILE_SIZE));
    }

}
