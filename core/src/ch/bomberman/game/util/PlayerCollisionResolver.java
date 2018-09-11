package ch.bomberman.game.util;

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

    private final Player player;
    private final Map map;

    private Rectangle intersectionPlayerOffset = new Rectangle();
    private Rectangle intersectionNext = new Rectangle();

    public PlayerCollisionResolver(Map map, Player player) {
        this.map = map;
        this.player = player;
    }

    public void resolveCollision(int curMovementKey, float deltaDistance) {
        //check player somehow managed to get outside borders
        if(!map.getAllowedZone().contains(player.getPlayerBox())) {
            //move him to starting position
            player.movePlayerToTile(Map.STARTING_POSITIONS[player.getPlayerNumber()]);
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
        Vector2 tileIndexPosition = player.getTileIndexPosition();
        Rectangle playerBox = player.getPlayerBox();
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
}
