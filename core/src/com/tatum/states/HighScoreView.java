
package com.tatum.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tatum.handlers.Background;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.InputProcessor;
import com.tatum.music.MusicItem;
public class HighScoreView extends GameState{

    private String twitterHandle;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private ContentManager cont;
    private MusicItem backButton;
    private MusicItem backButtonMenu;
    private MusicItem shareButton;
    private FontGenerator fontGenerator;
    private String trackName;
    private String artistName;
    private String album;
    private int score;
    private MusicItem ArtistName;
    private MusicItem TrackName;
    private MusicItem Album;
    private MusicItem Score;

    public HighScoreView(GameStateManager gsm, FontGenerator fontGenerator, String trackName, String artistName, String album, String twitterHandle, int score) {
        super(gsm);
        this.fontGenerator = fontGenerator;
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);

        //create back buttons
        backButtonMenu = new MusicItem(sb,fontGenerator.listFont,"Back to Menu",cam,10,game.getHeight()-10);
        backButton = new MusicItem(sb,fontGenerator.listFont,"Back to TrackList",cam,10,game.getHeight()-35);

        //create share on twitter button
        BitmapFont smallFont = fontGenerator.customFontSmall;
        smallFont.setScale(0.4f);
        float shareWidth = smallFont.getBounds("Share on Twitter").width;
        shareButton = new MusicItem(sb, smallFont, "Share on Twitter", cam, (int)(game.getWidth()/2 - shareWidth/2), 40);

        //store passed data and initialize state
        cont = gsm.getGame().getResources();
        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        this.trackName = trackName;
        this.artistName = artistName;
        this.album = album;
        this.score = score;
        this.twitterHandle = twitterHandle;

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setArtistSong();
            }
        }); //create centered text, done in separate thread to speed up state transaction
        Gdx.input.setInputProcessor(new InputProcessor()); // set input to click incase was set to swipe
    }
    public HighScoreView(GameStateManager gsm, FontGenerator fontGenerator, String trackName, String artistName, String album, String twitterHandle, int score, Background bg){
        this(gsm, fontGenerator, trackName,artistName,album, twitterHandle, score);
        this.bg = bg;
        // additional construction incase bg was passed (reduces visual lag when changing state)
    }
    private void setArtistSong(){
        update(Gdx.graphics.getDeltaTime());
        BitmapFont font;
        int size = 70;
        float widthA = 0;
        float widthS = 0;
        float widthL = 0;
        float widthG = 0;
        do {
            size -= 5;
            font = fontGenerator.makeFont(size, Color.BLACK);
            widthA = font.getBounds(artistName).width;
            widthS = font.getBounds(trackName).width;
            widthL = font.getBounds(album).width;
            widthG = font.getBounds("Score: " + score).width;
        } while(widthA > 300f || widthS > 300f || widthL > 300 || widthG > 300);
        float middle = game.getWidth()/2;
        ArtistName = new MusicItem(sb, font,artistName,cam, (int)(middle - widthA/2),game.getHeight()-100);
        TrackName =  new MusicItem(sb, font,trackName,cam, (int)(middle - widthS/2),game.getHeight()-70);
        Album =  new MusicItem(sb, font,album,cam, (int)(middle - widthL/2),game.getHeight()-130);
        Score =  new MusicItem(sb, font,"Score: "+ score,cam, (int)(middle - widthG/2),game.getHeight()-160);
        //this method works out the font size required to allow all of the text to be centered and not
        //overflow out of the screen
        // the itmes to display are then created using this size
    }

    @Override
    public void handleInput() {
        if(backButtonMenu.isClicked()){
            gsm.setState(new Menu(gsm));
        } // if back button is clicked go back to menu
        if(backButton.isClicked()){
            gsm.setState(new HighScoreList(gsm, fontGenerator, bg));
        } // if second back button is pressed, go back to high score list
        if(shareButton.isClicked()){
            //here goes twitter interface
            game.getTwitterInterface().share(artistName, trackName, score, twitterHandle);
        } // if twitter button is pressed, open twitter app with given message
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        backButton.update(dt);
        backButtonMenu.update(dt);
        shareButton.update(dt);
    } // update all buttons and state
    @Override
    public void render() {
        sb.setColor(1,1,1,1);
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        backButton.render();
        backButtonMenu.render();
        shareButton.render();
        if(Album!=null&&ArtistName!=null&&TrackName!=null&&Score!=null) { // checks if the setArtistSong() method is finished
            ArtistName.render();
            Album.render();
            TrackName.render();
            Score.render();
        }
    }   // render all text, buttons and background to teh screen
    @Override
    public void dispose() {

    }
}
