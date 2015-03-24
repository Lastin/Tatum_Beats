package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.tatum.handlers.SelectionHandler;
import com.tatum.music.MusicItem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
    private int listPosition[];
    private FontGenerator fontGenerator;
    MusicItem toWriteItem;
    private HashMap<String,Integer> highScores;

    public HighScoreList(GameStateManager gsm) {
        super(gsm);
        highScores = getHighScores();
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

    private HashMap<String,Integer> getHighScores(){
        HashMap<String,Integer> highScores = new HashMap<String, Integer>();

        FileHandle musicData = Gdx.files.external("/musicdata/");
        for(FileHandle file: musicData.list()){
            if(file.isDirectory())
                if (file.child("userData/test/score.json").exists())
                    if(file.child("/meta.json").exists()){
                        System.out.println(file);
                        InputStream is = file.child("userData/test/score.json").read();
                        JsonReader rdr = Json.createReader(is);
                        JsonObject hi = rdr.readObject();
                        int score = hi.getInt("Score");
                        System.out.println(score);
                        is = file.child("/meta.json").read();
                        rdr = Json.createReader(is);
                        JsonObject meta = rdr.readObject();
                        String artistName = meta.getString("artist");
                        String trackName = meta.getString("title");
                        String albumName = meta.getString("album");
                        highScores.put(artistName+"~"+albumName+"~"+trackName,score);
                }
        }
        Set<String> keySet = highScores.keySet();

        for(String key: keySet){
            System.out.println(key+": "+highScores.get(key));
        }
        return highScores;
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
            if(listPosition[4]<highScores.size()-1){
                System.out.println("in");
                setListPosition("down");
                setMusicItems();
            }
        }

        if(downButtonFast.isClicked()){
            System.out.println("Down click");
            if(listPosition[4]<highScores.size()-1){
                System.out.println("in");
                setListPosition("quickdown");
                setMusicItems();
            }
        }

        if(backButton.isClicked()){
           gsm.setState(new Menu(gsm));
        }
        for(int i =0;i< trackNames.size();i++){
            if(trackNames.get(i).isClicked()||metaData.get(i).isClicked()){
                String track = trackNames.get(i).getText();
                String meta = metaData.get(i).getText();
                String[] split = meta.split("~");
                int score = highScores.get(meta+"~"+track);
                gsm.setState(new HighScoreView(gsm,track,split[0],split[1],score));
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
        for (int i =0;i< trackNames.size();i++){
            trackNames.get(i).update(dt);
        }
        for (int i =0;i< metaData.size();i++){
            metaData .get(i).update(dt);
        }
        backButton.update(dt);
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);

        upButton.render(sb);
        downButton.render(sb);
        upButtonFast.render(sb);
        downButtonFast.render(sb);
        toWriteItem.renderFull();
        toWriteItem.render();


        for (int i =0;i< trackNames.size();i++){
            trackNames.get(i).render();
        }
        for (int i =0;i< metaData.size();i++){
            metaData .get(i).renderFull();
        }
        backButton.render();
    }

    private void setMusicItems(){
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> meta = new ArrayList<String>();
        Set<String> keysSet = highScores.keySet();
        ArrayList<String> keys = new ArrayList<String>();
        for(String key:keysSet){
            keys.add(key);
        }
        Collections.sort(keys);
        for(String key : keys){
            String[] split = key.split("~");
            names.add(split[2]);
            meta.add(split[0]+"~"+split[1]);
        }
        backButton = new MusicItem(sb,FontGenerator.listFont,"Back to Menu",cam,10,game.getHeight()-10);
        System.out.println(names.size());
        try{
            trackNames = new ArrayList<MusicItem>();
            metaData = new ArrayList<MusicItem>();
            int bufferFromCeil =40;
            for(int i =0;i<5;i++){
                System.out.println(listPosition[i]);
                String name = names.get(listPosition[i]);
                trackNames.add(new MusicItem(sb, FontGenerator.listFont, name, cam, 10, game.getHeight() - bufferFromCeil));
                String metaD = meta.get(listPosition[i]);
                metaData.add(new MusicItem(sb,FontGenerator.underListFont,metaD,cam,10,game.getHeight()- bufferFromCeil - 15));
                bufferFromCeil+=35;
            }
        }catch (IndexOutOfBoundsException e){

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
    }

    @Override
    public void dispose() {

    }
}
