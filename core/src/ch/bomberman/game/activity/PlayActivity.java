package ch.bomberman.game.activity;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.entity.play.map.Tile;
import ch.bomberman.game.entity.play.player.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import static ch.bomberman.game.entity.play.map.Map.MAP_TILES;
import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;
import static ch.bomberman.game.entity.play.player.Bomb.BOMB_SIZE;
import static ch.bomberman.game.entity.play.player.Player.PLAYER_HEIGHT;
import static ch.bomberman.game.entity.play.player.Player.PLAYER_WIDTH;

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
            mans.add(new Player(map, i));
        }
    }

    @Override
    public void update(float dt) {
        mans.forEach(player -> player.update(dt));
    }

    @Override
    public void draw(SpriteBatch batch) {
        drawMap(batch);
        drawPlayObjects(batch);
    }

    private void drawMap(SpriteBatch batch) {
        Tile[][] tiles = map.getTiles();
        for(int i = 0; i < MAP_TILES; i++) {
            for(int j = 0; j < MAP_TILES; j++) {
                Tile curTile = tiles[i][j];
                batch.draw(curTile.getTexture(), curTile.getBox().x, curTile.getBox().y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPlayObjects(SpriteBatch batch) {
        mans.forEach(player -> {
            batch.draw(player.getTexture(), player.getBox().x, player.getBox().y, PLAYER_WIDTH, PLAYER_HEIGHT);
            player.getBombs().forEach(
                    bomb -> batch.draw(bomb.getTexture(), bomb.getBox().x, bomb.getBox().y, BOMB_SIZE, BOMB_SIZE)
            );
        });
    }

    @Override
    public void dispose() {
        mans.forEach(Player::dispose);
        map.dispose();
    }
}
