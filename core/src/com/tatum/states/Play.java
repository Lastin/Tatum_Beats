package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tatum.Game;
import com.tatum.entities.B2DSprite;
import com.tatum.entities.Bat;
import com.tatum.entities.Coin;
import com.tatum.entities.HUD;
import com.tatum.entities.Instructor;
import com.tatum.entities.Player;
import com.tatum.entities.Slime;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.Background;
import com.tatum.handlers.BoundedCamera;
import com.tatum.handlers.CollisionListener;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameBodiesCreator;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.MonsterCoinLocation;
import com.tatum.handlers.PaceMaker;
import com.tatum.handlers.TatumDirectionListener;
import com.tatum.handlers.TatumMap;
import com.tatum.music.MusicItem;
import com.tatum.music.TrackData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.tatum.handlers.B2DVars.GRAVITY;
import static com.tatum.handlers.B2DVars.PPM;

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
    private MusicItem Album;
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
    private final TrackData trackData;
    private FontGenerator fontGenerator;
    private float sbColor = 1;


    public Play(GameStateManager gsm, TatumMap tatumMap, Music music, PaceMaker paceMaker, String path, TrackData trackData) {
        super(gsm);
        Slime.setSpriteNull(); // set to null as in android static memory is readdressed when the user changes to a different process
        Bat.setSpriteNull();    // therefore if they exit the game and come back in, the animations will appear as black squares
        //create map and saved passed variables
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

        //create the player and the objectives
        player = createPlayer();
        createObstacles();

        //create the oncreen hud and pass the pacemaker
        hud = new HUD(resources, fontGenerator, game, player,paceMaker, this);
        hud.setPaceMaker(paceMaker);

        backgrounds = createBackground();

        //create the blocks to stand on and the cameras
        GameBodiesCreator.createBlocks(tiledMap, world);
        initialiseCamerasAndRenderers();

        startTime= System.nanoTime();
        data = gsm.getGame().getData();

        //set the game to use the swipe input
        game.setSwipeInput();

        //create the meta data display items
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setArtistSong();
            }
        }); // done in separate thread to speed up transition time
        this.instructor = hud.getInstructor();
        music.play(); //play music
        setSongCharactaristics(); // set the way the instructor works based on song key
        shaderVal = 1;
    }

    private void setArtistSong(){
        BitmapFont font;
        int size = 70;
        float widthA = 0;
        float widthS = 0;
        float widthL = 0;
        do {
            size -= 5;
            font = fontGenerator.makeFont(size, Color.BLACK);
            widthA = font.getBounds(paceMaker.getTrackData().getArtist()).width;
            widthS = font.getBounds(paceMaker.getTrackData().getSongName()).width;
            widthL = font.getBounds(paceMaker.getTrackData().getAlbumName()).width;

        } while(widthA > 300f || widthS > 300f || widthL > 300);
        float middle = game.getWidth()/2;
        ArtistName = new MusicItem(sb, font,paceMaker.getTrackData().getArtist(),cam, (int)(middle - widthA/2),game.getHeight()-100);
        SongName =  new MusicItem(sb, font,paceMaker.getTrackData().getSongName(),cam, (int)(middle - widthS/2),game.getHeight()-70);
        Album =  new MusicItem(sb, font,paceMaker.getTrackData().getAlbumName(),cam, (int)(middle - widthL/2),game.getHeight()-130);
        // this method works out the font size required to display all of the artist information
        // at the start of the song if there is any
    }

    private void initialiseCamerasAndRenderers(){
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
        b2dCam.setBounds(0, (width * tileSide) / PPM, 0, (height * tileSide) / PPM);
        b2dRenderer = new Box2DDebugRenderer();
        cam.setBounds(0, width * PPM, 0, height * tileSide);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //this method creates all cameras that display the HUD and follows the character throughout the level
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
    // this method sets how the controles change based on the key that the song is in
    // this is done by passing an enum to the Instructor out of teh several options that are available
     // it additionally sets weather the camera should rotate based on the modality of the song
        // but this was removed as it wasn't liked by play testers
    }

    private Player createPlayer() {
        Player player;
        // create bodydef
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(60 / PPM, 120 / PPM);
        bdef.fixedRotation = true;

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
        for(int each : barsPositions){   //for each of the bars in the song we create an event
            float temp =random.nextFloat();
            // we get a random float, this is then split into thirds to get a distribution of ground/ flying enemies
            // and coins
            if(temp<=0.3) {
                Slime slime = GameBodiesCreator.createSlime(each, world, resources,trackData.getTheme());
                events.add(slime);
                monsterCoinLocation.addEvent("Slime", each,slime); // create ground enemy and add to array list
            }
            else if((temp>0.3)&&(temp<=0.6)){
                Bat bat = GameBodiesCreator.createBat(each, world, resources,trackData.getTheme());
                events.add(bat);
                monsterCoinLocation.addEvent("Bat", each,bat); //create flying enemy and add to array list
            }

            else if((temp>0.6)&&(temp<=0.7)) {
                if (lastCoin == 2) {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Pink");
                    events.add(coin);
                    monsterCoinLocation.addEvent("PinkCoin", each, coin); //create pink coin unless last coin was pink
                                                                         // in which case create blue coin
                    lastCoin=3;
                } else {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Blue");
                    events.add(coin);
                    monsterCoinLocation.addEvent("BlueCoin", each, coin);
                    lastCoin=2;
                }
            }
            else if((temp>0.7)&&(temp<=0.8)) {
                if (lastCoin == 3) {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Green");
                    events.add(coin);
                    monsterCoinLocation.addEvent("GreenCoin", each,coin); //create green coin unless last coin was green
                                                                          // in which case create pink coin
                    lastCoin=1;
                } else {
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources, "Pink");
                    events.add(coin);
                    monsterCoinLocation.addEvent("PinkCoin", each, coin);
                    lastCoin=3;
                }
            }
            else{
                if(lastCoin==1){
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Blue");
                    events.add(coin);
                    monsterCoinLocation.addEvent("BlueCoin", each,coin); //create blue coin unless last coin was blue
                                                                           // in which case create green coin
                    lastCoin=2;
                }
                else{
                    Coin coin = GameBodiesCreator.createCoin(each, world, resources,"Green");
                    events.add(coin);
                    monsterCoinLocation.addEvent("GreenCoin", each,coin);
                    lastCoin=1;
               }
            }

        }
        paceMaker.setMonsterCoinLocation(monsterCoinLocation);
        // give the pacemaker the events so that it knows what to do each bar/ beat
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
        //load in all backgrounds for the different themes

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
        //add all backgrounds to hashmap

        if((theme.equals("jazz"))||(theme.equals("asian"))){
            Background[] backgrounds = new Background[1];
            backgrounds[0] = new Background(game,bgTheme.get(theme),cam,0.0f);
            return backgrounds;
        }// these are separate as they have the option to rotate but this is turned off
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
        }// pop was kept with the original game theme, and as such is separate
        else{
            Background[] backgrounds = new Background[1];
            backgrounds[0] = new Background(game,bgTheme.get(theme),cam,0f);
            return backgrounds;
        } // checks the theme of the song, and sets the background accordingly
    }

    @Override
    public void render() {
        sb.setColor(sbColor, sbColor, sbColor, shaderVal);
        // camera follow player
        cam.setPosition(player.getPosition().x * PPM + game.getWidth() / 4, game.getHeight() / 3);
        cam.update();
        if(rotate){
            //turned off
        }

        sb.setProjectionMatrix(hudCam.combined);

        for (Background each : backgrounds) {
            each.render(sb);
        }// draw all backgrounds (some are multi layered)

        // draw tiledmap
        mapRenderer.setView(cam);
        mapRenderer.render();
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
            } //renders the events that are in view
            player.render(sb);

            sb.setProjectionMatrix(hudCam.combined);
            hud.render(sb);

        }

        // debug draw box2d
        if(debug) {
            b2dCam.setPosition(player.getPosition().x + game.getWidth() / 4 / PPM, game.getHeight() / 2 / PPM);
            b2dCam.update();
            b2dRenderer.render(world, b2dCam.combined);
        }

        if(music.getPosition()<5) {
            if(ArtistName!=null&&SongName!=null&&Album!=null) {
                SongName.getFont().setColor(0, 0, 0, titleFade);
                SongName.render();
                ArtistName.getFont().setColor(0, 0, 0, titleFade);
                ArtistName.render();
                Album.getFont().setColor(0, 0, 0, titleFade);
                Album.render();
            }
            if(titleFade<1f&&(music.getPosition()>titleTimer+0.1)) {
                titleFade += 0.05;
                titleTimer=music.getPosition();
                if(titleFade>1f){
                    titleFade=1f;
                }
            }
        } // draws the fade in song meta data

        else if(music.getPosition()>5 && titleFade>0){
            //  sb.setColor(255f,0f,0f,titleFade);
            if(ArtistName!=null&&SongName!=null&&Album!=null) {
                SongName.getFont().setColor(0, 0, 0, titleFade);
                SongName.render();
                ArtistName.getFont().setColor(0, 0, 0, titleFade);
                ArtistName.render();
                Album.getFont().setColor(0, 0, 0, titleFade);
                Album.render();
            }
            if((music.getPosition()>titleTimer+0.1))
                titleFade -= 0.03;
        }   // draw the song meta data fade out
        else{
            //don't render song meta data after set time
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
                sbColor = 1;
            }
        } // handles background flashing

        //end
        float currPosition = music.getPosition();
        deltaPos = currPosition - previousPosition;
        previousPosition = currPosition;
        deltaDiff = deltaPos - deltaPosPrev;
        deltaPosPrev = deltaPos;

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
            String trackName = trackData.getSongName();
            String artistName = trackData.getArtist();
            String album = trackData.getAlbumName();
            gsm.setState(new HighScoreView(gsm, fontGenerator, trackName, artistName, album, trackData.getTwitterHandle(), player.getHighScore()));
            // if level has finished go to highscore view for song
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
        } // depricated as player can no longer go through blocks, left in incase they somehow manage to

        //check if need to change from jumping/ducking back to walking
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
        }                                               //both depricated from previous versions of game
        if(player.getBody().getPosition().x>walkCheck){
            walkCheck+=(32/PPM);
        }


        for(int i =paceMaker.getRenderCounter()-2;i<paceMaker.getRenderCounter()+3;i++) {
            if (i < 0)
                continue;
            try {
                events.get(i).update(deltaTime);
            }catch (IndexOutOfBoundsException e){
                break;
                //end of song
            }
        }   // updates events that are in view
    }

    private void updateVelocity(float deltaTime){
        paceMaker.updateVelocity(player, music.getPosition());
        // this method used to use System nano time, changed to music position as it stopped
        // some issues with freezing
    }

    @Override
    public void handleInput(){

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

        } // check if the user has swipped, and if so call the instructor method in the chosen direction
            // so that the users action choice may be perfomred on the character
    }

    public void playerJump(){
        if(cl.playerCanJump()&&(!player.getIsJumping())&&(!player.getIsDucking())){ //checks if the user is not currently jumping or ducking

            //paceMaker.setJumping(true); // use for in time
            player.getBody().applyForceToCenter(0, 200, true); // use for working jump, time independent
            player.setJumpSkin();
            player.setJumpTime(music.getPosition());
            //resources.getSound("jump").play();
            // applies a force to the player to launch them into the air
        }
    }

    @Override
    public void dispose() {

    }

    //***************************** Below are the shake methods which are no longer being used
    //***************************** Left in in case we decide to reenable

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