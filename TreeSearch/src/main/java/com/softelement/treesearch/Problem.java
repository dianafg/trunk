package com.softelement.treesearch;

import java.util.ArrayList;

/**
 * 
 * @author "SoftElement"
 * Controller class for TreeSearch.
 * Implements the transition function (applying the Actions to the States).
 * Defines the initial, final and valid States.
 * Contains the fringe.
 * Defines some data needed for execution.
 *
 */
public class Problem {
	
	/**
	 * Security depth cut-off to avoid infinite loops
	 */
	public static final int cutOffLevel = 20;
	
	private enum RiverSide {
		LeftSide,
		RightSide
	}
	
	/**
	 * Static list of valid states. Newly expanded nodes are checked against this list. 
	 */
	protected static ArrayList<State> validStates;
	
	/**
	 * Fringe: list of current candidate nodes to be explored.
	 */
	private ArrayList<Node> fringe;
	
	private int currentNodePosition; //Position in fringe of the node being explored currently
	private int nodeCountGenerated;	//Counter for nodes generated during the search
	private int nodeCountExplored;	//Counter for nodes explored (expanded) during the search
	
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

	/**
	 * Constructor
	 */
	public Problem() {
		initializeValidStates();
		//Initialize fringe with initial state node
		Node node = new Node(Problem.getInitialState(), new Action());
		fringe = new ArrayList<Node>();
		fringe.add(node);	
	}
	
	public boolean fringeIsEmpty() {
		return (fringe.isEmpty());
	}

	/**
	 * Explores a fringe node, removing it from the fringe,
	 * generating its children and adding then to the end of the fringe.
	 * @param nodeWrapper Node to be explored
	 */
	public void expandFringeNode(NodeWrapper nodeWrapper) {
		
		int nodePosition = nodeWrapper.getNodePosition();
		Node node = nodeWrapper.getNode();
		
		//Remove current node from fringe
		fringe.remove(nodePosition);

		//Generate children nodes applying the transition function
		ArrayList<Node> childrenNodes = createChildrenNodes(node);
		//Add children nodes to the end of the fringe
		//Note: if they were added at a different position, for instance at the current node position,
		//the search strategy would be different.
		//Perhaps this operation should be done in the Strategy class.
		fringe.addAll(fringe.size(), childrenNodes);
		//Inserting children at current node position: fringe.addAll(nodePosition, childrenNodes);

		//Update node counters
		this.nodeCountExplored++;
		this.nodeCountGenerated += childrenNodes.size();
	}
	
	/**
	 * Expand current node by applying the transition function.
	 * @param node Parent node to be expanded
	 * @return List of newly generated children
	 */
	protected ArrayList<Node> createChildrenNodes(Node node) {
		
		State state = node.getState();
		
		//Find boat
		RiverSide riverSide = ((boatIsInLeftSide(state)) 
				? RiverSide.LeftSide
				: RiverSide.RightSide
		);
		
		//Compose list of actions to apply 
		//(do not evaluate now if they are applicable to the current state)
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("2M");
		actions.add("1M");
		actions.add("2C");
		actions.add("1C");
		actions.add("1M|1C");
		
		//Apply each action and generate valid children nodes
		ArrayList<Node> childrenNodes = new ArrayList<Node>();
		for (String action : actions) {
			Node childrenNode = applyAction(action, riverSide, state);
			if (childrenNode != null) { childrenNodes.add(childrenNode); }
		}
		
		if (!childrenNodes.isEmpty()) {	//Some of the generated children were valid
			for (Node child : childrenNodes) {
				//Set tree info: parent and level
				child.setParent(node);
				child.setLevel(node.getLevel() + 1);
			}
			return childrenNodes;
		} else {	//No valid children
			return null;
		}
	}

	/**
	 * Implements the state transition function.
	 * Applies an Action to the previous State and generates one child node.
	 * Checks the validity of the child node State against a static list of valid States,
	 * if the child node is valid then it is returned, otherwise it is discarded.
	 * @param action
	 * @param previousRiverSide
	 * @param previousState
	 * @return A children node, or null if the action is not valid for the current State
	 */
	protected Node applyAction(String action, RiverSide previousRiverSide, State previousState) {
			
		Node newNode;
		int howManyMLeft = 0;
		int howManyMRight = 0;
		int howManyCLeft = 0;
		int howManyCRight = 0;
		ArrayList<String> newLeftSide = new ArrayList<String>();
		ArrayList<String> newRightSide = new ArrayList<String>();

		//For the current action, calculate how many M and C are in each river side 
		//after applying the action to the previous state
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
		
		//Check validity of quantities of M and C in each river side: they must be between 0 and 3
		if (howManyMLeft < 0 || howManyMLeft > 3) return null;
		if (howManyCLeft < 0 || howManyCLeft > 3) return null;
		if (howManyMRight < 0 || howManyMRight > 3) return null;
		if (howManyCRight < 0 || howManyCRight > 3) return null;

		//Generate State string fragments for each river side with the new quantities of M and C
		if (howManyMLeft != 0) { newLeftSide.add(String.format("%sM", String.valueOf(howManyMLeft))); }
		if (howManyCLeft != 0) { newLeftSide.add(String.format("%sC", String.valueOf(howManyCLeft))); }
		if (howManyMRight != 0) { newRightSide.add(String.format("%sM", String.valueOf(howManyMRight))); }
		if (howManyCRight != 0) { newRightSide.add(String.format("%sC", String.valueOf(howManyCRight))); }

		//Change boat side, generate State string fragment
		if (previousRiverSide.equals(RiverSide.LeftSide)) 
			{ newRightSide.add("B"); }
		else
			{ newLeftSide.add("B"); }
		
		//Join state string fragments and compose node
		State newState = new State(join(newLeftSide), join(newRightSide));

		//Check if the State is in the valid states list
		if (validStates.contains(newState)) {	//Valid state 
			newNode = new Node(newState, new Action(action));
			return newNode;
		} else {	//Not a valid state (more Cs than Ms in some side)
			return null;
		}
	}
	
	/**
	 * Parses State string to find the boat
	 * @param state
	 * @return
	 */
	protected boolean boatIsInLeftSide(State state) {
		return (state.getLeftSide().contains("B"));
	}
	
	/**
	 * Parses State string
	 * @param state
	 * @return Quantity of M in left river side
	 */
	protected int howManyMLeft(State state) {
		if (state.getLeftSide().contains("3M")) { return 3; }
		else if (state.getLeftSide().contains("2M")) { return 2; }
		else if (state.getLeftSide().contains("1M")) { return 1; }
		else { return 0; }
	}
	
	/**
	 * Parses State string
	 * @param state
	 * @return Quantity of M in right river side
	 */
	protected int howManyMRight(State state) {
		if (state.getRightSide().contains("3M")) { return 3; }
		else if (state.getRightSide().contains("2M")) { return 2; }
		else if (state.getRightSide().contains("1M")) { return 1; }
		else { return 0; }
	}
	
	/**
	 * Parses State string
	 * @param state
	 * @return Quantity of C in left river side
	 */
	protected int howManyCLeft(State state) {
		if (state.getLeftSide().contains("3C")) { return 3; }
		else if (state.getLeftSide().contains("2C")) { return 2; }
		else if (state.getLeftSide().contains("1C")) { return 1; }
		else { return 0; }
	}
	
	/**
	 * Parses State string
	 * @param state
	 * @return Quantity of C in right river side
	 */
	protected int howManyCRight(State state) {
		if (state.getRightSide().contains("3C")) { return 3; }
		else if (state.getRightSide().contains("2C")) { return 2; }
		else if (state.getRightSide().contains("1C")) { return 1; }
		else { return 0; }
	}

	/**
	 * Invokes Strategy selectNextNode method.
	 * @param strategy
	 * @return Next node with some extra info.
	 */
	public NodeWrapper selectNextNode(IStrategy strategy) {
				
		return strategy.selectNextNode(fringe);
	}

	/**
	 * Checks if this state is a final state of the problem 
	 * (the solution has been found).
	 * @param state
	 * @return
	 */
	public boolean goalTest(State state) {
		return (
			state.getLeftSide().contentEquals("")
			&&
			state.getRightSide().contentEquals("3M|3C|B")
		);
	}
	
	/**
	 * Composes initial State of the problem.
	 * @return
	 */
	public static State getInitialState() {
		State state = new State();
		state.setLeftSide("3M|3C|B");
		state.setRightSide("");
		return state;
	}
	
	/**
	 * Fills the static list of valid States of the problem.
	 * Any expanded children not included in this list will be discarded.
	 * State.equals method must be overridden to correctly find States in this list.
	 *  
	 */
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
	
	/**
	 * Utility method to concatenate State string fragments with '|' separator.
	 * @param al List of string fragments to be concatenated
	 * @return
	 */
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
	

	
//	//All State combinations (including not valid)
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
