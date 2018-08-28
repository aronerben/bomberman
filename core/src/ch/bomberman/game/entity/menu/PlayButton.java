package ch.bomberman.game.entity.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayButton {

    private static final int START = Input.Keys.ENTER;

    private Sprite sprite;

    public PlayButton() {
        Texture texture = new Texture(Gdx.files.internal("play_button.png"));
        //TODO position properly (careful with camera and viewport)
        sprite = new Sprite(texture);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean pressed() {
        //TODO maybe use an input processor later and add this as a listener to the input processor
        return Gdx.input.isKeyJustPressed(START) ||
                (Gdx.input.isTouched()
                        && sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.input.getY()));
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
