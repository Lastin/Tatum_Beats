package com.tatum.handlers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tatum.entities.Bat;
import com.tatum.entities.Slime;

import java.util.ArrayList;

public class GameBodiesCreator {
    public static void createBlocks(TiledMap map, World world){
        for(MapLayer layer : map.getLayers()){
            String name = layer.getName();
            createBlocks((TiledMapTileLayer)layer, B2DVars.BIT_GRASS_BLOCK, world);
        }
    }
    public static void createBlocks(TiledMapTileLayer layer, short filter, World world) {
        // tile size
        float tileWidth = layer.getTileWidth();
        // go through all cells in layer

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * tileWidth / 100, (row + 0.5f) * tileWidth / 100);
                //create chain using vectors
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-0.16f, -0.16f);
                v[1] = new Vector2(-0.16f, 0.16f);
                v[2] = new Vector2(0.16f, 0.16f);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.filter.categoryBits = filter;
                fd.filter.maskBits = B2DVars.BIT_PLAYER;
                world.createBody(bdef).createFixture(fd);
                cs.dispose();
            }
        }
    }

    public static Bat createBat(int position, World world, ContentManager resources){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        float x = (position * 0.5f) * 32 / 100;
        float y = (3*0.5f) * 32 / 100;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(5/100);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BAT;
        fixtureDef.filter.maskBits =  B2DVars.BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData("bat");
        Bat bat = new Bat(body, resources);
        //body.setUserData(bat);
        circleShape.dispose();
        return bat;
    }

    public static Slime createSlime(int position, World world, ContentManager resources){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        float x = position * 0.32f;
        float y = 0.4f;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.15f);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BAT;
        fixtureDef.filter.maskBits =  B2DVars.BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData("slime");
        Slime slime = new Slime(body, resources);
        body.setUserData(slime);
        circleShape.dispose();
        return slime;
    }
}
