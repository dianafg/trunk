package com.softelement.treesearch;

public class Action {
	
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Action(String action) {
		this.action = action;
	}
	
	public String toString() {
		return String.format("%s", action);
	}
	
	public Action() {
		action = "-";
	}
	
	@Override public boolean equals(Object o) {
	    //check for self-comparison
	    if (this == o) { return true; }
	    if (!(o instanceof Action)) { return false; }
	    Action action = (Action)o;
	    return (
	    		this.getAction().equals(action.getAction())
	    );
	  } 
}