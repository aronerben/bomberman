package ch.bomberman.game;

import ch.bomberman.game.activity.ActivityManager;
import ch.bomberman.game.activity.MenuActivity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//TODO read extended tutorial, use Game + Screens?
//TODO add map, check how resizing affects map, pixel measuring?
//TODO make bombs poolable objects
//TODO add camera, decide on a viewport
//TODO add debug
//TODO add sounds
public class Main extends ApplicationAdapter {

	public static final String NAME = "Bomberman";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private static final int FRAMERATE = 999;

	private ActivityManager activityManager;
	private SpriteBatch batch;
	private float waitTime;
	
	@Override
	public void create() {
		//TODO fix loglevel
		Gdx.app.setLogLevel(Application.LOG_INFO);

		batch = new SpriteBatch();
		//start with menu
		activityManager = new ActivityManager();
		activityManager.setCurrentActivity(new MenuActivity(activityManager));
		//initial color clear
		Gdx.gl.glClearColor( 1, 0, 0, 1 );
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
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
		if(1/waitTime <= FRAMERATE) {
			//clear the screen
			Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
			batch.begin();
			activityManager.getCurrentActivity().draw(batch);
			batch.end();
			waitTime = 0;
			//TODO remove me
			Gdx.app.debug("HEAP", String.valueOf(Gdx.app.getJavaHeap()));
		}
	}

	@Override
	public void resize(int width, int height) {
		System.out.println(width);
		System.out.println(height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		activityManager.getCurrentActivity().dispose();
	}
}
