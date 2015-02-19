package com.softelement.treesearch;

/**
 * 
 * @author "SoftElement"
 * Action POJO class.
 *
 */
public class Action {
	
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Action() {
		action = "-";	//Aesthetic, for initial state node toString()
	}
	
	public Action(String action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return String.format("%s", action);
	}
	
	/**
	 * Equals operator redefinition, needed for searching in ArrayList of nodes
	 */
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