package com.tatum.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tatum.entities.Bat;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator {
    private ContentManager resources;
    private Cell[] cells;
    private int cellSide = 32;

    public LevelGenerator(ContentManager resources){
        this.resources = resources;
        cells = loadCells();
    }

    public TatumMap makeMap(TrackData trackData){
        float width = ((trackData.getBeats().size()/B2DVars.PPM)*32)-(32/B2DVars.PPM);
        int height = 20;
        //set properties
        TiledMap map = new TiledMap();
        MapProperties properties = map.getProperties();
        properties.put("width", width);
        properties.put("height", height);
        properties.put("tilewidth", cellSide);
        map.getLayers().add(makeBeatsLayer(trackData));

        int[] barsPositions = getBarsPositions(trackData);

        return new TatumMap(map, barsPositions);
    }

    private TiledMapTileLayer makeBeatsLayer(TrackData trackData){
        ArrayList<TimedEvent> beats = trackData.getBeats();
        TiledMapTileLayer beats_layer = new TiledMapTileLayer(beats.size(), 20, cellSide, cellSide);
        beats_layer.setName("blocks");
        System.out.println("number of beats:" + beats.size());
        for(int i=0; i<beats.size(); i++) {
            beats_layer.setCell(i, 0, cells[0]);
        }
        return beats_layer;
    }

    private TiledMapTileLayer makeBatsLayer(int[] barPositions, TrackData trackData){
        TiledMapTileLayer makeBatsLayer = new TiledMapTileLayer(trackData.getBeats().size(), 20, 32, 32);
        for(int each : barPositions){
            makeBatsLayer.setCell(each, 2, null);
        }
        return makeBatsLayer;
    }

    private int[] getBarsPositions(TrackData trackData){
        ArrayList<TimedEvent> beats = trackData.getBeats();
        ArrayList<TimedEvent> bars = trackData.getBars();
        int[] barsPositions = new int[bars.size()-1];
        int last_bar = 0;
        for(int i=0; i< beats.size()-1; i++){
            int currentBar = beats.get(i).getContainedIn();
            if(last_bar < currentBar){
                barsPositions[last_bar] = i;
                last_bar = currentBar;
            }
        }
        return barsPositions;
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