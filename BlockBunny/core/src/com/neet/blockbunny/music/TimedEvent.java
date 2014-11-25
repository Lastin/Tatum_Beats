package com.neet.blockbunny.music;

import java.util.ArrayList;

public class TimedEvent {
	private double start;
	private double duration;
	private double confidence;
	private ArrayList<TimedEvent> array;
	private int containedIn;
	public TimedEvent(double start, double duration, double confidence, ArrayList<TimedEvent> array){
		this.start = start;
		this.duration=duration;
		this.confidence=confidence;
		this.array = array;
		containedIn =-1;
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
}
