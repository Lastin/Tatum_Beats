package com.tatum.handlers;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.ArtistParams;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;
import com.echonest.api.v4.Location;
import com.echonest.api.v4.Segment;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import java.util.List;

class MetaGrabber{
    PrintStream supresser = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            //supressed
        }
    });
    private EchoNestAPI en;
    private String trackPath;
    private File file; //done
    private Track track;//done
    private TrackAnalysis analysis; //done
    private Song song; //song
    private String songID; //done

    private String artistName;//done
    private String trackName;//done
    private String releaseName;

    private double dancebility; //done
    private double speechiness; //done
    private double liveness; //done
    private double energy; //done

    private double duration; //done
    private double loudness;//done
    private double endFade;//done
    private double startFade; //done
    private double songHotness;//done
    private double artistHotness;//done

    private int key; //done
    private double keyConf;//done
    private String stringKey;//done
    private int mode;//done
    private double modeConf;//done
    private String stringMode;//done
    private double tempo;//done
    private double tempoConf;//done
    private int timeSig;//done
    private double timeSigConf;//done

    private Location location;//done
    private String locationName;//done
    private double latitude;//done
    private double longitude;//done

    private ArrayList<TimedEvent> beats;//done
    private ArrayList<TimedEvent> bars;//done
    private ArrayList<Segment> segments;//done
    private ArrayList<TimedEvent> tatums;//done


    public MetaGrabber()throws EchoNestException {
        System.setOut(supresser);
        en = new EchoNestAPI("B0EHJCUJPBJOZ5MOP");
        en.setTraceSends(true);
        en.setTraceRecvs(false);
        trackPath = "test.mp3";
        file = new File(trackPath);
    }


    public void upload(){
        try {
            track = en.uploadTrack(file);
        } catch (IOException e) {
            System.err.println("Trouble uploading file");
        } catch (EchoNestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setTrackPath(String path){

        trackPath = path;
        file = new File(trackPath);

    }

    public void setMeta() throws EchoNestException{
        tempo = track.getTempo();
        dancebility = track.getDanceability();
        speechiness = track.getSpeechiness();
        liveness = track.getLiveness();
        energy = track.getEnergy();
        loudness = track.getLoudness();
        trackName = track.getTitle();
        artistName = track.getArtistName();
        duration = track.getDuration();
        loudness = track.getLoudness();
        key = track.getKey();
        setKeyString();
        mode = track.getMode();
        setModeString();
        timeSig = track.getTimeSignature();
        analysis = track.getAnalysis();
        keyConf = analysis.getKeyConfidence();
        modeConf = analysis.getModeConfidence();
        tempoConf = analysis.getTempoConfidence();
        timeSigConf = analysis.getTimeSignatureConfidence();
        endFade = analysis.getEndOfFadeIn();
        startFade = analysis.getStartOfFadeOut();
        songID = track.getSongID();
        song = new Song(en,songID);
        location = song.getArtistLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locationName = location.getPlaceName();
        songHotness=song.getSongHotttnesss();
        artistHotness = song.getArtistHotttnesss();

        beats = (ArrayList<TimedEvent>) analysis.getBeats();
        bars = (ArrayList<TimedEvent>) analysis.getBars();
        tatums = (ArrayList<TimedEvent>) analysis.getTatums();
        segments = (ArrayList<Segment>) analysis.getSegments();

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

    private double[] genericTimedArray(ArrayList<TimedEvent> list, int index){

        double[] res = new double[3];
        res[0] = list.get(index).getStart();
        res[1] = list.get(index).getDuration();
        res[2] = list.get(index).getConfidence();
        return res;

    }

    public String getUploadProgress() throws EchoNestException{


        if (track.getStatus() == Track.AnalysisStatus.COMPLETE){
            return "complete";
        }
        else if (track.getStatus() == Track.AnalysisStatus.ERROR){
            return "error";
        }
        else if (track.getStatus() == Track.AnalysisStatus.PENDING){
            return "pending";
        }
        else if (track.getStatus() == Track.AnalysisStatus.UNAVAILABLE){
            return "unavailable";
        }
        else return "unknown";


    }

    public String getArtist(){ return artistName;}

    public String getSongName(){  return trackName;}

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

    public double[] getBeat(int i){return genericTimedArray(beats,i);}

    public double[] getBar(int i){return genericTimedArray(bars,i);}

    public double[] getTatum(int i){return genericTimedArray(tatums,i);}

    public ArrayList<TimedEvent> getBeats(){return beats;}

    public ArrayList<TimedEvent> getBars(){return bars;}

    public ArrayList<TimedEvent> getTatums(){return tatums;}

    public ArrayList<Segment> getSegments(){return segments;}

    public double getEndFade(){return endFade;}

    public double getStartFade(){return startFade;}

    public double getSongHotness(){return songHotness;}

    public double getArtistHotness(){return artistHotness;}

    public String getArtistLocation(){return locationName;}

    public double getArtistLatitude(){return latitude;}

    public double getArtistLongitude(){return longitude;}

    public double getKeyConfidence(){return keyConf;}

    public double getModeConfidence(){return modeConf;}

    public double getTempoConfidence(){return tempoConf;}

    public double getTimeSignatureConfidence(){return timeSigConf;}

}
