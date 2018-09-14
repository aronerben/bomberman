package ch.bomberman.game.entity.play.misc;

import ch.bomberman.game.util.MapTileHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//all map drawables have a surrounding box, a tile index position on the map and a texture
public abstract class MapDrawable {
    private Rectangle box;
    private Vector2 tileIndex;
    private Texture texture;

    protected MapDrawable(Vector2 tileIndex, Texture texture, float width, float height) {
        this.box = MapTileHelper.createNormalizedBox(tileIndex, width, height);
        this.tileIndex = tileIndex;
        this.texture = texture;
    }

    public Rectangle getBox() {
        return box;
    }

    public Vector2 getTileIndex() {
        return tileIndex;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTileIndex(Vector2 tileIndex) {
        this.tileIndex = tileIndex;
    }
}
