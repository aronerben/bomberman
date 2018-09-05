package ch.bomberman.game.activity;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.entity.play.map.Tile;
import ch.bomberman.game.entity.play.player.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;

public class PlayActivity extends Activity {

    public static final int PLAYERS = 1;

    private Array<Player> mans = new Array<>();
    private Map map;

    PlayActivity(ActivityManager activityManager) {
        super(activityManager);
        //TODO maybe make enum for map themes
        map = new Map();

        //TODO dynamic player add
        for(int i = 0; i < PLAYERS; i++) {
            mans.add(new Player(Map.STARTING_POSITIONS[i]));
        }

    }

    @Override
    public void update(float dt) {
        mans.forEach(player -> player.update(dt));
    }

    @Override
    public void draw(SpriteBatch batch) {
        drawMap(batch);
        mans.forEach(player -> batch.draw(player.getTexture(), player.getPlayerBox().x, player.getPlayerBox().y, Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT));
    }

    private void drawMap(SpriteBatch batch) {
        Tile[][] tiles = map.getTiles();
        for(int i = 0; i < MAP_TILES; i++) {
            for(int j = 0; j < MAP_TILES; j++) {
                Tile curTile = tiles[i][j];
                batch.draw(curTile.getTexture(), curTile.getTileBox().x, curTile.getTileBox().y, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }

    @Override
    public void dispose() {
        mans.forEach(Player::dispose);
    }

}
