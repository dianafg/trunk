package com.softelement.treesearch;

import java.util.ArrayList;

/**
 * 
 * @author "SoftElement"
 * Strategy class for LIFO (width first) search
 * not inserting already selected nodes in fringe.
 *
 */
public class LIFONotRepeatingStrategy implements IStrategy {

	//List of already selected nodes in previous calls to selectNextNode
	private ArrayList<Node> alreadySelectedNodes;
	
	public LIFONotRepeatingStrategy() {
		alreadySelectedNodes = new ArrayList<Node>();
	} 
	
	@Override
	public NodeWrapper selectNextNode(ArrayList<Node> fringe) {

		//Select next node with LIFO strategy
		if (!fringe.isEmpty()) {
			NodeWrapper nodeWrapper = new NodeWrapper();
			//Get LAST (LIFO) not already selected node
			boolean found = false;
			Node node = null;
			int pos;
			for (pos = fringe.size() - 1; pos >= 0; pos--) {
				node = fringe.get(pos);
				if (!alreadySelected(node)) { found = true; break; }
			}
			if (!found) { //All nodes has been previously selected
				return null; 
			} else {
				nodeWrapper.setNode(node);
				nodeWrapper.setNodePosition(pos);
				//Add node to the list of already selected nodes
				alreadySelectedNodes.add(node);
				return nodeWrapper;
			}
		} else {	//Empty fringe
			return null;
		}
	}
	
	/**
	 * Finds node in the list of already selected nodes,
	 * comparing by State and Action (ignores Parent and Level)
	 * Requires overridden equals methods in State and Action 
	 * to correctly compare nodes.
	 * @param node
	 * @return
	 */
	protected boolean alreadySelected(Node node) {

		boolean found = false;
		for (Node selectedNode : alreadySelectedNodes) {
			if (
				node.getAction().equals(selectedNode.getAction()) 
				&&
				node.getState().equals(selectedNode.getState())
			) {
				found = true;
				break;
			}
		}
		return found;
	}
}