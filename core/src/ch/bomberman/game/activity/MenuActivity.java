package ch.bomberman.game.activity;

import ch.bomberman.game.entity.menu.PlayButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuActivity extends Activity {

    private PlayButton playButton;

    public MenuActivity(ActivityManager activityManager) {
        super(activityManager);
        playButton = new PlayButton();
    }

    @Override
    public void update(float dt) {
        if(playButton.pressed()) {
            getActivityManager().setCurrentActivity(new PlayActivity(getActivityManager()));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        playButton.getSprite().draw(batch);
    }

    @Override
    public void dispose() {
        playButton.dispose();
    }
}
