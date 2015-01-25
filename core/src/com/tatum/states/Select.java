package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
// stage
// finish
import com.tatum.Game;
import com.tatum.handlers.Background;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.SelectionHandler;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;
import com.tatum.music.MusicItem;

import java.util.ArrayList;

/**
 * Created by Ben on 24/01/2015.
 */
public class Select extends GameState {

    private boolean debug = false;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;

    private LevelGenerator levelGenerator;
    private String musicSelectionPath;
    private SelectionHandler selectionHandler;
    private ContentManager cont;
    private ArrayList<MusicItem> musicItems;
    private MusicItem backButton;
    private GameButton upButton;
    private GameButton downButton;
    private int listPosition;
    private FontGenerator fontGenerator;
    public Select(GameStateManager gsm) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        selectionHandler = new SelectionHandler(Gdx.files.external(""));
        fontGenerator = new FontGenerator();
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);

        cont = gsm.getGame().getResources();
        cont.loadTexture("res/images/buttondown.png");
        cont.loadTexture("res/images/buttonup.png");
        Texture downArrow = cont.getTexture("buttondown");
        Texture upArrow = cont.getTexture("buttonup");
        upButton = new GameButton(resources, new TextureRegion(upArrow,70,70), game.getWidth()-30, game.getHeight()-50, cam);
        downButton = new GameButton(resources, new TextureRegion(downArrow,70,70), game.getWidth()-30, game.getHeight()-100, cam);

        listPosition=0;
        setMusicItems();

        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void handleInput() {

        if(upButton.isClicked()){
            if(listPosition!=0){
            listPosition-=1;
            setMusicItems();
            }

        }
        if(downButton.isClicked()){
            if(listPosition!=selectionHandler.getScreenCount()){
                listPosition+=1;
                setMusicItems();
            }
        }
        if(backButton.isClicked()){
            System.out.println("///");
            if(!(selectionHandler.getCurrent().equals(Gdx.files.external("")))){
                System.out.println("inside");
                selectionHandler = new SelectionHandler(selectionHandler.getCurrent().parent());
                listPosition=0;
                setMusicItems();
                return;
            }
        }
        for(int i =0;i<musicItems.size();i++){
            if(musicItems.get(i).isClicked()){
                String text = musicItems.get(i).getText();
                if(selectionHandler.isDir(text)){
                    System.out.println(musicItems.get(i).getText());
                    selectionHandler = new SelectionHandler(selectionHandler.getChild(text));
                    listPosition=0;
                    setMusicItems();
                    return;
                }
                else{
                    musicItems.get(i).getText();
                    gsm.setState(new Menu(gsm,selectionHandler.getChildFullPath(text)));
                    return;
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        upButton.update(dt);
        downButton.update(dt);
        for (int i =0;i<musicItems.size();i++){
            musicItems.get(i).update(dt);
        }
        backButton.update(dt);
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);

        upButton.render(sb);
        downButton.render(sb);
        sb.begin();
        sb.end();

        for (int i =0;i<musicItems.size();i++){
            musicItems.get(i).render();
        }
        backButton.render();
    }

    private void setMusicItems(){

        int screenCount = selectionHandler.getScreenCount();
        String[] names = selectionHandler.getNames();
        backButton = new MusicItem(sb,FontGenerator.listFont,"Previous Directory",cam,10,game.getHeight()-30);
            try{
                musicItems= new ArrayList<MusicItem>();
                int bufferFromCeil =60;
                for(int i =0;i<5;i++){
                    String name = names[5*listPosition+i];
                    musicItems.add(new MusicItem(sb,FontGenerator.listFont,name,cam,10,game.getHeight()-bufferFromCeil));
                    bufferFromCeil+=30;
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }



    }

    @Override
    public void dispose() {

    }

}
