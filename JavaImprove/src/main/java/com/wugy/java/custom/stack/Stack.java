package com.wugy.java.custom.stack;

public class Stack<T> {

	private Node<T> top;

	/**
	 * 压栈
	 * 
	 * @param data
	 */
	public void push(T data) {
		Node<T> node = new Node<T>();
		node.setData(data);
		if (!isEmpty()) {
			node.setNextNode(top);
			top.setPreNode(node);
		}
		top = node;
	}

	/**
	 * 取出栈顶元素“弹出”（移除）
	 * 
	 * @return
	 */
	public T pop() {
		T data = null;
		if (!isEmpty()) {
			data = peek();
			top = top.getNextNode();
		}
		return data;
	}

	/**
	 * 取出栈顶元素不“弹出”（移除）
	 * 
	 * @return
	 */
	public T peek() {
		return top.getData();
	}

	public boolean isEmpty() {
		return null == top;
	}
}
