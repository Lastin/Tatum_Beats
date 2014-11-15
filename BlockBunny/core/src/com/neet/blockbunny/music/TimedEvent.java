package com.neet.blockbunny.music;

public class TimedEvent {
	private double start;
	private double duration;
	private double confidence;
	public TimedEvent(double start, double duration, double confidence){
		this.start = start;
		this.duration=duration;
		this.confidence=confidence;
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
	
}
