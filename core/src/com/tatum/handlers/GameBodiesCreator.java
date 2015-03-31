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
import com.tatum.entities.Coin;
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

    public static Bat createBat(int position, World world, ContentManager resources,String theme){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // create new body
        float x = (position) * 0.32f; // put body in the position given *32 (where that beat lies)
        float y = batY(theme); // y is dependent on theme as sprites are different sizes
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);

        //depricated as collistion dealt with in pace maker
        //left in incase we change back
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(5/100);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BAT;
        fixtureDef.filter.maskBits =  B2DVars.BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData("bat");

        //create bat and return
        Bat bat = new Bat(body, resources, theme);
        body.setUserData(bat);
        circleShape.dispose();
        return bat;
    }

    public static float batY(String theme){
        //height varies depending on theme
        //returns the height needed to have flying enemy at right position over head
        switch(theme){
            case ("pop"):
                return 0.8f;
            case ("rock"):
                return 0.8f;
            case ("indie"):
                return 0.9f;
            case ("jazz"):
                return 0.8f;
            case ("asian"):
                return 0.8f;
            case ("metal"):
                return 0.8f;
            case ("death-metal"):
                return 0.85f;
            case ("hip-hop"):
                return 0.8f;
            case ("punk"):
                return 0.8f;
            case ("classical"):
                return 0.85f;
            case ("electronic"):
                return 0.8f;
            default:
                return 0.8f;
        }
    }
    public static Slime createSlime(int position, World world, ContentManager resources,String theme){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;//create body
        float x = (position) * 0.32f; // place at beat
        float y = slimeY(theme); // get Y for given theme
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);

        //depricated collision objects
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.15f);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BAT;
        fixtureDef.filter.maskBits =  B2DVars.BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData("slime");

        // create slime and return
        Slime slime = new Slime(body, resources,theme);
        body.setUserData(slime);
        circleShape.dispose();
        return slime;
    }
    public static float slimeY(String theme){
        //get Y to allow ground enemy to be on ground level
        switch(theme){
            case ("pop"):
                return 0.5f;
            case ("rock"):
                return 0.4f;
            case ("indie"):
                return 0.4f;
            case ("jazz"):
                return 0.4f;
            case ("asian"):
                return 0.4f;
            case ("metal"):
                return 0.4f;
            case ("death-metal"):
                return 0.41f;
            case ("hip-hop"):
                return 0.43f;
            case ("punk"):
                return 0.4f;
            case ("classical"):
                return 0.5f;
            case ("electronic"):
                return 0.4f;
            default:
                return 0.8f;
        }

    }
    public static Coin createCoin(int position, World world, ContentManager resources,String Colour){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //create body
        float x = (position) * 0.32f; //place coin at beat
        float y = 0.6f; // coin always at same height
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);

        //old collision objects
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.15f);
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = B2DVars.BIT_COIN;
        fixtureDef.filter.maskBits =  B2DVars.BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(Colour+" Coin");

        //create coin and return
        Coin coin = new Coin(body, resources,Colour);
        body.setUserData(coin);
        circleShape.dispose();
        return coin;
    }
}
