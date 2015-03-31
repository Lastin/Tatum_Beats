package com.tatum.handlers;

public class Input {
    // this classes deals with if the user is currently touching the screen
    public static int x, y;
    public static boolean down, pdown;
    public static boolean[] keys, pkeys;
    private static final int NUM_KEYS = 2;
    public static final int BUTTON1 = 0, BUTTON2 = 1;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        pdown = down;
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static boolean isDown() { return down; }
    public static boolean isPressed() { return down && !pdown; }
    public static boolean isReleased() { return !down && pdown; }

    public static void setKey(int i, boolean b) { keys[i] = b; }
    public static boolean isDown(int i) { return keys[i]; }
    public static boolean isPressed(int i) { return keys[i] && !pkeys[i]; }

}
