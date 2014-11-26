package com.tatum.music;

import java.util.ArrayList;

public class Segment extends TimedEvent{
	private double loudnessStart;
	private double loudnessMaxTime;
	private double loudnessMax;
	private ArrayList<Double> pitches;
	private ArrayList<Double> timbres;

	
	
	public Segment(double start, double duration, double confidence, double loudnessStart,
					double loudnessMaxTime, double loudnessMax, ArrayList<Double> pitches,
					 ArrayList<Double> timbres) {
		super(start, duration, confidence,new ArrayList<TimedEvent>());
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
