package com.tatum.states;

import static com.tatum.handlers.B2DVars.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tatum.entities.Crystal;
import com.tatum.entities.HUD;
import com.tatum.entities.Player;
import com.tatum.entities.Spike;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.CollisionListener;
import com.tatum.handlers.Input;
import com.tatum.handlers.Background;
import com.tatum.handlers.BoundedCamera;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;
import com.tatum.Game;
import com.tatum.music.Section;
import com.tatum.music.TrackData;
import java.io.File;
import java.util.ArrayList;


public class Play extends GameState {

    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private CollisionListener cl;
    private BoundedCamera b2dCam;

    private Player player;
    private TrackData track;
    private TiledMap tiledMap;
    private int mapWidth;
    private int mapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private Array<Crystal> crystals;
    private Array<Spike> spikes;

    private Background[] backgrounds;
    private HUD hud;

    public static int level = 1;
    public static String song;
    private LevelGenerator generator;
    private String userName;
    private String path;

    public Play(GameStateManager gsm) {
        super(gsm);
        // set up the box2d world and contact listener
        world = new World(GRAVITY, true);
        cl = new CollisionListener();
        world.setContactListener(cl);
        //
        userName="user1"; // to be given to constructor
        path = "Music/09 Leftovers.mp3";// to be given to constructor
        loadTrackData();
        tmRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //up to this point map should be generated, otherwise might throw errors
        createPlayer();
        //createWalls();
        createCrystals();
        createSpikes();
        createBackground();
        cam.setBounds(0, mapWidth * tileSize, 0, mapHeight * tileSize);
        player.setTotalCrystals(crystals.size);
        hud = new HUD(resources, game, player);
        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
        b2dCam.setBounds(0, (mapWidth * tileSize) / PPM, 0, (mapHeight * tileSize) / PPM);
        b2dRenderer = new Box2DDebugRenderer();
        resources.getMusic(song).play();
    }

    private void composeTrackMap() {
        generator = new LevelGenerator(resources, world);
        mapWidth = (int)track.getDuration();
        mapHeight = 200;
        tileSize = 32;
        tiledMap = generator.makeMap(mapWidth, mapHeight);
        ArrayList<Section> sections = track.getSections();
        int section = 0;
        int sectionBeginning = 0;
        for(Section each : sections){
            int sectionDuration = (int)Math.round(each.getduration());
            createBlocks(section, sectionDuration, sectionBeginning, (int)each.getTempo(), mapHeight);
            section++;
            sectionBeginning += sectionDuration;
        }
    }

    private void loadTrackData() {
        //testAndroidStorage();
        try{
            //String path = "/storage/removable/sdcard1/ALarum/09 Leftovers.mp3";
            //String path = "res/music/test.mp3";
            String key = resources.makeKey(path);
            song = resources.loadMusic(path);
            track = resources.getTrackData(key);
            if(track == null){
                FileHandle fh = Gdx.files.internal(path);
                //System.out.println(fh.file().getAbsolutePath());
                track = new TrackData(path);
                track.initilize();
                resources.addTrackData(key, track);
            }
            composeTrackMap();
            //resources.addMap(tiledMap, key);
            Thread thread = new Thread(){
                public void run(){
                    //do nothing for now
                }
            };
            thread.start();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Song data not loaded.");
        }
    }

    private void testAndroidStorage(){

        File f = new File("/storage/removable/sdcard1/ALarum/09 Leftovers.mp3");
        if(f == null)
            System.out.println("File is null");
        System.out.println(f.getAbsolutePath());
    }
    private void createBackground() {
        Texture bgs = resources.getTexture("bgs");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
        backgrounds = new Background[3];
        backgrounds[0] = new Background(game, sky, cam, 0f);
        backgrounds[1] = new Background(game, clouds, cam, 0.1f);
        backgrounds[2] = new Background(game, mountains, cam, 0.2f);
    }

    private void createPlayer() {

        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        bdef.linearVelocity.set(0.6f, 0f);

        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(13 / PPM, 23/ PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1;
        fdef.friction = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED_BLOCK | B2DVars.BIT_CRYSTAL | B2DVars.BIT_SPIKE;

        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create box shape for player foot
        shape = new PolygonShape();
        shape.setAsBox(13 / PPM, 3 / PPM, new Vector2(0, -23 / PPM), 0);

        // create fixturedef for player foot
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_RED_BLOCK;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        // create new player
        player = new Player(body, resources,userName,path);
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        // i need a ratio of 0.005
        // so at 1kg, i need 200 N jump force

    }

    private void createWalls() {
        /***
        This method should be deprecated
         ***/

        // load tile map and map renderer
        try {
            tiledMap = new TmxMapLoader().load("res/maps/level" + 1 + ".tmx");

        }
        catch(Exception e) {
            System.out.println("Cannot find file: res/maps/level" + level + ".tmx");
            Gdx.app.exit();
        }

        tiledMap = generator.makeMap(500, 200);
        //generator.addLayer(tiledMap, "red", 500, 200, generator.getCells()[0]);

        mapWidth = (Integer) tiledMap.getProperties().get("width");
        mapHeight = (Integer) tiledMap.getProperties().get("height");
        tileSize = (Integer) tiledMap.getProperties().get("tilewidth");
        tmRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // read each of the "red" "green" and "blue" layers
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createBlocks(layer, B2DVars.BIT_RED_BLOCK);
        //layer = (TiledMapTileLayer) tiledMap.getLayers().get("green");
        //createBlocks(layer, B2DVars.BIT_GREEN_BLOCK);
        //layer = (TiledMapTileLayer) tiledMap.getLayers().get("blue");
        //createBlocks(layer, B2DVars.BIT_BLUE_BLOCK);

    }

    private void createBlocks(int counter, int sectionDuration, int sectionBeginning, int tempo, int mapHeight){
        int cellsSize = generator.getCells().length;
        Cell cell = generator.getCells()[counter%cellsSize];
        System.out.println(counter%cellsSize);
        TiledMapTileLayer layer = generator.makeLayer(sectionDuration, mapHeight, sectionBeginning, cell);
        tiledMap.getLayers().add(layer);
        short block = 4;
        switch(counter%cellsSize) {
            case 1: block = 8;
                    break;
            case 2: block = 16;
                    break;
        }
        generator.createBlocks(layer, block, tempo);
    }

    private void createBlocks(TiledMapTileLayer layer, short bits) {

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
                bdef.type = BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
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

    private void createCrystals() {

        // create list of crystals
        crystals = new Array<Crystal>();

        // get all crystals in "crystals" layer,
        // create bodies for each, and add them
        // to the crystals list
        MapLayer ml = tiledMap.getLayers().get("crystals");
        if(ml == null) return;

        for(MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyType.StaticBody;
            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("crystal");
            Crystal c = new Crystal(body, resources);
            body.setUserData(c);
            crystals.add(c);
            cshape.dispose();
        }
    }

    private void createSpikes() {

        spikes = new Array<Spike>();

        MapLayer ml = tiledMap.getLayers().get("spikes");
        if(ml == null) return;

        for(MapObject mo : ml.getObjects()) {
            BodyDef cdef = new BodyDef();
            cdef.type = BodyType.StaticBody;
            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;
            cdef.position.set(x, y);
            Body body = world.createBody(cdef);
            FixtureDef cfdef = new FixtureDef();
            CircleShape cshape = new CircleShape();
            cshape.setRadius(5 / PPM);
            cfdef.shape = cshape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = B2DVars.BIT_SPIKE;
            cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
            body.createFixture(cfdef).setUserData("spike");
            Spike s = new Spike(body, resources);
            body.setUserData(s);
            spikes.add(s);
            cshape.dispose();
        }

    }

    private void playerJump() {
        if(cl.playerCanJump()) {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyForceToCenter(0, 200, true);
            resources.getSound("jump").play();
        }
    }

    private void switchBlocks() {
        // get player foot mask bits
        Filter filter = player.getBody().getFixtureList().get(1).getFilterData();
        short bits = filter.maskBits;

        // switch to next block bit
        // red -> green -> blue
        if(bits == B2DVars.BIT_RED_BLOCK) {
            bits = B2DVars.BIT_GREEN_BLOCK;
           // player.getBody().setLinearVelocity(0.6f, 0f);
        }
        else if(bits == B2DVars.BIT_GREEN_BLOCK) {
            bits = B2DVars.BIT_BLUE_BLOCK;
           // player.getBody().setLinearVelocity(0.6f, 0f);
        }
        else if(bits == B2DVars.BIT_BLUE_BLOCK) {
            bits = B2DVars.BIT_RED_BLOCK;
            //player.getBody().setLinearVelocity(0.6f, 0f);
        }

        // set player foot mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);

        // set player mask bits
        bits |= B2DVars.BIT_CRYSTAL | B2DVars.BIT_SPIKE;
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(0).setFilterData(filter);

        // play sound
        resources.getSound("changeblock").play();

    }

    public void handleInput() {
        // keyboard input
        if(Input.isPressed(Input.BUTTON1)) {
            playerJump();
        }
        if(Input.isPressed(Input.BUTTON2)) {
            switchBlocks();
        }

        // mouse/touch input for android
        // left side of screen to switch blocks
        // right side of screen to jump
        if(Input.isPressed()) {
            if(Input.x < Gdx.graphics.getWidth() / 2) {
                switchBlocks();
            }
            else {
                playerJump();
            }
        }

    }

    public void update(float dt) {

        // check input
        handleInput();
        // update box2d world
        world.step(Game.STEP, 1, 1);

        // check for collected crystals
        Array<Body> bodies = cl.getBodies();
        for(int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
            player.collectCrystal();
            resources.getSound("crystal").play();
        }
        bodies.clear();

        // update player
        player.update(dt);
        if(player.managescore())
            player.scoreStep();


        //check scores / set new high score
        if(player.getScore()>player.getHighScore()){

            player.setHighScore();
            player.newHighScore();
        }

        // check player win
        if(player.getBody().getPosition().x * PPM > mapWidth * tileSize) {
            resources.getSound("levelselect").play();
            player.saveHighScore();
            resources.getMusic(song).stop();
            resources.getMusic(song).dispose();
            gsm.setState(new Menu(gsm));
        }

        // check player failed
        if(player.getBody().getPosition().y < 0) {
            //resources.getSound("hit").play();
            //gsm.setState(new Menu(gsm));
            //resources.getMusic(song).stop();
            //resources.getMusic(song).dispose();
            //player.getBody().set
            player.getBody().setTransform(new Vector2(player.getPosition().x,player.getPosition().y+(300/PPM)),0);
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x,0);
            //player.getBody().setLinearVelocity(10,0);
            player.scoreBreak();
            player.randomSprite();
        }
        if(player.getBody().getLinearVelocity().x < 0.001f) {
            //resources.getSound("hit").play();
            //gsm.setState(new Menu(gsm));
            //resources.getMusic(song).stop();
            //resources.getMusic(song).dispose();
            player.getBody().setTransform(new Vector2(player.getPosition().x,player.getPosition().y+(300/PPM)),0);
            player.getBody().setLinearVelocity(1,0);
            player.scoreBreak();
            player.randomSprite();
        }
        if(cl.isPlayerDead()) {
            resources.getSound("hit").play();
            gsm.setState(new Menu(gsm));
            resources.getMusic(song).stop();
            resources.getMusic(song).dispose();
        }

        // update crystals
        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }

        // update spikes
        for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).update(dt);
        }

    }

    public void render() {
        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + game.getWidth() / 4, game.getHeight()/3);
        cam.update();
        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].render(sb);
        }
        // draw tilemap
        tmRenderer.setView(cam);
        tmRenderer.render();
        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        // draw crystals
        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);
        }

        // draw spikes
        for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).render(sb);
        }

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);

        hud.render(sb);

        // debug draw box2d
        if(debug) {
            b2dCam.setPosition(player.getPosition().x + game.getWidth() / 4 / PPM, game.getHeight() / 2 / PPM);
            b2dCam.update();
            b2dRenderer.render(world, b2dCam.combined);
        }

    }
    public void setMap(TiledMap map){
        tiledMap = map;
    }
    public void dispose() {
        // everything is in the resource manager com.tatum.handlers.Content
    }

}