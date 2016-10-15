package com.wugy.javaPattern.mediator;

public class CPU extends Colleague {

	private String videoData = "";
	private String soundData = "";

	public CPU(Mediator mediator) {
		super(mediator);
	}

	public String getVideoData() {
		return videoData;
	}

	public String getSoundData() {
		return soundData;
	}

	public void executeData(String videoData, String soundData) {
		this.videoData = videoData;
		this.soundData = soundData;
		getMidiator().changed(this);
	}
}
