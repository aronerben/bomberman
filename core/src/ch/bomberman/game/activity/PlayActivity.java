package ch.bomberman.game.activity;

import ch.bomberman.game.entity.play.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends Activity {

    private List<Player> mans = new ArrayList<>();

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
        batch.draw(mans.get(0).getTexture(), (int) mans.get(0).getPosition().x, (int) mans.get(0).getPosition().y);
    }

    @Override
    public void dispose() {
        mans.forEach(Player::dispose);
    }

}
