import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandCWS {
	public static Individual solve(Test aTest, Inputs inputs, Costs costs, Random rng) {
		// resets isInterior and inRoute in nodes
		Individual currentSol = new Individual(); 	
		currentSol = generateDummySol(inputs,costs,aTest);
		Node depot = inputs.getNodes()[0];
		int index;
		double p1 = aTest.getFirstParam();
		double p2 = aTest.getSecondParam();
		List<Edge> savings = new LinkedList<Edge>();
		List<Edge> savingsInputs = inputs.getSavings();
		for(Edge e:savingsInputs){
			savings.add(0, e);}
		// Copy savingsList in reverse order
		while(!savings.isEmpty()){
			index = getRandomPosition(p1,p2, rng, savings.size());
			Edge ijEdge = savings.get(index);
			savings.remove(ijEdge); // remove edge from list
			// Determine the nodes i < j that define the edge
			Node iNode = ijEdge.getOrigin();
			Node jNode = ijEdge.getEnd();
			// Determine the routes associated with each node
			Route iR = iNode.getInRoute();
			Route jR = jNode.getInRoute();
			// If all necessary conditions are satisfied, merge
			boolean isMergePossible;
			isMergePossible = checkMergingConditions(aTest, inputs, iR, jR, ijEdge,costs);
			if(isMergePossible) {
				// Get an edge iE in iR containing nodes i and 0
				Edge iE = getEdge(iR, iNode, depot); // iE is either (0,i) or (i,0)
				// Remove edge iE from iR route and update costs
				iR.getEdges().remove(iE);
				iR.setDist(iR.getDist() - iE.getDist());
				iR.setTime(iR.getTime() - iE.getTime());	             
				// If there are more than one edge then i will be interior
				if(iR.getEdges().size() > 1) iNode.setIsInterior(true);
				// If new route iR does not start at 0 it must be reversed
				if(iR.getEdges().get(0).getOrigin() != depot) iR.reverse();
				// Get an edge jE in jR containing nodes j and 0
				Edge jE = getEdge(jR, jNode, depot); // jE is either (0,j) or (j,0)
				// Remove edge jE from jR route
				jR.getEdges().remove(jE);
				jR.setDist(jR.getDist() - jE.getDist());
				jR.setTime(jR.getTime() - jE.getTime());
				// If there are more than one edge then j will be interior
				if(jR.getEdges().size() > 1) jNode.setIsInterior(true);
				// If new route jR starts at 0 it must be reversed
				if(jR.getEdges().get(0).getOrigin() == depot) jR.reverse(); // reverseRoute(inputs, jR);
				// Add ijEdge = (i, j) to new route iR
				iR.getEdges().add(ijEdge);
				iR.setDist(iR.getDist() + ijEdge.getDist());
				iR.setTime(iR.getTime() + ijEdge.getTime());
				iR.setDemand(iR.getDemand() + ijEdge.getEnd().getDemand());
				jNode.setInRoute(iR);
				// Add route jR to new route iR
				for(Edge e : jR.getEdges()) {
					iR.getEdges().add(e);
					iR.setDemand(iR.getDemand() + e.getEnd().getDemand());
					iR.setDist(iR.getDist() + e.getDist());
					iR.setTime(iR.getTime() + e.getTime());
					e.getEnd().setInRoute(iR);}
				// Delete route jR from currentSolution
				currentSol.getRoutes().remove(jR);}}
		currentSol.updateSol();
		currentSol.computeOptCost(costs, aTest);
		return currentSol;}

	private static Individual generateDummySol(Inputs inputs,Costs costList,Test aTest) {
		Individual sol = new Individual();
		for(int i = 1; i < inputs.getNodes().length; i++) { // i = 0 is the depot
			Node iNode = inputs.getNodes()[i];
			Edge diEdge = iNode.getDiEdge();
			Edge idEdge = iNode.getIdEdge();
			Route didRoute = new Route();
			didRoute.getEdges().add(diEdge);
			didRoute.setDemand(didRoute.getDemand() + diEdge.getEnd().getDemand());
			didRoute.setDist(didRoute.getDist() + diEdge.getDist());
			didRoute.setTime(didRoute.getTime() + diEdge.getTime());
			didRoute.getEdges().add(idEdge);
			didRoute.setDemand(didRoute.getDemand() + idEdge.getEnd().getDemand());
			didRoute.setDist(didRoute.getDist() + idEdge.getDist());
			didRoute.setTime(didRoute.getTime() + idEdge.getTime());
			iNode.setInRoute(didRoute);
			iNode.setIsInterior(false);
			sol.getRoutes().add(didRoute);
			sol.setDemand(sol.getDemand() + didRoute.getDemand());}
		return sol;}

	/** 
	 * Given aRoute, iNode and depot, returns the edge in aRoute which
	 * contains iNode and depot (i.e., the first or the last edge)
	 */
	private static Edge getEdge(Route aRoute, Node iNode, Node depot){   
		// Check if firstEdge in aRoute contains iNode and depot
		Edge firstEdge = aRoute.getEdges().get(0);
		Node origin = firstEdge.getOrigin();
		Node end = firstEdge.getEnd();
		if((origin == iNode && end == depot)||(origin == depot && end == iNode))
			return firstEdge;
		else{
		   int lastIndex = aRoute.getEdges().size() - 1;
		   Edge lastEdge = aRoute.getEdges().get(lastIndex);
		   return lastEdge;}}

	private static boolean checkMergingConditions(Test test, Inputs inputs, Route iR, Route jR, Edge ijEdge, Costs costList) {
		// Condition 1: iR and jR are not the same route
		if( iR == jR ) return false;
		// Condition 2: both nodes are exterior nodes in their respective routes
		Node iNode = ijEdge.getOrigin();
		Node jNode = ijEdge.getEnd();
		if(iNode.getIsInterior()||jNode.getIsInterior()) return false;
		// Condition 3: demand after merging can be covered by a single vehicle
		if((1 - test.getSafetyStockC1()) * inputs.getVehCap() < (iR.getDemand() + jR.getDemand())) return false;
		// Condition 4: if the traveling time exceeds the driving range battery life
		double battery = (1 - test.getSafetyStockB()) * costList.getBattery();
		if(battery < iR.getTime() + jR.getTime()) return false;
		return true;}

	private static int getRandomPosition(double a, double b, Random r, int size) {
		double beta = a + r.nextDouble() * (b - a);
		int index = (int) (Math.log(r.nextDouble()) / Math.log(1 - beta));
		index = index % size;
		return index;}
}