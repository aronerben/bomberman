package ch.bomberman.game.activity;

import ch.bomberman.game.entity.play.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PlayActivity extends Activity {

    private Array<Player> mans = new Array<>();

    public PlayActivity(ActivityManager activityManager) {
        super(activityManager);
        //TODO dynamic player add
        mans.add(new Player());
    }

    @Override
    public void update(float dt) {
        mans.forEach(player -> player.update(dt));
    }

    @Override
    public void draw(SpriteBatch batch) {
        mans.forEach(player -> batch.draw(player.getTexture(), (int) player.getPlayerBox().x, (int) player.getPlayerBox().y));
    }

    @Override
    public void dispose() {
        mans.forEach(Player::dispose);
    }

}
