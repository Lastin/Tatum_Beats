package com.neet.blockbunny.music;

import java.util.ArrayList;

public class Segment extends TimedEvent{
	private double loudnessStart;
	private double loudnessMaxTime;
	private double loudnessMax;
	private ArrayList<Double> pitches;
	private ArrayList<Double> timbres;

	// what about this?
	public Segment(ArrayList<Double> pitches, ArrayList<Double> timbres, double... params) {
		super(
			params[0], // segment start
			params[1], // segment duration
			params[2] // segment confidence
		);
		setLoudnessStart(params[3]);
		setLoudnessMaxTime(params[4]);
		setLoudnessMax(params[5]);
		setPitches(pitches);
		setTimbres(timbres);
		
	}
	
	public Segment(double start, double duration, double confidence, double loudnessStart,
					double loudnessMaxTime, double loudnessMax, ArrayList<Double> pitches,
					 ArrayList<Double> timbres) {
		super(start, duration, confidence);
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
