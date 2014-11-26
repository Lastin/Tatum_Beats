package com.tatum.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class CollisionListener implements ContactListener {

    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private boolean playerDead;

    public CollisionListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() != null) {
            if (fa.getUserData().equals("foot"))
                numFootContacts++;
            else if (fa.getUserData().equals("crystal"))
                bodiesToRemove.add(fa.getBody());
            else if (fa.getUserData().equals("spike"))
                playerDead = true;
        }
        else if (fb.getUserData() != null) {
            if (fb.getUserData().equals("foot"))
                numFootContacts++;
            else if (fb.getUserData().equals("crystal"))
                bodiesToRemove.add(fb.getBody());
            else if (fb.getUserData().equals("spike"))
                playerDead = true;
        }
    }

    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean playerCanJump() { return numFootContacts > 0; }
    public Array<Body> getBodies() { return bodiesToRemove; }
    public boolean isPlayerDead() { return playerDead; }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
