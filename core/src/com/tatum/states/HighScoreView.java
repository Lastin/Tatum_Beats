
package com.tatum.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private ContentManager cont;
    private MusicItem backButton;
    private MusicItem backButtonMenu;
    private FontGenerator fontGenerator;
    private String trackName;
    private String artistName;
    private String album;
    private int score;
    private MusicItem ArtistName;
    private MusicItem TrackName;
    private MusicItem Album;
    private MusicItem Score;

    public HighScoreView(GameStateManager gsm,String trackName, String artistName, String album, int score) {
        super(gsm);
        fontGenerator = new FontGenerator();
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);
        backButtonMenu = new MusicItem(sb,fontGenerator.listFont,"Back to Menu",cam,10,game.getHeight()-10);
        backButton = new MusicItem(sb,fontGenerator.listFont,"Back to TrackList",cam,10,game.getHeight()-35);
        cont = gsm.getGame().getResources();
        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        this.trackName = trackName;
        this.artistName = artistName;
        this.album = album;
        this.score = score;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setArtistSong();
            }
        });

        Gdx.input.setInputProcessor(new InputProcessor());
    }
    private void setArtistSong(){

        float widthA = new MusicItem(sb,fontGenerator.loadingFont,artistName,cam,0,game.getHeight()-100).getWidth();
        float widthS = new MusicItem(sb,fontGenerator.loadingFont,trackName,cam,0,game.getHeight()-130).getWidth();
        float widthL = new MusicItem(sb,fontGenerator.loadingFont,album,cam,0,game.getHeight()-130).getWidth();
        float widthG = new MusicItem(sb,fontGenerator.loadingFont,"Score: "+score,cam,0,game.getHeight()-130).getWidth();

        float newXArtist = (320/2)-(widthA/2);
        float newXSong = (320/2)-(widthS/2);
        float newXAlbum = (320/2)-(widthL/2);
        float newXScore = (320/2)-(widthG/2);
        int size =70;
        while(true)
            if(newXArtist<10 || newXSong < 10 || newXAlbum<10 || newXScore < 10 ||widthA>310 || widthS>310  ||widthL>310 || widthG>310 ){
                size = size-10;
                widthA = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),artistName,cam,0,game.getHeight()-100).getWidth();
                widthS = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),trackName,cam,0,game.getHeight()-130).getWidth();
                widthL = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),album,cam,0,game.getHeight()-160).getWidth();
                widthG = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),"Score: "+score,cam,0,game.getHeight()-190).getWidth();
                newXArtist = (320/2)-(widthA/2);
                newXSong = (320/2)-(widthS/2);
                newXAlbum = (320/2)-(widthL/2);
                newXScore = (320/2)-(widthG/2);
            }
           else break;

        ArtistName = new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),artistName,cam,(int)newXArtist,game.getHeight()-100);
        TrackName =  new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),trackName,cam,(int)newXSong,game.getHeight()-70);
        Album =  new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),album,cam,(int)newXAlbum,game.getHeight()-130);
        Score =  new MusicItem(sb,fontGenerator.makeFont(size, Color.BLACK),"Score: "+ score,cam,(int)newXScore,game.getHeight()-160);

    }

    @Override
    public void handleInput() {
        if(backButtonMenu.isClicked()){
            gsm.setState(new Menu(gsm));
        }
        if(backButton.isClicked()){
            gsm.setState(new HighScoreList(gsm));
        }
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        backButton.update(dt);
        backButtonMenu.update(dt);
    }
    @Override
    public void render() {
        sb.setColor(1,1,1,1);
        sb.setProjectionMatrix(cam.combined);
       bg.render(sb);
        backButton.render();
        backButtonMenu.render();
        if(Album!=null&&ArtistName!=null&&TrackName!=null&&Score!=null) {
            ArtistName.render();
            Album.render();
            TrackName.render();
            Score.render();
        }
    }
    @Override
    public void dispose() {

    }
}
