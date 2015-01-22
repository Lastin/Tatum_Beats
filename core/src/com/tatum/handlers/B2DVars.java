package com.tatum.handlers;

import com.badlogic.gdx.math.Vector2;

public class B2DVars {
    // pixels per meter
    public static final float PPM = 100;
    public static final Vector2 GRAVITY = new Vector2(0, -7f);
    // collision bit filters
    public static final short BIT_PLAYER = 2;
    public static final short BIT_GRASS_BLOCK = 4;
    public static final short BIT_ICE_BLOCK = 8;
    public static final short BIT_SAND_BLOCK = 16;
    public static final short BIT_CRYSTAL = 32;
    public static final short BIT_SPIKE = 64;

    public static final short BIT_TOP_BLOCK = 2;
    public static final short BIT_BOTTOM_BLOCK = 4;
    public static final short BIT_TOP_PLATFORM = 8;
    public static final short BIT_BOTTOM_PLATFORM = 16;
}