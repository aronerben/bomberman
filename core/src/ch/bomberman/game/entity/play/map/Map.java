package ch.bomberman.game.entity.play.map;

import ch.bomberman.game.util.CoordinateSystemHelper;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static ch.bomberman.game.entity.play.map.Tile.TILE_SIZE;


public class Map {
    //TODO always square map?
    public static final int MAP_TILES = 15;
    //all possible starting positions
    public static final Vector2[] STARTING_POSITIONS = {
            new Vector2(1, 1),
            new Vector2(1, MAP_TILES - 1),
            new Vector2(MAP_TILES - 1, 1),
            new Vector2(MAP_TILES - 1, MAP_TILES - 1)};

    private Tile[][] tiles;
    private Rectangle allowedZone;

    public Map() {
        initializeMap();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Rectangle getAllowedZone() {
        return allowedZone;
    }

    //TODO create initialization with a pixel map, PixMap?
    private void initializeMap() {
        //create tile map
        //calculate map offset
        tiles = new Tile[MAP_TILES][MAP_TILES];
        for(int i = 0; i < MAP_TILES; i++) {
            for(int j = 0; j < MAP_TILES; j++) {
                Vector2 tileIndex = new Vector2(i, j);
                //borders unbreakable, every second block breakable
                if(i == 0 || i == MAP_TILES - 1 || j == 0 || j == MAP_TILES - 1) {
                    tiles[i][j] = new UnbreakableBlockTile(tileIndex);
                } else if(j % 2 == 0 && (i + 1) % 2 != 0) {
                    tiles[i][j] = new BreakableBlockTile(tileIndex);
                } else {
                    tiles[i][j] = new SpaceTile(tileIndex);
                }
            }
        }
        Vector2 zonePosition = new Vector2();
        CoordinateSystemHelper.tileIndexToVirtualUnits(new Vector2(0, 0), zonePosition);
        allowedZone = new Rectangle(zonePosition.x, zonePosition.y, MAP_TILES * TILE_SIZE, MAP_TILES * TILE_SIZE);
    }
}
