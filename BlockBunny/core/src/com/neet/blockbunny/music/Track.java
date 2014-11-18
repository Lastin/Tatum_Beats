package com.neet.blockbunny.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.echonest.api.v4.EchoNestException;
import com.neet.blockbunny.main.FileUploaderGDX;

public class Track {

	private FileUploaderGDX fileUploader;
	private String trackPath;//done
	private String artistName;//done
	private String trackName;//done
	private String albumName; //done
	private String genre; //done

	private double dancebility; //done
	private double speechiness; //done
	private double liveness; //done
	private double energy; //done
	private double acousticness;//done
	private double duration;  //done
	private double loudness; //done
	private double valence; //done
	private double instrumentalness; //done
	
	private int bitrate;//done
	private int sampleRate; //done

	private int key; //done
	private double keyConf;//done
	private String stringKey; //done
	
	private int mode; //done
	private double modeConf;//done
	private String stringMode; //done
	
	private double tempo; //done
	private double tempoConf;//done
	
	private int timeSig; //done
	private double timeSigConf;//done
	
	private double endFade;//done
	private double startFade;//done
	
	private ArrayList<TimedEvent> bars;
	private ArrayList<TimedEvent> beats;
	private ArrayList<TimedEvent> tatums;
	
	private ArrayList<Section> sections;
	private ArrayList<Segment> segments;

	//to be worked out
	private String locationName;
	private double latitude;
	private double longitude;

	private double songHotness;
	private double artistHotness;

	private HashMap<String, Object> trackInformation;

	public Track(String trackPath){

		fileUploader = new FileUploaderGDX(trackPath);

	}
	public void setTrack(String trackPath){

		fileUploader = new FileUploaderGDX(trackPath);

	}
	
	public void initilize(){
		try {
			fileUploader.uploadGDX();
			
			trackInformation = fileUploader.getJsonMap();
			JsonObject meta = (JsonObject) trackInformation.get("Meta");
			JsonObject track = (JsonObject) trackInformation.get("Track");
			JsonObject audio = (JsonObject) trackInformation.get("audio_summary");
			List<JsonObject> Jbars = getAsList(trackInformation.get("Bars"));
			List<JsonObject> Jbeats = getAsList(trackInformation.get("Beats"));
			List<JsonObject> Jtatums = getAsList(trackInformation.get("Tatums"));
			List<JsonObject> Jsections = getAsList(trackInformation.get("Sections"));
			List<JsonObject> Jsegments = getAsList(trackInformation.get("Segments"));
			// get all json objects
			this.trackPath = meta.getString("filename");
			this.artistName = meta.getString("artist");
			this.trackName = meta.getString("title");
			this.albumName = meta.getString("album");
			this.genre = meta.getString("genre");
			
			this.bitrate = meta.getInt("bitrate");
			this.sampleRate = meta.getInt( "sample_rate");
			this.dancebility = audio.getJsonNumber("danceability").doubleValue();
			
			this.duration = audio.getJsonNumber("duration").doubleValue();
			this.energy =  audio.getJsonNumber("energy").doubleValue();
			
			this.loudness = audio.getJsonNumber("loudness").doubleValue();
			this.speechiness = audio.getJsonNumber("speechiness").doubleValue();
			this.acousticness =  audio.getJsonNumber("acousticness").doubleValue();
			this.valence = audio.getJsonNumber("valence").doubleValue();
			this.instrumentalness =  audio.getJsonNumber("instrumentalness").doubleValue();
			
			this.liveness =  audio.getJsonNumber("liveness").doubleValue();
			this.tempo = audio.getJsonNumber("tempo").doubleValue();
			
			this.key = audio.getInt("key");
			setKeyString();
			this.mode = audio.getInt("mode");
			setModeString();
			this.timeSig = audio.getInt("time_signature");
			
			this.endFade = track.getJsonNumber("end_of_fade_in").doubleValue();
			this.startFade =  track.getJsonNumber("start_of_fade_out").doubleValue();
			this.tempoConf = track.getJsonNumber("tempo_confidence").doubleValue();
			this.timeSigConf = track.getJsonNumber("time_signature_confidence").doubleValue();
			this.keyConf =  track.getJsonNumber("key_confidence").doubleValue();
			this.modeConf = track.getJsonNumber("mode_confidence").doubleValue();
			
			bars = new ArrayList<>();
			for(JsonObject object: Jbars){
				double start = object.getJsonNumber("start").doubleValue();
				double duration = object.getJsonNumber("duration").doubleValue();
				double confidence = object.getJsonNumber("confidence").doubleValue();
				bars.add(new TimedEvent(start, duration, confidence));
			}
			beats = new ArrayList<>();
			for(JsonObject object: Jbeats){
				double start = object.getJsonNumber("start").doubleValue();
				double duration = object.getJsonNumber("duration").doubleValue();
				double confidence = object.getJsonNumber("confidence").doubleValue();
				beats.add(new TimedEvent(start, duration, confidence));
			}
			tatums = new ArrayList<>();
			for(JsonObject object: Jtatums){
				double start = object.getJsonNumber("start").doubleValue();
				double duration = object.getJsonNumber("duration").doubleValue();
				double confidence = object.getJsonNumber("confidence").doubleValue();
				tatums.add(new TimedEvent(start, duration, confidence));
			}
			sections = new ArrayList<>();
			for(JsonObject object: Jsections){
				double start = object.getJsonNumber("start").doubleValue();
				double duration = object.getJsonNumber("duration").doubleValue();
				double confidence = object.getJsonNumber("confidence").doubleValue();
				double loudness = object.getJsonNumber("loudness").doubleValue();
				double tempo = object.getJsonNumber("tempo").doubleValue();
				double tempoConf = object.getJsonNumber("tempo_confidence").doubleValue();
				int key = object.getInt("key");
				double keyConf = object.getJsonNumber("key_confidence").doubleValue();
				int mode = object.getInt("mode");
				double modeConf = object.getJsonNumber("mode_confidence").doubleValue();
				int timeSig = object.getInt("time_signature");
				double timeSigConf = object.getJsonNumber("time_signature_confidence").doubleValue();
				sections.add(new Section(start, duration, confidence,loudness,tempo,tempoConf,key,keyConf,mode,modeConf,timeSig,timeSigConf));
			}
			segments = new ArrayList<>();
			for(JsonObject object: Jsegments){
				double start = object.getJsonNumber("start").doubleValue();
				double duration = object.getJsonNumber("duration").doubleValue();
				double confidence = object.getJsonNumber("confidence").doubleValue();
				double loudness_start = object.getJsonNumber("loudness_start").doubleValue();
				double loudness_max_time = object.getJsonNumber("loudness_max_time").doubleValue();
				double loudness_max = object.getJsonNumber("loudness_max").doubleValue();
				JsonArray pitches = object.getJsonArray("pitches");
				ArrayList<Double> pitchArray = new ArrayList<>();
				try{
					int i =0;
					while(true){
						pitchArray.add(Double.parseDouble(pitches.get(i).toString()));
						i++;
					}
				
				}
				catch(IndexOutOfBoundsException e){
					//end of array because I am a lazy bastard and can't be bothered to find out how to get the length
				}
				JsonArray timbres = object.getJsonArray("timbre");
				ArrayList<Double> timbreArray = new ArrayList<>();
				try{
					int i =0;
					while(true){
						timbreArray.add(Double.parseDouble(timbres.get(i).toString()));
						i++;
					}
				
				}
				catch(IndexOutOfBoundsException e){
					//end of array because I am a lazy bastard and can't be bothered to find out how to get the length
				}
				
				segments.add(new Segment(start,duration,confidence,loudness_start,loudness_max_time,loudness_max,pitchArray, timbreArray));
			}
			
		} catch (EchoNestException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	public List<JsonObject> getAsList(Object array) {
		if(array instanceof JsonArray)
			return ((JsonArray) array).getValuesAs(JsonObject.class);
		return null;
	}
	
	public static void main(String... args) {
		new Track("test.mp3").initilize();
	}
	  private void setModeString(){

	    if(mode == 0){
	      stringMode = "minor";
	    }
	    else stringMode = "major";

	  }
	  
	  private void setKeyString() {

	    switch(key){

	      case 0: stringKey = "c";
	              return;

	      case 1: stringKey = "c#";
	              return;

	      case 2: stringKey = "d";
	              return;

	      case 3: stringKey = "d#";
	              return;

	      case 4: stringKey = "e";
	              return;

	      case 5: stringKey = "f";
	              return;

	      case 6: stringKey = "f#";
	              return;

	      case 7: stringKey = "g";
	              return;

	      case 8: stringKey = "g#";
	              return;

	      case 9: stringKey = "a";
	              return;

	      case 10: stringKey = "a#";
	              return;

	      case 11: stringKey = "b";
	              return;
	    }
	  }
	  
	  public String getArtist(){ return artistName;}

	  public String getSongName(){  return trackName;}
	  
	  public String getAlbumName() { return albumName;}
	  
	  public String getTrackPath() {return trackPath;}
	  
	  public String getGenre() {return genre;}
	  
	  public double getAcousticness() {return acousticness;}
	  
	  public double getValence() {return valence;}
	  
	  public double getInstrumentalness() {return instrumentalness;}
	  
	  public int getBitRate(){ return bitrate;}
	  
	  public int getSampleRate() {return sampleRate;}

	  public double getDanceability(){  return dancebility;}

	  public double getSpeechiness(){ return speechiness;}

	  public double getLiveness(){ return liveness;}

	  public double getEnergy(){  return energy;}

	  public double getDuration(){ return duration;}

	  public double getLoudness(){ return loudness;}

	  public int getKeyInt(){ return key;}

	  public String getKeyString(){return stringKey;}

	  public int getModeInt(){ return mode;}

	  public String getModeString(){ return stringMode;}

	  public double getTempo(){return tempo;}

	  public int getTimeSignature(){return timeSig;}
	  
	  public double getEndFade(){return endFade;}

	  public double getStartFade(){return startFade;}
	  
	  public double getKeyConfidence(){return keyConf;}
	  
	  public double getModeConfidence(){return modeConf;}
	  
	  public double getTempoConfidence(){return tempoConf;}
	  
	  public double getTimeSignatureConfidence(){return timeSigConf;}

	  public ArrayList<TimedEvent> getBars() {return bars;}
	  
	  public ArrayList<TimedEvent> getBeats() {return beats;}
	  
	  public ArrayList<TimedEvent> getTatums() { return tatums;}
	  
	  public ArrayList<Segment> getSegments() {return segments;}
	  
	  public ArrayList<Section> getSections() {return sections;}

	  public FileUploaderGDX getUploader() {return fileUploader;}
}


