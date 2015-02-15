package com.softelement.treesearch;

import java.util.ArrayList;

public class Problem {
	
	//Security depth cut-off to avoid infinite loops
	public static final int cutOffLevel = 20;
	
	private enum RiverSide {
		LeftSide,
		RightSide
	}
	
	protected static ArrayList<State> validStates;
	
	private ArrayList<Node> fringe;
	private int currentNodePosition;
	private int nodeCountGenerated;
	private int nodeCountExplored;
	
	public Problem() {
		initializeValidStates();
		Node node = new Node(Problem.getInitialState(), new Action());
		fringe = new ArrayList<Node>();
		fringe.add(node);	
	}
	
	public ArrayList<Node> getFringe() {
		return fringe;
	}

	public void setFringe(ArrayList<Node> fringe) {
		this.fringe = fringe;
	}

	public int getCurrentNodePosition() {
		return currentNodePosition;
	}

	public void setCurrentNodePosition(int currentNodePosition) {
		this.currentNodePosition = currentNodePosition;
	}
	
	public int getNodeCountGenerated() {
		return nodeCountGenerated;
	}

	public void setNodeCountGenerated(int nodeCountGenerated) {
		this.nodeCountGenerated = nodeCountGenerated;
	}

	public int getNodeCountExplored() {
		return nodeCountExplored;
	}

	public void setNodeCountExplored(int nodeCountExplored) {
		this.nodeCountExplored = nodeCountExplored;
	}

	public boolean fringeIsEmpty() {
		return (fringe.isEmpty());
	}

	public void expandFringeNode(NodeWrapper nodeWrapper) {
		
		int nodePosition = nodeWrapper.getNodePosition();
		Node node = nodeWrapper.getNode();
		
		//Remove current node from fringe
		fringe.remove(nodePosition);

		//Add children nodes to fringe
		ArrayList<Node> childrenNodes = createChildrenNodes(node);
		//fringe.addAll(nodePosition, childrenNodes);
		fringe.addAll(fringe.size(), childrenNodes);

		//Update node counters
		this.nodeCountExplored++;
		this.nodeCountGenerated += childrenNodes.size();
	}
	
	protected ArrayList<Node> createChildrenNodes(Node node) {
		
		State state = node.getState();
		
		//Find boat
		RiverSide riverSide = ((boatIsInLeftSide(state)) 
				? RiverSide.LeftSide
				: RiverSide.RightSide
		);
		
		//Compose actions list
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("2M");
		actions.add("1M");
		actions.add("2C");
		actions.add("1C");
		actions.add("1M|1C");
		
		//Create 0 or 1 children node for each action
		ArrayList<Node> childrenNodes = new ArrayList<Node>();
		for (String action : actions) {
			Node childrenNode = applyAction(action, riverSide, state);
			if (childrenNode != null) { childrenNodes.add(childrenNode); }
		}
		
		if (!childrenNodes.isEmpty()) {
			for (Node child : childrenNodes) {
				child.setParent(node);
				child.setLevel(node.getLevel() + 1);
			}
			return childrenNodes;
		} else {
			return null;
		}
	}

	protected Node applyAction(String action, RiverSide previousRiverSide, State previousState) {
			
		Node newNode;
		int howManyMLeft = 0;
		int howManyMRight = 0;
		int howManyCLeft = 0;
		int howManyCRight = 0;
		ArrayList<String> newLeftSide = new ArrayList<String>();
		ArrayList<String> newRightSide = new ArrayList<String>();

		switch (action) {
		case "2M":
			if (previousRiverSide.equals(RiverSide.LeftSide)) {
				howManyMLeft = howManyMLeft(previousState) - 2;
				howManyCLeft = howManyCLeft(previousState);
				howManyMRight = howManyMRight(previousState) + 2;
				howManyCRight = howManyCRight(previousState);
			} else {
				howManyMLeft = howManyMLeft(previousState) + 2;
				howManyCLeft = howManyCLeft(previousState);
				howManyMRight = howManyMRight(previousState) - 2;
				howManyCRight = howManyCRight(previousState);
			}
			break;
			
		case "1M":
			if (previousRiverSide.equals(RiverSide.LeftSide)) {
				howManyMLeft = howManyMLeft(previousState) - 1;
				howManyCLeft = howManyCLeft(previousState);
				howManyMRight = howManyMRight(previousState) + 1;
				howManyCRight = howManyCRight(previousState);
			} else {
				howManyMLeft = howManyMLeft(previousState) + 1;
				howManyCLeft = howManyCLeft(previousState);
				howManyMRight = howManyMRight(previousState) - 1;
				howManyCRight = howManyCRight(previousState);
			}
			break;
			
		case "2C":
			if (previousRiverSide.equals(RiverSide.LeftSide)) {
				howManyMLeft = howManyMLeft(previousState);
				howManyCLeft = howManyCLeft(previousState) - 2;
				howManyMRight = howManyMRight(previousState);
				howManyCRight = howManyCRight(previousState) + 2;
			} else {
				howManyMLeft = howManyMLeft(previousState);
				howManyCLeft = howManyCLeft(previousState) + 2;
				howManyMRight = howManyMRight(previousState);
				howManyCRight = howManyCRight(previousState) - 2;
			}
			break;
			
		case "1C":
			if (previousRiverSide.equals(RiverSide.LeftSide)) {
				howManyMLeft = howManyMLeft(previousState);
				howManyCLeft = howManyCLeft(previousState) - 1;
				howManyMRight = howManyMRight(previousState);
				howManyCRight = howManyCRight(previousState) + 1;
			} else {
				howManyMLeft = howManyMLeft(previousState);
				howManyCLeft = howManyCLeft(previousState) + 1;
				howManyMRight = howManyMRight(previousState);
				howManyCRight = howManyCRight(previousState) - 1;
			}
			break;
			
		case "1M|1C":
			if (previousRiverSide.equals(RiverSide.LeftSide)) {
				howManyMLeft = howManyMLeft(previousState) - 1;
				howManyCLeft = howManyCLeft(previousState) - 1;
				howManyMRight = howManyMRight(previousState) + 1;
				howManyCRight = howManyCRight(previousState) + 1;
			} else {
				howManyMLeft = howManyMLeft(previousState) + 1;
				howManyCLeft = howManyCLeft(previousState) + 1;
				howManyMRight = howManyMRight(previousState) - 1;
				howManyCRight = howManyCRight(previousState) - 1;
			}
			break;
			
		default:
		}
		
		//Check quantities between 0 and 3
		if (howManyMLeft < 0 || howManyMLeft > 3) return null;
		if (howManyMLeft != 0) { newLeftSide.add(String.format("%sM", String.valueOf(howManyMLeft))); }
		if (howManyCLeft < 0 || howManyCLeft > 3) return null;
		if (howManyCLeft != 0) { newLeftSide.add(String.format("%sC", String.valueOf(howManyCLeft))); }
		if (howManyMRight < 0 || howManyMRight > 3) return null;
		if (howManyMRight != 0) { newRightSide.add(String.format("%sM", String.valueOf(howManyMRight))); }
		if (howManyCRight < 0 || howManyCRight > 3) return null;
		if (howManyCRight != 0) { newRightSide.add(String.format("%sC", String.valueOf(howManyCRight))); }
		//Change boat side
		if (previousRiverSide.equals(RiverSide.LeftSide)) 
			{ newRightSide.add("B"); }
		else
			{ newLeftSide.add("B"); }
		
		//Join state fragments and compose node
		State newState = new State(join(newLeftSide), join(newRightSide));
		//Check if the State is in the valid states list
		if (validStates.contains(newState)) { 
			newNode = new Node(newState, new Action(action));
			return newNode;
		} else {	//Not a valid state (more Cs than Ms in some side)
			return null;
		}
	}
	
	protected boolean boatIsInLeftSide(State state) {
		return (state.getLeftSide().contains("B"));
	}
	
	protected int howManyMLeft(State state) {
		if (state.getLeftSide().contains("3M")) { return 3; }
		else if (state.getLeftSide().contains("2M")) { return 2; }
		else if (state.getLeftSide().contains("1M")) { return 1; }
		else { return 0; }
	}
	
	protected int howManyMRight(State state) {
		if (state.getRightSide().contains("3M")) { return 3; }
		else if (state.getRightSide().contains("2M")) { return 2; }
		else if (state.getRightSide().contains("1M")) { return 1; }
		else { return 0; }
	}
	
	protected int howManyCLeft(State state) {
		if (state.getLeftSide().contains("3C")) { return 3; }
		else if (state.getLeftSide().contains("2C")) { return 2; }
		else if (state.getLeftSide().contains("1C")) { return 1; }
		else { return 0; }
	}
	
	protected int howManyCRight(State state) {
		if (state.getRightSide().contains("3C")) { return 3; }
		else if (state.getRightSide().contains("2C")) { return 2; }
		else if (state.getRightSide().contains("1C")) { return 1; }
		else { return 0; }
	}

	public NodeWrapper selectNextNode(IStrategy strategy) {
		
		return strategy.selectNextNode(fringe);
	}

	public boolean goalTest(State state) {
		return (
			state.getLeftSide().contentEquals("")
			&&
			state.getRightSide().contentEquals("3M|3C|B")
		);
	}
	
	public static State getInitialState() {
		State state = new State();
		state.setLeftSide("3M|3C|B");
		state.setRightSide("");
		return state;
	}
	
	protected static void initializeValidStates() {
		
		validStates = new ArrayList<State>();

		validStates.add(new State("3M|3C|B", ""));
		validStates.add(new State("3M|2C|B", "1C"));
		validStates.add(new State("3M|2C",   "1C|B"));
		validStates.add(new State("3M|1C|B", "2C"));
		validStates.add(new State("3M|1C",   "2C|B"));
		validStates.add(new State("3M|B",    "3C"));
		validStates.add(new State("3M",      "3C|B"));
			
		validStates.add(new State("2M|2C|B", "1M|1C"));
		validStates.add(new State("2M|2C",   "1M|1C|B"));
			
		validStates.add(new State("1M|1C|B", "2M|2C"));
		validStates.add(new State("1M|1C",   "2M|2C|B"));
			
		validStates.add(new State("3C|B", "3M"));
		validStates.add(new State("3C",   "3M|B"));
		validStates.add(new State("2C|B", "3M|1C"));
		validStates.add(new State("2C",   "3M|1C|B"));
		validStates.add(new State("1C|B", "3M|2C"));
		validStates.add(new State("1C",   "3M|2C|B"));
		validStates.add(new State("",     "3M|3C|B"));				
	}
	
	protected String join(ArrayList<String> al) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < al.size(); i++)
		{
		    sb.append(al.get(i));
		    if (i < al.size() - 1) { sb.append("|"); }
		}
		return sb.toString();
	}
}
	

//	//Only allowed combinations
//	"3M|3C|B", ""
//	"3M|2C|B", "1C"
//	"3M|2C",   "1C|B"
//	"3M|1C|B", "2C"
//	"3M|1C",   "2C|B"
//	"3M|B",    "3C"
//	"3M",      "3C|B"
//	
//	"2M|2C|B", "1M|1C"
//	"2M|2C",   "1M|1C|B"
//	
//	"1M|1C|B", "2M|2C"
//	"1M|1C",   "2M|2C|B"
//	
//	"3C|B", "3M"
//	"3C",   "3M|B"
//	"2C|B", "3M|1C"
//	"2C",   "3M|1C|B"
//	"1C|B", "3M|2C"
//	"1C",   "3M|2C|B"
//	"",     "3M|3C|B"
	

	
	
//	//All combinations
//	"3M|3C|B", ""
//	"3M|2C|B", "1C"
//	"3M|2C",   "1C|B"
//	"3M|1C|B", "2C"
//	"3M|1C",   "2C|B"
//	"3M|B",    "3C"
//	"3M",      "3C|B"
//	
//	"2M|3C|B", "1M"
//	"2M|3C",   "1M|B"
//	"2M|2C|B", "1M|1C"
//	"2M|2C",   "1M|1C|B"
//	"2M|1C|B", "1M|2C"
//	"2M|1C",   "1M|2C|B"
//	"2M|B",    "1M|3C"
//	"2M",      "1M|3C|B"
//	
//	"1M|3C|B", "2M"
//	"1M|3C",   "2M|B"
//	"1M|2C|B", "2M|1C"
//	"1M|2C",   "2M|1C|B"
//	"1M|1C|B", "2M|2C"
//	"1M|1C",   "2M|2C|B"
//	"1M|B",    "2M|3C"
//	"1M",      "2M|3C|B"
//	
//	"3C|B", "3M"
//	"3C",   "3M|B"
//	"2C|B", "3M|1C"
//	"2C",   "3M|1C|B"
//	"1C|B", "3M|2C"
//	"1C",   "3M|2C|B"
//	"",     "3M|3C|B"
//	


////Ms leftSide
//howManyMLeft = howManyMLeft(previousState) - 2;
//if (howManyMLeft < 0 || howManyMLeft > 3) break;
//if (howManyMLeft != 0) { newLeftSide.add(String.format("%sM", String.valueOf(howManyMLeft))); }
////Cs leftSide
//howManyCLeft = howManyCLeft(previousState);
//if (howManyCLeft != 0) { newLeftSide.add(String.format("%sC", String.valueOf(howManyCLeft))); }
////Ms rightSide
//howManyMRight = howManyMRight(previousState) + 2;
//if (howManyMRight < 0 || howManyMRight > 3) break;
//if (howManyMRight != 0) { newRightSide.add(String.format("%sM", String.valueOf(howManyMRight))); }
////Cs rightside
//howManyCRight = howManyCRight(previousState);
//if (howManyCRight != 0) { newRightSide.add(String.format("%sC", String.valueOf(howManyCRight))); }
////Boat
//if (previousRiverSide.equals(RiverSide.LeftSide)) 
//	{ newRightSide.add("B"); }
//else
//	{ newLeftSide.add("B"); }
//
////Join state fragments and compose node
//State newState = new State(join(newLeftSide), join(newRightSide));
//if (validStates.contains(newState)) { 
//	newNode = new Node(newState, new Action("2M"));
//	return newNode;
//}
////Check if the State is in the valid states list

