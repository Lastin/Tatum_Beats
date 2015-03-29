package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tatum.handlers.Animation;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.Background;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;
import com.tatum.handlers.MenuButton;
import com.tatum.handlers.PaceMaker;
import com.tatum.handlers.TatumMap;
import com.tatum.handlers.TrackLoader;
import com.tatum.music.MusicItem;

import static com.tatum.handlers.B2DVars.PPM;

public class Menu extends GameState {
    private boolean debug = false;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
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
    private MusicItem uploadingText;
    private MusicItem loadingText;
    private MusicItem generatingText;
    //buttons
    private MenuButton playButton;
    private MenuButton selectSong;
    private MenuButton scoresButton;
    private Stage stage;

    private float time;
    private boolean timeChange = false;
    private int dotCount = 0;

    private FontGenerator fontGenerator;

    public Menu(GameStateManager gsm) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        fontGenerator = new FontGenerator();
        loadPlayers();
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);
        p1Animation = new Animation(sprites1, 1/15f);
        p2Animation = new Animation(sprites2, 1/15f);
        p3Animation = new Animation(sprites3, 1/15f);

        Texture hud = resources.getTexture("hud2");
        initialiseButtons();

        cam.setToOrtho(false, game.getWidth(), game.getHeight());

        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        createLoadings();
        time = System.nanoTime()/1000000000;

    }
    public Menu(GameStateManager gsm, String Path){
        this(gsm);
        musicSelectionPath = Path;
    }

    private void initialiseButtons(){
        Texture myStyle = resources.getTexture("sprites");
        playButton = new MenuButton(fontGenerator, "PLAY", 160, 160);
        selectSong = new MenuButton(fontGenerator, "SELECT SONG", 160, 130);
        scoresButton = new MenuButton(fontGenerator, "HIGHSCORES", 160, 100);
        //set action on playButton, running in a separate thread
        playButton.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(musicSelectionPath == null){
                    gsm.setState(new Select(gsm));
                    return;
                }
                selectSong.getButton().setDisabled(true);
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
                                    gsm.setState(new Play(gsm, map, trackLoader.getMusic(), paceMaker, musicSelectionPath, trackLoader.getTrackData()));
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
        });
        selectSong.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new Select(gsm));
                selectSong.getButton().setDisabled(true);
            }
        });
        scoresButton.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new HighScoreList(gsm));
            }
        });
        stage = new Stage(new ExtendViewport(320, 240, cam));
        stage.addActor(playButton.getButton());
        stage.addActor(selectSong.getButton());
        stage.addActor(scoresButton.getButton());
        Gdx.input.setInputProcessor(stage);
    }

    public void createLoadings(){
        MusicItem temp = new MusicItem(sb, fontGenerator.loadingFont,"Loading",cam,0,game.getHeight()-100);
        float widthL = temp.getWidth();
        float height = temp.getHeight();
        float widthG = new MusicItem(sb, fontGenerator.loadingFont,"Generating",cam,0,game.getHeight()-130).getWidth();
        float widthU = new MusicItem(sb, fontGenerator.loadingFont,"Uploading",cam,0,game.getHeight()-130).getWidth();

        float newXLoading = (320/2)-(widthL/2);
        float newXGenerating = (320/2)-(widthG/2);
        float newXUploading = (320/2)-(widthU/2);
        float newY = (320/2)-(height/2);

        uploadingText =  new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.red),"Uploading",cam,(int)newXUploading,(int)newY);
        loadingText =  new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.yellow),"Loading",cam,(int)newXLoading,(int)newY);
        generatingText = new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.green),"Generating",cam,(int)newXGenerating,(int)newY);

    }

    @Override
    public void handleInput() {

    }
    @Override
    public void update(float dt) {
        if(!uploading&& !loading && !generating && !done)
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        p1Animation.update(dt);
        p2Animation.update(dt);
        p3Animation.update(dt);
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
            sb.begin();
            playButton.render(sb);
            selectSong.render(sb);
            scoresButton.render(sb);
            sb.end();
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
