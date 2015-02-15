package com.softelement.treesearch;


public class TreeSearch {

	public static void main(String [ ] args) {
		
		//FIFO with depth cut-off strategy
		testTreeSearch(new FIFOStrategy(), "FIFO (width first)");

		//FIFO without repeating nodes in fringe strategy
		testTreeSearch(new FIFONotRepeatingStrategy(), "FIFO (width first) without repetition");

		//LIFO strategy
		testTreeSearch(new LIFOStrategy(), "LIFO (depth first)");

		//LIFO without repeting nodes in fringe strategy
		testTreeSearch(new LIFONotRepeatingStrategy(), "LIFO (depth first) without repetition");
	}
	
	protected static void testTreeSearch(IStrategy strategy, String strategyName) {
		
		String solutionPath;
		long startTime;
		long stopTime;
		long ellapsedTime;
		Problem problem = null;

		System.out.println(String.format("Starting %s...", strategyName));
		startTime = System.currentTimeMillis();
		problem = new Problem();
		solutionPath = treeSearch(problem, strategy);
		stopTime = System.currentTimeMillis();
	    ellapsedTime = stopTime - startTime;
	    if (!solutionPath.equals("")) { System.out.println(String.format("\n%s SOLUTION: %s", strategyName, solutionPath)); }
		System.out.println("Nodes generated: " + problem.getNodeCountGenerated());
		System.out.println("Nodes explored: " + problem.getNodeCountExplored());
		System.out.println("Execution time (ms): " + ellapsedTime);
		System.out.println("\n\n");
	}	
	
	protected static String treeSearch(Problem problem, IStrategy strategy) {
		do {
			//No more nodes to explore
			if (problem.fringeIsEmpty()) {
				System.out.println("Solution not found!");
				return "";
			}

			//Select node to explore
			NodeWrapper nodeWrapper = problem.selectNextNode(strategy);
			Node node = nodeWrapper.getNode();
			System.out.println(node);	//Print trace of explored node

			//Goal test
			if (problem.goalTest(node.getState())) {
				//@TODO: store path and output it here
				String solutionPath = node.pathTo(); 
				return solutionPath;
			}

			//Security depth cut-off
			if (node.getLevel() >= Problem.cutOffLevel) {
				System.out.println(String.format("Search aborted - Too deep, security cut-off at level %d", Problem.cutOffLevel));
				return "";
			}
			
			//fringe.expand(problem, node);
			problem.expandFringeNode(nodeWrapper);
		} while (true);
	} 
}

