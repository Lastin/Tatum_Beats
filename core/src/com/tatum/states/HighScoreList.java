package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tatum.handlers.Background;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.MusicItem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class HighScoreList extends GameState{
    private boolean debug = false;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private ContentManager cont;
    private ArrayList<MusicItem> trackNames;
    private ArrayList<MusicItem> metaData;
    private MusicItem backButton;
    private GameButton upButton;
    private GameButton downButton;
    private GameButton upButtonFast;
    private GameButton downButtonFast;
    private MusicItem notracks;
    private int listPosition[];
    MusicItem toWriteItem;
    private HashMap<String,Integer> highScores;
    private FontGenerator fontGenerator;

    public HighScoreList(GameStateManager gsm, FontGenerator fontGenerator, Background bg) {
        super(gsm);
        this.fontGenerator = fontGenerator;
        highScores = getHighScores();
        fontGenerator = new FontGenerator();
        this.bg = bg;
        cont = gsm.getGame().getResources();

        //get textures for arrows
        Texture downArrow = cont.getTexture("arrowDown");
        Texture upArrow = cont.getTexture("arrowUp");
        Texture downArrowFast = cont.getTexture("arrowDownFast");
        Texture upArrowFast = cont.getTexture("arrowUpFast");

        //initilize arrows
        upButtonFast = new GameButton(resources, new TextureRegion(upArrowFast,70,85), game.getWidth()-30, game.getHeight()-50, cam);
        upButton = new GameButton(resources, new TextureRegion(upArrow,70,70), game.getWidth()-30, game.getHeight()-90, cam);
        downButton = new GameButton(resources, new TextureRegion(downArrow,70,70), game.getWidth()-30, game.getHeight()-130, cam);
        downButtonFast = new GameButton(resources, new TextureRegion(downArrowFast,70,85), game.getWidth()-30, game.getHeight()-170, cam);
        toWriteItem = new MusicItem(sb, fontGenerator.listFont,"",cam,10,game.getHeight()-5);

        //create the list position (we have max 5 items on the screen at a time)
        listPosition= new int[5];
        setListPosition("start"); // set the list to be at the top/start
        setMusicItems();       //create the initial list items
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setNoTracks();
            }
        }); // creates the "Go play first" string (in separate thread to improve performance)

        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        game.setTouchInput(); // change input to touch incase has come from game and input set to swipe
    }
    private void setNoTracks(){
        //cut down as string is static
        notracks = new MusicItem(sb,fontGenerator.makeFont(50, Color.BLACK),"Go Play The Game First!",cam,15,151); // create go play first string in centre of state/screen

    }

    private HashMap<String,Integer> getHighScores(){
        HashMap<String,Integer> highScores = new HashMap<String, Integer>();

        FileHandle musicData = Gdx.files.external("/musicdata/");
        for(FileHandle file: musicData.list()){
            if(file.isDirectory())
                if (file.child("userData/test/score.json").exists())
                    if(file.child("/meta.json").exists()){
                        InputStream is = file.child("userData/test/score.json").read();
                        JsonReader rdr = Json.createReader(is);
                        JsonObject hi = rdr.readObject();
                        int score = hi.getInt("Score");
                        is = file.child("/meta.json").read();
                        rdr = Json.createReader(is);
                        JsonObject meta = rdr.readObject();
                        String artistName = meta.getString("artist");
                        String trackName = meta.getString("title");
                        String albumName = meta.getString("album");
                        highScores.put(artistName+"~"+albumName+"~"+trackName,score);
                    }
        } // this block checks through all the users data in the music data folder (all songs attempted)
        // if the song has a score and some meta data to display, it is added to the Hashmap of highscores to display

        return highScores;
    }

    @Override
    public void handleInput() {
        if(upButton.isClicked()){

            if(listPosition[0]!=0) { // makes sure list does not go below 0
                setListPosition("up");
                setMusicItems();
            }
        } // check if the up button has been clicked, if so recreate the list with from the new position
        else if(upButtonFast.isClicked()){
            if(listPosition[0]!=0) {
                setListPosition("quickup");
                setMusicItems();
            }
        }   // same as above, except moves up by 5 instead of 1


        else if(downButton.isClicked()){
            if(listPosition[4]<highScores.size()-1){
                setListPosition("down");
                setMusicItems();
            }
        }   // if down button has been pressed, move list down by one

        else if(downButtonFast.isClicked()){
            if(listPosition[4]<highScores.size()-1){
                setListPosition("quickdown");
                setMusicItems();
            }
        } // save as above, moves down by 5 instead of 1

        else if(backButton.isClicked()){
            gsm.setState(new Menu(gsm,bg));
        }   // if the back button is pressed go back to menu
        else if(Gdx.input.isTouched())
            for(int i =0;i< trackNames.size();i++){
                if(trackNames.get(i).isClicked()||metaData.get(i).isClicked()){
                    String track = trackNames.get(i).getText();
                    String meta = metaData.get(i).getText();
                    String[] split = meta.split("~");
                    int score = highScores.get(meta+"~"+track);
                    gsm.setState(new HighScoreView(gsm, fontGenerator, track,split[0],split[1],"no handle",score,bg));
                }
            } //checks if the list items have been clicked, if so go to the highscore view for that list item

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
        for (int i =0;i< trackNames.size();i++){
            trackNames.get(i).update(dt);
        }
        for (int i =0;i< metaData.size();i++){
            metaData .get(i).update(dt);
        }
        backButton.update(dt);
    }   // updates all of the buttons, list items and background

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        if(metaData.size()==0 && notracks !=null){
            notracks.render();
        }else {
            upButton.render(sb);
            downButton.render(sb);
            upButtonFast.render(sb);
            downButtonFast.render(sb);
            toWriteItem.renderFull();
            toWriteItem.render();
            for (int i = 0; i < trackNames.size(); i++) {
                trackNames.get(i).render();
            }
            for (int i = 0; i < metaData.size(); i++) {
                metaData.get(i).renderFull();
            }
        }


        backButton.render();
    }   // renders all the buttons, list items and background

    private void setMusicItems(){
        ArrayList<String> names;
        ArrayList<String> meta;
        Set<String> keysSet;
        try {
            names = new ArrayList<String>();
            meta = new ArrayList<String>();
            keysSet = highScores.keySet();
            ArrayList<String> keys = new ArrayList<String>();
            for (String key : keysSet) {
                keys.add(key);
            }
            Collections.sort(keys);
            for (String key : keys) {
                try {
                    String[] split = key.split("~");
                    names.add(split[2]);
                    meta.add(split[0] + "~" + split[1]);
                }catch(ArrayIndexOutOfBoundsException e){
                    //dis a broken one
                }
            }
        }catch (Exception e){
            // some times crashes with bad track data
            e.printStackTrace();
            return;
        }
        // This above segment gets all of the Highscore meta data and sorts it alphabetically
        // Two lists are produced which can then be accessed and list list items made from
        backButton = new MusicItem(sb, fontGenerator.listFont, "Back to Menu", cam, 10, game.getHeight() - 10);

        try {
            trackNames = new ArrayList<MusicItem>();
            metaData = new ArrayList<MusicItem>();
            int bufferFromCeil = 40;
            for (int i = 0; i < 5; i++) {
                String name = names.get(listPosition[i]);
                trackNames.add(new MusicItem(sb, fontGenerator.listFont, name, cam, 10, game.getHeight() - bufferFromCeil));
                String metaD = meta.get(listPosition[i]);
                metaData.add(new MusicItem(sb, fontGenerator.underListFont, metaD, cam, 10, game.getHeight() - bufferFromCeil - 15));
                bufferFromCeil += 35;
            }// create list items showing song names in big and artist/album in small - space accordingly
        } catch (IndexOutOfBoundsException e) {
            //incase there are less than 5 items to display, simpler than createing several if cases
        }

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
            if(listPosition[4]+5>=highScores.size()-1)
                setListPosition("quickdown2ElectricBoogaloo");
            else
                for(int i =0;i<5;i++){
                    listPosition[i]+=5;
                }
        }
        else if (position.equals("quickdown2ElectricBoogaloo")){
            int value = highScores.size()-1;
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
    } // this class sets the 5 items to be viewed out of all possible items
    // it takes a string parameter and based upon that moves the list accordingly
    // error handling for our of bounds is dealt with by makes sure the values never go out of bounds of
    // the amount of highscores (<0 and >size)

    @Override
    public void dispose() {

    }
}
