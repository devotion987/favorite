package com.wugy.javaPattern.mediator;

public class VideoCard extends Colleague {

	public VideoCard(Mediator mediator) {
		super(mediator);
	}

	public void showData(String data) {
		System.out.println("你正在看的是：" + data);
	}
}
