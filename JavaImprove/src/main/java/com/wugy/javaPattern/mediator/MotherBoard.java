package com.wugy.javaPattern.mediator;

public class MotherBoard implements Mediator {

	private CDDriver cdDriver;
	private CPU cpu;
	private SoundCard soundCard;
	private VideoCard videoCard;

	public void setCdDriver(CDDriver cdDriver) {
		this.cdDriver = cdDriver;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public void setSoundCard(SoundCard soundCard) {
		this.soundCard = soundCard;
	}

	public void setVideoCard(VideoCard videoCard) {
		this.videoCard = videoCard;
	}

	public CDDriver getCdDriver() {
		return cdDriver;
	}

	public CPU getCpu() {
		return cpu;
	}

	public SoundCard getSoundCard() {
		return soundCard;
	}

	public VideoCard getVideoCard() {
		return videoCard;
	}

	@Override
	public void changed(Colleague colleague) {
		if (colleague instanceof CDDriver) {
			CDDriver cdDriver = (CDDriver) colleague;
			String data = cdDriver.getData();
			String[] s = data.split(",");
			cpu.executeData(s[0], s[1]);
		} else if (colleague instanceof CPU) {
			CPU cpu = (CPU) colleague;
			String videoData = cpu.getVideoData();
			String soundData = cpu.getSoundData();
			videoCard.showData(videoData);
			soundCard.soundData(soundData);
		}
	}

}
