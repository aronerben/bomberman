package ch.bomberman.game.entity.play.misc;

import ch.bomberman.game.entity.play.map.Map;
import ch.bomberman.game.entity.play.map.Tile;
import ch.bomberman.game.entity.play.player.Player;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;
import static ch.bomberman.game.entity.play.player.Player.PLAYER_HEIGHT;
import static ch.bomberman.game.entity.play.player.Player.PLAYER_WIDTH;
import static ch.bomberman.game.util.KeyBindings.*;

public class PlayerCollisionResolver {

    //TODO tweak this multiplier
    /*
     * Handles how much of the player can collide with a wall and still go around it to a space tile.
     * (Provided there is a space tile around the wall tile)
     */
    private static final int OBSTACLE_LENIENCY_MULTIPLIER = 2;

    private final Player player;
    private final Map map;

    private Rectangle intersectionPlayerOffset = new Rectangle();
    private Rectangle intersectionNext = new Rectangle();

    public PlayerCollisionResolver(Map map, Player player) {
        this.map = map;
        this.player = player;
    }

    public void resolveCollision(int curMovementKey) {
        //check player somehow managed to get outside borders
        if(!map.getAllowedZone().contains(player.getBox())) {
            //move him to starting position
            player.movePlayerToTile(Map.STARTING_POSITIONS[player.getPlayerNumber()]);
        }
        resolveTileCollision(curMovementKey);
    }

    private void resolveTileCollision(int curMovementKey) {
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
        Vector2 playerTileIndex = player.getTileIndex();
        Tile tilePlayerOffset = map.getTiles()[(int) playerTileIndex.x + columnAdd][(int) playerTileIndex.y + rowAdd];
        Tile tileNext = map.getTiles()[(int) playerTileIndex.x + (columnAdd != 0 ? columnAdd : 1)][(int) playerTileIndex.y + (rowAdd != 0 ? rowAdd : 1)];

        intersectTilesWithPlayer(curMovementKey, playerTileIndex, tilePlayerOffset, tileNext, player.getBox());
    }

    private void intersectTilesWithPlayer(int curMovementKey, Vector2 playerTileIndex, Tile tilePlayerOffset, Tile tileNext, Rectangle playerBox) {
        Rectangle tilePlayerOffsetBox = tilePlayerOffset.getBox();
        Rectangle tileNextBox = tileNext.getBox();
        //use vars to ensure evaluation of both
        boolean playerIntersectsOffsetTile = Intersector.intersectRectangles(playerBox, tilePlayerOffsetBox, intersectionPlayerOffset);
        boolean playerIntersectsNextTile = Intersector.intersectRectangles(playerBox, tileNextBox, intersectionNext);

        if(!tilePlayerOffset.isTraversable() && playerIntersectsOffsetTile
                || !tileNext.isTraversable() && playerIntersectsNextTile) {

            stickPlayerToBox(curMovementKey, playerBox, tilePlayerOffsetBox);

            if(tilePlayerOffset.isTraversable() || tileNext.isTraversable()) {
                Tile spaceTile = tileNext.isTraversable() ? tileNext : tilePlayerOffset;
                Rectangle spaceIntersection = tileNext.isTraversable() ? intersectionNext : intersectionPlayerOffset;
                Rectangle collisionIntersection = tileNext.isTraversable() ? intersectionPlayerOffset : intersectionNext;

                if(OBSTACLE_LENIENCY_MULTIPLIER * spaceIntersection.area() > collisionIntersection.area()) {
                    initPlayerAnimation(curMovementKey, playerTileIndex, spaceTile, collisionIntersection);
                }
            }
        }
    }

    private void initPlayerAnimation(int curMovementKey, Vector2 playerTileIndex, Tile spaceTile, Rectangle collisionIntersection) {
        //abuse direction keys as directions
        int direction;
        float distance;
        if(curMovementKey == LEFT || curMovementKey == RIGHT) {
            direction = playerTileIndex.y == spaceTile.getTileIndex().y ? DOWN : UP;
            distance = collisionIntersection.getHeight();
        } else {
            direction = playerTileIndex.x == spaceTile.getTileIndex().x ? LEFT : RIGHT;
            distance = collisionIntersection.getWidth();
        }
        //reset intersection boxes
        intersectionNext.set(0, 0, 0, 0);
        intersectionPlayerOffset.set(0, 0, 0, 0);
        player.collisionAnimate(curMovementKey, direction, distance);
    }

    private void stickPlayerToBox(int curMovementKey, Rectangle playerBox, Rectangle tilePlayerOffsetBox) {
        //stick player to the colliding wall
        if(curMovementKey == LEFT) {
            playerBox.x = tilePlayerOffsetBox.x + TILE_SIZE;
        } else if(curMovementKey == RIGHT) {
            playerBox.x = tilePlayerOffsetBox.x - PLAYER_WIDTH;
        } else if(curMovementKey == UP) {
            playerBox.y = tilePlayerOffsetBox.y - PLAYER_HEIGHT;
        } else if(curMovementKey == DOWN) {
            playerBox.y = tilePlayerOffsetBox.y + TILE_SIZE;
        }
    }
}