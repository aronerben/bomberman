package ch.bomberman.game.entity.menu;

import ch.bomberman.game.Main;
import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static ch.bomberman.game.util.KeyBindings.START;


public class PlayButton {

    private final static int BUTTON_WIDTH = 13;
    private final static int BUTTON_HEIGHT = 16;

    private Sprite sprite;

    public PlayButton() {
        sprite = new Sprite(new Texture(AssetCollection.PLAY_BUTTON), BUTTON_WIDTH, BUTTON_HEIGHT);
        sprite.setOriginBasedPosition(Main.VIRTUAL_WIDTH / 2, Main.VIRTUAL_HEIGHT / 2);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean pressed() {
        return Gdx.input.isKeyJustPressed(START);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
