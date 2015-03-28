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
        FileHandle fH = Gdx.files.external(trackPath);
        File file = fH.file();

        String extRoot = Gdx.files.getExternalStoragePath();
        String locRoot = Gdx.files.getLocalStoragePath();

        String trackName = trackPath.replaceAll("/","");

        //trackName = trackName.replaceAll(".","");

        File dirCheck = Gdx.files.external("musicdata/"+trackName).file();

        trackInformation = new HashMap<String, Object>();
        if(dirCheck.exists()) {
            /*
            InputStream is = new FileInputStream("musicdata/"+trackName+"/meta.json");
            JsonReader rdr = Json.createReader(is);
            JsonObject meta = rdr.readObject();
            System.out.println(meta);
            is = new FileInputStream("musicdata/"+trackName+"/track.json");
            rdr = Json.createReader(is);
            JsonObject track = rdr.readObject();

            is = new FileInputStream("musicdata/"+trackName+"/audioSum.json");
            rdr = Json.createReader(is);
            JsonObject audio = rdr.readObject();

            is = new FileInputStream("musicdata/"+trackName+"/bars.json");
            rdr = Json.createReader(is);
            JsonArray bars = rdr.readArray();

            is = new FileInputStream("musicdata/"+trackName+"/beats.json");
            rdr = Json.createReader(is);
            JsonArray beats = rdr.readArray();

            is = new FileInputStream("musicdata/"+trackName+"/tatums.json");
            rdr = Json.createReader(is);
            JsonArray tatums = rdr.readArray();

            is = new FileInputStream("musicdata/"+trackName+"/sections.json");
            rdr = Json.createReader(is);
            JsonArray sections = rdr.readArray();

            is = new FileInputStream("musicdata/"+trackName+"/segments.json");
            rdr = Json.createReader(is);
            JsonArray segments = rdr.readArray();
            */

            InputStream is = Gdx.files.external("musicdata/"+trackName+"/meta.json").read();
            JsonReader rdr = Json.createReader(is);
            JsonObject meta = rdr.readObject();
            System.out.println(meta);

            is = Gdx.files.external("musicdata/"+trackName+"/track.json").read();
            rdr = Json.createReader(is);
            JsonObject track = rdr.readObject();

            is = Gdx.files.external("musicdata/"+trackName+"/audioSum.json").read();
            rdr = Json.createReader(is);
            JsonObject audio = rdr.readObject();

            is = Gdx.files.external("musicdata/"+trackName+"/bars.json").read();
            rdr = Json.createReader(is);
            JsonArray bars = rdr.readArray();

            is = Gdx.files.external("musicdata/"+trackName+"/beats.json").read();
            rdr = Json.createReader(is);
            JsonArray beats = rdr.readArray();

            is = Gdx.files.external("musicdata/"+trackName+"/tatums.json").read();
            rdr = Json.createReader(is);
            JsonArray tatums = rdr.readArray();

            is = Gdx.files.external("musicdata/"+trackName+"/sections.json").read();
            rdr = Json.createReader(is);
            JsonArray sections = rdr.readArray();

            is = Gdx.files.external("musicdata/"+trackName+"/segments.json").read();
            rdr = Json.createReader(is);
            JsonArray segments = rdr.readArray();

            trackInformation.put("Meta", meta);
            trackInformation.put("Track", track);
            trackInformation.put("audio_summary", audio);
            trackInformation.put("Bars", bars);
            trackInformation.put("Beats", beats);
            trackInformation.put("Tatums", tatums);
            trackInformation.put("Sections", sections);
            trackInformation.put("Segments", segments);
        }
        else{

            realUpload(file);

        }
        artistTwitterHandle(trackName);

    }
    private void realUpload(File file) throws EchoNestException, IOException {

        en = new EchoNestAPI("B0EHJCUJPBJOZ5MOP");
        en.setTraceSends(true);
        en.setTraceRecvs(false);
        track = null;

        track = en.uploadTrack(file);
        while (track.getStatus() == Track.AnalysisStatus.PENDING) {
            //do nothing
        }


        //System.out.println(track.getOriginalID()); // weird stuff
        //System.out.println(track.getSongID()); // SOMBINS136004B720
        //System.out.println(track.getAudioUrl()); // NULL
        //System.out.println(track.getForeignID()); // NULL
        //System.out.println(track.getID()); // THIS ONE!! Track ID, TRAESKT145E82A824C for test
        URL url = null;
        trackInformation = new HashMap<String, Object>();

            try {
                url = new URL(track.getAnalysisURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try { //changed, because it seems that android does not support try with resources
                InputStream is = url.openStream();

                JsonReader rdr = Json.createReader(is);
                JsonObject struct = rdr.readObject();

                trackInformation.put("Meta", struct.getJsonObject("meta"));
                trackInformation.put("Track", struct.getJsonObject("track"));
                trackInformation.put("Bars", struct.getJsonArray("bars"));
                trackInformation.put("Beats", struct.getJsonArray("beats"));
                trackInformation.put("Tatums", struct.getJsonArray("tatums"));
                trackInformation.put("Sections", struct.getJsonArray("sections"));
                trackInformation.put("Segments", struct.getJsonArray("segments"));
                // above returns a java List type containing all JsonObjects in our JsonArray!

                audioSummary();
                String trackname = trackPath.replaceAll("/", "");
                //trackname = trackname.replaceAll(".","");
                FileHandle ff = Gdx.files.internal("");
                boolean exists = Gdx.files.external("doitexist.txt").exists();
                boolean isDirectory = Gdx.files.external("test/").isDirectory();
                File fff = Gdx.files.external("musicData").file();
                if (!fff.exists()) {
                    fff.mkdir();
                    System.out.println("BEEP BEEP BEPP");
                } /// if music directory does not exist create

                boolean test = (Gdx.files.external("musicdata/" + trackname).file()).mkdir();
                if (test) System.out.println("good shit fam"); //make directory for song

                ff = Gdx.files.external("musicdata/" + trackname + "/meta.json");
                OutputStream OS = ff.write(false);
                OS.write(trackInformation.get("Meta").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/track.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Track").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/bars.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Bars").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/beats.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Beats").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/tatums.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Tatums").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/sections.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Sections").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/segments.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("Segments").toString().getBytes());
                OS.close();

                ff = Gdx.files.external("musicdata/" + trackname + "/audioSum.json");
                OS = ff.write(false);
                OS.write(trackInformation.get("audio_summary").toString().getBytes());
                OS.close();
                //out = new PrintWriter(ff.file());
                //out.println(trackInformation.get("Meta").toString());
                // out.close(); // save metaData to file
            /*
            PrintWriter  out = new PrintWriter("musicdata/"+trackname+"/track.json");
            out.println(trackInformation.get("Track").toString());
            out.close(); // track data to file

            out = new PrintWriter("musicdata/"+trackname+"/bars.json");
            out.println(trackInformation.get("Bars").toString());
            out.close(); // bars data to file

            out = new PrintWriter("musicdata/"+trackname+"/beats.json");
            out.println(trackInformation.get("Beats").toString());
            out.close(); //beats data to file

            out = new PrintWriter("musicdata/"+trackname+"/tatums.json");
            out.println(trackInformation.get("Tatums").toString());
            out.close(); //tatums to file

            out = new PrintWriter("musicdata/"+trackname+"/sections.json");
            out.println(trackInformation.get("Sections").toString());
            out.close(); // sections to file

            out = new PrintWriter("musicdata/"+trackname+"/segments.json");
            out.println(trackInformation.get("Segments").toString());
            out.close();  //segments to file

            out = new PrintWriter("musicdata/"+trackname+"/audioSum.json");
            out.println(trackInformation.get("audio_summary").toString());
            out.close();  //segments to file
            */

            } catch (Exception e) {
                System.out.println("Error line 116");
                e.printStackTrace();
            }

		/*
        Map<String, String> parameters = new HashMap<String, String>();

    	//parameters.put("api_key", "B0EHJCUJPBJOZ5MOP");
    	//parameters.put("filetype", "mp3");
    	parameters.put("track", "@test.mp3");

    	InputStream is = null;
        try {
        	is = new FileInputStream("res/music/test.mp3");
        	is.close();
        }
        catch(Exception e) { e.printStackTrace(); }

    	Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
    	request.setHeader("Content-Type", "application/x-www-form-urlencoded");
    	request.setHeader("Content-Type", "application/octet-stream");

        request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        //request.setContent(is, fileSize);

        //request.setUrl("http://developer.echonest.com/api/v4/track/upload");
        //request.setUrl("http://developer.echonest.com/api/v4/track/profile?api_key=FILDTEOIK2HBORODV&format=json&id=TRTLKZV12E5AC92E11&bucket=audio_summary");
        request.setUrl("http://developer.echonest.com/api/v4/track/upload?api_key=B0EHJCUJPBJOZ5MOP&filetype=mp3");

        System.out.println(request.getContent());
    	Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {

            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("kjlkjbfcxkcxv", "response : " + httpResponse.getResultAsString());
            }


            public void failed(Throwable t) {
                Gdx.app.error("HttpRequestExample", "something went wrong", t);
            }


            public void cancelled() {
                Gdx.app.log("HttpRequestExample", "cancelled");
            }
        });
		 */
    }
    public FileUploaderGDX(String trackPath) {
        this.trackPath = trackPath;
    }

    public void setPath(String trackPath) {
        this.trackPath = trackPath;
    }

    public HashMap<String, Object> getJsonMap() {
        return trackInformation;
    }

    public String getUploadProgress() throws EchoNestException {


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

        if(twitter.exists()) {
            InputStream is = Gdx.files.external("musicdata/"+trackName+"/twitterhandle.json").read();
            JsonReader rdr = Json.createReader(is);
            JsonObject artist = rdr.readObject();
            trackInformation.put("twitter",artist);
        }
        else {
            boolean outOfApiCalls = true;
            while(outOfApiCalls) {
                URL url = null;
                try {
                    JsonObject meta = (JsonObject) trackInformation.get("Meta");
                    String artist = meta.getString("artist").replaceAll(" ","+");
                    if(artist.equals("")){
                        break;
                    }
                    url = new URL("http://developer.echonest.com/api/v4/artist/twitter?api_key=B0EHJCUJPBJOZ5MOP&name="+artist+"&format=json");
                    //	url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id=TRTLKZV12E5AC92E11&bucket=audio_summary");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream is = url.openStream();

                    JsonReader rdr = Json.createReader(is);
                    JsonObject struct = rdr.readObject();
                    struct = struct.getJsonObject("response");
                    if(!struct.getJsonObject("status").getString("message").equals("Success")){
                        //the api is our of calls
                        System.out.println("out of Api Calls");
                        continue;
                    }else{
                        System.out.println("Got the calls");
                    }
                    JsonObject artist = struct.getJsonObject("artist");
                    trackInformation.put("twitter", artist);

                    FileHandle ff = Gdx.files.external("musicdata/" + trackName + "/twitterhandle.json");
                    OutputStream OS = ff.write(false);
                    OS.write(trackInformation.get("twitter").toString().getBytes());
                    OS.close();

                } catch (Exception e) {
                    e.printStackTrace(); //catching any error, not a good habit, but there are like 500 errors thrown
                }
                break;
            }
        }
    }
    public void audioSummary(){
        boolean outOfApiCalls = true;
        while(outOfApiCalls) {
            URL url = null;
            try {
                url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id="
                        + track.getID() + "&bucket=audio_summary");
                //	url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id=TRTLKZV12E5AC92E11&bucket=audio_summary");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                InputStream is = url.openStream();

                JsonReader rdr = Json.createReader(is);
                JsonObject struct = rdr.readObject();
                struct = struct.getJsonObject("response");
                if(!struct.getJsonObject("status").getString("message").equals("Success")){
                    //the api is our of calls
                    System.out.println("out of Api Calls");
                    continue;
                }else{
                    System.out.println("Got the calls");
                }
                JsonObject track1 = struct.getJsonObject("track");
                trackInformation.put("audio_summary", track1.getJsonObject("audio_summary"));
            } catch (Exception e) {
                e.printStackTrace(); //catching any error, not a good habit, but there are like 500 errors thrown
            }
            break;
        }
    }
}