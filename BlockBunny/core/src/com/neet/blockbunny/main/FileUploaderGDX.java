package com.neet.blockbunny.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.badlogic.gdx.net.HttpParametersUtils;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;

/**
 * Created by miratepuffin on 13/11/14.
 */

public class FileUploaderGDX {
	private EchoNestAPI en;

	private String trackPath;
	//-F "api_key=FILDTEOIK2HBORODV" -F "filetype=mp3" -F "track=@audio.mp3" 
	HashMap<String, Object> trackInformation;
	public void uploadGDX() throws EchoNestException, IOException {

		File file = new File(trackPath);
		System.out.println(file.getAbsolutePath());
		//Long fileSize = file.getTotalSpace();
		en = new EchoNestAPI("B0EHJCUJPBJOZ5MOP");
		en.setTraceSends(true);
		en.setTraceRecvs(false);
		Track track = null;

		track = en.uploadTrack(file);
		while(track.getStatus() == Track.AnalysisStatus.PENDING){
			//do nothing
		}

		System.out.println(track.getOriginalID()); // weird stuff
		System.out.println(track.getSongID()); // SOMBINS136004B720
		System.out.println(track.getAudioUrl()); // NULL
		System.out.println(track.getForeignID()); // NULL
		System.out.println(track.getID()); // THIS ONE!! Track ID, TRAESKT145E82A824C for test
		
		URL url = null;
		trackInformation = new HashMap<String, Object>();
		try {
			url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id="
					+ track.getID() + "&bucket=audio_summary");
		//	url = new URL("http://developer.echonest.com/api/v4/track/profile?api_key=B0EHJCUJPBJOZ5MOP&format=json&id=TRTLKZV12E5AC92E11&bucket=audio_summary");
		} 
		catch (MalformedURLException e) { e.printStackTrace(); }
		
		try(InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)) {
			JsonObject struct = rdr.readObject();
			for(String x :struct.keySet()){System.out.println(x);}
			
			struct = struct.getJsonObject("response");
			for(String x :struct.keySet()){System.out.println(x);}
			
			JsonObject track1 = struct.getJsonObject("track");
			for(String x :track1.keySet()){System.out.println(x);}
			
			trackInformation.put("audio_summary", track1.getJsonObject("audio_summary") );
		}
		
		try {
			url = new URL(track.getAnalysisURL());
		} 
		catch (MalformedURLException e) { e.printStackTrace();  }

				
		try (InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)) {
			JsonObject struct = rdr.readObject();
			trackInformation.put("Meta", struct.getJsonObject("meta"));
			trackInformation.put("Track", struct.getJsonObject("track"));
			trackInformation.put("Bars", struct.getJsonArray("bars"));
			trackInformation.put("Beats", struct.getJsonArray("beats"));
			trackInformation.put("Tatums", struct.getJsonArray("tatums"));
			trackInformation.put("Sections", struct.getJsonArray("sections"));
			trackInformation.put("Segments", struct.getJsonArray("segments"));
			// above returns a java List type containing all JsonObjects in our JsonArray!
			
		}
		catch(Exception e){ e.printStackTrace(); }

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
	public FileUploaderGDX(String trackPath){
		this.trackPath = trackPath;
	}
	public void setPath(String trackPath){this.trackPath=trackPath;}
	public HashMap<String, Object> getJsonMap(){return trackInformation;}
}