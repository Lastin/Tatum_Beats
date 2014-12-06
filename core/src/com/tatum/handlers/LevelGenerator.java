package com.tatum.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class LevelGenerator {
    private ContentManager resources;
    private Cell[] cells;
    private int cellSide = 32;

    public LevelGenerator(ContentManager resources){
        this.resources = resources;
        loadCells();
    }
    public TiledMap makeMap(int width, int height){
        TiledMap map = new TiledMap();
        MapProperties properties = map.getProperties();
        properties.put("width", width);
        properties.put("height", height);
        properties.put("tilewidth", cellSide);
        return map;
    }
    private MapLayer makeLayer (){
        /*int cellsize = 32;
        int minh = 64;
        TiledMapTileLayer l = new TiledMapTileLayer(32*20, 32*501, 32, 32);
        l.setName("red");
        Texture cell_t = resources.getTexture("blocks");
        TextureRegion[] cell_tr = TextureRegion.split(cell_t, 32, 32)[0];
        Cell cell_a = new Cell();
        StaticTiledMapTile stmt = new StaticTiledMapTile(cell_tr[0]);
        cell_a.setTile(stmt);
        for(int i = 0; i < 500; i++){
            l.setCell(i, i%2, cell_a);
        }
        System.out.println(l.getTileHeight());
        System.out.println(l.getTileWidth());
        System.out.println(l.getWidth());
        System.out.println(l.getHeight());
        return l;
        */
        return null;
    }

    public void addLayer(TiledMap map, String name, int width, int height, Cell cell) {
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, cellSide, cellSide);
        layer.setName(name);
        for(int i=0; i<width; i++) {
            layer.setCell(i, i%1, cell);
        }
        map.getLayers().add(layer);
    }

    private void loadCells() {
        Texture blocks_texture = resources.getTexture("blocks");
        if(blocks_texture == null) {
            resources.loadTexture("res/images/blocks.png");
            blocks_texture = resources.getTexture("blocks");
        }
        TextureRegion[] blocks_textures = TextureRegion.split(blocks_texture, 32, 32)[0];
        cells = new Cell[blocks_textures.length-1];
        for(int i=0; i<cells.length; i++){
            cells[i] = new Cell();
            cells[i].setTile(new StaticTiledMapTile(blocks_textures[i]));
        }
    }
    private boolean isWithinRange(){
        //range: [speed^2 * sin(2degree)]/grav
        //int range = (speed^2 * Math.sin(2*jump_degree))/gravity;
        //this is not correct.
        return false;
    }

    public Cell[] getCells() {
        return cells;
    }
}