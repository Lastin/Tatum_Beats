package com.tatum.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class CollisionListener implements ContactListener {

    //this class checks if the player is currently touching the floor
    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private boolean playerDead;

    public CollisionListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    public void beginContact(Contact contact) { //called whenever there is contact between bodies in the game
        Fixture fa = contact.getFixtureA(); // get both fixtures
        Fixture fb = contact.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() != null) {
            if (fa.getUserData().equals("foot")) //check if one is the players foot, and if so, foot is touching platform
                numFootContacts++;
        }
        else if (fb.getUserData() != null) {
            if (fb.getUserData().equals("foot"))
                numFootContacts++;
        }
    }

    public void endContact(Contact contact) { //called when contact ends

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) { //check if one is the player foot, if so, no longer touching floor
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean playerCanJump() { return numFootContacts > 0; } //player can jump if foot touching floor
    public Array<Body> getBodies() { return bodiesToRemove; }
    public boolean isPlayerDead() { return playerDead; } // method from old version of game

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
