package ch.bomberman.game;

import ch.bomberman.game.activity.ActivityManager;
import ch.bomberman.game.activity.MenuActivity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//TODO make bombs poolable objects
//TODO add debug
//TODO add sounds
public class Main extends ApplicationAdapter {

    public static final String NAME = "Bomberman";

    /**
     * For easier handling, the game uses virtual units and not pixels.
     * For example, a player has height 3. Once can think of this unit as meters
     */
    public static final float VIRTUAL_WIDTH = 100;
    public static final float VIRTUAL_HEIGHT = 100;

    private static final int FRAMERATE = 999;

    private ActivityManager activityManager;
    private SpriteBatch batch;
    private float waitTime;

    //TODO refactor these?
    private OrthographicCamera cam;
    private Viewport viewport;

    @Override
    public void create() {
        //TODO fix loglevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        cam = new OrthographicCamera();
        //TODO decide wether fill, fit or screen
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, cam);
        viewport.apply(true);

        batch = new SpriteBatch();
        //start with menu
        activityManager = new ActivityManager();
        activityManager.setCurrentActivity(new MenuActivity(activityManager));
        //initial color clear
        Gdx.gl.glClearColor(1, 0, 0, 1);
    }

    @Override
    public void render() {
        //IMPORTANT: dont use streams in this call hierarchy, it causes memory leaks
        float dt = Gdx.graphics.getDeltaTime();
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        logicalRender(dt);
        physicalRender(dt);
    }

    //logical rendering, not dependent on given framerate, dependent on provided tick-rate by libgdx
    private void logicalRender(float dt) {
        //TODO check if tickrate is always 144
        activityManager.getCurrentActivity().update(dt);
    }

    //physical rendering, draw calls dependent on given framerate
    private void physicalRender(float dt) {
        waitTime += dt;
        if(1 / waitTime <= FRAMERATE) {
            //clear the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            batch.begin();
            activityManager.getCurrentActivity().draw(batch);
            batch.end();
            waitTime = 0;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        //TODO disable and reset all input when resizing
    }

    @Override
    public void dispose() {
        batch.dispose();
        activityManager.getCurrentActivity().dispose();
    }
}
