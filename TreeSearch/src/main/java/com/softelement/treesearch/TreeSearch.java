package com.softelement.treesearch;


/**
 * @author "SoftElement"
 * TreeSearch entry point with main executable method.
 * Executes the TreeSearch with different strategies, to compare results.
 * Writes output to standard Out (Console).
 * 
 */
public class TreeSearch {

	public static void main(String [ ] args) {
		
		//FIFO strategy
		testTreeSearch(new FIFOStrategy(), "FIFO (width first)");

		//FIFO without repeating nodes in fringe strategy
		testTreeSearch(new FIFONotRepeatingStrategy(), "FIFO (width first) without repetition");

		//LIFO strategy
		testTreeSearch(new LIFOStrategy(), "LIFO (depth first)");

		//LIFO without repeating nodes in fringe strategy
		testTreeSearch(new LIFONotRepeatingStrategy(), "LIFO (depth first) without repetition");
	}
	
	/**
	 * Executes TreeSearch with the given Strategy.
	 * Prints execution trace, solution and performance info.
	 * @param strategy
	 * @param strategyName
	 */
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
	
	/**
	 * Implements TreeSearch loop.
	 * Applies security cut-off in depth to avoid infinite loops.
	 * @param problem
	 * @param strategy
	 * @return
	 */
	protected static String treeSearch(Problem problem, IStrategy strategy) {
		do {
			//No more nodes to explore
			if (problem.fringeIsEmpty()) {
				System.out.println("Solution not found!");
				return "";
			}

			//Select next node to explore
			NodeWrapper nodeWrapper = problem.selectNextNode(strategy);
			Node node = nodeWrapper.getNode();
			System.out.println(node);	//Print trace of explored node

			//Goal test: have we found a solution?
			if (problem.goalTest(node.getState())) {
				//Generate solution path and return it
				String solutionPath = node.pathTo(); 
				return solutionPath;
			}

			//Security depth cut-off
			if (node.getLevel() >= Problem.cutOffLevel) {	//Level is too deep
				System.out.println(String.format("Search aborted - Too deep, security cut-off at level %d", Problem.cutOffLevel));
				return "";
			}
		
			//Expand node children and update fringe
			problem.expandFringeNode(nodeWrapper);

		} while (true);
	} 
}

