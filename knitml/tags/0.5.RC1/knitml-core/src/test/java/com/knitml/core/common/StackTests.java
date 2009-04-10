/**
 * 
 */
package com.knitml.core.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StackTests {
	@Test
	public void createStackImplementation() throws Exception {
		Stack<String> stack = new Stack<String>();
		assertTrue(stack.empty());
		stack.push("A");
		stack.push("B");
		stack.push("C");
		assertThat(stack.pop(), is("C"));
		assertThat(stack.peek(), is("B"));
		assertThat(stack.pop(), is("B"));
		assertThat(stack.pop(), is("A"));
		assertTrue(stack.empty());
	}

}
