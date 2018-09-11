package ch.bomberman.game.entity.play.player;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.util.AssetCollection;
import ch.bomberman.game.util.CoordinateSystemHelper;
import ch.bomberman.game.util.PlayerCollisionResolver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

import java.util.Arrays;
import java.util.List;

import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;
import static ch.bomberman.game.util.KeyBindings.*;


public class Player {

    float SPEED = 15;

    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    //TODO make these fields?
    public final static float PLAYER_WIDTH = 4;
    public final static float PLAYER_HEIGHT = 4;

    private final Map map;
    private final int playerNumber;
    private Rectangle playerBox;
    private Vector2 tileIndexPosition;
    private Texture texture;
    private IntArray pressedMovementKeys;
    private PlayerCollisionResolver collisionResolver;

    //TODO remove me
    private float waitTimeTemp = 0;

    public Player(Map map, int playerNumber) {
        this.map = map;
        this.playerNumber = playerNumber;
        texture = new Texture(AssetCollection.PLAYER);
        pressedMovementKeys = new IntArray(MOVEMENT_KEYS.size());
        tileIndexPosition = Map.STARTING_POSITIONS[playerNumber];
        playerBox = createNormalizedPlayerBox();
        //initially center player nicely on tile
        centerPlayerOnTile();
        collisionResolver = new PlayerCollisionResolver(this.map, this);
    }

    private Rectangle createNormalizedPlayerBox() {
        Vector2 normalizedPos = new Vector2();
        CoordinateSystemHelper.tileIndexToVirtualUnits(tileIndexPosition, normalizedPos);
        return new Rectangle(normalizedPos.x, normalizedPos.y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private void centerPlayerOnTile() {
        playerBox.setPosition(
                playerBox.x + TILE_SIZE / 2f - PLAYER_WIDTH / 2f,
                playerBox.y + TILE_SIZE / 2f - PLAYER_HEIGHT / 2f);
    }

    public Rectangle getPlayerBox() {
        return playerBox;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Vector2 getTileIndexPosition() {
        return tileIndexPosition;
    }

    public void update(float dt) {
        move(dt);
        //TODO more update components
        debug(dt);
    }

    //TODO remove debugging
    private void debug(float dt) {

        waitTimeTemp += dt;
        if(waitTimeTemp > 0.1f) {
//            System.out.println(tileIndexPosition);
//            System.out.println(map.getTiles()[(int)tileIndexPosition.y][(int)tileIndexPosition.x].getClass().getName());

//            System.out.println(
//                                    "x: " + getPlayerBox().x +
//                                    " y: " + getPlayerBox().y +
//                                    " width: " + getPlayerBox().getWidth() +
//                                    " height: " + getPlayerBox().getHeight() +
//                                            " aspect ratio: " + getPlayerBox().getAspectRatio()
//
//            );
//            Gdx.app.debug("HEAP", String.valueOf(Gdx.app.getJavaHeap()));
            waitTimeTemp = 0;
        }
    }

    /*
     * The movement keys are stored in an array, where the last pressed key has the highest index.
     * The highest index input-key dictates the position change. This allows the user to "override" their direction
     * input. Example: Pressing W to go up and pressing D before letting go of W causes the character to move right and
     * upwards movement is "overriden".
     */
    private void move(float dt) {
        float deltaDistance = dt * SPEED;

        for(int key : MOVEMENT_KEYS) {
            if(Gdx.input.isKeyPressed(key) && !pressedMovementKeys.contains(key)) {
                //add all input-keys currently being pressed & aren't already in the list
                pressedMovementKeys.add(key);
            } else if(!Gdx.input.isKeyPressed(key) && pressedMovementKeys.contains(key)) {
                //remove all input-keys not currently being pressed & are in the list
                pressedMovementKeys.removeValue(key);
            }
        }

        //the element with the highest index in the array is the latest keypress => use this input-key for movement
        int curMovementKey = pressedMovementKeys.size == 0 ? MISC : pressedMovementKeys.get(pressedMovementKeys.size - 1);

        //select direction
        if(curMovementKey == LEFT) {
            playerBox.x -= deltaDistance;
        } else if(curMovementKey == RIGHT) {
            playerBox.x += deltaDistance;
        } else if(curMovementKey == UP) {
            playerBox.y += deltaDistance;
        } else if(curMovementKey == DOWN) {
            playerBox.y -= deltaDistance;
        }

        //only resolve collision if there was movement
        if(curMovementKey != MISC) {
            collisionResolver.resolveCollision(curMovementKey, deltaDistance);
        }

        //update tile index position
        CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox, tileIndexPosition);
    }

    public void movePlayerToTile(Vector2 tileIndex) {
        tileIndexPosition = tileIndex;
        Vector2 position = new Vector2();
        CoordinateSystemHelper.tileIndexToVirtualUnits(tileIndex, position);
        playerBox.setPosition(position);
        centerPlayerOnTile();
    }


    public void dispose() {
        texture.dispose();
    }
}
