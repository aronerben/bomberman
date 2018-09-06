package ch.bomberman.game.entity.play.player;

import ch.bomberman.game.util.AssetCollection;
import ch.bomberman.game.util.CoordinateSystemHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

import java.util.Arrays;
import java.util.List;


public class Player {

    private final static float SPEED = 10;
    private final static int UP = Input.Keys.W;
    private final static int LEFT = Input.Keys.A;
    private final static int DOWN = Input.Keys.S;
    private final static int RIGHT = Input.Keys.D;
    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    //TODO make these fields?
    public final static float PLAYER_WIDTH = 2;
    public final static float PLAYER_HEIGHT = 3;

    private Rectangle playerBox;
    private Texture texture;
    private IntArray pressedMovementKeys;

    //TODO make tile indexes as position?

    private float waitTimeTemp = 0;

    public Player(Vector2 startingPosition) {
        texture = new Texture(AssetCollection.PLAYER);
        pressedMovementKeys = new IntArray(MOVEMENT_KEYS.size());
        Vector2 normalizedPos = CoordinateSystemHelper.tileIndexToVirtualUnits(startingPosition.x, startingPosition.y);
        //TODO center player on tile?
        playerBox = new Rectangle(normalizedPos.x, normalizedPos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
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
        if(waitTimeTemp > 1f) {
            //TODO remove me
            System.out.println(
                                    "x: " + getPlayerBox().x +
                                    " y: " + getPlayerBox().y +
                                    " width: " + getPlayerBox().getWidth() +
                                    " height: " + getPlayerBox().getHeight() +
                                            " aspect ratio: " + getPlayerBox().getAspectRatio()

            );
            Gdx.app.debug("HEAP", String.valueOf(Gdx.app.getJavaHeap()));
            waitTimeTemp = 0;
        }
    }

    /**
     * The movement keys are stored in an array, where the last pressed key has the highest index.
     * The highest index input-key dictates the position change. This allows the user to "override" their direction
     * input. Example: Pressing W to go up and pressing D before letting go of W causes the character to move right.
     * Upwards movement is "overriden".
     */
    private void move(float dt) {
        float deltaDistance = dt * SPEED;

        for(int key : MOVEMENT_KEYS) {
            if(Gdx.input.isKeyPressed(key) && !pressedMovementKeys.contains(key)) {
                //add all input-keys currently being pressed & aren't already in the list
                pressedMovementKeys.add(key);
            } else if (!Gdx.input.isKeyPressed(key) && pressedMovementKeys.contains(key)) {
                //remove all input-keys not currently being pressed & are in the list
                pressedMovementKeys.removeValue(key);
            }
        }

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
        System.out.println("Player tile: " + CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox.x, playerBox.y));
    }

    public void dispose() {
        texture.dispose();
    }

}
