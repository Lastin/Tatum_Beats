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
    private MusicItem backButtonMenu;
    private GameButton upButton;
    private GameButton downButton;
    private GameButton upButtonFast;
    private GameButton downButtonFast;
    private int listPosition[];
    private FontGenerator fontGenerator;
    MusicItem toWriteItem;
    public Select(GameStateManager gsm) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        selectionHandler = new SelectionHandler(Gdx.files.external(""));
        fontGenerator = new FontGenerator();
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);

        cont = gsm.getGame().getResources();
        cont.loadTexture("res/images/arrowDown.png");
        cont.loadTexture("res/images/arrowUp.png");
        cont.loadTexture("res/images/arrowUpFast.png");
        cont.loadTexture("res/images/arrowDownFast.png");
        Texture downArrow = cont.getTexture("arrowDown");
        Texture upArrow = cont.getTexture("arrowUp");
        Texture downArrowFast = cont.getTexture("arrowDownFast");
        Texture upArrowFast = cont.getTexture("arrowUpFast");
        upButtonFast = new GameButton(resources, new TextureRegion(upArrowFast,70,85), game.getWidth()-30, game.getHeight()-50, cam);
        upButton = new GameButton(resources, new TextureRegion(upArrow,70,70), game.getWidth()-30, game.getHeight()-90, cam);
        downButton = new GameButton(resources, new TextureRegion(downArrow,70,70), game.getWidth()-30, game.getHeight()-130, cam);
        downButtonFast = new GameButton(resources, new TextureRegion(downArrowFast,70,85), game.getWidth()-30, game.getHeight()-170, cam);
        toWriteItem = new MusicItem(sb,FontGenerator.listFont,"",cam,10,game.getHeight()-5);
        listPosition= new int[5];
        setListPosition("start");
        setMusicItems();

        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void handleInput() {

        if(upButton.isClicked()){

            System.out.println("Up click");
            if(listPosition[0]!=0) {
                System.out.println("inside");
                setListPosition("up");
                setMusicItems();
            }
            }
        if(upButtonFast.isClicked()){

            System.out.println("Up click");
            if(listPosition[0]!=0) {
                System.out.println("inside");
                setListPosition("quickup");
                setMusicItems();
            }
        }


        if(downButton.isClicked()){
            System.out.println("Down click");
            if(listPosition[4]<selectionHandler.getScreenCount()-1){
                    System.out.println("in");
                    setListPosition("down");
                    setMusicItems();
            }
         }

        if(downButtonFast.isClicked()){
            System.out.println("Down click");
            if(listPosition[4]<selectionHandler.getScreenCount()-1){
                System.out.println("in");
                setListPosition("quickdown");
                setMusicItems();
            }
        }

        if(backButton.isClicked()){
            if(!(selectionHandler.getCurrent().equals(Gdx.files.external("")))){
                selectionHandler = new SelectionHandler(selectionHandler.getCurrent().parent());

                setMusicItems();
                return;
            }
        }
        if(backButtonMenu.isClicked()){
            gsm.setState(new Menu(gsm));
        }
        for(int i =0;i<musicItems.size();i++){
            if(musicItems.get(i).isClicked()){
                String text = musicItems.get(i).getText();
                if(selectionHandler.isDir(text)){
                    System.out.println(musicItems.get(i).getText());
                    selectionHandler = new SelectionHandler(selectionHandler.getChild(text));
                    setListPosition("start");
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
        downButtonFast.update(dt);
        upButtonFast.update(dt);
        toWriteItem.update(dt);
        for (int i =0;i<musicItems.size();i++){
            musicItems.get(i).update(dt);
        }
        backButton.update(dt);
        backButtonMenu.update(dt);
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        backButtonMenu.render();
        upButton.render(sb);
        downButton.render(sb);
        upButtonFast.render(sb);
        downButtonFast.render(sb);
        toWriteItem.renderFull();
        toWriteItem.render();
        sb.begin();
        sb.end();

        for (int i =0;i<musicItems.size();i++){
            musicItems.get(i).render();
        }
        backButton.render();
    }

    private void setMusicItems(){

        ArrayList<String> names = selectionHandler.getPrunedNames();
        backButtonMenu = new MusicItem(sb,FontGenerator.listFont,"Back to Menu",cam,10,game.getHeight()-20);
        backButton = new MusicItem(sb,FontGenerator.listFont,"Previous Directory",cam,10,game.getHeight()-50);
        System.out.println(names.size());
            try{
                musicItems= new ArrayList<MusicItem>();
                int bufferFromCeil = 75;
                for(int i =0;i<5;i++){
                    System.out.println(listPosition[i]);
                    String name = names.get(listPosition[i]);
                    musicItems.add(new MusicItem(sb,FontGenerator.listFont,name,cam,10,game.getHeight()-bufferFromCeil));
                    bufferFromCeil+=25;
                }
            }catch (IndexOutOfBoundsException e){

            }

        fileCounter();
    }
    public void setListPosition(String position){

        if(position.equals("start")){
            for(int i =0;i<5;i++){
                listPosition[i]=i;
            }
        }
        else if(position.equals("down")){
            for(int i =0;i<5;i++)
                listPosition[i]++;

        }
        else if(position.equals("up")){
            if(listPosition[0]!=0)
                for(int i =0;i<5;i++)
                    listPosition[i]--;

        }
        else if(position.equals("quickdown")){
            if(listPosition[4]+5>=selectionHandler.getScreenCount())
                setListPosition("quickdown2ElectricBoogaloo");
            else
                for(int i =0;i<5;i++){
                    listPosition[i]+=5;
            }
        }
        else if (position.equals("quickdown2ElectricBoogaloo")){
            int value = selectionHandler.getScreenCount()-1;
            for(int i =4;i>=0;i--){
                listPosition[i]=value;
                value--;
            }
        }

        else if(position.equals("quickup")){
            if(listPosition[0]-5<0)
                setListPosition("start");
            else
                for(int i =0;i<5;i++){
                    listPosition[i]-=5;
            }
        }
    }

    public void fileCounter(){

        String toWrite = "";
        int size = selectionHandler.getScreenCount();
        String start = selectionHandler.getCurrent().path();
        if(start==""){
            start = "root";
        }
        if(size==0){
            toWrite = start+": " + (listPosition[0]) + "-"+(listPosition[0]) +" / " +size;
        }
        else if(listPosition[1]>=size){
            toWrite = start+": " + (listPosition[0]+1) + "-"+(listPosition[0]+1) +" / " +size;
        }

        else if(listPosition[2]>=size){
            toWrite = start+": " + (listPosition[0]+1) + "-"+(listPosition[2]+1) +" / " +size;
        }

        else if(listPosition[3]>=size){
            toWrite = start+": " + (listPosition[0]+1) + "-"+(listPosition[2]+1) +" / " +size;
        }

        else if(listPosition[4]>=size){
            toWrite = start+": " + (listPosition[0]+1) + "-"+(listPosition[3]+1) +" / " +size;
        }
        else {
            toWrite = start+": " + (listPosition[0]+1) + "-"+(listPosition[4]+1) +" / " +size;
        }
        toWriteItem = new MusicItem(sb,FontGenerator.titleFont,toWrite,cam,10,game.getHeight()-5);
    }
    @Override
    public void dispose() {

    }

}
