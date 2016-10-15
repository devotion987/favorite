package com.wugy.java.custom.stack;

public class Node<T> {

	private T data;
	private Node<T> preNode;
	private Node<T> nextNode;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Node<T> getPreNode() {
		return preNode;
	}

	public void setPreNode(Node<T> preNode) {
		this.preNode = preNode;
	}

	public Node<T> getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node<T> nextNode) {
		this.nextNode = nextNode;
	}

}
