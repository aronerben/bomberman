package ch.bomberman.game.util;

import com.badlogic.gdx.Input;

//TODO make this configurable
//keybindings collection, cant be instantiated
public class KeyBindings {

    private KeyBindings() {
    }

    //movement
    public static final int UP = Input.Keys.W;
    public static final int LEFT = Input.Keys.A;
    public static final int DOWN = Input.Keys.S;
    public static final int RIGHT = Input.Keys.D;
    public static final int MISC = Input.Keys.ANY_KEY;

    //menu
    public static final int START = Input.Keys.ENTER;
}
