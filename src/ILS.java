import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ILS{
	
	public static Individual perturb(Individual initSol, Test test, Inputs inputs, Costs costs, Random rng) {
		int n = inputs.getNumNodes();
		double u = rng.nextDouble();
		double pNodes = test.getminDest() + u * (test.getmaxDest() - test.getminDest());
		Individual newSol = new Individual(initSol);
		int totalRemove = (int) (pNodes * n);
		LinkedList<Node> selectedNodes = newSol.removeFrom(rng, totalRemove, costs, test, inputs);
		newSol.computeOptCost(costs, test);
		List<Route> routes = new LinkedList<Route>(newSol.getRoutes());
		Node depot = routes.get(0).getEdges().get(0).getOrigin();
		for (Node node:selectedNodes) {
			int bestRoute = -1;
			int bestPos = -1;
			double bestDiff = Double.MAX_VALUE;
			int nRoute = 0;
			for (Route sr:routes) {
				List<Node> subNodesOrder = extractNodes(sr);
				Node last = depot;
				int pos = 0;
				if (sr.getDemand() + node.getDemand() <=  inputs.getVehCap()) {
					for (Node curr:subNodesOrder) {
						double costIncrement = calcArc(last, node, curr, inputs, costs, test);
						if (costIncrement < bestDiff) {
							bestDiff = costIncrement;
							bestPos = pos;
							bestRoute = nRoute;}
						pos++;
						last = curr;}}
				nRoute++;}
			if (bestRoute >= 0) {
				Route toInsert = routes.get(bestRoute);
				Route result = insertNodeInPosition(toInsert, node, bestPos, inputs, costs, test);
				routes.set(bestRoute, result);} 
			else {
				LinkedList<Node> nodes = new LinkedList<Node>();
				nodes.add(node);
				nodes.add(depot);
				Route r = createRoute(nodes, inputs, costs, test);
				routes.add(r);}}
		Individual reconstructed = new Individual();
		addRoutes(reconstructed, routes, test, costs, inputs);
		reconstructed.updateSol();
		if(initSol.getOptCost() < reconstructed.getOptCost()) return initSol;
		return reconstructed;}

	public static Route createRoute(LinkedList<Node> nodes, Inputs input, Costs cost, Test test) {
		Node last =  nodes.getLast();
		Route route = new Route();
		for (Node n:nodes) {
			Edge edge = input.getEdge(last.getId(), n.getId());
			route.addEdge(edge);
			last = n;}
		route.updateRoute(cost, test);
		return route;}
	
	private static double calcArc(Node pre, Node curr, Node post, Inputs input, Costs cost, Test test) {
		Edge precurr = input.getEdge(pre.getId(), curr.getId());
		precurr.setTime(input.getTime(pre.getId(), curr.getId()));
		precurr.calcOptCosts(cost, test);
		Edge currpost = input.getEdge(curr.getId(), post.getId());
		currpost.setTime(input.getTime(curr.getId(), post.getId()));
		currpost.calcOptCosts(cost, test);
		Edge prepost = input.getEdge(pre.getId(), post.getId());
		prepost.setTime(input.getTime(pre.getId(), post.getId()));
		prepost.calcOptCosts(cost, test);
		return precurr.getOptCost() + currpost.getOptCost() - prepost.getOptCost();}
	
	private static Individual addRoutes(Individual sol, List<Route> routes, Test test, Costs costs, Inputs inputs) {
		for (Route preR:routes) {
			Route r = preR.improveNodesOrder(preR,inputs,test,costs);
			sol.getRoutes().add(r);
			sol.setDemand(sol.getDemand() + r.getDemand());}
		sol.updateSol();
		sol.computeOptCost(costs, test);
		return sol;}

	private static Route insertNodeInPosition(Route route, Node node, int position, Inputs inputs, Costs costs, Test test) {
		Route toInsert = route;
		LinkedList<Node> newnodes = extractNodes(toInsert);
		newnodes.add(position, node);
		Route newRoute = createRoute(newnodes, inputs, costs, test);
		return newRoute;}

	private static LinkedList<Node> extractNodes(Route route) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		for (Edge e:route.getEdges()) {
			Node current = e.getEnd();
			nodes.add(current);}
		return nodes;}

	public static LinkedList<Node> extractnodes(Individual sol) {
		LinkedList<Node> nodes = new  LinkedList<Node>();
		Node depot = sol.getRoutes().getFirst().getEdges().get(0).getOrigin();
		nodes.add(0,depot);
		for(Route r:sol.getRoutes()){
			for(int e=1;e<=r.getEdges().size()-1;e++){
				nodes.add(r.getEdges().get(e).getOrigin());}}
		return nodes;}
}