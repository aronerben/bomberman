package ch.bomberman.game.entity.play.misc;

import ch.bomberman.game.util.MapTileHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//all map drawables have a surrounding collision box, a tile index position on the map and a texture (sprite)
//don't use sprites bounding box, since that is inaccurate
//use sprites to draw the element, but use box to resolve collision
public abstract class MapDrawable {
    private Rectangle box;
    private Vector2 tileIndex;
    private Sprite mapObject;

    protected MapDrawable(Vector2 tileIndex, Texture texture, int width, int height) {
        this.box = MapTileHelper.createNormalizedBox(tileIndex, width, height);
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

    public Rectangle getBox() {
        return box;
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
