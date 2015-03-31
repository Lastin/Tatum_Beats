package com.tatum.music;

import java.util.ArrayList;

public class Segment extends TimedEvent{
	private double loudnessStart;
	private double loudnessMaxTime;
	private double loudnessMax;
	private ArrayList<Double> pitches;
	private ArrayList<Double> timbres;

    //this class stores all the information for a segment (even sized cuts throughout the song,
    // holding information on timbre and pitch and loudness)
    // of the song, it is just getters and setters for the different data
    // parsed from the echonest api call with our additional generated data explained in track data
	
	public Segment(double start, double duration, double confidence, double loudnessStart,
					double loudnessMaxTime, double loudnessMax, ArrayList<Double> pitches,
					 ArrayList<Double> timbres,int position) {
		super(start, duration, confidence,new ArrayList<TimedEvent>(),position);
		setLoudnessStart(loudnessStart);
		setLoudnessMaxTime(loudnessMaxTime);
		setLoudnessMax(loudnessMax);
		setPitches(pitches);
		setTimbres(timbres);
	}
	public double getLoudnessStart() {
		return loudnessStart;
	}
	private void setLoudnessStart(double loudnessStart) {
		this.loudnessStart = loudnessStart;
	}
	public double getLoudnessMaxTime() {
		return loudnessMaxTime;
	}
	private void setLoudnessMaxTime(double loudnessMaxTime) {
		this.loudnessMaxTime = loudnessMaxTime;
	}
	public double getLoudnessMax() {
		return loudnessMax;
	}
	private void setLoudnessMax(double loudnessMax) {
		this.loudnessMax = loudnessMax;
	}
	public ArrayList<Double> getPitches() {
		return pitches;
	}
	private void setPitches(ArrayList<Double> pitches) {
		this.pitches = pitches;
	}
	public ArrayList<Double> getTimbres() {
		return timbres;
	}
	private void setTimbres(ArrayList<Double> timbres) {
		this.timbres = timbres;
	}

}
