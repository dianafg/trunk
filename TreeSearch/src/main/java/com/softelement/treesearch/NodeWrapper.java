package com.softelement.treesearch;

/**
 * 
 * @author "SoftElement"
 * Wrapper POJO class to store some additional info to a Node.
 *
 */
public class NodeWrapper {
	
	private Node node;
	private int nodePosition;	//Current position of node in fringe
	
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public int getNodePosition() {
		return nodePosition;
	}
	public void setNodePosition(int nodePosition) {
		this.nodePosition = nodePosition;
	}	
}