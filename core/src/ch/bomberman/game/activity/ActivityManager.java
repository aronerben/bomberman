package ch.bomberman.game.activity;

public class ActivityManager {

    private Activity currentActivity;

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        //dispose when swapping activities (prevent memory leaks)
        this.currentActivity.dispose();
        this.currentActivity = currentActivity;
    }
}
