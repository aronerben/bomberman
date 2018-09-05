package ch.bomberman.game.entity.menu;

import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class PlayButton {

    private static final int START = Input.Keys.ENTER;

    public final static float BUTTON_WIDTH = 25;
    public final static float BUTTON_HEIGHT = 25;

    private Sprite sprite;

    public PlayButton() {
        //TODO position properly (careful with camera and viewport)
        sprite = new Sprite(new Texture(AssetCollection.PLAY_BUTTON));
        sprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        sprite.setOriginCenter();
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
