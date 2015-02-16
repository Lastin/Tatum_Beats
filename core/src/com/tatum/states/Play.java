package com.tatum.states;

import static com.tatum.handlers.B2DVars.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.tatum.entities.HUD;
import com.tatum.entities.Player;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.CollisionListener;
import com.tatum.handlers.Background;
import com.tatum.handlers.BoundedCamera;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameBodiesCreator;
import com.tatum.handlers.GameStateManager;
import com.tatum.Game;
import com.tatum.handlers.Input;
import com.tatum.handlers.PaceMaker;
import com.tatum.music.MusicItem;

public class Play extends GameState {
    private boolean debug = false;
    private World world;
    private CollisionListener cl;
    //renderers
    private Box2DDebugRenderer b2dRenderer;
    private BoundedCamera b2dCam;
    private OrthogonalTiledMapRenderer mapRenderer;
    //map and properties
    private TiledMap map;
    private int height;
    private float width;
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
    private float shaderVal = 0.1f;
    private float walkCheck = 32/PPM;

    //buttons
    MusicItem backButton;

    //paceMaker
    private final PaceMaker paceMaker;
    private float delay = 0.2f;
    private long startTime;

    //shake stuff
    private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;
    private float Zthreshold = 3;
    private float Ythreshold = 1.5f;
    private float interval = 10;
    private boolean yResetLeft = true;
    private boolean yResetRight = true;


    public Play(GameStateManager gsm, TiledMap map, Music music, PaceMaker paceMaker) {
        super(gsm);
        this.map = map;
        this.music = music;
        this.paceMaker = paceMaker;
        world = new World(GRAVITY, true);
        cl = new CollisionListener();
        world.setContactListener(cl);
        MapProperties properties = map.getProperties();
        width = (Float) properties.get("width");
        height = (Integer) properties.get("height");
        player = createPlayer();
        hud = new HUD(resources, game, player,paceMaker);
        hud.setPaceMaker(paceMaker);
        backgrounds = createBackground();
        GameBodiesCreator.createBlocks(map, world);
        initialiseCamerasAndRenderers();
        music.play();
        startTime= System.nanoTime();
        data = gsm.getGame().getData();
        backButton = new MusicItem(sb, FontGenerator.listFont,"Menu",cam,5,game.height-10);
    }

    private void initialiseCamerasAndRenderers(){
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
        b2dCam.setBounds(0, (width * tileSide) / PPM, 0, (height * tileSide) / PPM);
        b2dRenderer = new Box2DDebugRenderer();
        cam.setBounds(0, width * PPM, 0, height * tileSide);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    private Player createPlayer() {
        Player player;
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;
        //bdef.linearVelocity.set(1f, 0f);

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
        player = new Player(body, resources, userName, path,paceMaker);
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

    @Override
    public void render() {
        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + game.getWidth() / 4, game.getHeight() / 3);
        cam.update();
        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        sb.setColor(1f, 1f, 1f, 1f);
        if(!paceMaker.getNewBeat()&&paceMaker.hitSecondSection())
           sb.setColor(0.5F, 0.5F, 0.5F, 1F);
       for (Background each : backgrounds) {
            each.render(sb);
        }
        // draw tiledmap
        mapRenderer.setView(cam);
        mapRenderer.render();
        sb.setColor(1f, 1f, 1f, shaderVal);

        if(paceMaker.gotFirstBeat()) {

            // draw player
            sb.setProjectionMatrix(cam.combined);
            player.render(sb);

            // draw hud
            sb.setProjectionMatrix(hudCam.combined);
            hud.render(sb);
            if(shaderVal<1f){
                shaderVal+=0.05f;
                if(shaderVal>1f)
                    shaderVal=1f;
            }
        }
        // debug draw box2d
        if(debug) {
            b2dCam.setPosition(player.getPosition().x + game.getWidth() / 4 / PPM, game.getHeight() / 2 / PPM);
            b2dCam.update();
            b2dRenderer.render(world, b2dCam.combined);
        }

        backButton.render();
    }

    float previousPosition  = 0;
    float deltaPos = 0, deltaPosPrev =  0, deltaDiff = 0;
    float total = 0;
    @Override
    public void update(float deltaTime){
        handleInput();
        float currPosition = music.getPosition();
        deltaPos = currPosition - previousPosition;
        previousPosition = currPosition;
        deltaDiff = deltaPos - deltaPosPrev;
        deltaPosPrev = deltaPos;
       // System.out.println("Delta diff: " + deltaDiff);

        //System.out.println(System.nanoTime());
        // update box2d world
        world.step(Game.STEP, 1, 1);
        // update player
        player.update(deltaTime);
        //set speed
        updateVelocity(deltaTime);

        if(player.manageScore(music.getPosition()))
            player.scoreStep();


        //check if level is finished
        if(player.getBody().getPosition().x>=width){
            if(player.getHighScore()>player.loadHighScore())
                player.saveHighScore();
            gsm.setState(new Menu(gsm));
        }

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
        //if(player.getBody().getLinearVelocity().x < 0.001f) {
        //    player.getBody().setTransform(new Vector2(player.getPosition().x,player.getPosition().y+(300/PPM)),0);
            //player.getBody().setLinearVelocity(1,0);
        //    player.scoreBreak();
        //    player.randomSprite();
        //}
        if(cl.isPlayerDead()) {
            resources.getSound("hit").play();
            gsm.setState(new Menu(gsm));
            music.stop();
        }
        if(player.getBody().getPosition().x>walkCheck){
            //resources.getSound("crystal").play();
            walkCheck+=(32/PPM);
        }
        checkMotion();

    }



    private void updateVelocity(float deltaTime){
        double currTime = (System.nanoTime()-startTime);
        //System.out.println("Curr time:" + currTime);
        //System.out.println(currTime/1000000000);
        //System.out.println("Music: "+ music.getPosition());
        //if(time >= delay){
        paceMaker.updateVelocity(player, music.getPosition());
         // paceMaker.updateVelocity(player, currTime/1000000000);

        //}
    }

    @Override
    public void handleInput(){
        backButton.update(0);

            if(backButton.isClickedPlay()){
            System.out.println("Clicked");
            sb.setColor(1f, 1f, 1f, 1f);
            music.stop();
            gsm.setState(new Menu(gsm));
            return;
        }
        if(Input.isPressed(Input.BUTTON1))
            playerJump();
        if(Input.isPressed(Input.BUTTON2))
            switchBlocks();
        if(Input.isPressed()) {
      if (Input.x < Gdx.graphics.getWidth() / 2)
                switchBlocks();
            else
                playerJump();
       }
    }

    private void playerJump(){
        if(cl.playerCanJump()){
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyForceToCenter(0, 200, true);
            //resources.getSound("jump").play();
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
    public void checkMotion(){
        now = Long.parseLong(data[0]);

        x = Float.parseFloat(data[1]);
        y = Float.parseFloat(data[2]);
        z = Float.parseFloat(data[3]);


        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;


        } else {
            timeDiff = now - lastUpdate;

            if (timeDiff > 0) {

                float Zforce = Math.abs(z - lastZ);
                if (now - lastShake >= interval ) {
                    if (Float.compare(Zforce, Zthreshold) >0) {

                        playerJump();
                        lastX = x;
                        lastY = y;
                        lastZ = z;
                        lastUpdate = now;
                        lastShake = now;
                        return;
                    }
                    if(yResetLeft) {
                         if ((y<0)&&(y < lastY)) {
                            if (!(y + Ythreshold > lastY)) {
                                resources.getSound("jump").play();
                                yResetLeft=false;
                                lastX = x;
                                lastY = y;
                                lastZ = z;
                                lastUpdate = now;
                                lastShake = now;
                                return;
                            }
                        }
                    }
                    else if(!yResetLeft)
                        if(y>0)
                            yResetLeft = true;

                    if(yResetRight){
                        if ((y>0)&&(y > lastY)) {
                            if (!(lastY + Ythreshold > y)) {
                                resources.getSound("crystal").play();
                                yResetRight = false;
                                lastX = x;
                                lastY = y;
                                lastZ = z;
                                lastUpdate = now;
                                lastShake = now;
                                return;
                            }
                        }
                    }
                    else if(!yResetRight)
                        if(y<0)
                            yResetRight = true;
                }



                lastShake = now;
            }
            lastX = x;
            lastY = y;
            lastZ = z;
            lastUpdate = now;
        }
    }
}