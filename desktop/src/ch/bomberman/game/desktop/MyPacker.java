package ch.bomberman.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main(String[] args) throws Exception {
        TexturePacker.process("core/assets", "core/packed", "packed-images");
    }
}