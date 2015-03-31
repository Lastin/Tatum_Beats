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
import com.tatum.music.Section;
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
        resources.loadTexture("res/images/blocks3.png"); // load in the blocks used for themes
    }

    public TatumMap makeMap(TrackData trackData){
        cells = loadCells();
        //set map width to the number of beats *32 as each beat is a block and a block is 32 px
        float width = ((trackData.getBeats().size()/B2DVars.PPM)*32)-(32/B2DVars.PPM);
        int height = 20;

        //create map, set properties and make a layer for blocks
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
        //get beats
        ArrayList<TimedEvent> beats = trackData.getBeats();
        //create new layer
        TiledMapTileLayer beats_layer = new TiledMapTileLayer(beats.size(), 20, cellSide, cellSide);
        beats_layer.setName("blocks");

        int block = 0;
        //choose block according to theme
        switch (trackData.getTheme()){
            case "asian":
                block=2;
                break;
            case "classical":
                block=5;
                break;
            case "death-metal":
                block=22;
                break;
            case "electronic":
                block=16;
                break;
            case "hip-hop":
                block=23;
                break;
            case "indie":
                block=21;
                break;
            case "jazz":
                block=18;
                break;
            case "metal":
                block=15;
                break;
            case "rock":
                block=23;
                break;
            case "punk":
                block=17;
                break;
            case "pop":
                block=0;
                break;

        }
        for(int i=0; i<beats.size(); i++) { // for each of the beats, get the chosen block style and
            //add it to the cells
            beats_layer.setCell(i, 0, cells[block]);
        }
        return beats_layer;
    }
    //method deprecated
    private TiledMapTileLayer makeBatsLayer(int[] barPositions, TrackData trackData){
        TiledMapTileLayer makeBatsLayer = new TiledMapTileLayer(trackData.getBeats().size(), 20, 32, 32);
        for(int each : barPositions){
            makeBatsLayer.setCell(each, 2, null);
        }
        return makeBatsLayer;
    }

    private int[] getBarsPositions(TrackData trackData){
        //return the postion of the bars within the map
        //so that events can be placed at them
        ArrayList<TimedEvent> bars = trackData.getBars();
        ArrayList<Section> sections = trackData.getSections();
        int secondSectionStart = sections.get(1).getContains().get(0).getPosition();
        int[] barsPositions = new int[bars.size()-1-secondSectionStart]; //start from second section
        int last_bar = secondSectionStart;                              // normally where the intro finishes
        int count =0;
        for(int i=last_bar; i< bars.size()-1; i++){
           barsPositions[count] = bars.get(i).getContains().get(0).getPosition();
            count++;
        }
        return barsPositions;
    }

    private Cell[] loadCells() {
        Cell[] cells;
        Texture blocks_texture = resources.getTexture("blocks3");

        TextureRegion[] blocks_textures = TextureRegion.split(blocks_texture, 32, 32)[0];
        //get block textures and split into array which can be accessed in the make beats layer method
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