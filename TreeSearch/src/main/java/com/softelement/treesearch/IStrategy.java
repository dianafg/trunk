package com.softelement.treesearch;

import java.util.ArrayList;
	
public interface IStrategy {
	//Node selectNextNode(ArrayList<Node> fringe);
	//Node selectNextNode(Fringe fringe);
	NodeWrapper selectNextNode(ArrayList<Node> fringe);
}