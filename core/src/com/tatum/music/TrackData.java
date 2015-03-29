//TODO: modify the code to make it compatible with android
package com.tatum.music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.echonest.api.v4.EchoNestException;
import com.tatum.handlers.FileUploaderGDX;

public class TrackData {

    private FileUploaderGDX fileUploader;
    private String trackPath;//done
    private String artistName;//done
    private String trackName;//done
    private String albumName; //done
    private String genre; //done
    private String theme;

    private int length;
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

    private String twitterHandle;

    //to be worked out
    private String locationName;
    private double latitude;
    private double longitude;

    private double songHotness;
    private double artistHotness;

    private HashMap<String, Object> trackInformation;

    public TrackData(String trackPath) {

        fileUploader = new FileUploaderGDX(trackPath);

    }

    public void setTrack(String trackPath) {

        fileUploader = new FileUploaderGDX(trackPath);

    }
    public void upload(){
        try {
            fileUploader.uploadGDX();
        } catch (EchoNestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initilize() {
        try {
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
            try {
                this.trackPath = meta.getString("filename");
                this.artistName = meta.getString("artist");
                this.trackName = meta.getString("title");
                this.albumName = meta.getString("album");
                this.genre = meta.getString("genre");
                setGenreBucket();
                this.bitrate = meta.getInt("bitrate");
                this.sampleRate = meta.getInt("sample_rate");
                this.dancebility = audio.getJsonNumber("danceability").doubleValue();

                this.duration = audio.getJsonNumber("duration").doubleValue();
                this.energy = audio.getJsonNumber("energy").doubleValue();
                this.loudness = 0;
                try {
                    this.loudness = audio.getJsonNumber("loudness").doubleValue();
                } catch (NullPointerException e) {
                }
                try {
                    this.speechiness = audio.getJsonNumber("speechiness").doubleValue();
                }catch (ClassCastException e){
                    this.speechiness = 0;
                }
                try {
                this.acousticness = audio.getJsonNumber("acousticness").doubleValue();
                }catch (ClassCastException e){
                    this.acousticness = 0;
                }
                try {
                this.valence = audio.getJsonNumber("valence").doubleValue();
                }catch (ClassCastException e){
                this.valence = 0;
                }
                try {
                this.instrumentalness = audio.getJsonNumber("instrumentalness").doubleValue();
                }
                catch (ClassCastException e){
                    this.instrumentalness = 0;
                }
                try{
                this.liveness = audio.getJsonNumber("liveness").doubleValue();
                }
                catch (ClassCastException e){
                    this.liveness = 0;
                }
                this.tempo = audio.getJsonNumber("tempo").doubleValue();

                this.key = audio.getInt("key");
                setKeyString();
                this.mode = audio.getInt("mode");
                setModeString();
                this.timeSig = audio.getInt("time_signature");

                this.endFade = track.getJsonNumber("end_of_fade_in").doubleValue();
                this.startFade = track.getJsonNumber("start_of_fade_out").doubleValue();
                this.tempoConf = track.getJsonNumber("tempo_confidence").doubleValue();
                this.timeSigConf = track.getJsonNumber("time_signature_confidence").doubleValue();
                this.keyConf = track.getJsonNumber("key_confidence").doubleValue();
                this.modeConf = track.getJsonNumber("mode_confidence").doubleValue();
                twitterhandle();
            } catch (NullPointerException e) {
               // JOptionPane.showMessageDialog(null, "not possible to use this file");
                e.printStackTrace();
            }
            tatums = new ArrayList<TimedEvent>();
            int position =0;
            for (JsonObject object : Jtatums) {
                double start = (double) Math.floor(object.getJsonNumber("start").doubleValue() * 100) / 100;
                double duration = (double) Math.floor(object.getJsonNumber("duration").doubleValue() * 100) / 100;
                double confidence = object.getJsonNumber("confidence").doubleValue();
                tatums.add(new TimedEvent(start, duration, confidence, new ArrayList<TimedEvent>(),position));
                position++;
            }
            beats = new ArrayList<TimedEvent>();
            int tatumsCount = 0;
            position =0;
            for (JsonObject object : Jbeats) {
                double start = (double) Math.floor(object.getJsonNumber("start").doubleValue() * 100) / 100;
                double duration = (double) Math.floor(object.getJsonNumber("duration").doubleValue() * 100) / 100;
                double confidence = object.getJsonNumber("confidence").doubleValue();
                ArrayList<TimedEvent> array = new ArrayList<TimedEvent>();
                while (true) {
                    try {
                        TimedEvent unit = tatums.get(tatumsCount);
                        if ((unit.getStart() >= start) && (unit.getStart() + unit.getduration() <= start + duration)) {
                            array.add(unit);
                            unit.setContainedIn(beats.size());
                            int count = beats.size() + 1;
                            //System.out.println("Added tatum " + tatumsCount + " to beat " + count);
                            tatumsCount++;
                        } else if (unit.getStart() >= start + duration) {
                            break;
                        }
                        //else if((unit.getStart()>=start)&&(unit.getStart()+unit.getduration()>start+duration)){
                        else {
                            double unitDur = unit.getStart() + unit.getduration();
                            double startDur = (start + duration);
                            //System.out.println("Unit start "+ unit.getStart() + "unit finished " + unitDur + " beat start " + start + " beat finished" + startDur);
                            if ((unit.getStart() + 0.0001 >= start + duration)) {

                                //	System.out.println("break inner");
                                break;
                            }
                            if ((unit.getStart() >= start - 0.0001)) {
                                int count = beats.size() + 1;
                                unit.setContainedIn(beats.size());
                                //System.out.println("Added tatum " + tatumsCount + " to beat " + count);
                                array.add(unit);
                            } else {
                                int count = beats.size() + 1;
                                //System.out.println("NOT Added tatum " + tatumsCount + " to beat " + count);
                            }
                            tatumsCount++;
                        }

                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
                beats.add(new TimedEvent(start, duration, confidence, array,position));
                position++;
            }
            bars = new ArrayList<TimedEvent>();
            tatumsCount = 0;
            position =0;
            for (JsonObject object : Jbars) {
                double start = (double) Math.floor(object.getJsonNumber("start").doubleValue() * 100) / 100;
                double duration = (double) Math.floor(object.getJsonNumber("duration").doubleValue() * 100) / 100;
                double confidence = object.getJsonNumber("confidence").doubleValue();
                ArrayList<TimedEvent> array = new ArrayList<TimedEvent>();
                int Testcount = 0;
                while (true) {
                    try {
                        TimedEvent unit = beats.get(tatumsCount);
                        double unitDur = unit.getStart() + unit.getduration();
                        double startDur = (start + duration);
                        System.out.println("Unit start "+ unit.getStart() + "unit finished " + unitDur + " beat start " + start + " beat finished" + startDur);


                        if ((unit.getStart() >= start) && (unit.getStart() + unit.getduration() <= start + duration)) {
                            array.add(unit);
                            unit.setContainedIn(bars.size());
                            int count = bars.size() + 1;
                            System.out.println("Added beat " + tatumsCount + " to bar " + count);
                            tatumsCount++;
                        } else if (unit.getStart() >= start + duration) {
                            break;
                        }else if (bars.size()==0 && unit.getStart()<start){
                            array.add(unit);
                            unit.setContainedIn(bars.size());
                            tatumsCount++;

                        }else {
                            //double unitDur = unit.getStart() + unit.getduration();
                            //double startDur = (start + duration);
                            //System.out.println("Unit start "+ unit.getStart() + "unit finished " + unitDur + " beat start " + start + " beat finished" + startDur);
                            if ((unit.getStart() + 0.0001 >= start + duration)) {

                                //	System.out.println("break inner");
                                break;
                            }
                            if ((unit.getStart() >= start - 0.0001)) {
                                int count = bars.size() + 1;

                                unit.setContainedIn(bars.size());
                                System.out.println("Added beat " + tatumsCount + " to bar " + count);
                                array.add(unit);
                            }
                            //if((unit.getStart()>=start)&&(unit.getStart()+unit.getduration()<=start+duration)){

                            //}
                            else {
                                int count = bars.size() + 1;
                                //System.out.println("NOT Added beat " + tatumsCount + " to bar " + count);
                            }
                            tatumsCount++;
                        }
                        Testcount++;
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
                if (Testcount != 3) {
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
                bars.add(new TimedEvent(start, duration, confidence, array,position));
                position++;
            }

            sections = new ArrayList<Section>();
            tatumsCount = 0;
            position =0;
            boolean wierdBeat = false; // there are inbetween bars that seem to mess up, this flags them and adds to the next section
            for (JsonObject object : Jsections) {
                double start = (double) Math.floor(object.getJsonNumber("start").doubleValue() * 100) / 100;
                double duration = (double) Math.floor(object.getJsonNumber("duration").doubleValue() * 100) / 100;
                double confidence = object.getJsonNumber("confidence").doubleValue();
                double loudness = 0;
                try {
                    loudness = object.getJsonNumber("loudness").doubleValue();
                } catch (NullPointerException e) {
                }
                double tempo = this.tempo;
                try {
                    tempo = object.getJsonNumber("tempo").doubleValue();
                } catch (NullPointerException e) {
                }
                double tempoConf = this.tempoConf;
                try {
                    tempoConf = object.getJsonNumber("tempo_confidence").doubleValue();
                } catch (NullPointerException e) {
                }
                int key = this.key;
                try {
                    key = object.getInt("key");
                } catch (NullPointerException e) {
                }
                double keyConf = this.keyConf;
                try {
                    keyConf = object.getJsonNumber("key_confidence").doubleValue();
                } catch (NullPointerException e) {
                }
                int mode = this.mode;
                try {
                    mode = object.getInt("mode");
                } catch (NullPointerException e) {
                }
                double modeConf = this.modeConf;
                try {
                    modeConf = object.getJsonNumber("mode_confidence").doubleValue();
                } catch (NullPointerException e) {
                }
                int timeSig = this.timeSig;
                try {
                    timeSig = object.getInt("time_signature");
                } catch (NullPointerException e) {
                }
                double timeSigConf = this.timeSigConf;
                try {
                    timeSigConf = object.getJsonNumber("time_signature_confidence").doubleValue();
                } catch (NullPointerException e) {
                }

                ArrayList<TimedEvent> array = new ArrayList<TimedEvent>();
                while (true) {
                    try {
                        TimedEvent unit = bars.get(tatumsCount);
                        double unitDur = unit.getStart() + unit.getduration();
                        double startDur = (start + duration);
                        //System.out.println("Unit start "+ unit.getStart() + "unit finished " + unitDur + " section start " + start + " section finished" + startDur);
                        if(wierdBeat){
                            wierdBeat=!wierdBeat;
                            tatumsCount++;
                            array.add(unit);
                            unit.setContainedIn(sections.size());

                        }
                        else if ((unit.getStart() >= start) && (unit.getStart() + unit.getduration() <= start + duration)) {
                            array.add(unit);
                            unit.setContainedIn(sections.size());
                            int count = sections.size() + 1;
                            System.out.println("Added bar " + tatumsCount + " to section " + count);
                            //double unitDur = unit.getStart() + unit.getduration();
                            //double startDur = (start + duration);
                            //System.out.println("Unit start "+ unit.getStart() + "unit finished " + unitDur + " section start " + start + " section finished" + startDur);

                            tatumsCount++;
                        }

                        else if (unit.getStart() >= start + duration) {
                            break;
                        }
                        else if(unit.getStart()+unit.getduration() >start + duration){
                            wierdBeat = true;
                            break; //remove if you want in the previous bar
                        }
                        else {

                            if ((unit.getStart() + 0.0001 >= start + duration)) {

                                //	System.out.println("break inner");
                                break;
                            }
                            if ((unit.getStart() >= start - 0.0001)) {
                                int count = sections.size() + 1;
                                //System.out.println("Added bar " + tatumsCount + " to section " + count + "dsas");
                                array.add(unit);
                            }
                            //if((unit.getStart()>=start)&&(unit.getStart()+unit.getduration()<=start+duration)){

                            //}
                            else {
                                int count = sections.size() + 1;
                                //System.out.println("NOT Added bar " + tatumsCount + " to section " + count);
                            }
                            tatumsCount++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }

                }
                sections.add(new Section(start, duration, confidence, loudness, tempo, tempoConf, key, keyConf, mode, modeConf, timeSig, timeSigConf, array,position));
            }
            position =0;
            segments = new ArrayList<Segment>();
            for (JsonObject object : Jsegments) {
                double start = object.getJsonNumber("start").doubleValue();
                double duration = object.getJsonNumber("duration").doubleValue();
                double confidence = object.getJsonNumber("confidence").doubleValue();
                double loudness_start = object.getJsonNumber("loudness_start").doubleValue();
                double loudness_max_time = object.getJsonNumber("loudness_max_time").doubleValue();
                double loudness_max = object.getJsonNumber("loudness_max").doubleValue();
                JsonArray pitches = object.getJsonArray("pitches");
                ArrayList<Double> pitchArray = new ArrayList<Double>();
                try {
                    int i = 0;
                    while (true) {
                        pitchArray.add(Double.parseDouble(pitches.get(i).toString()));
                        i++;
                    }

                } catch (IndexOutOfBoundsException e) {
                    //end of array because I am a lazy bastard and can't be bothered to find out how to get the length
                }
                JsonArray timbres = object.getJsonArray("timbre");
                ArrayList<Double> timbreArray = new ArrayList<Double>();
                try {
                    int i = 0;
                    while (true) {
                        timbreArray.add(Double.parseDouble(timbres.get(i).toString()));
                        i++;
                    }

                } catch (IndexOutOfBoundsException e) {
                    //end of array because I am a lazy bastard and can't be bothered to find out how to get the length
                }

                segments.add(new Segment(start, duration, confidence, loudness_start, loudness_max_time, loudness_max, pitchArray, timbreArray,position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void twitterhandle(){
        if(trackInformation.containsKey("twitter")){
            JsonObject twitter = (JsonObject) trackInformation.get("twitter");
            if(twitter.containsKey("twitter")){
                twitterHandle = twitter.getString("twitter");
            }
            else{
                twitterHandle = "no handle";
            }
        }
    }
    private void setGenreBucket(){

        InputStream is = Gdx.files.internal("res/GenreBuckets.json").read();
        JsonReader rdr = Json.createReader(is);
        JsonObject genreBucketJson = rdr.readObject();
        JsonArray genres = genreBucketJson.getJsonArray("genres");
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i =0; i < genres.size(); i++){

            JsonObject temp = genres.getJsonObject(i);
            String name = temp.getString("name");
            String theme = temp.getString("theme");
            System.out.println(name +": "+ theme);
            map.put(name.toLowerCase(),theme);
        }
        if(map.containsKey(genre.toLowerCase())){
            theme = map.get(genre.toLowerCase());
        }
        else{
            theme = "rock";
        }
        System.out.println(genre+" HERE WE ARE " +theme);
        //this.genreBucket = genreBucket.getString("bucket");
    }

    public List<JsonObject> getAsList(Object array) {
        if (array instanceof JsonArray)
            return ((JsonArray) array).getValuesAs(JsonObject.class);
        return null;
    }

    private void setModeString() {

        if (mode == 0) {
            stringMode = "minor";
        } else stringMode = "major";

    }

    private void setKeyString() {

        switch (key) {

            case 0:
                stringKey = "c";
                return;

            case 1:
                stringKey = "c#";
                return;

            case 2:
                stringKey = "d";
                return;

            case 3:
                stringKey = "d#";
                return;

            case 4:
                stringKey = "e";
                return;

            case 5:
                stringKey = "f";
                return;

            case 6:
                stringKey = "f#";
                return;

            case 7:
                stringKey = "g";
                return;

            case 8:
                stringKey = "g#";
                return;

            case 9:
                stringKey = "a";
                return;

            case 10:
                stringKey = "a#";
                return;

            case 11:
                stringKey = "b";
                return;
        }
    }

    public int getBarIn(double time) {
        if (time == 0) return 1;
        if (time < 0) return -1;
        TimedEvent timeBar = bars.get(bars.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return -1;
        int bar = 0;

        int midpoint = bars.size() / 2;
        int start = 0;
        int end = bars.size();

        int count = 0;
        while (true) {
            //System.out.println("Count:" + count);
            try {
                timeBar = bars.get(midpoint);

                if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration())) {
                    bar = midpoint;
                    break;
                } else if (time < timeBar.getStart()) {
                    end = midpoint;
                    midpoint = (start + end) / 2;
                } else if (time > timeBar.getStart() + timeBar.getduration()) {
                    start = midpoint;
                    midpoint = (start + end) / 2;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.println("error bars " + midpoint);
                midpoint--;
            }
            count++;

        }
        /*System.out.println("bars");
        System.out.println(time);
        System.out.println(bar);
        System.out.println(timeBar.getStart());
        System.out.println(timeBar.getduration());*/

        return bar;
    }

    public int getBeatIn(double time) {
        if (time == 0) return 1;
        if (time < 0) return -1;
        TimedEvent timeBar = beats.get(beats.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return -1;
        int beat = 0;
        int count = 0;
        int midpoint = beats.size() / 2;
        int start = 0;
        int end = beats.size();

        while (true) {
            System.out.println("Count:" + count);
            try {
                timeBar = beats.get(midpoint);

                if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration())) {
                    beat = midpoint;
                    break;
                } else if (time < timeBar.getStart()) {
                    end = midpoint;
                    midpoint = (start + end) / 2;
                } else if (time > timeBar.getStart() + timeBar.getduration()) {
                    start = midpoint;
                    midpoint = (start + end) / 2;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("error beats" + midpoint);
                midpoint--;
            }
            count++;
        }
        System.out.println("beats");
        System.out.println(time);
        System.out.println(beat);
        System.out.println(timeBar.getStart());
        System.out.println(timeBar.getduration());
        return beat;
    }

    public int getTatumIn(double time) {
        if (time == 0) return 1;
        if (time < 0) return -1;
        TimedEvent timeBar = tatums.get(tatums.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return -1;
        int bar = 0;
        int count = 0;
        int midpoint = tatums.size() / 2;
        int start = 0;
        int end = tatums.size();

        while (true) {

            System.out.println("Count:" + count);
            try {
                timeBar = tatums.get(midpoint);

                if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration())) {
                    bar = midpoint;
                    break;
                } else if (time < timeBar.getStart()) {
                    end = midpoint;
                    midpoint = (start + end) / 2;
                } else if (time > timeBar.getStart() + timeBar.getduration()) {
                    start = midpoint;
                    midpoint = (start + end) / 2;
                } else {
                    bar = -1;
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("error tatums" + midpoint);
                midpoint--;
            }
            count++;
        }
        System.out.println("tatums");
        System.out.println(time);
        System.out.println(bar);
        System.out.println(timeBar.getStart());
        System.out.println(timeBar.getduration());
        return bar;
    }

    public int getSectionIn(double time) {
        if (time == 0) return 1;
        if (time < 0) return -1;
        TimedEvent timeBar = sections.get(sections.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return -1;
        int bar = 0;
        int midpoint = sections.size() / 2;
        int start = 0;
        int end = sections.size();
        int count = 0;
        while (true) {
            System.out.println("Count:" + count);
            try {
                timeBar = sections.get(midpoint);

                if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration())) {
                    bar = midpoint;
                    break;
                } else if (time < timeBar.getStart()) {
                    end = midpoint;
                    midpoint = (start + end) / 2;
                } else if (time > timeBar.getStart() + timeBar.getduration()) {
                    start = midpoint;
                    midpoint = (start + end) / 2;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("error sections " + midpoint);
                midpoint--;
            }
            count++;

        }   // This is too slow, gonna write a new one where you add the section you are currently in
//        System.out.println("sections");
//        System.out.println(time);
//        System.out.println(bar);
//        System.out.println(timeBar.getStart());
//        System.out.println(timeBar.getduration());
        return bar;
    }

    public int getSectionInRedux(double time, int currSection){
        if(time<0) return -1;
        TimedEvent timeBar = sections.get(sections.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return sections.size()-1;

        for(int i = currSection;i<sections.size();i++){
            timeBar = sections.get(i);
            if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration()))
                return i;
        }
        return sections.size()-1;

    }

    public int getSegmentIn(double time) {
        if (time == 0) return 1;
        if (time < 0) return -1;
        TimedEvent timeBar = segments.get(segments.size() - 1);
        if (time > timeBar.getStart() + timeBar.getduration()) return -1;
        int bar = 0;
        int midpoint = segments.size() / 2;
        int start = 0;
        int end = segments.size();
        int count = 0;
        while (true) {

            //System.out.println("Count:" + count);
            try {
                timeBar = segments.get(midpoint);

                if ((time >= timeBar.getStart()) && (time <= timeBar.getStart() + timeBar.getduration())) {
                    bar = midpoint;
                    break;
                } else if (time < timeBar.getStart()) {
                    end = midpoint;
                    midpoint = (start + end) / 2;
                } else if (time > timeBar.getStart() + timeBar.getduration()) {
                    start = midpoint;
                    midpoint = (start + end) / 2;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.println("error segments " + midpoint);
                midpoint--;
            }
            count++;
        }
        //System.out.println("segments");
        //System.out.println(time);
        //System.out.println(bar);
        //System.out.println(timeBar.getStart());
        //System.out.println(timeBar.getduration());
        return bar;
    }


    public String getArtist() {
        return artistName;
    }

    public String getSongName() {
        return trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getTrackPath() {
        return trackPath;
    }

    public String getGenre() {
        return genre;
    }

    public double getAcousticness() {
        return acousticness;
    }

    public double getValence() {
        return valence;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public int getBitRate() {
        return bitrate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public double getDanceability() {
        return dancebility;
    }

    public double getSpeechiness() {
        return speechiness;
    }

    public double getLiveness() {
        return liveness;
    }

    public double getEnergy() {
        return energy;
    }

    public double getDuration() {
        return duration;
    }

    public double getLoudness() {
        return loudness;
    }

    public int getKeyInt() {
        return key;
    }

    public String getKeyString() {
        return stringKey;
    }

    public int getModeInt() {
        return mode;
    }

    public String getModeString() {
        return stringMode;
    }

    public double getTempo() {
        return tempo;
    }

    public int getTimeSignature() {
        return timeSig;
    }

    public double getEndFade() {
        return endFade;
    }

    public double getStartFade() {
        return startFade;
    }

    public double getKeyConfidence() {
        return keyConf;
    }

    public double getModeConfidence() {
        return modeConf;
    }

    public double getTempoConfidence() {
        return tempoConf;
    }

    public double getTimeSignatureConfidence() {
        return timeSigConf;
    }

    public ArrayList<TimedEvent> getBars() {
        return bars;
    }

    public ArrayList<TimedEvent> getBeats() {
        return beats;
    }

    public ArrayList<TimedEvent> getTatums() {
        return tatums;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public FileUploaderGDX getUploader() {
        return fileUploader;
    }

    public String getTheme(){ return theme;}

    public String getTwitterHandle(){
        return twitterHandle;
    }
}


