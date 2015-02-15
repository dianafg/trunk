package com.softelement.treesearch;

import java.util.ArrayList;

public class FIFONotRepeatingStrategy implements IStrategy {

	private ArrayList<Node> alreadySelectedNodes;
	
	public FIFONotRepeatingStrategy() {
		alreadySelectedNodes = new ArrayList<Node>();
	} 
	
	@Override
	public NodeWrapper selectNextNode(ArrayList<Node> fringe) {

		//Select next node with FIFO strategy
		if (!fringe.isEmpty()) {
			NodeWrapper nodeWrapper = new NodeWrapper();
			//Find first not already selected node (ignore parent and level)
			boolean found = false;
			Node node = null;
			int pos;
			for (pos = 0; pos < fringe.size(); pos++) {
				node = fringe.get(pos);
				if (!alreadySelected(node)) { found = true; break; }
			}
			if (!found) { 
				return null; 
			} else {
				nodeWrapper.setNode(node);
				nodeWrapper.setNodePosition(pos);
				//Add node to the list of already selected nodes
				alreadySelectedNodes.add(node);
				return nodeWrapper;
			}
		} else {
			return null;
		}
	}
	
	protected boolean alreadySelected(Node node) {
		//Compare only Action and State, not Level nor Parent
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