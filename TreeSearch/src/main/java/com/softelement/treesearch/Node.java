package com.softelement.treesearch;

import java.util.Arrays;

public class Node {
	
	private State state;
	private Action action;
	private Node parent;
	private int level;
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	} 

	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	} 
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public Node() {}

	public Node(State state, Action action) {
		this.state = state;
		this.action = action;
	}
	
	public Node(State state, Action action, Node parent, int level) {
		this.state = state;
		this.action = action;
		this.parent = parent;
		this.level = level;
	}
	
	@Override public String toString() {
		//Level padding: a '-' for each level down the decision tree
		char[] chars = new char[level + 1];
		Arrays.fill(chars, '-');
		String levelPadding = new String(chars);
		return String.format("%s> State: %s - Action: %s", levelPadding, state.toString(), action.toString()); 
	}

	public String pathTo() {
		if (parent == null) {
			return (String.format("\n%s", this.toString()));
		} else {
			return (String.format("%s\n%s", parent.pathTo(), this.toString()));
		}
	}
}
