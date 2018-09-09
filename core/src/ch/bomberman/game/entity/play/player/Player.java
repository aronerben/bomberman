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
    public final static float PLAYER_WIDTH = 4;
    public final static float PLAYER_HEIGHT = 4;

    private final Map map;
    private final int playerNumber;
    private Rectangle playerBox;
    private Vector2 tileIndexPosition;
    private Texture texture;
    private IntArray pressedMovementKeys;

    private Rectangle intersectionPlayerOffset = new Rectangle();
    private Rectangle intersectionNext = new Rectangle();

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

        //only resolve collision if there was movement
        if(curMovementKey != Input.Keys.ANY_KEY) {
            resolveCollision(curMovementKey, deltaDistance);
        }

        //update tile index position
        CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox, tileIndexPosition);
    }

    private void resolveCollision(int curMovementKey, float deltaDistance) {
        //check player somehow managed to get outside borders
        if(!map.getAllowedZone().contains(playerBox)) {
            //move him to starting position
            movePlayerToTile(Map.STARTING_POSITIONS[playerNumber]);
        }
        resolveTileCollision(curMovementKey);
    }

    //TODO create collision resolution class
    private void resolveTileCollision(int curMovementKey) {
        //get movement direction moves and only check collision for the 3 tiles the player might touch
        int rowAdd = 0;
        int columnAdd = 0;

        //encode relevant tile selection
        if(curMovementKey == LEFT) {
            columnAdd = -1;
        } else if(curMovementKey == RIGHT) {
            columnAdd = 1;
        } else if(curMovementKey == UP) {
            rowAdd = 1;
        } else if(curMovementKey == DOWN) {
            rowAdd = -1;
        }

        /*
         * Since the player is smaller than a tile (width and height) he can only collide with two tiles at once.
         * The players coordinates are in the corner of the sprite, so only the tile in the same row OR same column AND the
         * tile in the row above OR column above are relevant for collision detection.
         */
        Tile tilePlayerOffset = map.getTiles()[(int) tileIndexPosition.x + columnAdd][(int) tileIndexPosition.y + rowAdd];
        Tile tileNext = map.getTiles()[(int) tileIndexPosition.x + (columnAdd != 0 ? columnAdd : 1)][(int) tileIndexPosition.y + (rowAdd != 0 ? rowAdd : 1)];

        Rectangle tilePlayerOffsetBox = tilePlayerOffset.getTileBox();
        Rectangle tileNextBox = tileNext.getTileBox();

        if(tilePlayerOffset.isWall() && Intersector.intersectRectangles(playerBox, tilePlayerOffsetBox, intersectionPlayerOffset)
                || tileNext.isWall() && Intersector.intersectRectangles(playerBox, tileNextBox, intersectionNext)) {
            if(curMovementKey == LEFT) {
                playerBox.x = tilePlayerOffsetBox.x + TILE_SIZE;
            } else if(curMovementKey == RIGHT) {
                playerBox.x = tilePlayerOffsetBox.x - PLAYER_WIDTH;
            } else if(curMovementKey == UP) {
                playerBox.y = tilePlayerOffsetBox.y - PLAYER_HEIGHT;
            } else if(curMovementKey == DOWN) {
                playerBox.y = tilePlayerOffsetBox.y + TILE_SIZE;
            }
            //TODO CONTINUE HERE better calculation when to autowalk player (maybe intersection x/y * 1000 and THEN area? or just decide by case with height etc of intersections
            if(!tilePlayerOffset.isWall() || !tileNext.isWall()) {
                //TODO rewrite this
                Tile spaceTile = !tileNext.isWall() ? tileNext : tilePlayerOffset;
                Tile collisionTile = !tileNext.isWall() ? tilePlayerOffset : tileNext;
                Rectangle spaceTileBox = spaceTile.getTileBox();
                Rectangle collisionTileBox = collisionTile.getTileBox();
                if(Vector2.dst(playerBox.x, playerBox.y, spaceTileBox.getX(), spaceTileBox.getY())
                        < Vector2.dst(playerBox.x, playerBox.y, collisionTileBox.getX(), collisionTileBox.getY()))
                    System.out.println(spaceTile.getTileIndex());
            }
        }

//        for(int x = -1; x < 2; x++) {
//            Tile tile = map.getTiles()[(int) tileIndexPosition.x + (isColumn ? index : x)][(int) tileIndexPosition.y + (isColumn ? x : index)];
//            Rectangle tileBox = tile.getTileBox();
//            if(Intersector.intersectRectangles(playerBox, tileBox, intersection)) {
//
//                if(!tile.isWall()) {
//                    System.out.println("air");
//                }
//
//
//                //collision with non-traversable tile detected => stick player to colliding tile
//                if(tile.isWall()) {
//                    if(curMovementKey == LEFT) {
//                        playerBox.x = tileBox.x + TILE_SIZE;
//                    } else if(curMovementKey == RIGHT) {
//                        playerBox.x = tileBox.x - PLAYER_WIDTH;
//                    } else if(curMovementKey == UP) {
//                        playerBox.y = tileBox.y - PLAYER_HEIGHT;
//                    } else if(curMovementKey == DOWN) {
//                        playerBox.y = tileBox.y + TILE_SIZE;
//                    }
//                }
//
////                if(intersection.x + intersection.width < playerBox.x + playerBox.width) {
////                    playerBox.x = tileBox.x + TILE_SIZE;
////                    System.out.println(CoordinateSystemHelper.virtualUnitsToTileIndex(playerBox));
////                }
////                if(intersection.x > playerBox.x) {
////                    System.out.println("test");
////                    playerBox.x = tileBox.x - PLAYER_WIDTH;
////                }
////                if(intersection.y > playerBox.y) {
////                    playerBox.y = tileBox.y - PLAYER_HEIGHT;
////                }
////                if(intersection.y + intersection.height < playerBox.y + playerBox.height) {
////                    playerBox.y = tileBox.y + TILE_SIZE;
////                }
//            }
//        }
    }

    private void movePlayerToTile(Vector2 tileIndex) {
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
