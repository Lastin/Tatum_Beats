/*****
Container for game resources
*****/
package com.tatum.handlers;
import java.util.Collection;
import java.util.HashMap;
//gdx stuff
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;


public class Content {
    private HashMap<String, Texture> textures;
    private HashMap<String, Music> music;
    private HashMap<String, Sound> sounds;
    public Content() {
      textures = new HashMap<String, Texture>();
      music = new HashMap<String, Music>();
      sounds = new HashMap<String, Sound>();
    }

    //Textures
    public void loadTexture(String path) {
        Texture t = new Texture(Gdx.files.internal(path));
        textures.put(makeKey(path), t);
    }
    //Music
    public void loadMusic(String path) {
        Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.put(makeKey(path), m);
    }
    //SFX
    public void loadSound(String path) {
        Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(makeKey(path), s);
    }
    //unified method for all above to produce a key
    private String makeKey(String path) {
        int slashIndex = path.lastIndexOf('/');
        int dotIndex = path.lastIndexOf('.');
        if(slashIndex < 0) {
            return path.substring(0, dotIndex);
        }
        return path.substring(++slashIndex, dotIndex);
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
            o.dispose();
    }
    //END
}
