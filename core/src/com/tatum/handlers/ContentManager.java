/*****
Container for game resources
*****/
package com.tatum.handlers;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
//gdx stuff
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.tatum.music.TrackData;


public class ContentManager {
    private HashMap<String, Texture> textures = new HashMap<String, Texture>();
    private HashMap<String, Music> music = new HashMap<String, Music>();
    private HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    private HashMap<String, File> files = new HashMap<String, File>();
    private HashMap<String, TiledMap> maps = new HashMap<String, TiledMap>();
    private HashMap<String, TrackData> tracksData = new HashMap<String, TrackData>();
    public ContentManager() {
        loadResources();
    }
    private void loadResources(){
        loadTexture("res/images/backgrounds/menu2.png");
        loadTexture("res/images/bgs.png");
        loadTexture("res/images/hud2.png");
        loadTexture("res/images/bunny.png");
        loadTexture("res/images/crystal.png");
        loadTexture("res/images/blocks2.png");
        loadTexture("res/images/backgrounds/GrassColour.png");
        //resources.loadTexture("res/images/spikes.png");
        loadTexture("res/images/Play.png");
        loadTexture("res/images/Leader.png");
        loadTexture("res/images/Track.png");
        loadTexture("res/images/PlatformerPack/Player/p1_walk/fix.png");
        loadTexture("res/images/PlatformerPack/Player/mini_walk_combined.png");
        loadTexture("res/images/mm/sprites.png");
        loadTexture("res/images/mm/signs.png");
        loadTexture("res/images/mm/tatumLogoMini.png");
        //arrows up and down
        loadTexture("res/images/arrowDown.png");
        loadTexture("res/images/arrowUp.png");
        loadTexture("res/images/arrowUpFast.png");
        loadTexture("res/images/arrowDownFast.png");

        loadSound("res/sfx/jump.wav");
        loadSound("res/sfx/crystal.wav");
        loadSound("res/sfx/levelselect.wav");
        loadSound("res/sfx/hit.wav");
        loadSound("res/sfx/changeblock.wav");

        loadFile("res/music/test.mp3");
    }

    //Setters
    public void loadTexture(String path) {
        if(Gdx.files.internal(path) == null) return;
        Texture t = new Texture(path);
        textures.put(makeKey(path), t);
    }
    public Music loadMusic(String path) {
        if(Gdx.files.external(path) == null) return null;
        Music m = Gdx.audio.newMusic(Gdx.files.external(path));
        music.put(makeKey(path), m);
        return m;
    }
    public void loadSound(String path) {
        if(Gdx.files.internal(path) == null) return;
        Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(makeKey(path), s);
    }
    public void loadFile(String path) {
        if(Gdx.files.internal(path) == null) return;
        File f = Gdx.files.internal(path).file();
        files.put(makeKey(path), f);
    }
    public void addMap(String key, TiledMap map){
        maps.put(key, map);
    }
    public void addTrackData(String key, TrackData track){
        tracksData.put(key, track);
    }
    //unified method for all above to produce a key
    public String makeKey(String path) {
        int slashIndex = path.lastIndexOf('/');
        int dotIndex = path.lastIndexOf('.');
        if(slashIndex < 0) {
            return path.substring(0, dotIndex);
        }
        return path.substring(++slashIndex, dotIndex);
    }
    //GETTERS
    public Texture getTexture(String key) {
        return textures.get(key);
    }
    public Music getMusic(String key) {
        return music.get(key);
    }
    public Sound getSound(String key) {
        return sounds.get(key);
    }
    public File getFile(String key) {
        return files.get(key);
    }
    public TiledMap getMap(String key){
        return maps.get(key);
    }
    public TrackData getTrackData(String key){
        return tracksData.get(key);
    }
    //DESTRUCTOR
    public void removeAll() {
        dispose_objs(textures);
        dispose_objs(music);
        dispose_objs(sounds);
        textures.clear();
        music.clear();
        sounds.clear();
    }
    private void dispose_objs(HashMap<String, ? extends Disposable> map) {
        for(Disposable o : map.values())
            if(o != null)
                o.dispose();
    }
    public void printKeys(){
        Set<String> h =  textures.keySet();
        System.out.println("KEYS KEYS KEYS");
        for(String s : h ){
            System.out.println(s);
        }
    }
    //END
}
