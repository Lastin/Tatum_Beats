package com.tatum.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;

public class LevelGenerator {
    private ContentManager resources;
    private Cell[] cells;
    private int cellSide = 32;

    public LevelGenerator(ContentManager resources){
        this.resources = resources;
        cells = loadCells();
    }

    public TiledMap makeMap(TrackData trackData){
        int width = trackData.getBeats().size()*6;
        int height = 100;
        //set properties
        TiledMap map = new TiledMap();
        MapProperties properties = map.getProperties();
        properties.put("width", width);
        properties.put("height", height);
        properties.put("tilewidth", cellSide);
        map.getLayers().add(makeLayer(trackData));
        return map;
    }

    private TiledMapTileLayer makeLayer(TrackData trackData){
        ArrayList<TimedEvent> beats = trackData.getBeats();
        TiledMapTileLayer layer = new TiledMapTileLayer(beats.size(), 20, cellSide, cellSide);
        System.out.println("number of beats:" + beats.size());
        for(int i=0; i<beats.size(); i++) {
            //for(int j=0; j<3; j++) {
                layer.setCell(i, 0, cells[0]);
            //}
        }
        return layer;
    }

    private Cell[] loadCells() {
        Cell[] cells;
        Texture blocks_texture = resources.getTexture("blocks2");
        if(blocks_texture == null) {
            resources.loadTexture("res/images/blocks2.png");
            blocks_texture = resources.getTexture("blocks2");
        }
        TextureRegion[] blocks_textures = TextureRegion.split(blocks_texture, 32, 32)[0];
        cells = new Cell[blocks_textures.length];
        for(int i=0; i<cells.length; i++){
            cells[i] = new Cell();
            cells[i].setTile(new StaticTiledMapTile(blocks_textures[i]));
        }
        return cells;
    }

    public Cell[] getCells() {
        return cells;
    }
}