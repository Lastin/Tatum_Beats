package com.tatum.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.tatum.handlers.ContentManager;
public class LevelGenerator {
    private TiledMap map;
    //Jump data
    private int cell_side = 32;
    private int lastcell_x = 0;
    private int lastcell_y = 0;
    private int jump_limit = 2;
    private int progress_counter = 0;
    private double gravity = 40;//9.8;
    //
    private double speed = 48.0;
    private double jump_degree = 45.0;
    private ContentManager cont;
    public LevelGenerator(ContentManager cont){
        this.cont = cont;
    }
    public TiledMap makeMap(){
        map = new TiledMap();
        MapLayers layers = map.getLayers();
        layers.add(makeLayer());
        //tiledMapTileLayer();
        return map;
    }
    private TiledMapTileLayer makeLayer (){
        int cellsize = 32;
        int minh = 64;
        TiledMapTileLayer l = new TiledMapTileLayer(32*20, 32*501, 32, 32);
        Texture cell_t = cont.getTexture("crystal");
        TextureRegion cell_tr = new TextureRegion(cell_t, 0, 0, 32, 32);
        Cell cell_a = new Cell();
        StaticTiledMapTile stmt = new StaticTiledMapTile(cell_tr);
        cell_a.setTile(stmt);
        for(int i = 0; i < 500; i++){
            l.setCell(i * 32, i % 3 * 32 + minh, cell_a);
        }
        return l;
    }
    private void nextCell(TiledMapTileLayer l, int r, Cell c){
        progress_counter +=32;
        if(r == 0 && isWithinRange())
            return;

        //l.setCell(x,yc)
    }
    private boolean isWithinRange(){
        //range: [speed^2 * sin(2degree)]/grav
        //int range = (speed^2 * Math.sin(2*jump_degree))/gravity;
        //this is not correct.
        return false;
    }

}

/***********
 TODO:
 * Find a way to find all x and y that are in range of jump
 *
 ***********/