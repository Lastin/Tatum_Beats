package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import com.tatum.handlers.Background;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.SelectionHandler;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;
import com.tatum.handlers.MusicItem;

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
    public Select(GameStateManager gsm,Background bg) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        selectionHandler = new SelectionHandler(Gdx.files.external(""));
        fontGenerator = new FontGenerator();
        Texture menu = resources.getTexture("menu2");

        this.bg = bg;
        cont = gsm.getGame().getResources();

        //get textures for arrows
        Texture downArrow = cont.getTexture("arrowDown");
        Texture upArrow = cont.getTexture("arrowUp");
        Texture downArrowFast = cont.getTexture("arrowDownFast");
        Texture upArrowFast = cont.getTexture("arrowUpFast");

        //create arrows and place them at side of screen
        upButtonFast = new GameButton(resources, new TextureRegion(upArrowFast,70,85), game.getWidth()-30, game.getHeight()-50, cam);
        upButton = new GameButton(resources, new TextureRegion(upArrow,70,70), game.getWidth()-30, game.getHeight()-90, cam);
        downButton = new GameButton(resources, new TextureRegion(downArrow,70,70), game.getWidth()-30, game.getHeight()-130, cam);
        downButtonFast = new GameButton(resources, new TextureRegion(downArrowFast,70,85), game.getWidth()-30, game.getHeight()-170, cam);
        toWriteItem = new MusicItem(sb,fontGenerator.listFont,"",cam,10,game.getHeight()-5);

        listPosition= new int[5];
        setListPosition("start");
        setMusicItems();

        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        game.setTouchInput();
    }

    @Override
    public void handleInput() {
        if(upButton.isClicked()){

            if(listPosition[0]!=0) { // make sure list does not go below 0

                setListPosition("up");
                setMusicItems();
            }
            } // if up button is clicked move up file list by 1
        else if(upButtonFast.isClicked()){

            if(listPosition[0]!=0) {
                setListPosition("quickup");
                setMusicItems();
            }
        } // if fast up is pressed move up list by 5 or as much as possible


        else if(downButton.isClicked()){
            if(listPosition[4]<selectionHandler.getScreenCount()-1){

                    setListPosition("down");
                    setMusicItems();
            }
         } // if down button is pressed, move down the file list by one

        else if(downButtonFast.isClicked()){
            if(listPosition[4]<selectionHandler.getScreenCount()-1){

                setListPosition("quickdown");
                setMusicItems();
            }
        } // if fast down is pressed, move down the file list by 5 or as much as possible

        else if(backButton.isClicked()){
            if(!(selectionHandler.getCurrent().equals(Gdx.files.external("")))){
                selectionHandler = new SelectionHandler(selectionHandler.getCurrent().parent());

                setMusicItems();
                return;
            }
        } // if previous directory button is pressed go up a directory unless already in route
        else if(backButtonMenu.isClicked()){
            gsm.setState(new Menu(gsm));
        }   // if back button is pressed, go back to menu

        else{
            for(int i =0;i<musicItems.size();i++){
                if(musicItems.get(i).isClicked()){
                    String text = musicItems.get(i).getText();
                    if(selectionHandler.isDir(text)){
                        selectionHandler = new SelectionHandler(selectionHandler.getChild(text));
                        setListPosition("start");
                        setMusicItems();
                        return;
                    } // if a list is clicked, change the select screen to represent that directory and display contained files in list
                    else{
                        musicItems.get(i).getText();
                        gsm.setState(new Expert(gsm,bg,selectionHandler.getChildFullPath(text)));
                        return; // if a track is clicked, return to the menu, passing the path to the track
                    }
                }
            }
        }
    }

    @Override
    public void update(float dt) {
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
        handleInput();
    }   // upadte the background, buttons and all shown files

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
    }   // render the background, buttons and all shown files

    private void setMusicItems(){

        ArrayList<String> names = selectionHandler.getPrunedNames();    // get list of files/directories that are relevant
        backButtonMenu = new MusicItem(sb,fontGenerator.listFont,"Back to Menu",cam,10,game.getHeight()-20);
        backButton = new MusicItem(sb,fontGenerator.listFont,"Previous Directory",cam,10,game.getHeight()-50);

            try{
                musicItems= new ArrayList<MusicItem>();
                int bufferFromCeil = 75;
                for(int i =0;i<5;i++){
                    BitmapFont font = fontGenerator.listFont;
                    String name = names.get(listPosition[i]);
                    if(selectionHandler.isDir(name)){
                        font = fontGenerator.listFolderFont;
                    } else if(selectionHandler.isLegalFormat(name)){
                        font = fontGenerator.listLegalFormatFont;
                    }

                    musicItems.add(new MusicItem(sb,font,name,cam,10,game.getHeight()-bufferFromCeil));
                    bufferFromCeil+=25;
                }   //create file text items for current list position - space evening down the screen
            }catch (IndexOutOfBoundsException e){
                // if there are less than 5 items to display
                // easier than creating several if cases
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
        // this class sets the "view" that we have of the files. As we only see 5 at a time,
        // and there might be many more. We move up and down the list of possible files using the arrows
        // which pass a string param moving our position but a specified amount
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
        toWriteItem = new MusicItem(sb,fontGenerator.titleFont,toWrite,cam,10,game.getHeight()-5);

        //this method creates a string of the current directory that you are in and displays it at
        // the top of the screen - additionally it specifies the file numbers we are currently viewing
        // out of the total to further aid navigation
    }
    @Override
    public void dispose() {

    }

}
