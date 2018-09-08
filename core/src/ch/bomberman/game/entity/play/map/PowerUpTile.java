package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

class PowerUpTile extends Tile {

    PowerUpTile(Vector2 tileIndex) {
        super(new Texture(AssetCollection.POWER_UP_TILE), tileIndex);
    }

    @Override
    public boolean isTraversable() {
        return true;
    }
}
