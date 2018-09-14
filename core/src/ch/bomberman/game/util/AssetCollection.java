package ch.bomberman.game.util;

//asset reference collection, cant be instantiated
public class AssetCollection {
    private AssetCollection() {
    }

    //TODO use textureatlas for textures
    public static final String PLAYER = "badlogic.jpg";
    public static final String PLAY_BUTTON = "play_button.png";
    public static final String BREAKABLE_BLOCK_TILE = "breakable_block_tile.png";
    public static final String UNBREAKABLE_BLOCK_TILE = "unbreakable_block_tile.png";
    public static final String POWER_UP_TILE = "power_up_tile.png";
    public static final String SPACE_TILE = "power_up_tile.png";
    public static final String BOMB = "bomb.png";
}
