package com.softelement.treesearch;

import java.util.ArrayList;

/**
 * 
 * @author "SoftElement"
 * Strategy class for LIFO (width first) search.
 *
 */
public class LIFOStrategy implements IStrategy {

	@Override
	public NodeWrapper selectNextNode(ArrayList<Node> fringe) {

		//Select next node with LIFO strategy
		if (!fringe.isEmpty()) {
			NodeWrapper nodeWrapper = new NodeWrapper();
			//Get LAST (LIFO) node
			nodeWrapper.setNode(fringe.get(fringe.size() - 1));
			nodeWrapper.setNodePosition(fringe.size() - 1);
			return nodeWrapper;
		} else {
			return null;
		}		
	}
}