package ch.bomberman.game.entity.play.map;

import com.badlogic.gdx.math.Vector2;


public class Map {
    //TODO square map?
    public static final int MAP_TILES = 15;
    public static final Vector2[] STARTING_POSITIONS = {
            new Vector2(1, 1),
            new Vector2(1, MAP_TILES-1),
            new Vector2(MAP_TILES-1, 1),
            new Vector2(MAP_TILES-1, MAP_TILES-1)};

    private Tile[][] tiles;

    public Map() {
        tiles = initializeMap();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    //TODO create initialization with a pixel map, PixMap?
    private Tile[][] initializeMap() {
        //calculate map offset
        Tile[][] tiles = new Tile[MAP_TILES][MAP_TILES];
        for(int i = 0; i < MAP_TILES; i++) {
            for(int j = 0; j < MAP_TILES; j++) {
                Vector2 tileIndex = new Vector2(j, i);
                //borders unbreakable, every second block breakable
                if(i == 0 || i == MAP_TILES - 1 || j == 0 || j == MAP_TILES - 1) {
                    tiles[i][j] = new UnbreakableBlockTile(tileIndex);
                } else if(j % 2 == 0 && (i+1) % 2 != 0) {
                    tiles[i][j] = new BreakableBlockTile(tileIndex);
                } else {
                    tiles[i][j] = new SpaceTile(tileIndex);
                }
            }
        }
        return tiles;
    }
}
