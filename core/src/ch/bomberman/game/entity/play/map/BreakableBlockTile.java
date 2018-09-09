package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

class BreakableBlockTile extends Tile {

    BreakableBlockTile(Vector2 tileIndex) {
        super(new Texture(AssetCollection.BREAKABLE_BLOCK_TILE), tileIndex);
    }

    @Override
    public boolean isWall() {
        return true;
    }
}
