package com.tatum.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.echonest.api.v4.TimedEvent;
import com.tatum.music.Section;
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
        int width = (int)trackData.getDuration();
        int height = 100;
        //set properties
        TiledMap map = new TiledMap();
        MapProperties properties = map.getProperties();
        properties.put("width", width);
        properties.put("height", height);
        properties.put("tilewidth", cellSide);
        //now compose for each section
        ArrayList<Section> sections = trackData.getSections();
        double sectionBeginning = 0.00;
        int sectionCounter = 0;
        for(Section each : sections){
            sectionBeginning += each.getduration();
            sectionCounter++;
        }
        return map;
    }

    private TiledMapTileLayer makeLayer(int width, int height, int sectionBeginning, Cell cell) {
        TiledMapTileLayer layer = new TiledMapTileLayer(sectionBeginning+width, sectionBeginning+height, cellSide, cellSide);
        //layer.setName(name);
        int i = sectionBeginning;
        for(; i<width+sectionBeginning; i++) {
            layer.setCell(i, i%1, cell);
        }
        //map.getLayers().add(layer);
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

    public static void createBlocks(TiledMapTileLayer layer, short bits, World world) {
        // tile size
        float tileWidth = layer.getTileWidth();
        // go through all cells in layer
        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                // get cell
                Cell cell = layer.getCell(col, row);
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * tileWidth / 100, (row + 0.5f) * tileWidth / 100);
                //create chain using vectors
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-tileWidth / 2, -tileWidth / 2);
                v[1] = new Vector2(-tileWidth / 2, tileWidth / 2);
                v[2] = new Vector2(tileWidth / 2, tileWidth / 2);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;
                fd.shape = cs;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = B2DVars.BIT_PLAYER;
                world.createBody(bdef).createFixture(fd);
                cs.dispose();
            }
        }
    }

    public static void createBlocks(TiledMap map, World world) {
        MapLayers layers = map.getLayers();
        for(MapLayer layer : layers){
            MapObjects blocks = layer.getObjects();
            for(MapObject block : blocks){
                System.out.println("block");
            }
        }
    }

    public Cell[] getCells() {
        return cells;
    }
}