package com.knitml.core.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class Stack<E> {

	private Object stackDelegate;
	private Method pushMethod;
	private Method popMethod;
	private Method peekMethod;
	private Method sizeMethod;

	public Stack() {
		Class<?> stackClass = null;
		try {
			// try using the JDK 1.6 new ArrayDeque implementation
			stackClass = Class.forName("java.util.ArrayDeque");
		} catch (Exception ex) {
			// fall back to JDK 1.0's java.util.Stack
			stackClass = java.util.Stack.class;
		}
		
		try {
			pushMethod = stackClass.getMethod("push", Object.class);
			popMethod = stackClass.getMethod("pop");
			peekMethod = stackClass.getMethod("peek");
			sizeMethod = stackClass.getMethod("size");
			stackDelegate = stackClass.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	public E peek() {
		try {
			return (E)peekMethod.invoke(stackDelegate);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getTargetException());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public E pop() {
		try {
			return (E)popMethod.invoke(stackDelegate);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getTargetException());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void push(E element) {
		try {
			pushMethod.invoke(stackDelegate, element);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getTargetException());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean empty() {
		try {
			int size = (Integer)sizeMethod.invoke(stackDelegate);
			return size == 0;
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getTargetException());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
