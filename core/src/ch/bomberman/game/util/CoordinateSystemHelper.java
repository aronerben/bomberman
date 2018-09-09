package ch.bomberman.game.util;

import ch.bomberman.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;
import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;

//these functions might be called in the render loop => avoid memory leaks
public class CoordinateSystemHelper {

    //TODO careful with camera/viewport later, remove this?
    public static Vector2 getNormalizedMousePosition() {
        return new Vector2(Gdx.input.getX(), Main.VIRTUAL_HEIGHT - Gdx.input.getY());
    }

    public static Vector2 getCenterCoord(float width, float height) {
        return new Vector2(Main.VIRTUAL_WIDTH / 2 - width / 2, Main.VIRTUAL_HEIGHT / 2 - height / 2);
    }

    public static void tileIndexToVirtualUnits(Vector2 tileIndex, Vector2 virtualUnits) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        virtualUnits.set(tileIndex.x * TILE_SIZE + offset.x, tileIndex.y * TILE_SIZE + offset.y);
    }

    public static void virtualUnitsToTileIndex(Rectangle position, Vector2 tileIndexPosition) {
        Vector2 offset = CoordinateSystemHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        tileIndexPosition.set((float) Math.floor(((position.x - offset.x) / TILE_SIZE)), (float) Math.floor(((position.y - offset.y) / TILE_SIZE)));
    }

}
