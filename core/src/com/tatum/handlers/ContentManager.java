/*****
Container for game resources
*****/
package com.tatum.handlers;
import java.io.File;
import java.util.HashMap;
//gdx stuff
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.tatum.music.TrackData;


public class ContentManager {
    private HashMap<String, Texture> textures;
    private HashMap<String, Music> music;
    private HashMap<String, Sound> sounds;
    private HashMap<String, File> files;
    private HashMap<String, TiledMap> maps;
    private HashMap<String, TrackData> tracksData;
    public ContentManager() {
      textures = new HashMap<String, Texture>();
      music = new HashMap<String, Music>();
      sounds = new HashMap<String, Sound>();
      files = new HashMap<String, File>();
      maps = new HashMap<String, TiledMap>();
      tracksData = new HashMap<String, TrackData>();
    }

    //Setters
    public void loadTexture(String path) {
        if(Gdx.files.internal(path) == null) return;
        Texture t = new Texture(path);
        textures.put(makeKey(path), t);
    }
    public String loadMusic(String path) {
        if(Gdx.files.internal(path) == null) return "";
        Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
        String key = makeKey(path);
        music.put(key, m);
        return key;
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
    //END
}
