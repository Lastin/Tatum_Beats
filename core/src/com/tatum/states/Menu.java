package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tatum.handlers.Animation;
import com.tatum.handlers.Background;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;
import com.tatum.handlers.MenuButton;
import com.tatum.handlers.PaceMaker;
import com.tatum.handlers.TatumMap;
import com.tatum.handlers.TrackLoader;
import com.tatum.handlers.MusicItem;

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
    private boolean showTip = false;
    private boolean generatingError = false;

    private MusicItem uploadingText;
    private MusicItem loadingText;
    private MusicItem generatingText;

    private MenuButton playButton;
    private MenuButton selectSong;
    private MenuButton scoresButton;

    private Stage stage;
    private float time;
    private boolean timeChange = false;
    private int dotCount = 0;

    private FontGenerator fontGenerator;
    private boolean expert = false;

    public Menu(GameStateManager gsm) {
        super(gsm);

        levelGenerator = new LevelGenerator(resources);
        fontGenerator = new FontGenerator();

        loadPlayers(); // load in the player sprites

        Texture menu = resources.getTexture("menu2");
        if(bg==null) {
            bg = new Background(game, new TextureRegion(menu), cam, 1f);
            bg.setVector(-20, 0);
        }//if there is no background create one

        //attach the player sprites to the rendered Animations
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
        playButton.getButton().setDisabled(true);
    }
    public Menu(GameStateManager gsm, String Path,boolean expert){
        this(gsm);
        musicSelectionPath = Path;
    } // menu when a song has been slected
    public Menu(GameStateManager gsm, String Path,Background bg, boolean expert){
        super(gsm);

        levelGenerator = new LevelGenerator(resources);
        fontGenerator = new FontGenerator();

        loadPlayers(); // load in the player sprites
        this.bg =bg;
        musicSelectionPath = Path;

        //attach the player sprites to the rendered Animations
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
        playButton.getButton().setDisabled(true);

        this.expert = expert;
    } // song has been selected, and background is passed
    public Menu(GameStateManager gsm,Background bg){
        this(gsm);
        this.bg =bg;
    } // no song but background passed

    private void initialiseButtons(){
        Texture myStyle = resources.getTexture("sprites");
        //create menu buttons
        playButton = new MenuButton(fontGenerator, "PLAY", 160, 160);
        if(musicSelectionPath==null) {
            selectSong = new MenuButton(fontGenerator, "SELECT SONG", 160, 130);

        }
        else
            selectSong = new MenuButton(fontGenerator, "CHANGE SONG", 160, 130);
        scoresButton = new MenuButton(fontGenerator, "HIGHSCORES", 160, 100);
        //set action on playButton, running in a separate thread
        playButton.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(musicSelectionPath == null){
                    gsm.setState(new Select(gsm,bg));
                    return; // if no song is selected, swap to select state
                }
                selectSong.getButton().setTouchable(Touchable.disabled);
                playButton.getButton().setTouchable(Touchable.disabled);
                scoresButton.getButton().setTouchable(Touchable.disabled); // disables button during loading

                System.gc();
                Thread thread = new Thread() {
                    public void run(){
                        uploading = true; // notify render that we are uploading
                        try{
                            final TrackLoader trackLoader = new TrackLoader(resources, musicSelectionPath);
                            trackLoader.loadTrackData();
                            trackLoader.getTrackData().upload(); // create track loader and upload chosen song
                            uploading=false;
                            loading=true; // notify render that we are now loading
                            trackLoader.getTrackData().initilize();
                            loading=false;
                            generating=true; // notify render we are now generating map
                            final TatumMap map = levelGenerator.makeMap(trackLoader.getTrackData());
                            final PaceMaker paceMaker = new PaceMaker(trackLoader.getTrackData(), map.getTiledMap());
                            generating = false;
                            done = true;    // let render know we are done with generation
                            showTip = true;
                            sleep(3000);    // generate map and pacemaker which deals with keeping map in time with song
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    gsm.setState(new Play(gsm, map, trackLoader.getMusic(), paceMaker, musicSelectionPath, trackLoader.getTrackData(),expert));
                                }
                            }); // set state to play - done in separate thread as loading can take a while
                        } catch (Exception e) {
                            // somethign broke in the upload, allows the user to try again
                            musicSelectionPath = null;
                            uploading = false;
                            loading = false;
                            generating = false;
                            done = true;
                            generatingError = true;
                            try {
                                sleep(4000);
                            } catch (InterruptedException interruptedE) {
                            }
                            selectSong.getButton().setTouchable(Touchable.enabled);
                            scoresButton.getButton().setTouchable(Touchable.enabled);
                            generatingError = false;
                            done = false;
                            //e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        selectSong.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new Select(gsm,bg));
                selectSong.getButton().setDisabled(true); // if select is pressed change to select state
            }
        });
        scoresButton.getButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new HighScoreList(gsm, fontGenerator, bg)); // if Highscore is clicked change to highscore
                scoresButton.getButton().setDisabled(true);
            }
        });
        stage = new Stage(new ExtendViewport(320, 240, cam));
        stage.addActor(playButton.getButton());
        stage.addActor(selectSong.getButton());
        stage.addActor(scoresButton.getButton()); // add buttons to stage
        Gdx.input.setInputProcessor(stage); // set input to stage so buttons may be clicked
    }

    public void createLoadings(){
      //cut down as string is static
        uploadingText =  new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.red),"Uploading",cam,74,148);
        loadingText =  new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.yellow),"Loading",cam,91,148);
        generatingText = new MusicItem(sb, fontGenerator.makeFont(70, fontGenerator.green),"Generating",cam,67,148);
        // create loading messages
    }

    @Override
    public void handleInput() {
        // depreicated for stage implementation
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
        if(tempTimeF >= time+0.3){ // checks if enough time has passed to draw additional dot onto loading strings
            timeChange=true;
            time = tempTimeF;
        }
        else{
            timeChange=false;
        }
    }   //updates all items
    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        if(!uploading&& !loading && !generating && !done) {
            sb.begin();
            selectSong.render(sb);
            scoresButton.render(sb);
            sb.end();
        } // renders all buttons as long as play hasn't been pressed
        if(!uploading&& !loading && !generating && !done&&musicSelectionPath!=null) {
            sb.begin();
            playButton.render(sb);
            sb.end();
        } // renders all buttons as long as play hasn't been pressed
        sb.begin();
        sb.draw(p1Animation.getFrame(), 100, 31);
        sb.draw(p2Animation.getFrame(), 140, 31);
        sb.draw(p3Animation.getFrame(), 180, 31);
        sb.end(); // draw characters
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
            } // sets the loading strings additional dots according to the time change var in update
        }
        if(uploading) {
            uploadingText.render();
        }
        if(loading) {
            loadingText.render();
        }
        if(generating) {
            generatingText.render();
        } else if (showTip){
            //render tip
            sb.begin();
            float width = fontGenerator.tipFont.getBounds("Remember!").width;
            fontGenerator.getTipFont().draw(sb, "Remember!", game.width/2 - width/2, 230);
            width = fontGenerator.tipFont.getBounds("Double tap to").width;
            fontGenerator.getTipFont().draw(sb, "Double tap to", game.width/2 - width/2, 200);
            width = fontGenerator.getTipFont().getBounds("pause").width;
            fontGenerator.getTipFont().draw(sb, "pause", game.width/2 - width/2, 195 - fontGenerator.getTipFont().getBounds("Double tap to").height);
            sb.draw(resources.getTexture("tap"), game.width/2 - 25, 100, 50, 50);
            sb.end();
        } else if (generatingError) {
            String[] info = {
                    "Selected track",
                    "is invalid. It cannot be",
                    "used to generate level."
            };
            sb.begin();
            BitmapFont font = fontGenerator.errorFont;
            TextBounds textDims = font.getBounds(info[0]);
            font.draw(sb, info[0], game.width / 2 - textDims.width / 2, 210);
            textDims = font.getBounds(info[1]);
            font.draw(sb, info[1], game.width/2 - textDims.width/2, 180);
            textDims = font.getBounds(info[2]);
            font.draw(sb, info[2], game.width/2 - textDims.width/2, 150);
            sb.end();
        }
        //redners the corrent loading message according to the stage the loading process is at

        if (debug) {
            cam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
            b2dRenderer.render(world, cam.combined);
            cam.setToOrtho(false, game.getWidth(), game.getHeight());
        } // renders debug extras if required
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
        } // gets all of the walking sprites for teh three characters so that they may be
        //animated at the bottom of the screen
    }
}
