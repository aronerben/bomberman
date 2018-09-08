package ch.bomberman.game.util;

import ch.bomberman.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;
import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;

public class CoordinateSystemHelper {

    //TODO careful with camera/viewport later, remove this?
    public static Vector2 getNormalizedMousePosition() {
        return new Vector2(Gdx.input.getX(), Main.VIRTUAL_HEIGHT - Gdx.input.getY());
    }

    public static Vector2 getCenterCoord(float width, float height) {
        return new Vector2(Main.VIRTUAL_WIDTH / 2 - width / 2, Main.VIRTUAL_HEIGHT / 2 - height / 2);
    }

    public static Vector2 tileIndexToVirtualUnits(Vector2 tileIndex) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        return new Vector2(tileIndex.x * TILE_SIZE + offset.x, tileIndex.y * TILE_SIZE + offset.y);
    }

    public static Vector2 virtualUnitsToTileIndex(Rectangle position) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        return new Vector2((float)Math.floor(((position.x - offset.x) / TILE_SIZE)), (float)Math.floor(((position.y - offset.y) / TILE_SIZE)));
    }

}
