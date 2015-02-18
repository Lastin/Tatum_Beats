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
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;

public class GameBodiesCreator {
    public static void createBlocks(TiledMap map, World world){
        for(MapLayer layer : map.getLayers()){
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

    public static ArrayList<Bat> createBats(World world, ContentManager resources, TrackData trackData){
        ArrayList<Bat> bats = new ArrayList<Bat>();
        ArrayList<TimedEvent> beats = trackData.getBeats();
        for(int i = 0; i < beats.size(); i++){
            int ppm = 100;
            int blocksize = 32;
            float y = blocksize * 4 / 100;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(i, y);
            Body body = world.createBody(bdef);
            CircleShape cs = new CircleShape();
            cs.setRadius(4/ppm);
            FixtureDef fd = new FixtureDef();
            fd.shape = cs;
            fd.isSensor = true;
            fd.filter.categoryBits = B2DVars.BIT_BAT;
            fd.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(fd).setUserData("bat");
            cs.dispose();
            bats.add(new Bat(body, resources, i));
        }
        return bats;
    }
}
