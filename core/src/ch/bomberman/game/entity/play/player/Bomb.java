package ch.bomberman.game.entity.play.player;

import ch.bomberman.game.entity.play.misc.MapDrawable;
import ch.bomberman.game.util.AssetCollection;
import ch.bomberman.game.util.MapTileHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;

//TODO replace blown up tiles with space tiles
//TODO set bombs on tiles
//TODO owner by playernumber
//TODO make bombs poolable objects

public class Bomb extends MapDrawable {
    //TODO width and height separate?
    public static final float BOMB_SIZE = 3;
    public static final int BOMB_LIMIT_ON_MAP = MAP_TILES * MAP_TILES;

    public Bomb(Vector2 tileIndex) {
        super(tileIndex, new Texture(AssetCollection.BOMB), BOMB_SIZE, BOMB_SIZE);
        MapTileHelper.centerObjectOnTile(getBox(), BOMB_SIZE, BOMB_SIZE);
    }
}
