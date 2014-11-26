//TODO: make the tread query device for all song on it, create a list, preferably with the paths of each song. Later allow user to pick one from the list.
package com.tatum.android;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.tatum.music.Song;

import java.io.File;
import java.util.ArrayList;

public class TrackLoader{
    private File musicFolder;
    private ArrayList<Song> songs =  new ArrayList<Song>();

    public TrackLoader(){
        musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        loadSongs(musicFolder);
        for(Song s : songs)
            Log.d("Song: ", s.getTitle());
    }

    private void loadSongs(File musicFolder) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        for (File f : musicFolder.listFiles()) {
            retriever.setDataSource(musicFolder + "/" + f.getName());
            int secs = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
            int mins= secs / 60;
            secs = secs % 60;
            String duration = String.format("%02d:%02d", mins, secs);
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (artist == null || artist.equals(""))
                artist = "Unknown";
            String title  = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (title == null || title.equals(""))
                title = f.getName();
            String path = f.getAbsolutePath();
            Song s = new Song(title, duration, artist, path);
            songs.add(s);
        }
    }
}
