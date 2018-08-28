package ch.bomberman.game.entity.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Player {

    private final static float SPEED = 100;
    private final static int UP = Input.Keys.W;
    private final static int LEFT = Input.Keys.A;
    private final static int DOWN = Input.Keys.S;
    private final static int RIGHT = Input.Keys.D;
    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    private List<Integer> pressedMovementKeys = new ArrayList<>();
    private Vector2 position;
    private Texture texture;


    public Player() {
        position = new Vector2(0,0);
        texture = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void update(float dt) {
        move(dt);
        //TODO more update components
    }

    /**
     * The movement keys are stored in an array, where the last pressed key has the highest index.
     * The highest index input-key dictates the position change. This allows the user to "override" their direction
     * input. Example: Pressing W to go up and pressing D before letting go of W causes the character to move right.
     * Upwards movement is "overriden".
     */
    private void move(float dt) {
        double deltaDistance = dt * SPEED;

        //add all input-keys currently being pressed & aren't already in the list
        MOVEMENT_KEYS.stream()
                .filter(k -> Gdx.input.isKeyPressed(k) && !pressedMovementKeys.contains(k))
                .forEach(k -> pressedMovementKeys.add(k));

        //remove all input-keys not currently being pressed & are in the list
        MOVEMENT_KEYS.stream()
                .filter(k -> !Gdx.input.isKeyPressed(k) && pressedMovementKeys.contains(k))
                .forEach(k -> pressedMovementKeys.remove(k));

        //the element with the highest index in the array is the latest keypress => use this input-key for movement
        int curMovementKey = pressedMovementKeys.isEmpty() ? Input.Keys.ANY_KEY : pressedMovementKeys.get(pressedMovementKeys.size() - 1);

        //select direction
        if (curMovementKey == LEFT) {
            position.x -= deltaDistance;
        } else if (curMovementKey == RIGHT) {
            position.x += deltaDistance;
        } else if (curMovementKey == UP) {
            position.y += deltaDistance;
        } else if (curMovementKey == DOWN) {
            position.y -= deltaDistance;
        }
    }

    public void dispose() {
        texture.dispose();
    }

}
