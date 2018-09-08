package ch.bomberman.game.entity.play.player;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.entity.play.map.Tile;
import ch.bomberman.game.util.AssetCollection;
import ch.bomberman.game.util.CoordinateSystemHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

import java.util.Arrays;
import java.util.List;

import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;


public class Player {

    private final static float SPEED = 15;
    private final static int UP = Input.Keys.W;
    private final static int LEFT = Input.Keys.A;
    private final static int DOWN = Input.Keys.S;
    private final static int RIGHT = Input.Keys.D;
    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    //TODO make these fields?
    public final static float PLAYER_WIDTH = 3;
    public final static float PLAYER_HEIGHT = 4;

    private final Map map;
    private final int playerNumber;
    private Rectangle playerBox;
    private Vector2 tileIndexPosition;
    private Texture texture;
    private IntArray pressedMovementKeys;
    private Rectangle intersection = new Rectangle();

    //TODO make tile indexes as position?

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
    }

    private Rectangle createNormalizedPlayerBox() {
        Vector2 virtualPosition = CoordinateSystemHelper.tileIndexToVirtualUnits(tileIndexPosition);
        return new Rectangle(virtualPosition.x, virtualPosition.y, PLAYER_WIDTH, PLAYER_HEIGHT);
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
            Gdx.app.debug("HEAP", String.valueOf(Gdx.app.getJavaHeap()));
            waitTimeTemp = 0;
        }
    }

    /**
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
        int curMovementKey = pressedMovementKeys.size == 0 ? Input.Keys.ANY_KEY : pressedMovementKeys.get(pressedMovementKeys.size - 1);

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

        resolveCollision(curMovementKey, deltaDistance);

        //update tile index position
        tileIndexPosition = CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox);
    }

    private void resolveCollision(int curMovementKey, float deltaDistance) {
        //check player somehow managed to get outside borders
        if(!map.getAllowedZone().contains(playerBox)) {
            //move him to starting position
            movePlayerToTile(Map.STARTING_POSITIONS[playerNumber]);
        }

        //goes through all tiles surrounding the player and detects collision with each
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Tile tile = map.getTiles()[(int) tileIndexPosition.x + i][(int) tileIndexPosition.y + j];
                Rectangle tileBox = tile.getTileBox();
                if(!tile.isTraversable() && Intersector.intersectRectangles(playerBox, tileBox, intersection)) {
                    //TODO [aer] CONTINUE HERE: better collision handling way (create method for it):
                    /*
                     * check if one of 2 tiles next to collision tile is a space tile by checking movement direction.
                     * check which tile to autowalk to by checking intersection height (find good height) and compare
                     * player position with colliding tile (prob need to store tileindex of collided tile).
                     * autowalk to the row if conditions are met
                     *
                     */
                    //collision with non-traversable tile detected => stick player to colliding tile
//                    if(curMovementKey == LEFT) {
//                        playerBox.x = tileBox.x + TILE_SIZE;
//                    } else if(curMovementKey == RIGHT) {
//                        playerBox.x = tileBox.x - PLAYER_WIDTH;
//                    } else if(curMovementKey == UP) {
//                        playerBox.y = tileBox.y - PLAYER_HEIGHT;
//                    } else if(curMovementKey == DOWN) {
//                        playerBox.y = tileBox.y + TILE_SIZE;
//                    }

                    if(intersection.x + intersection.width < playerBox.x + playerBox.width) {
                        playerBox.x = tileBox.x + TILE_SIZE;
                        System.out.println(CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox));
                    }
                    if(intersection.x > playerBox.x) {
                        playerBox.x = tileBox.x - PLAYER_WIDTH;
                    }
                    if(intersection.y > playerBox.y) {
                        playerBox.y = tileBox.y - PLAYER_HEIGHT;
                    }
                    if(intersection.y + intersection.height < playerBox.y + playerBox.height) {
                        playerBox.y = tileBox.y + TILE_SIZE;
                    }
                }
            }
        }
    }

    private void movePlayerToTile(Vector2 tileIndex) {
        tileIndexPosition = tileIndex;
        playerBox.setPosition(CoordinateSystemHelper.tileIndexToVirtualUnits(tileIndex));
        centerPlayerOnTile();
    }

    public void dispose() {
        texture.dispose();
    }

}
