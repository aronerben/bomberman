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
                curTile.getObject().draw(batch);
            }
        }
    }

    private void drawPlayObjects(SpriteBatch batch) {
        mans.forEach(player -> {
            player.getBombs().forEach(
                    bomb -> bomb.getObject().draw(batch));
            player.getObject().draw(batch);
        });
    }

    @Override
    public void dispose() {
        mans.forEach(Player::dispose);
        map.dispose();
    }
}
