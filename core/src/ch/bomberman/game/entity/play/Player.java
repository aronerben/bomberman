package ch.bomberman.game.entity.play;

import ch.bomberman.game.util.AssetCollection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;

import java.util.Arrays;
import java.util.List;


public class Player {

    private final static float SPEED = 100;
    private final static int UP = Input.Keys.W;
    private final static int LEFT = Input.Keys.A;
    private final static int DOWN = Input.Keys.S;
    private final static int RIGHT = Input.Keys.D;
    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    private Rectangle playerBox;
    private Texture texture;
    private IntArray pressedMovementKeys;

    private float waitTimeTemp = 0;

    public Player() {
        texture = new Texture(AssetCollection.PLAYER);
        playerBox = new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
        pressedMovementKeys = new IntArray(MOVEMENT_KEYS.size());
    }

    public Rectangle getPlayerBox() {
        return playerBox;
    }

    public Texture getTexture() {
        return texture;
    }

    public void update(float dt) {
        move(dt);
        //TODO more update components

        //TODO remove debugging
        waitTimeTemp += dt;
        if(waitTimeTemp > 0.1f) {
            System.out.println(
                                    "x: " + getPlayerBox().x +
                                    " y: " + getPlayerBox().y +
                                    " width: " + getPlayerBox().getWidth() +
                                    " height: " + getPlayerBox().getHeight() +
                                            " aspect ratio: " + getPlayerBox().getAspectRatio()

            );
        }
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
                .forEach(k -> pressedMovementKeys.removeValue(k));

        //the element with the highest index in the array is the latest keypress => use this input-key for movement
        int curMovementKey = pressedMovementKeys.size == 0 ? Input.Keys.ANY_KEY : pressedMovementKeys.get(pressedMovementKeys.size - 1);

        //select direction
        if (curMovementKey == LEFT) {
            playerBox.x -= deltaDistance;
        } else if (curMovementKey == RIGHT) {
            playerBox.x += deltaDistance;
        } else if (curMovementKey == UP) {
            playerBox.y += deltaDistance;
        } else if (curMovementKey == DOWN) {
            playerBox.y -= deltaDistance;
        }
    }

    public void dispose() {
        texture.dispose();
    }

}
