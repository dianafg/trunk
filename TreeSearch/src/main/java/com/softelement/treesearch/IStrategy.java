package com.softelement.treesearch;

import java.util.ArrayList;
	
/**
 * 
 * @author "SoftElement"
 * Interface implementations will be the different strategies
 * that explore the decision tree in a different order.
 *
 */
public interface IStrategy {

	/**
	 * Selects next node to be explored, does not modify fringe
	 * @param fringe
	 * @return Next node, including extra info: node position in fringe
	 */
	NodeWrapper selectNextNode(ArrayList<Node> fringe);
}