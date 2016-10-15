package com.wugy.java.custom.stack;

import org.junit.Test;

public class CustomTest {

	@Test
	public void testStack() {
		Stack<Integer> stack = new Stack<>();
		stack.push(1);
		stack.push(2);
		System.out.println(stack.peek());
		stack.push(3);
		stack.push(4);
		System.out.println(stack.peek());
		stack.push(5);
		stack.push(6);
		System.out.println(stack.pop());
        System.out.println(stack.peek());
	}
}
