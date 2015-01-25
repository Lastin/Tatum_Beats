package com.tatum.states;

import static com.tatum.handlers.B2DVars.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.tatum.entities.HUD;
import com.tatum.entities.Player;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.CollisionListener;
import com.tatum.handlers.Background;
import com.tatum.handlers.BoundedCamera;
import com.tatum.handlers.GameStateManager;
import com.tatum.Game;
import com.tatum.handlers.Input;
import com.tatum.handlers.LevelGenerator;

import java.util.logging.Level;

public class Play extends GameState {
    private boolean debug = true;
    private World world;
    private CollisionListener cl;
    //renderers
    private Box2DDebugRenderer b2dRenderer;
    private BoundedCamera b2dCam;
    private OrthogonalTiledMapRenderer tmRenderer;
    //map and properties
    private TiledMap map;
    private int height;
    private int width;
    private final int tileSide = 32;
    //rendered components
    private Player player;
    private HUD hud;
    private Background[] backgrounds;
    private Music music;
    //other settings
    private String userName = "test";
    private String path = "tempPath";
    private String[] data;

    public Play(GameStateManager gsm, TiledMap map, Music music) {
        super(gsm);
        this.map = map;
        this.music = music;
        world = new World(GRAVITY, true);
        cl = new CollisionListener();
        world.setContactListener(cl);
        MapProperties properties = map.getProperties();
        width = (Integer) properties.get("width");
        height = (Integer) properties.get("height");
        player = createPlayer();
        hud = new HUD(resources, game, player);
        backgrounds = createBackground();
        createBlocks();
        initialiseCamerasAndRenderers();
        music.play();
        data = gsm.getGame().getData();
    }

    private void initialiseCamerasAndRenderers(){
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
        b2dCam.setBounds(0, (width * tileSide) / PPM, 0, (height * tileSide) / PPM);
        b2dRenderer = new Box2DDebugRenderer();
        cam.setBounds(0, width * PPM, 0, height * tileSide);
        tmRenderer = new OrthogonalTiledMapRenderer(map);
    }

    private Player createPlayer() {
        Player player;
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        bdef.linearVelocity.set(1f, 0f);

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
        fdef.filter.maskBits = B2DVars.BIT_GRASS_BLOCK | B2DVars.BIT_CRYSTAL | B2DVars.BIT_SPIKE;

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
        fdef.filter.maskBits = B2DVars.BIT_GRASS_BLOCK;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();

        // create new player
        player = new Player(body, resources, userName, path);
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);
        return player;
        // i need a ratio of 0.005
        // so at 1kg, i need 200 N jump force
    }

    private Background[] createBackground() {
        Texture bgs = resources.getTexture("bgs");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
        Background[] backgrounds = new Background[3];
        backgrounds[0] = new Background(game, sky, cam, 0f);
        backgrounds[1] = new Background(game, clouds, cam, 0.1f);
        backgrounds[2] = new Background(game, mountains, cam, 0.2f);
        return backgrounds;
    }

    private void createBlocks(){
        //LevelGenerator.createBlocks(map, world);
    }

    @Override
    public void render() {
        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + game.getWidth() / 4, game.getHeight() / 3);
        cam.update();
        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (Background each : backgrounds) {
            each.render(sb);
        }
        // draw tiledmap
        tmRenderer.setView(cam);
        tmRenderer.render();
        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

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

    @Override
    public void update(float deltaTime){
        handleInput();
        // update box2d world
        world.step(Game.STEP, 1, 1);

        // update player
        player.update(deltaTime);
        if(player.manageScore())
            player.scoreStep();


        //check scores / set new high score
        if(player.getScore()>player.getHighScore()){
            player.setHighScore();
            player.newHighScore();
        }

        // check player failed
        if(player.getBody().getPosition().y < 0) {
            player.getBody().setTransform(new Vector2(player.getPosition().x,player.getPosition().y+(300/PPM)),0);
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x,0);
            player.scoreBreak();
            player.randomSprite();
        }
        if(player.getBody().getLinearVelocity().x < 0.001f) {
            player.getBody().setTransform(new Vector2(player.getPosition().x,player.getPosition().y+(300/PPM)),0);
            player.getBody().setLinearVelocity(1,0);
            player.scoreBreak();
            player.randomSprite();
        }
        if(cl.isPlayerDead()) {
            resources.getSound("hit").play();
            gsm.setState(new Menu(gsm));
            music.stop();
        }
    }

    @Override
    public void handleInput(){
        if(Input.isPressed(Input.BUTTON1))
            playerJump();
        if(Input.isPressed(Input.BUTTON2))
            switchBlocks();
    }

    private void playerJump(){
        if(cl.playerCanJump()){
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyForceToCenter(0, 200, true);
            resources.getSound("jump").play();
        }
    }

    private void switchBlocks(){
        //get foot mask bits
        Filter filter = player.getBody().getFixtureList().get(1).getFilterData();
        short bits = filter.maskBits;
        //switch block
        switch (bits){
            case BIT_GRASS_BLOCK:
                bits = BIT_ICE_BLOCK;
                break;
            case BIT_ICE_BLOCK:
                bits = BIT_SAND_BLOCK;
                break;
            case BIT_SAND_BLOCK:
                bits = BIT_GRASS_BLOCK;
                break;
        }
        //set foot mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);
        //set mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(0).setFilterData(filter);

        resources.getSound("changeblock").play();
    }

    @Override
    public void dispose() {

    }

}