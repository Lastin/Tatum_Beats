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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.tatum.handlers.B2DVars.PPM;

public class LevelGenerator {
    private ContentManager resources;
    private Cell[] cells;
    private int cellSide = 32;
    private World world;

    public LevelGenerator(ContentManager resources, World world){
        this.resources = resources;
        this.world = world;
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

    public TiledMapTileLayer makeLayer(int width, int height, int sectionBeginning, Cell cell) {
        TiledMapTileLayer layer = new TiledMapTileLayer(sectionBeginning+width, sectionBeginning+height, cellSide, cellSide);
        //layer.setName(name);
        int i = sectionBeginning;
        for(; i<width+sectionBeginning; i++) {
            layer.setCell(i, i%1, cell);
        }
        //map.getLayers().add(layer);
        return layer;
    }

    private void loadCells() {
        Texture blocks_texture = resources.getTexture("blocks");
        if(blocks_texture == null) {
            resources.loadTexture("res/images/blocks.png");
            blocks_texture = resources.getTexture("blocks");
        }
        TextureRegion[] blocks_textures = TextureRegion.split(blocks_texture, 32, 32)[0];
        cells = new Cell[blocks_textures.length];
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

    private enum TileTypes{
        RED(4), GREEN(8), BLUE(16);
        private short value;
        private TileTypes(int value){
            this.value = (short)value;
        }
        public short getValue(){
            return value;
        }
    }

    public void createBlocks(TiledMapTileLayer layer, short bits, int tempo) {
        // tile size
        float ts = layer.getTileWidth();

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
                bdef.position.set((col + 0.5f) * ts / 100, (row + 0.5f) * ts / 100);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / tempo, -ts / 2 / tempo);
                v[1] = new Vector2(-ts / 2 / tempo, ts / 2 / tempo);
                v[2] = new Vector2(ts / 2 / tempo, ts / 2 / tempo);
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

    public Cell[] getCells() {
        return cells;
    }
}