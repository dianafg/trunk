package com.softelement.treesearch;

public class State {
	
	private String leftSide;
	private String rightSide;

	public String getLeftSide() {
		return leftSide;
	}
	
	public void setLeftSide(String leftSide) {
		this.leftSide = leftSide;
	}

	public String getRightSide() {
		return rightSide;
	}
	
	public void setRightSide(String rightSide) {
		this.rightSide = rightSide;
	}
	
	public State() {}
	
	public State(String leftSide, String rightSide){
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}
	
	@Override public String toString() {
		return String.format("[%s, %s]", leftSide, rightSide);
	}
	
	@Override public boolean equals(Object o) {
	    //check for self-comparison
	    if (this == o) { return true; }
	    if (!(o instanceof State)) { return false; }
	    State state = (State)o;
	    return (
	    		this.getLeftSide().equals(state.getLeftSide())
	    		&&
	    		this.getRightSide().equals(state.getRightSide())
	    );
	  } 
}