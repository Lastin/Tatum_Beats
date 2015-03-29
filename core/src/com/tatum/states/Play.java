package com.tatum.states;

import static com.tatum.handlers.B2DVars.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.echonest.api.v4.Song;
import com.tatum.entities.B2DSprite;
import com.tatum.entities.Bat;
import com.tatum.entities.Coin;
import com.tatum.entities.HUD;
import com.tatum.entities.Instructor;
import com.tatum.entities.Player;
import com.tatum.entities.Slime;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.CollisionListener;
import com.tatum.handlers.Background;
import com.tatum.handlers.BoundedCamera;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameBodiesCreator;
import com.tatum.handlers.GameStateManager;
import com.tatum.Game;
import com.tatum.handlers.Input;
import com.tatum.handlers.InputProcessor;
import com.tatum.handlers.MonsterCoinLocation;
import com.tatum.handlers.PaceMaker;
import com.tatum.handlers.TatumDirectionListener;
import com.tatum.handlers.TatumMap;
import com.tatum.music.MusicItem;
import com.tatum.music.TrackData;

import java.awt.Font;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Play extends GameState {
    private boolean debug = false;
    private World world;
    private CollisionListener cl;
    //renderers
    private Box2DDebugRenderer b2dRenderer;
    private BoundedCamera b2dCam;
    private OrthogonalTiledMapRenderer mapRenderer;
    //map and properties
    private final TatumMap tatumMap;
    private TiledMap tiledMap;
    private int height;
    private float width;
    private final int tileSide = 32;
    //rendered components
    private Player player;
    private ArrayList<B2DSprite> events = new ArrayList<B2DSprite>();
    private HUD hud;
    private Background[] backgrounds;
    private Music music;
    private Instructor instructor;
    //other settings
    private String userName = "test";
    private String path;
    private String[] data;
    private float shaderVal = 0.1f;
    private float walkCheck = 32/PPM;

    //MusicItems
    private MusicItem backButton;
    private MusicItem SongName;
    private MusicItem ArtistName;
    float titleFade = 0.1f;
    float titleTimer =0;
    private boolean rotate;
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
    private float Zthreshold = 3f;
    private float Ythreshold = 1.5f;
    private float interval = 20;
    private boolean yResetLeft = true;
    private boolean yResetRight = true;
    private float movementTimer =0;


    private int lastCoin =1;

    float previousPosition  = 0;
    float deltaPos = 0, deltaPosPrev =  0, deltaDiff = 0;
    float total = 0;
    private final TrackData trackData;
    private FontGenerator fontGenerator;
    private float sbColor = 1;
    private float lastFlashUpdate = 0;


    public Play(GameStateManager gsm, TatumMap tatumMap, Music music, PaceMaker paceMaker, String path, TrackData trackData) {
        super(gsm);
        this.tatumMap = tatumMap;
        this.tiledMap = tatumMap.getTiledMap();
        this.music = music;
        this.path = path;
        this.paceMaker = paceMaker;
        this.trackData = trackData;
        fontGenerator = new FontGenerator();
        world = new World(GRAVITY, true);
        cl = new CollisionListener();
        world.setContactListener(cl);
        MapProperties properties = tiledMap.getProperties();
        width = (Float) properties.get("width");
        height = (Integer) properties.get("height");
        player = createPlayer();
        createObstacles();
        hud = new HUD(resources, fontGenerator, game, player,paceMaker, this);
        hud.setPaceMaker(paceMaker);
        backgrounds = createBackground();
        GameBodiesCreator.createBlocks(tiledMap, world);
        initialiseCamerasAndRenderers();
        startTime= System.nanoTime();
        data = gsm.getGame().getData();
        //backButton = new MusicItem(sb, FontGenerator.listFont,"Menu",cam,5,game.height-10);
        game.setSwipeInput();
        setArtistSong();
        this.instructor = hud.getInstructor();
        music.play();
        setSongCharactaristics();
        System.out.println(trackData.getTwitterHandle());
    }

    private void setArtistSong(){

        float widthA = new MusicItem(sb,fontGenerator.makeFont(70, Color.WHITE),paceMaker.getTrackData().getArtist(),cam,0,game.getHeight()-100).getWidth();
        float widthS = new MusicItem(sb,fontGenerator.makeFont(70, Color.WHITE),paceMaker.getTrackData().getSongName(),cam,0,game.getHeight()-130).getWidth();

        float newXArtist = (320/2)-(widthA/2);
        float newXSong = (320/2)-(widthS/2);
        int size =70;
        sb.setColor(0,0,0,0);
        while(true)
            if(newXArtist<10 || newXSong < 10 || widthA>310 || widthS>310){
                size--;
                widthA = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),paceMaker.getTrackData().getArtist(),cam,0,game.getHeight()-100).getWidth();
                widthS = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),paceMaker.getTrackData().getSongName(),cam,0,game.getHeight()-130).getWidth();
                newXArtist = (320/2)-(widthA/2);
                newXSong = (320/2)-(widthS/2);
            }
            else break;

        //System.out.println("Width: "+width+" Artist Width: "+ widthA + " newX: " + newXArtist);
        //System.out.println("Width: "+width+" Song Width: "+ widthS + " newX: " + newXSong);
        ArtistName = new MusicItem(sb,fontGenerator.makeFont(size, Color.WHITE),paceMaker.getTrackData().getArtist(),cam,(int)newXArtist,game.getHeight()-130);
        SongName =  new MusicItem(sb,fontGenerator.makeFont(size, Color.WHITE),paceMaker.getTrackData().getSongName(),cam,(int)newXSong,game.getHeight()-100);
        System.out.println(ArtistName.getWidth());
    }

    private void initialiseCamerasAndRenderers(){
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
        b2dCam.setBounds(0, (width * tileSide) / PPM, 0, (height * tileSide) / PPM);
        b2dRenderer = new Box2DDebugRenderer();
        cam.setBounds(0, width * PPM, 0, height * tileSide);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void setSongCharactaristics(){

        String key = paceMaker.getTrackData().getKeyString();
        switch (key){
            case "c":
                instructor.setRotation(Instructor.Rotation.COUNTER_CLOCKWISE);
                break;
            case "c#":
                instructor.setRotation(Instructor.Rotation.COUNTER_CLOCKWISE);
                break;
            case "d":
                instructor.setRotation(Instructor.Rotation.INVERT);
                break;
            case "d#":
                instructor.setRotation(Instructor.Rotation.INVERT);
                break;
            case "e":
                instructor.setRotation(Instructor.Rotation.RANDOMISE);
                break;
            case "f":
                instructor.setRotation(Instructor.Rotation.BOTTOM);
                break;
            case "f#":
                instructor.setRotation(Instructor.Rotation.BOTTOM);
                break;
            case "g":
                instructor.setRotation(Instructor.Rotation.TOP);
                break;
            case "g#":
                instructor.setRotation(Instructor.Rotation.BOTTOM);
                break;
            case "a":
                instructor.setRotation(Instructor.Rotation.CLOCKWISE);
                break;
            case "a#":
                instructor.setRotation(Instructor.Rotation.CLOCKWISE);
                break;
            case "b":
                instructor.setRotation(Instructor.Rotation.SWAP_VERTICAL);
        }
        String mode = paceMaker.getTrackData().getModeString();
        if(mode.equals("minor"))
            rotate = false;
        else rotate = true;

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
        fdef.filter.maskBits = B2DVars.BIT_GRASS_BLOCK | B2DVars.BIT_BAT | B2DVars.BIT_SLIME;

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

    private void createObstacles(){
        int[] barsPositions = tatumMap.getBarsPositions();
        MonsterCoinLocation monsterCoinLocation = new MonsterCoinLocation(); // contains all event data for collision
        Random random = new Random();
        for(int each : barsPositions){
            float temp =random.nextFloat();
            System.out.println("Float = "+temp);
            if(temp<=0.3) {
                Slime slime = GameBodiesCreator.createSlime(each, world, resources,trackData.getTheme());
                events.add(slime);
                monsterCoinLocation.addEvent("Slime", each,slime); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
            }
            else if((temp>0.3)&&(temp<=0.6)){
                Bat bat = GameBodiesCreator.createBat(each, world, resources,trackData.getTheme());
                events.add(bat);
                monsterCoinLocation.addEvent("Bat", each,bat); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
            }

            else if((temp>0.6)&&(temp<=0.7)) {
                if (lastCoin == 2) {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Pink");
                    events.add(coin);
                    monsterCoinLocation.addEvent("PinkCoin", each, coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=3;
                } else {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Blue");
                    events.add(coin);
                    monsterCoinLocation.addEvent("BlueCoin", each, coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=2;
                }
            }
            else if((temp>0.7)&&(temp<=0.8)) {
                if (lastCoin == 3) {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Green");
                    events.add(coin);
                    monsterCoinLocation.addEvent("GreenCoin", each,coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=1;
                } else {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Pink");
                    events.add(coin);
                    monsterCoinLocation.addEvent("PinkCoin", each, coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=3;
                }
            }
            else{
                if(lastCoin==1){
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Blue");
                    events.add(coin);
                    monsterCoinLocation.addEvent("BlueCoin", each,coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=2;
                }
                else{
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Green");
                    events.add(coin);
                    monsterCoinLocation.addEvent("GreenCoin", each,coin); // will add other later "Bat" "Slime" "RedCoin" "GreenCoin" "BlueCoin"
                    lastCoin=1;
               }
            }

            //bats.add(GameBodiesCreator.createBat(each, world, resources));
        }
        paceMaker.setMonsterCoinLocation(monsterCoinLocation);
    }


    private Background[] createBackground() {
        String theme = paceMaker.getTrackData().getTheme();

        HashMap<String, TextureRegion> bgTheme = new HashMap<>();
        resources.loadTexture("res/images/backgrounds/asian.png");
        resources.loadTexture("res/images/backgrounds/classical.png");
        resources.loadTexture("res/images/backgrounds/death-metal.png");
        resources.loadTexture("res/images/backgrounds/electronic.png");
        resources.loadTexture("res/images/backgrounds/hip-hop.png");
        resources.loadTexture("res/images/backgrounds/indie.png");
        resources.loadTexture("res/images/backgrounds/jazz.png");
        resources.loadTexture("res/images/backgrounds/metal.png");
        resources.loadTexture("res/images/backgrounds/rock.png");
        resources.loadTexture("res/images/backgrounds/punk.png");
        Texture temp = resources.getTexture("asian");
        bgTheme.put("asian",new TextureRegion(temp,0,0,949,240));
        temp = resources.getTexture("classical");
        bgTheme.put("classical",new TextureRegion(temp,0,0,427,240));
        temp = resources.getTexture("death-metal");
        bgTheme.put("death-metal",new TextureRegion(temp,0,0,320,240));
        temp = resources.getTexture("electronic");
        bgTheme.put("electronic",new TextureRegion(temp,0,0,340,240));
        temp = resources.getTexture("hip-hop");
        bgTheme.put("hip-hop",new TextureRegion(temp,0,0,427,240));
        temp = resources.getTexture("indie");
        bgTheme.put("indie",new TextureRegion(temp,0,0,427,240));
        temp = resources.getTexture("jazz");
        bgTheme.put("jazz",new TextureRegion(temp,0,0,960,240));
        temp = resources.getTexture("metal");
        bgTheme.put("metal",new TextureRegion(temp,0,0,427,240));
        temp = resources.getTexture("rock");
        bgTheme.put("rock",new TextureRegion(temp,0,0,320,240));
        temp = resources.getTexture("punk");
        bgTheme.put("punk",new TextureRegion(temp,0,0,427,240));

        if((theme.equals("jazz"))||(theme.equals("asian"))){
            Background[] backgrounds = new Background[1];
            backgrounds[0] = new Background(game,bgTheme.get(theme),cam,0.0f);
            return backgrounds;
        }
        else if (theme.equals("pop")) {
            Texture bgs = resources.getTexture("GrassColour");
            TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
            TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
            TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
            Background[] backgrounds = new Background[3];
            backgrounds[0] = new Background(game, sky, cam, 0.1f);
            backgrounds[1] = new Background(game, clouds, cam, 0.15f);
            backgrounds[2] = new Background(game, mountains, cam, 0.25f);
            return backgrounds;
        }
        else{
            System.out.println(theme);
            Background[] backgrounds = new Background[1];
            backgrounds[0] = new Background(game,bgTheme.get(theme),cam,0f);
            return backgrounds;
        }
    }

    @Override
    public void render() {
        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + game.getWidth() / 4, game.getHeight() / 3);
        cam.update();
        if(rotate){
            //cam.rotate(-0.4f);

            //needs to be done better
        }
        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);

        for (Background each : backgrounds) {
            each.render(sb);
        }

        // draw tiledmap
        mapRenderer.setView(cam);
        mapRenderer.render();

        sb.setColor(sbColor, sbColor, sbColor, shaderVal);

        if(paceMaker.gotFirstBeat()) {
            // draw player
            sb.setProjectionMatrix(cam.combined);

            //draw slimes and bats
            for(int i =paceMaker.getRenderCounter()-2;i<paceMaker.getRenderCounter()+3;i++) {
                if(i<0)
                    continue;
                try {
                    if(events.get(i)instanceof Coin){
                        Coin coin = (Coin) events.get(i);
                       if(coin.doRender()){
                            coin.render(sb);
                        }
                    }
                    else
                        events.get(i).render(sb);
                }catch (IndexOutOfBoundsException e){
                    break; //end of song
                }
            }
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

        //backButton.render();
        if(music.getPosition()<5) {
            //sb.setColor(255f,0f,0f,titleFade);
            SongName.getFont().setColor(0,0,0,titleFade);
            SongName.render();
            ArtistName.getFont().setColor(0,0,0,titleFade);
            ArtistName.render();
            if(titleFade<1f&&(music.getPosition()>titleTimer+0.1)) {
                titleFade += 0.05;
                titleTimer=music.getPosition();
                if(titleFade>1f){
                    titleFade=1f;
                }
            }
        }
        else if(music.getPosition()>5 && titleFade>0){
            //  sb.setColor(255f,0f,0f,titleFade);
            SongName.getFont().setColor(0,0,0,titleFade);
            SongName.render();
            ArtistName.getFont().setColor(0,0,0,titleFade);
            ArtistName.render();
            if((music.getPosition()>titleTimer+0.1))
                titleFade -= 0.03;
        }
        else{
            //don't render
        }
    }

    @Override
    public void update(float deltaTime){
        handleInput();
        //flash
        if(paceMaker.hitSecondSection()){
            if(!paceMaker.getNewBeat() && sbColor > 0.7f) {
                sbColor -= 0.01f;
            } else {
                System.out.println("pulse");
                sbColor = 1;
            }
        }

        //end
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
            player.saveHighScore();
            //gsm.setState(new Menu(gsm));
            String trackName = trackData.getSongName();
            String artistName = trackData.getArtist();
            String album = trackData.getAlbumName();
            gsm.setState(new HighScoreView(gsm, trackName, artistName, album, player.getHighScore()));
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

        //check if need to reskin
        if(player.getIsJumping() && cl.playerCanJump() && (music.getPosition() > player.getJumpTime()+0.1) && !paceMaker.getJumping()){
            player.removeSkin();
            player.setIsJumping(false);
        }
        if(player.getIsDucking() && (paceMaker.getLastBeatHitId() >= player.getDuckBeat()+2) ){
            player.removeSkin();
            player.setIsDucking(false);
        }


        if(cl.isPlayerDead()) {
            resources.getSound("hit").play();
            gsm.setState(new Menu(gsm));
            music.stop();
        }
        if(player.getBody().getPosition().x>walkCheck){
            //resources.getSound("crystal").play();
            walkCheck+=(32/PPM);
        }

        //checkMotion2ElectricBoogaloo();

        for(int i =paceMaker.getRenderCounter()-2;i<paceMaker.getRenderCounter()+3;i++) {
            if (i < 0)
                continue;
            try {
                events.get(i).update(deltaTime);
            }catch (IndexOutOfBoundsException e){
                break;
                //end of song
            }
        }
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
        /*backButton.update(0);
        if(backButton.isClickedPlay()){
            System.out.println("Clicked");
            sb.setColor(1f, 1f, 1f, 1f);
            music.stop();
            gsm.setState(new Menu(gsm));
            return;
        }*/
        TatumDirectionListener tatumDirectionListener = game.getTatumDirectionListener();

        if(!player.getIsJumping()&&!player.getIsDucking()) {
            if (tatumDirectionListener.down()) {
                instructor.doBot();
            }
            else if (tatumDirectionListener.up()){
                instructor.doTop();
            }
            else if(tatumDirectionListener.right()) {
                instructor.doRight();
            }
            else if(tatumDirectionListener.left()) {
                instructor.doLeft();
            }
            tatumDirectionListener.resetBools();

        }
    }

    public void playerJump(){
        if(cl.playerCanJump()&&(!player.getIsJumping())&&(!player.getIsDucking())){

            //paceMaker.setJumping(true); // use for in time
            player.getBody().applyForceToCenter(0, 200, true); // use for working jump, time independent
            player.setJumpSkin();
            player.setJumpTime(music.getPosition());
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
                    //if (Float.compare(Zforce, Zthreshold) >0) {
//                    player.randomSprite();

                    //    playerJump();
                    //    lastX = x;
                    //    lastY = y;
                    //    lastZ = z;
                    //    lastUpdate = now;
                    //    lastShake = now;
                    //    return;
                    //}
                    if(yResetLeft) {
                        if ((y<0)&&(y < lastY)) {
                            if (!(y + Ythreshold > lastY)) {
                                playerJump();
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
                        if(y>0) {
                            yResetLeft = true;
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                            lastShake = now;
                            return;
                        }

                    if(yResetRight){
                        if ((y>0)&&(y > lastY)) {
                            if (!(lastY + Ythreshold > y)) {
                                player.setCrouchSkin(paceMaker.getLastBeatHitId());
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
                        if(y<0) {
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                            lastShake = now;
                            yResetRight = true;
                        }
                }



                lastShake = now;
            }
            lastX = x;
            lastY = y;
            lastZ = z;
            lastUpdate = now;
        }
    }
    public void checkMotion2ElectricBoogaloo(){
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

               /*     if(yResetRight){
                        if (y>4) {
                            playerJump();
                            yResetRight = false;
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                            lastShake = now;
                            return;
                        }
                    }
                    else if(!yResetRight)
                        if(y<0) {
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                            lastShake = now;
                            yResetRight = true;
                        }
                        */
                    if (Float.compare(Zforce, Zthreshold) >0) {
                        if(paceMaker.canShake()) {
                            player.randomSprite();
                            paceMaker.cantShake();
                        }
                        lastX = x;
                        lastY = y;
                        lastZ = z;
                        lastUpdate = now;
                        lastShake = now;
                        return;
                    }
                /*    if(yResetLeft) {
                            if (y<-4) {
                                player.setCrouchSkin(paceMaker.getLastBeatHitId());
                                yResetLeft=false;
                                lastX = x;
                                lastY = y;
                                lastZ = z;
                                lastUpdate = now;
                                lastShake = now;
                                return;
                            }
                    }
                    else if(!yResetLeft)
                        if(y>0) {
                            yResetLeft = true;
                            lastX = x;
                            lastY = y;
                            lastZ = z;
                            lastUpdate = now;
                            lastShake = now;
                            return;
                        }
                   */
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