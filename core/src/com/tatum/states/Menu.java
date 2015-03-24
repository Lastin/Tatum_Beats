package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tatum.entities.B2DSprite;
import com.tatum.errors.MusicNotFoundException;
import com.tatum.handlers.Animation;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.Background;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.InputProcessor;
import com.tatum.handlers.LevelGenerator;
import com.tatum.handlers.PaceMaker;
import com.tatum.handlers.TatumMap;
import com.tatum.handlers.TrackLoader;
import com.tatum.music.MusicItem;
import com.tatum.music.TrackData;

import java.util.Timer;

import static com.tatum.handlers.B2DVars.PPM;

public class Menu extends GameState {
    private boolean debug = false;
    private Background bg;
    private GameButton playButton;
    private GameButton selectTrackButton;
    private GameButton highScoreButton;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private Array<B2DSprite> blocks;
    private TextureRegion[] sprites1 = new TextureRegion[11];
    private TextureRegion[] sprites2 = new TextureRegion[11];
    private TextureRegion[] sprites3 = new TextureRegion[11];
    private Animation p1Animation;
    private Animation p2Animation;
    private Animation p3Animation;
    private LevelGenerator levelGenerator;
    private String musicSelectionPath;
    private boolean loading = false;
    private boolean generating = false;
    private boolean uploading = false;
    private boolean done = false;
    private TextureRegion[] signs;
    private Texture miniLogo;
    private MusicItem uploadingText;
    private MusicItem loadingText;
    private MusicItem generatingText;

    private float time;
    private boolean timeChange = false;
    private int dotCount = 0;

    public Menu(GameStateManager gsm) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        loadPlayers();
        signs = loadSigns();
        miniLogo = resources.getTexture("tatumLogoMini");
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);
        p1Animation = new Animation(sprites1, 1/15f);
        p2Animation = new Animation(sprites2, 1/15f);
        p3Animation = new Animation(sprites3, 1/15f);

        Texture hud = resources.getTexture("hud2");
        Texture myStyle = resources.getTexture("sprites");
        playButton = new GameButton(resources, new TextureRegion(myStyle, 190, 156, 169, 51), 160, 100, cam);
        selectTrackButton = new GameButton(resources, new TextureRegion(myStyle, 79, 0, 472, 51), 160, 130, cam);
        highScoreButton = new GameButton(resources, new TextureRegion(myStyle, 79, 0, 472, 51), 160, 160, cam);
        cam.setToOrtho(false, game.getWidth(), game.getHeight());

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(10, 10), true);
        b2dRenderer = new Box2DDebugRenderer();
        createLoadings();
        createTitleBodies();
        time = System.nanoTime()/1000000000;

    }
    public Menu(GameStateManager gsm, String Path){
        super(gsm);
        musicSelectionPath = Path;
        levelGenerator = new LevelGenerator(resources);
        loadPlayers();
        signs = loadSigns();
        miniLogo = resources.getTexture("tatumLogoMini");
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);
        p1Animation = new Animation(sprites1, 1/15f);
        p2Animation = new Animation(sprites2, 1/15f);
        p3Animation = new Animation(sprites3, 1/15f);

        Texture hud = resources.getTexture("hud2");
        Texture myStyle = resources.getTexture("sprites");
        playButton = new GameButton(resources, new TextureRegion(myStyle, 190, 156, 169, 51), 160, 100, cam);
        selectTrackButton = new GameButton(resources, new TextureRegion(myStyle, 79, 0, 472, 51), 160, 130, cam);
        highScoreButton = new GameButton(resources, new TextureRegion(myStyle, 79, 0, 472, 51), 160, 160, cam);
        cam.setToOrtho(false, game.getWidth(), game.getHeight());

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(10, 10), true);
        b2dRenderer = new Box2DDebugRenderer();
        createLoadings();
        createTitleBodies();
    }
    public void createLoadings(){
        MusicItem temp = new MusicItem(sb,FontGenerator.makeFont(70, Color.WHITE),"Loading",cam,0,game.getHeight()-100);
        float widthL = temp.getWidth();
        float height = temp.getHeight();
        float widthG = new MusicItem(sb,FontGenerator.makeFont(70, Color.WHITE),"Generating",cam,0,game.getHeight()-130).getWidth();
        float widthU = new MusicItem(sb,FontGenerator.makeFont(70, new Color(255,120,120,1)),"Uploading",cam,0,game.getHeight()-130).getWidth();

        float newXLoading = (320/2)-(widthL/2);
        float newXGenerating = (320/2)-(widthG/2);
        float newXUploading = (320/2)-(widthU/2);
        float newY = (320/2)-(height/2);
//        java.awt.Color red = new java.awt.Color(255,120,120);
//        java.awt.Color yellow = new java.awt.Color(241,217,116);
//        java.awt.Color green = new java.awt.Color(90,250,183);
        Color yellow = Color.valueOf("F1D974");
        yellow.a=1f;
        Color green = Color.valueOf("68F367");
        green.a=1f;
        uploadingText =  new MusicItem(sb,FontGenerator.makeFont(70, new Color((255/255),(120/255),(120/255),1f)),"Uploading",cam,(int)newXUploading,(int)newY);
        loadingText =  new MusicItem(sb,FontGenerator.makeFont(70, yellow),"Loading",cam,(int)newXLoading,(int)newY);
        generatingText = new MusicItem(sb,FontGenerator.makeFont(70, green),"Generating",cam,(int)newXGenerating,(int)newY);

    }

    private TextureRegion[] loadSigns() {
        Texture texture = resources.getTexture("signs");
        TextureRegion[][] signs_temp = TextureRegion.split(texture, 85, 70);
        TextureRegion[] signs = new TextureRegion[3];
        for(int i=0; i<3; i++){
            signs[i] = signs_temp[i][0];
        }
        return signs;
    }

    private void createTitleBodies() {
        int[][] spellBlock = {
                {1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1}
        };
        int[][] spellBunny = {
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0},
        };
        // top platform
        BodyDef tpbdef = new BodyDef();
        tpbdef.type = BodyType.StaticBody;
        tpbdef.position.set(160 / PPM, 180 / PPM);
        Body tpbody = world.createBody(tpbdef);
        PolygonShape tpshape = new PolygonShape();
        tpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef tpfdef = new FixtureDef();
        tpfdef.shape = tpshape;
        tpfdef.filter.categoryBits = B2DVars.BIT_TOP_PLATFORM;
        tpfdef.filter.maskBits = B2DVars.BIT_TOP_BLOCK;
        tpbody.createFixture(tpfdef);
        tpshape.dispose();

        // bottom platform
        BodyDef bpbdef = new BodyDef();
        bpbdef.type = BodyType.StaticBody;
        bpbdef.position.set(160 / PPM, 130 / PPM);
        Body bpbody = world.createBody(bpbdef);
        PolygonShape bpshape = new PolygonShape();
        bpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef bpfdef = new FixtureDef();
        bpfdef.shape = bpshape;
        bpfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_PLATFORM;
        bpfdef.filter.maskBits = B2DVars.BIT_BOTTOM_BLOCK;
        bpbody.createFixture(bpfdef);
        bpshape.dispose();

        Texture tex = resources.getTexture("hud2");
        TextureRegion[] blockSprites = new TextureRegion[3];
        for(int i = 0; i < blockSprites.length; i++) {
            blockSprites[i] = new TextureRegion(tex, 58 + i * 5, 34, 5, 5);
        }
        blocks = new Array<B2DSprite>();

        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 29; col++) {
                BodyDef tbbdef = new BodyDef();
                tbbdef.type = BodyType.DynamicBody;
                tbbdef.fixedRotation = true;
                tbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6  + row) / PPM);
                Body tbbody = world.createBody(tbbdef);
                PolygonShape tbshape = new PolygonShape();
                tbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef tbfdef = new FixtureDef();
                tbfdef.shape = tbshape;
                tbfdef.filter.categoryBits = B2DVars.BIT_TOP_BLOCK;
                tbfdef.filter.maskBits = B2DVars.BIT_TOP_PLATFORM | B2DVars.BIT_TOP_BLOCK;
                tbbody.createFixture(tbfdef);
                tbshape.dispose();
                if(spellBlock[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(tbbody, resources);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }

        // bottom blocks
        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 29; col++) {
                BodyDef bbbdef = new BodyDef();
                bbbdef.type = BodyType.DynamicBody;
                bbbdef.fixedRotation = true;
                bbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6 + row) / PPM);
                Body bbbody = world.createBody(bbbdef);
                PolygonShape bbshape = new PolygonShape();
                bbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef bbfdef = new FixtureDef();
                bbfdef.shape = bbshape;
                bbfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_BLOCK;
                bbfdef.filter.maskBits = B2DVars.BIT_BOTTOM_PLATFORM | B2DVars.BIT_BOTTOM_BLOCK;
                bbbody.createFixture(bbfdef);
                bbshape.dispose();
                if(spellBunny[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(bbbody, resources);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }
        Gdx.input.setInputProcessor(new InputProcessor());
    }
    public void handleInput() {
        if (playButton.isClicked() && playButton.isEnabled()) {
            playButton.setEnabled(false);
            //resources.getSound("crystal").play();
            if(musicSelectionPath == null){
                gsm.setState(new Select(gsm));
            }
            System.out.println(musicSelectionPath);
            Thread thread = new Thread() {
                public void run(){
                    uploading = true;
                    try{
                        final TrackLoader trackLoader = new TrackLoader(resources, musicSelectionPath);
                        trackLoader.loadTrackData();
                        trackLoader.getTrackData().upload();
                        uploading=false;
                        loading=true;
                        trackLoader.getTrackData().initilize();
                        loading=false;
                        generating=true;
                        final TatumMap map = levelGenerator.makeMap(trackLoader.getTrackData());
                        final PaceMaker paceMaker = new PaceMaker(trackLoader.getTrackData(), map.getTiledMap());
                        sleep(1000);
                        generating = false;
                        done = true;
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                gsm.setState(new Play(gsm, map, trackLoader.getMusic(), paceMaker,musicSelectionPath));
                            }
                        });
                    } catch (Exception e) {
                        loading = false;
                        generating = false;
                        done = false;
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        else if(selectTrackButton.isClicked()){
            //deal with track selection
            gsm.setState(new Select(gsm));

        }
        else if(highScoreButton.isClicked()){
            gsm.setState(new HighScoreList(gsm));
        }
    }
    public void updateProgress(double progress){

    }
    public void updateMusicSelection(String path){

    }
    @Override
    public void update(float dt) {
        if(!uploading&& !loading && !generating && !done)
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        p1Animation.update(dt);
        p2Animation.update(dt);
        p3Animation.update(dt);
        playButton.update(dt);
        selectTrackButton.update(dt);
        highScoreButton.update(dt);
        long tempTime = System.nanoTime();
        float tempTimeF = tempTime/1000000000;
        if(tempTimeF >= time+0.3){
            timeChange=true;
            time = tempTimeF;
        }
        else{
            timeChange=false;
        }

    }
    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        if(!uploading&& !loading && !generating && !done) {
            playButton.render(sb);
            selectTrackButton.render(sb);
            highScoreButton.render(sb);
        }
        sb.begin();
        sb.draw(p1Animation.getFrame(), 100, 31);
        sb.draw(p2Animation.getFrame(), 140, 31);
        sb.draw(p3Animation.getFrame(), 180, 31);
        sb.end();
        if(timeChange){
            if(dotCount==0){
                dotCount=1;
                uploadingText.setText("Uploading.");
                loadingText.setText("Loading.");
                generatingText.setText("Generating.");
            }
            else if(dotCount==1){
                dotCount=2;
                uploadingText.setText("Uploading..");
                loadingText.setText("Loading..");
                generatingText.setText("Generating..");
            }
            else if(dotCount==2){
                dotCount=3;
                uploadingText.setText("Uploading...");
                loadingText.setText("Loading...");
                generatingText.setText("Generating...");
            }
            else {
                dotCount=0;
                uploadingText.setText("Uploading");
                loadingText.setText("Loading");
                generatingText.setText("Generating");
            }
        }
        if(uploading) {
            uploadingText.render();
        }
        if(loading) {
            loadingText.render();
        }
        if(generating) {
            generatingText.render();
        }

        if (debug) {
            cam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
            b2dRenderer.render(world, cam.combined);
            cam.setToOrtho(false, game.getWidth(), game.getHeight());
        }
        for (int i = 0; i < blocks.size; i++) {
            //blocks.get(i).render(sb);
        }
    }
    @Override
    public void dispose(){

    }
    public void loadPlayers(){
        Texture texture = resources.getTexture("mini_walk_combined");
        TextureRegion[][] walking = TextureRegion.split(texture, 36, 50);
        for(int i=0; i<walking.length; i++) {
            if(i<11)
                sprites1[i%11] = walking[i][0];
            else if(i<22)
                sprites2[i%11] = walking[i][0];
            else
                sprites3[i%11] = walking[i][0];
        }
    }
}
