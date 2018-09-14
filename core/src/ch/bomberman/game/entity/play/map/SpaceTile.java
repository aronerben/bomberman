package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.entity.play.player.Bomb;
import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class SpaceTile extends Tile {

    private Bomb bomb;

    SpaceTile(Vector2 tileIndex) {
        super(new Texture(AssetCollection.SPACE_TILE), tileIndex);
    }

    //TODO handle player spacetile with bomb collision differently
    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public boolean canContainObject() {
        return true;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
}
