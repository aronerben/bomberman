package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.util.CoordinateSystemHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Tile {
    //TODO width and height separate?
    public static final int TILE_SIZE = 6;

    private Texture texture;
    private Vector2 tileIndex;
    private Rectangle tileBox;

    Tile(Texture texture, Vector2 tileIndex) {
        this.texture = texture;
        this.tileIndex = tileIndex;
        this.tileBox = createNormalizedTileBox();
    }

    private Rectangle createNormalizedTileBox() {
        Vector2 normalizedPos = CoordinateSystemHelper.tileIndexToVirtualUnits(tileIndex.x, tileIndex.y);
        return new Rectangle(normalizedPos.x, normalizedPos.y, TILE_SIZE, TILE_SIZE);
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getTileBox() {
        return tileBox;
    }
}
