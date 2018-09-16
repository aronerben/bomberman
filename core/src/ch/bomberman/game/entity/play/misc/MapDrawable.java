package ch.bomberman.game.entity.play.misc;

import ch.bomberman.game.util.MapTileHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

//all map drawables have a surrounding box, a tile index position on the map and a texture
public abstract class MapDrawable {
    private Sprite mapObject;
    private Vector2 tileIndex;

    protected MapDrawable(Vector2 tileIndex, Texture texture, int width, int height) {
        this.tileIndex = tileIndex;
        mapObject = new Sprite(texture);
        mapObject.setSize(width, height);
        positionSprite();
    }

    private void positionSprite() {
        Vector2 position = new Vector2();
        MapTileHelper.tileIndexToVirtualUnits(tileIndex, position);
        mapObject.setPosition(position.x, position.y);
    }

    public Sprite getObject() {
        return mapObject;
    }

    public Vector2 getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(Vector2 tileIndex) {
        this.tileIndex = tileIndex;
    }
}
