package ch.bomberman.game.util;

import com.badlogic.gdx.Input;

//TODO make this configurable
public interface KeyBindings {
    //movement
    int UP = Input.Keys.W;
    int LEFT = Input.Keys.A;
    int DOWN = Input.Keys.S;
    int RIGHT = Input.Keys.D;
    int MISC = Input.Keys.ANY_KEY;

    //menu
    int START = Input.Keys.ENTER;
}
