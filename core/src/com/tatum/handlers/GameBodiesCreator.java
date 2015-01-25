package com.tatum.handlers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

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
                v[0] = new Vector2(-tileWidth / 2 / 100, -tileWidth / 2 / 100);
                v[1] = new Vector2(-tileWidth / 2 / 100, tileWidth / 2 / 100);
                v[2] = new Vector2(tileWidth / 2 / 100, tileWidth / 2 / 100);
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
}
