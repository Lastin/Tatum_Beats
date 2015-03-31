package com.tatum.music;

import java.util.ArrayList;

public class TimedEvent {
	private double start;
	private double duration;
	private double confidence;
	private ArrayList<TimedEvent> array;
	private int containedIn;
    private int position;
    //this class stores all the information for tatums/beats/bars
    // of the song, it is just getters and setters for the different data
    // parsed from the echonest api call with our additional generated data explained in track data
	public TimedEvent(double start, double duration, double confidence, ArrayList<TimedEvent> array, int position){
		this.start = start;
		this.duration=duration;
		this.confidence=confidence;
		this.array = array;
		containedIn =-1;
        this.position = position;
	}
	public double getStart(){
		return start;
	}
	public double getduration(){
		return duration;
	}
	public double getConfidence(){
		return confidence;
	}
	public ArrayList<TimedEvent> getContains(){
		return array;
	}
	public int getContainedIn(){
		return containedIn;
	}
	public void setContainedIn(int cont){
		if(containedIn==-1){
			containedIn=cont;
		}
	}
    public int getPosition(){
        return position;
    }
}
