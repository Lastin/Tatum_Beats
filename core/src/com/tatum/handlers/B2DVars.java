package com.tatum.handlers;

import com.badlogic.gdx.math.Vector2;

public class B2DVars {
    //Static variables used throughout the project  -  Kept trach of here to make sure they are always the same
    // pixels per meter
    public static final float PPM = 100;
    public static final Vector2 GRAVITY = new Vector2(0, -7f);
    // collision bit filters
    public static final short BIT_PLAYER = 2;
    public static final short BIT_GRASS_BLOCK = 4;
    public static final short BIT_ICE_BLOCK = 8;
    public static final short BIT_SAND_BLOCK = 16;
    public static final short BIT_BAT = 32;
    public static final short BIT_SLIME = 64;
    public static final short BIT_COIN = 128;

    public static final short BIT_TOP_BLOCK = 2;
    public static final short BIT_BOTTOM_BLOCK = 4;
    public static final short BIT_TOP_PLATFORM = 8;
    public static final short BIT_BOTTOM_PLATFORM = 16;

    public static enum Filters{
        PLAYER((short)1),
        GRASS((short)2),
        SAND((short)3),
        ICE((short)4),
        CRYSTAL((short)5),
        SPIKE((short)6),
        BLOCK_TOP((short)7),
        BLOCK_BOTTOM((short)8),
        PLATFORM_TOP((short)9),
        PLATFORM_BOTTOM((short)10);
        private final short bits;
        private Filters(short bits){
            this.bits = bits;
        }
    }
}