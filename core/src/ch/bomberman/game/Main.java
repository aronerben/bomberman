package ch.bomberman.game;

import ch.bomberman.game.entity.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
//TODO make architecture stack based
//TODO add map, check how resizing affects map, pixel measuring?
//TODO add camera, decide on a viewport
//TODO add debug
//TODO add sounds
public class Main extends ApplicationAdapter {

	public static final String NAME = "Bomberman";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private static final int FRAMERATE = 999;


	private SpriteBatch batch;
	private List<Player> mans = new ArrayList<>();
	private float waitTime;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		//TODO dynamic player add
		mans.add(new Player());

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
		updateEntities(dt);
	}

	//physical rendering, draw calls dependent on given framerate
	private void physicalRender(float dt) {
		waitTime += dt;
		if(1/waitTime <= FRAMERATE) {
			//clear the screen
			Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
			batch.begin();
			batch.draw(mans.get(0).getTexture(), (int) mans.get(0).getPosition().x, (int) mans.get(0).getPosition().y);
			batch.end();

			waitTime = 0;
		}
	}

	private void updateEntities(float dt) {
		mans.forEach(player -> player.update(dt));
		//TODO more entities
	}

	@Override
	public void resize(int width, int height) {
		System.out.println(width);
		System.out.println(height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		mans.forEach(Player::dispose);
		//TODO more entities
	}
}
