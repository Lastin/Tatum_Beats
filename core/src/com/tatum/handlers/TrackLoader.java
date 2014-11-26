//TODO: make the tread query device for all song on it, create a list, preferably with the paths of each song. Later allow user to pick one from the list.
package com.tatum.handlers;

import android.content.ContentResolver;

import java.util.ArrayList;

public class TrackLoader extends Thread{
    private ArrayList<Song> songs =  new ArrayList<Song>();
    private class Song {
        private long id;
        private String title;
        private String artist;
        private Song(long id, String title, String artist) {
            this.id = id;
            this.title = title;
            this.artist = artist;
        }
    }
    public void run() {
        //ContentResolver musicResolver = get();

    }
}
