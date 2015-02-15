package com.softelement.treesearch;

import java.util.ArrayList;

public class FIFOStrategy implements IStrategy {

	@Override
	public NodeWrapper selectNextNode(ArrayList<Node> fringe) {

		//Select next node with FIFO strategy
		if (!fringe.isEmpty()) {
			NodeWrapper nodeWrapper = new NodeWrapper();
			nodeWrapper.setNode(fringe.get(0));
			nodeWrapper.setNodePosition(0);
			return nodeWrapper;
		} else {
			return null;
		}
	}
}