package com.tatum.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;

/**
 * Created by miratepuffin on 13/11/14.
 */

public class FileUploaderGDX {
    PrintStream supresser = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            //supressed
        }
    });

    private EchoNestAPI en;
    private Track track;
    private String trackPath;
    //-F "api_key=FILDTEOIK2HBORODV" -F "filetype=mp3" -F "track=@audio.mp3"
    HashMap<String, Object> trackInformation;

    public void uploadGDX() throws EchoNestException, IOException {
        //System.setOut(supresser);
        FileHandle fH = Gdx.files.external(trackPath); //get file from external source
        File file = fH.file();

        String trackName = trackPath.replaceAll("/",""); //create unique folder naem by removing / from path

        File dirCheck = Gdx.files.external("musicdata/"+trackName).file();

        trackInformation = new HashMap<String, Object>();
        if(dirCheck.exists()) { // check if that track has already been uploaded previously

            //if so load in echonest data saved to file
            InputStream is = Gdx.files.external("musicdata/"+trackName+"/meta.json").read();
            JsonReader rdr = Json.createReader(is);
            JsonObject meta = rdr.readObject(); //load in meta data

            is = Gdx.files.external("musicdata/"+trackName+"/track.json").read();
            rdr = Json.createReader(is);
            JsonObject track = rdr.readObject(); //read in track data

            is = Gdx.files.external("musicdata/"+trackName+"/audioSum.json").read();
            rdr = Json.createReader(is);
            JsonObject audio = rdr.readObject(); // read in audio summary

            is = Gdx.files.external("musicdata/"+trackName+"/bars.json").read();
            rdr = Json.createReader(is);
            JsonArray bars = rdr.readArray(); //ready in bar data

            is = Gdx.files.external("musicdata/"+trackName+"/beats.json").read();
            rdr = Json.createReader(is);
            JsonArray beats = rdr.readArray(); //read in bears data

            is = Gdx.files.external("musicdata/"+trackName+"/tatums.json").read();
            rdr = Json.createReader(is);
            JsonArray tatums = rdr.readArray(); //read in tatum data

            is = Gdx.files.external("musicdata/"+trackName+"/sections.json").read();
            rdr = Json.createReader(is);
            JsonArray sections = rdr.readArray(); //read in section data

            is = Gdx.files.external("musicdata/"+trackName+"/segments.json").read();
            rdr = Json.createReader(is);
            JsonArray segments = rdr.readArray(); //read in segment data

            //put all read in data into track information hashmap, which is then passed to Trackdata
            // from trackdata perspective, same result if track is uploaded or data loaded from memory
            trackInformation.put("Meta", meta);
            trackInformation.put("Track", track);
            trackInformation.put("audio_summary", audio);
            trackInformation.put("Bars", bars);
            trackInformation.put("Beats", beats);
            trackInformation.put("Tatums", tatums);
            trackInformation.put("Sections", sections);
            trackInformation.put("Segments", segments);
        }
        else{ //if this is the first time, the track is uploaded

            realUpload(file);

        }
        artistTwitterHandle(trackName); // deals with twitter data
        //separate as it was added after everything above, and was easier to keep it on its own

    }
    private void realUpload(File file) throws EchoNestException, IOException {

        //this is the original echonest api call,
        //we couldn't upload the track ourself, so used their working method
        en = new EchoNestAPI("B0EHJCUJPBJOZ5MOP"); //pass our api key
        en.setTraceSends(true);
        en.setTraceRecvs(false);
        track = null;

        track = en.uploadTrack(file);
        while (track.getStatus() == Track.AnalysisStatus.PENDING) {
            //wait for track to upload
        }
        //checking what each method done as there is no API documentation
        //System.out.println(track.getOriginalID()); // weird stuff
        //System.out.println(track.getSongID()); // SOMBINS136004B720
        //System.out.println(track.getAudioUrl()); // NULL
        //System.out.println(track.getForeignID()); // NULL
        //System.out.println(track.getID()); // THIS ONE!! Track ID, TRAESKT145E82A824C for test


        //where our api starts
        URL url = null;
        trackInformation = new HashMap<String, Object>();

            try {
                url = new URL(track.getAnalysisURL()); //create url of the analysis data returned
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try { //changed, because it seems that android does not support try with resources
                InputStream is = url.openStream(); //open stream to url

                JsonReader rdr = Json.createReader(is);
                JsonObject struct = rdr.readObject(); //read full json file into json object

                //get the individual json objects out of the file and put them into the trackInformation hashmap
                trackInformation.put("Meta", struct.getJsonObject("meta"));
                trackInformation.put("Track", struct.getJsonObject("track"));
                trackInformation.put("Bars", struct.getJsonArray("bars"));
                trackInformation.put("Beats", struct.getJsonArray("beats"));
                trackInformation.put("Tatums", struct.getJsonArray("tatums"));
                trackInformation.put("Sections", struct.getJsonArray("sections"));
                trackInformation.put("Segments", struct.getJsonArray("segments"));
                // above returns a java List type containing all JsonObjects in our JsonArray!

                audioSummary(); // does addtional api call to get audio summary data

                String trackname = trackPath.replaceAll("/", ""); //generate track name as above
                FileHandle ff = Gdx.files.internal("");

                File fff = Gdx.files.external("musicData").file();
                if (!fff.exists()) {
                    fff.mkdir();
                } /// if music data directory does not exist create

               Gdx.files.external("musicdata/" + trackname).file().mkdir();


                ff = Gdx.files.external("musicdata/" + trackname + "/meta.json");
                OutputStream OS = ff.write(false);
                OS.write(trackInformation.get("Meta").toString().getBytes());
                OS.close(); //create meta data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/track.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Track").toString().getBytes());
                OS.close(); //create track data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/bars.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Bars").toString().getBytes());
                OS.close(); //create bars data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/beats.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Beats").toString().getBytes());
                OS.close(); //create beats data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/tatums.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Tatums").toString().getBytes());
                OS.close(); //create tatums data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/sections.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Sections").toString().getBytes());
                OS.close(); // create sections data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/segments.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Segments").toString().getBytes());
                OS.close(); //create segments data file and write out to it

                ff = Gdx.files.external("musicdata/" + trackname + "/audioSum.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("audio_summary").toString().getBytes());
                OS.close(); //create audio summary data file and write out to it

            } catch (Exception e) {
                //catching Exception is bad, but there are like 500 different ones thrown and I do the same for all of then - this looks a lot cleaner
                e.printStackTrace();
            }
    }
    public FileUploaderGDX(String trackPath) {
        this.trackPath = trackPath;
    }

    public void setPath(String trackPath) {
        this.trackPath = trackPath;
    } // allow path to change to load different data

    public HashMap<String, Object> getJsonMap() {
        return trackInformation;
    } //return hashmap of data

    public String getUploadProgress() throws EchoNestException {

        //get the progress of the upload
        if (track.getStatus() == Track.AnalysisStatus.COMPLETE) {
            return "complete";
        } else if (track.getStatus() == Track.AnalysisStatus.ERROR) {
            return "error";
        } else if (track.getStatus() == Track.AnalysisStatus.PENDING) {
            return "pending";
        } else if (track.getStatus() == Track.AnalysisStatus.UNAVAILABLE) {
            return "unavailable";
        } else return "unknown";


    }
    public void artistTwitterHandle(String trackName){
        File twitter = Gdx.files.external("musicdata/"+trackName+"/twitterhandle.json").file();

        if(twitter.exists()) { //checks if this track already has twitter data
            InputStream is = Gdx.files.external("musicdata/"+trackName+"/twitterhandle.json").read();
            JsonReader rdr = Json.createReader(is);
            JsonObject artist = rdr.readObject();
            trackInformation.put("twitter",artist); // if so, reads it in and puts it in the hashmap
        }
        else { // if not we must do an additional api call to ge the twitter data
            boolean outOfApiCalls = true;
            while(outOfApiCalls) { // if the upload fails due to no api calls, attempts to rerun until there are some
                URL url = null;
                try {
                    JsonObject meta = (JsonObject) trackInformation.get("Meta");
                    String artist = meta.getString("artist").replaceAll(" ","+"); //gets artist name and replaces spaces with plus for url
                    if(artist.equals("")){
                        break; // if no meta data we cannot run
                    }
                    url = new URL("http://developer.echonest.com/api/v4/artist/twitter?api_key=B0EHJCUJPBJOZ5MOP&name="+artist+"&format=json"); //create url
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream is = url.openStream(); // open stream to url

                    JsonReader rdr = Json.createReader(is);
                    JsonObject struct = rdr.readObject();
                    struct = struct.getJsonObject("response"); // read the response object
                    if(!struct.getJsonObject("status").getString("message").equals("Success")){ //check if the call was successful
                        //the api is our of calls
                        continue; // if not go back to try again
                    }
                    JsonObject artist = struct.getJsonObject("artist"); // get twitter info (called artist)
                    trackInformation.put("twitter", artist); // add to hashmap

                    FileHandle ff = Gdx.files.external("musicdata/" + trackName + "/twitterhandle.json");
                    OutputStream OS = ff.write(false);
                    OS.write(trackInformation.get("twitter").toString().getBytes());
                    OS.close(); //write twitter data to file

                } catch (Exception e) {
                    e.printStackTrace(); //catching any error, not a good habit, but there are like 500 errors thrown
                }
                break; // we were successful, break the loop
            }
        }
    }
    public void audioSummary(){
        boolean outOfApiCalls = true;
        while(outOfApiCalls) { // same as above
            URL url = null;
            try {
                url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id="
                        + track.getID() + "&bucket=audio_summary"); // build url to audio summery for given track
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                InputStream is = url.openStream(); //open stream to url

                JsonReader rdr = Json.createReader(is);
                JsonObject struct = rdr.readObject();
                struct = struct.getJsonObject("response"); //read in the response object
                if(!struct.getJsonObject("status").getString("message").equals("Success")){ // check if we were successful
                    //the api is our of calls
                    continue; // if not retry
                }
                JsonObject track1 = struct.getJsonObject("track");
                trackInformation.put("audio_summary", track1.getJsonObject("audio_summary")); //get the tsummary data and store it in the hashmap
            } catch (Exception e) {
                e.printStackTrace(); //catching any error, not a good habit, but there are like 500 errors thrown
            }
            break; //we were successful - break
        }
    }
}