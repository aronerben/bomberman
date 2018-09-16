package ch.bomberman.game.util;

import ch.bomberman.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;
import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;

//these functions might be called in the render loop => avoid memory leaks
public class MapTileHelper {

    //TODO careful with camera/viewport later, remove this?
    public static Vector2 getNormalizedMousePosition() {
        return new Vector2(Gdx.input.getX(), Main.VIRTUAL_HEIGHT - Gdx.input.getY());
    }

    public static Vector2 getCenterCoord(float width, float height) {
        return new Vector2(Main.VIRTUAL_WIDTH / 2 - width / 2, Main.VIRTUAL_HEIGHT / 2 - height / 2);
    }

    public static void tileIndexToVirtualUnits(Vector2 tileIndex, Vector2 virtualUnits) {
        Vector2 offset = MapTileHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        virtualUnits.set(tileIndex.x * TILE_SIZE + offset.x, tileIndex.y * TILE_SIZE + offset.y);
    }

    public static void virtualUnitsToTileIndex(Sprite position, Vector2 tileIndex) {
        Vector2 offset = MapTileHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        tileIndex.set((float) Math.floor(((position.getX() - offset.x) / TILE_SIZE)), (float) Math.floor(((position.getY() - offset.y) / TILE_SIZE)));
    }

    public static void virtualUnitsToTileIndex(Vector2 position, Vector2 tileIndex) {
        Vector2 offset = MapTileHelper.getCenterCoord(MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
        tileIndex.set((float) Math.floor(((position.x - offset.x) / TILE_SIZE)), (float) Math.floor(((position.y - offset.y) / TILE_SIZE)));
    }

    public static void centerObjectOnTile(Sprite object, float width, float height) {
        object.setPosition(
                object.getX() + TILE_SIZE / 2f - width / 2f,
                object.getY() + TILE_SIZE / 2f - height / 2f
        );
    }
}
