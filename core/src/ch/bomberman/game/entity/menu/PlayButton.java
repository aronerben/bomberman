package ch.bomberman.game.entity.menu;

import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static ch.bomberman.game.util.CoordinateSystemHelper.getNormalizedMousePosition;


public class PlayButton {

    private static final int START = Input.Keys.ENTER;

    private Sprite sprite;

    public PlayButton() {
        //TODO position properly (careful with camera and viewport)
        sprite = new Sprite(new Texture(AssetCollection.PLAY_BUTTON));
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean pressed() {
        //TODO maybe use an input processor later and add this as a listener to the input processor
        //TODO not twice same method call pls
        return Gdx.input.isKeyJustPressed(START) ||
                (Gdx.input.isTouched()
                        && sprite.getBoundingRectangle().contains(getNormalizedMousePosition().x, getNormalizedMousePosition().y));
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
