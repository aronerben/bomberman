package ch.bomberman.game.entity.play.player;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.entity.play.map.SpaceTile;
import ch.bomberman.game.entity.play.map.Tile;
import ch.bomberman.game.entity.play.misc.MapDrawable;
import ch.bomberman.game.entity.play.misc.PlayerCollisionResolver;
import ch.bomberman.game.util.AssetCollection;
import ch.bomberman.game.util.MapTileHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.bomberman.game.entity.play.player.Bomb.BOMB_LIMIT_ON_MAP;
import static ch.bomberman.game.util.KeyBindings.*;


public class Player extends MapDrawable {

    private float SPEED = 20;

    private final static List<Integer> MOVEMENT_KEYS = Arrays.asList(UP, LEFT, DOWN, RIGHT);

    //TODO make these fields?
    public final static int PLAYER_WIDTH = 5;
    public final static int PLAYER_HEIGHT = 5;

    private final Map map;
    private final int playerNumber;
    private IntArray pressedMovementKeys;
    private PlayerCollisionResolver collisionResolver;

    private List<Bomb> bombs = new ArrayList<>();
    private boolean bombKeyPressed = false;

    //TODO remove me
    private float waitTimeTemp = 0;

    //animation related fields
    private boolean animated;
    private int animatedMoveKey;
    private int animatedMoveDirection;
    private float animatedDistance;

    public Player(Map map, int playerNumber) {
        super(Map.STARTING_POSITIONS[playerNumber], new Texture(AssetCollection.PLAYER), PLAYER_WIDTH, PLAYER_HEIGHT);
        this.map = map;
        this.playerNumber = playerNumber;
        pressedMovementKeys = new IntArray(MOVEMENT_KEYS.size());
        //initially center player nicely on tile
        MapTileHelper.centerObjectOnTile(getObject(), PLAYER_WIDTH, PLAYER_HEIGHT);
        collisionResolver = new PlayerCollisionResolver(this.map, this);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void update(float dt) {
        handleInput(dt);
        //TODO more update components
        debug(dt);
    }

    //TODO remove debugging
    private void debug(float dt) {

        waitTimeTemp += dt;
        if(waitTimeTemp > 0.1f) {
//            System.out.println(playerTileIndex);
//            System.out.println(map.getTiles()[(int)playerTileIndex.y][(int)playerTileIndex.x].getClass().getName());

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

    //react to all possible inputs
    private void handleInput(float dt) {
        move(dt);
        placeBomb(dt);
    }

    //TODO CREATE ANIMATIONS FIRST FOR PLAYER, PACKED CREATED, USE ATLAS
    //TODO CONTINUE WITH BOMB COLLISION HANDLING AND EXPLOSIONS
    //TODO THEN DO PLAYER AND BOMB ANIMATIONS WITH SPRITESHEET, SEE WIKI FOR ANIMATION
    //TODO make animation fps dependent on speed powerups
    //TODO register new textures in textureatlas, create subfolders for assets
    private void placeBomb(float dt) {
        //disallow bomb spawn when: holding bomb key, too many bombs, or current tile is not suitable for bomb
        if(Gdx.input.isKeyPressed(BOMB) && !bombKeyPressed && bombs.size() <= BOMB_LIMIT_ON_MAP && allowBombOnCurrentTile()) {
            Bomb bomb = new Bomb(getPlayerCenterTileIndex());
            bombs.add(bomb);
            //TODO allow placing bombs on powerups?
            ((SpaceTile) getCurrentPlayerCenterTile()).setBomb(bomb);
            bombKeyPressed = true;
        } else if(!Gdx.input.isKeyPressed(BOMB)) {
            bombKeyPressed = false;
        }
    }

    private Tile getCurrentPlayerCenterTile() {
        Vector2 centerTileIndex = getPlayerCenterTileIndex();
        return map.getTiles()[(int) centerTileIndex.x][(int) centerTileIndex.y];
    }

    private boolean allowBombOnCurrentTile() {
        Tile tile = getCurrentPlayerCenterTile();
        if(tile.canContainObject()) {
            //check if space tile already has bomb
            //TODO allow placing bombs on powerups?
            return ((SpaceTile) tile).getBomb() == null;
        } else {
            return false;
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
        moveDirection(curMovementKey, deltaDistance);

        //only resolve collision if there was movement
        if(curMovementKey != MISC) {
            collisionResolver.resolveCollision(curMovementKey);
        } else if(animated) {
            animated = false;
        }

        //update tile index position
        MapTileHelper.virtualUnitsToTileIndex(getObject(), getTileIndex());
    }

    private void moveDirection(int curMovementKey, float deltaDistance) {
        if(animated && curMovementKey == animatedMoveKey) {
            moveAnimatedDirection(deltaDistance);
        } else {
            animated = false;
            mapDirectionToMovement(curMovementKey, deltaDistance);
        }
    }

    private void moveAnimatedDirection(float deltaDistance) {
        animatedDistance -= deltaDistance;
        //over animation distance => stop animation
        if(animatedDistance < 0) {
            animated = false;
            deltaDistance += animatedDistance;
        }
        mapDirectionToMovement(animatedMoveDirection, deltaDistance);
    }

    private void mapDirectionToMovement(int directionKey, float deltaDistance) {
        if(directionKey == LEFT) {
            getObject().translateX(-deltaDistance);
        } else if(directionKey == RIGHT) {
            getObject().translateX(deltaDistance);
        } else if(directionKey == UP) {
            getObject().translateY(deltaDistance);
        } else if(directionKey == DOWN) {
            getObject().translateY(-deltaDistance);
        }
    }

    private Vector2 getPlayerCenterTileIndex() {
        Vector2 tileIndex = new Vector2(getObject().getX() + PLAYER_WIDTH / 2f, getObject().getY() + PLAYER_HEIGHT / 2f);
        MapTileHelper.virtualUnitsToTileIndex(tileIndex, tileIndex);
        return tileIndex;
    }

    public void moveToTile(Vector2 tileIndex) {
        setTileIndex(tileIndex);
        Vector2 position = new Vector2();
        MapTileHelper.tileIndexToVirtualUnits(tileIndex, position);
        getObject().setPosition(position.x, position.y);
        MapTileHelper.centerObjectOnTile(getObject(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void dispose() {
        bombs.forEach(bomb -> bomb.getObject().getTexture().dispose());
        getObject().getTexture().dispose();
    }

    public void collisionAnimate(int curMovementKey, int direction, float distance) {
        animated = true;
        //key that caused the collision animation (used to check if this key is being pressed continuously)
        animatedMoveKey = curMovementKey;
        //calculated animation direction
        animatedMoveDirection = direction;
        //distance to the next row which the player is being animated to
        animatedDistance = distance;
    }
}
