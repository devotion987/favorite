package com.wugy.javaPattern.mediator;

public class Client {

	public static void main(String[] args) {
		// 创建中介者
		MotherBoard mediator = new MotherBoard();

		// 创建同事类
		CDDriver cdDriver = new CDDriver(mediator);
		CPU cpu = new CPU(mediator);
		VideoCard videoCard = new VideoCard(mediator);
		SoundCard soundCard = new SoundCard(mediator);

		// 让中介者知道所有同事类
		mediator.setCdDriver(cdDriver);
		mediator.setCpu(cpu);
		mediator.setSoundCard(soundCard);
		mediator.setVideoCard(videoCard);
		cdDriver.readCD();
	}
}
