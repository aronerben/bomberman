package ch.bomberman.game.desktop;

import ch.bomberman.game.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(Main.NAME);
		config.setWindowedMode(WIDTH, HEIGHT);
		new Lwjgl3Application(new Main(), config);
	}
}
