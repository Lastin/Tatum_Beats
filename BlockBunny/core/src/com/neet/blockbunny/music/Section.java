package com.neet.blockbunny.music;

public class Section extends TimedEvent {
	private double loudness;
	private double tempo;
	private double tempoConfidence;
	private int key;
	private double keyConfidence;
	private int mode;
	private double modeConfidence;
	private int timeSignature;
	private double timeSignatureConfidence;
	
	public Section(double start, double duration, double confidence,double loudness,double tempo,
				   double tempoConfidence, int key, double keyConfidence, int mode, double modeConfidence,
			       int timeSignature, double timeSignatureConfidence) {
		
		super(start, duration, confidence);
		this.setLoudness(loudness);
		this.setTempo(tempo);
		this.setTempoConfidence(tempoConfidence);
		this.setKey(key);
		this.setKeyConfidence(keyConfidence);
		this.setMode(mode);
		this.setModeConfidence(modeConfidence);
		this.setTimeSignature(timeSignature);
		this.setTimeSignatureConfidence(timeSignatureConfidence);

	}

	public double getLoudness() {
		return loudness;
	}

	private void setLoudness(double loudness) {
		this.loudness = loudness;
	}

	public double getTempo() {
		return tempo;
	}

	private void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public double getTempoConfidence() {
		return tempoConfidence;
	}

	private void setTempoConfidence(double tempoConfidence) {
		this.tempoConfidence = tempoConfidence;
	}

	public int getKey() {
		return key;
	}

	private void setKey(int key) {
		this.key = key;
	}

	public int getMode() {
		return mode;
	}

	private void setMode(int mode) {
		this.mode = mode;
	}

	public double getKeyConfidence() {
		return keyConfidence;
	}

	private void setKeyConfidence(double keyConfidence) {
		this.keyConfidence = keyConfidence;
	}

	public double getModeConfidence() {
		return modeConfidence;
	}

	private void setModeConfidence(double modeConfidence) {
		this.modeConfidence = modeConfidence;
	}

	public int getTimeSignature() {
		return timeSignature;
	}

	private void setTimeSignature(int timeSignature) {
		this.timeSignature = timeSignature;
	}

	public double getTimeSignatureConfidence() {
		return timeSignatureConfidence;
	}

	private void setTimeSignatureConfidence(double timeSignatureConfidence) {
		this.timeSignatureConfidence = timeSignatureConfidence;
	}

}
