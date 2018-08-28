package ch.bomberman.game.activity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Activity {
    private ActivityManager activityManager;

    Activity(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public abstract void dispose();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);

    ActivityManager getActivityManager() {
        return activityManager;
    }
}
