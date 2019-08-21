import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Route {
	private double optCost = 0;
	private double totalCost = 0;
	private double distance = 0, fuelCost = 0;
	private double time = 0, timeCost = 0, economicCost = 0;
	private double socialCost = 0;
	private double co2Cost = 0;
	private int demand = 0;
	private double expOptCost = 0;
	private double expTotalCost = 0;
	private double expDist = 0;
	private double expDistCost = 0;
	private double expTime = 0;
	private double expTimeCost = 0;
	private double expEconomicCost = 0;
	private double expSocialCost = 0;
	private double expCo2Cost = 0;
	private double expDemand = 0;
	private double relDemand = 0;
	private double relTime = 0;
	private double rel = 0;
	private LinkedList<Edge> edges;
	
	public Route() {
		edges = new LinkedList<Edge>();}

	public Route(Route r) {
		edges = new LinkedList<Edge>(r.edges);
		optCost = r.optCost;
		totalCost =r.totalCost;
		distance = r.distance;
		fuelCost = r.fuelCost;
		time = r.time;
		timeCost = r.timeCost;
		economicCost = r.economicCost;
		socialCost = r.socialCost;
		co2Cost = r.co2Cost;
		demand = r.demand;
		
		expOptCost = r.expOptCost;
		expTotalCost = r.expTotalCost;
		expDist = r.expDist;
		expDistCost = r.expDistCost;
		expTime = r.expTime;
		expTimeCost = r.expTimeCost;
		expEconomicCost = r.expEconomicCost;
		expSocialCost = r.expSocialCost;
		expCo2Cost = r.expCo2Cost;
		expDemand = r.expDemand;
		relDemand = r.relDemand;
		relTime = r.relTime;
		rel = r.rel;}
	
	public void setOptCost(Costs c, Test test) {
		double costs = 0;
		if(test.getOptDist()){
			costs += this.distance;
		}else{
			if(test.getWeightEconomic() > 0) {
				calcTimeCost(c);
				calcFuelCost(c);
				calcEconomicCost(c);
				costs += test.getWeightEconomic() * economicCost;}
			if(test.getWeightEnvironment() > 0) {
				calcCo2Cost(c);
				costs += test.getWeightEnvironment() * co2Cost;}
			if(test.getWeightSocial() > 0) {
				calcSocialCost(c);
				costs += test.getWeightSocial() * socialCost;}}
		this.optCost = costs;}

	public void calcSocialCost(Costs c) {
		this.socialCost = 0;
		double[] demandAcc = new double[edges.size()];
		int i = 0;
		for(Edge e:edges) {
			if(i == 0) demandAcc[i] = 0;
			else demandAcc[i] = demandAcc[i-1] + e.getOrigin().getDemand();
			i++;
			}
		double demandaTotal=demandAcc[edges.size()-1];
		for(i = 0; i < edges.size(); i++) {
			double Yij = demandaTotal - demandAcc[i];
			socialCost += c.getSocialCost() * Yij * edges.get(i).getDist();}}

	public void calcTimeCost(Costs c) {
		this.timeCost = this.time * c.getDriverCost() + c.getFixedCost();}

	public void calcFuelCost(Costs c) {
		this.fuelCost = c.getCF() * c.getKpl() * this.distance;}

	public void calcEconomicCost(Costs c) {
		this.economicCost = this.fuelCost + this.timeCost;}

	public void calcCo2Cost(Costs c) {
		this.co2Cost = c.getCE() * c.getGamma() * this.distance;}

	public void reverse() {
		for(int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			Edge invE = e.getInverseEdge();
			edges.remove(e);
			edges.add(0, invE);}}

	public Set<Node> getNodes() {
		Set<Node> nodes = new LinkedHashSet<>();
		for(Edge e : edges){
			nodes.add(e.getOrigin());
			nodes.add(e.getEnd());}
		return Collections.unmodifiableSet(nodes);}

	public LinkedList<Node> extractnodesFromRoute(Route r) {
		LinkedList<Node> nodes =new  LinkedList<Node>();
		for(int e=1;e<=r.edges.size()-1;e++){
			nodes.add(r.edges.get(e).getOrigin());}
		return nodes;}

	public LinkedList<Integer> extractnodesFromRoute() {
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for(int e=1; e <= this.edges.size()-1; e++){
			ids.add(this.edges.get(e).getOrigin().getId());}
		return ids;}
	
	public boolean repairRoute(Inputs inputs, Test test, Costs costs) {
		boolean anyChange = false;
		if (this.edges.size() >= 4) {
			boolean improve = true;
			while(improve){
				improve = false;
				for (int i = 1; i < this.edges.size()-1 && !improve; i++){
					for (int j = i + 1; j < this.edges.size() && !improve; j++){
						Node ni_1 = this.edges.get(i-1).getOrigin();
						Node ni = this.edges.get(i).getOrigin();
						Node nj = this.edges.get(j).getOrigin();
						Node njP1 = this.edges.get(j).getEnd();
						Edge e1 = this.edges.get(i-1);
						Edge e2 = this.edges.get(j);
						Edge e1Alternative = inputs.getEdge(ni_1.getId(),nj.getId());
						Edge e2Alternative = inputs.getEdge(ni.getId(),njP1.getId());
						double currentEstimateCost = e1.getOptCost() + e2.getOptCost();
						double altEstimateCost = e1Alternative.getOptCost() + e2Alternative.getOptCost();
						if(altEstimateCost < currentEstimateCost){
							improve = twoOptSwap(i, j, e1, e2, e1Alternative, e2Alternative, costs, test);
							if(improve) anyChange = true;}}}}}
		return anyChange;}

	public boolean twoOptSwap(int i, int j, Edge e1, Edge e2, Edge e1Alt, Edge e2Alt, Costs costs, Test test){
		Route route = new Route(this);
		route.removeEdges(i - 1);
		route.addEdge(i - 1, e1Alt);
		route.reverse(i, j - 1);
		route.removeEdges(j);
		route.addEdge(j, e2Alt);
		route.setOptCost(costs,test);
		if(route.getOptCost() < this.optCost){ 
			this.distance = route.distance;
			this.time = route.time;
			this.fuelCost = route.fuelCost;
			this.timeCost = route.timeCost;
			this.economicCost = route.economicCost;
			this.socialCost = route.socialCost;
			this.co2Cost = route.co2Cost;
			this.optCost = route.optCost;
			this.edges = route.edges;
			return true;}
		return false;}

	private void removeEdges(int i) {
		this.distance -= this.edges.get(i).getDist();
		this.time -= this.edges.get(i).getTime();
		this.demand -= this.edges.get(i).getEnd().getDemand();
		this.edges.remove(i);}

	private void addEdge(int i, Edge edge) {
		this.edges.add(i, edge);
		this.distance += edge.getDist();
		this.time += edge.getTime();
		this.demand += edge.getEnd().getDemand();}
	
	public void addEdge(Edge edge) {		
		edges.add(edge);
		this.distance += edge.getDist();
		this.time += edge.getTime();
		this.demand += edge.getEnd().getDemand();}
	
	public void removeEdge(Edge edge){   
		this.distance -= edge.getDist();
		this.time -= edge.getTime();
		this.demand -= edge.getEnd().getDemand();
		edges.remove(edge);}
	
	public void updateRoute(Costs costs,Test test) {
		this.setOptCost(costs,test);}

	private void reverse(int init, int end){
		for (int i = init; i <= end; i++) {
			Edge e = edges.get(i);
			Edge invE = e.getInverseEdge();
			if(invE == null){
				invE = new Edge(e.getEnd(), e.getOrigin());
				invE.setInverse(e);}
			edges.remove(i);
			edges.add(init, invE);}}

	public void setInvEdges() {
		for(Edge e:this.edges){
			Edge ie = new Edge(e.getEnd(),e.getOrigin());
			e.setInverse(ie);
			ie.setInverse(e);}}

	public Route improveNodesOrder(Route aRoute, Inputs inputs, Test test, Costs costs){
		List<Edge> edges = aRoute.edges;
		if(edges.size() >= 4){
			for(int i = 0; i <= edges.size()-3; i++){
				// Get the current way
				Edge e1 = edges.get(i);
				Edge e2 = edges.get(i + 1);
				Edge e3 = edges.get(i + 2);
				double currentCosts = e1.getOptCost() + e2.getOptCost() + e3.getOptCost();
				Node originE1 = e1.getOrigin();
				Node originE2 = e2.getOrigin();
				Node endE2 = e2.getEnd();
				Node endE3 = e3.getEnd();
				Edge e1b = inputs.getEdge(originE1.getId(), endE2.getId());
				e1b.calcOptCosts(costs, test);
				Edge e2b =  inputs.getEdge(endE2.getId(), originE2.getId());
				e2b.calcOptCosts(costs, test);;
				Edge e3b =  inputs.getEdge(originE2.getId(), endE3.getId());
				e3b.calcOptCosts(costs, test);
				double alterCosts = e1b.getOptCost() + e2b.getOptCost() + e3b.getOptCost();
				if(alterCosts < currentCosts){
					aRoute.removeEdge(e1);
					aRoute.addEdge(i, e1b);
					aRoute.removeEdge(e2);
					aRoute.addEdge(i + 1, e2b);
					aRoute.removeEdge(e3);
					aRoute.addEdge(i + 2, e3b);
					aRoute.updateRoute(costs, test);}}}
		return aRoute;}

	public void totalCost() {
		this.totalCost = this.economicCost + this.socialCost + this.co2Cost;}

	public void delete(int id) {
		Node a = edges.get(id).getOrigin();
		Node b = edges.get(id+1).getEnd();
		this.distance -= edges.get(id).getDist();
		this.time -= edges.get(id).getTime();
		this.demand -= edges.get(id).getEnd().getDemand();
		edges.remove(id);
		edges.add(id, new Edge(a, b));
		this.distance += edges.get(id).getDist() - edges.get(id+1).getDist();
		this.time += edges.get(id).getTime() - edges.get(id+1).getTime();
		edges.remove(id+1);}
	
	public double getTotalCosts() {return totalCost;}
	public double getOptCost() {return optCost;}
	public double getDist() {return distance;}
	public double getDistsCost() {return fuelCost;}
	public double getTime() {return time;}
	public double getTimeCost() {return timeCost;}
	public double getEconomicCost() {return economicCost;}
	public double getSocialCost() {return socialCost;}
	public double getCo2Cost() {return co2Cost;}
	public int getDemand() {return demand;}
	public List<Edge> getEdges() {return this.edges;}
	public int getSize() {return this.edges.size();}
	public double getExpDist() {return this.expDist;}
	public double getExpFuelCost() {return this.expDistCost;}
	public double getExpTime() {return this.expTime;}
	public double getExpTimeCost() {return this.expTimeCost;}
	public double getExpSocialCost() {return this.expSocialCost;}
	public double getExpCo2Cost() {return this.expCo2Cost;}
	public double getExpDemand() {return this.expDemand;}
	public double getRelDemand() {return this.relDemand;}
	public double getRelBattery() {return this.relTime;}
	public double getRel() {return this.rel;}
	public void setDist(double d) {distance = d;}
	public void setTime(double t) {time = t;}
	public void setOptCost(double q) {optCost = q;}
	public void setDemand(int q) {demand = q;}
	public void setExpDist(double expDist) {this.expDist = expDist;}
	public void setExpDistCost(double expDistCost) {this.expDistCost = expDistCost;}
	public void setExpTime(double expTime) {this.expTime = expTime;}
	public void setExpTimeCost(double expTimeCost) {this.expTimeCost = expTimeCost;}
	public void setExpEconomicCost(){this.expEconomicCost = this.expDistCost + this.expTimeCost;}
	public void setExpSocialCost(double expSocialCost) {this.expSocialCost = expSocialCost;}
	public void setExpCo2Cost(double expCo2Cost) {this.expCo2Cost = expCo2Cost;}
	public void setExpDemand(double expDemand) {this.expDemand = expDemand;}
	public void setExpTotalCost() {this.expTotalCost = this.expEconomicCost + this.expSocialCost + this.expCo2Cost;}
	public void setRelDemand(double reliabilityDemand) {relDemand = reliabilityDemand;}
	public void setRelTime(double reliabilityTime) {relTime = reliabilityTime;}
	public void setRel(double reliability) {rel = reliability;}
	
	public void setExpOptCost(Test test) {
		if(test.getOptDist()){
			this.expOptCost = this.expDist;
		}else{
			this.expOptCost = 
			test.getWeightEconomic() * this.expEconomicCost + 
			test.getWeightSocial() * this.expSocialCost + 
			test.getWeightEnvironment() * this.expCo2Cost;}}
		
	@Override
	public String toString(){
		String s = "";
		for(Edge e:this.edges) {
			if(e.getOrigin().getId() == 0)
				s = s.concat("0" + "-" + e.getEnd().getId());
			else s = s.concat("-" + e.getEnd().getId());}         
		return s;}
	
	public String toString(DecimalFormat df){
		String s = "";
		s = s.concat("\nOptimization costs = " + df.format(this.optCost) + " || ");
		s = s.concat("\nTotal costs = " + df.format(this.totalCost) + " || ");
		s = s.concat("Distance  = " + df.format(this.distance) + " || ");
		s = s.concat("Fuel cost = " + df.format(this.fuelCost) + " || ");
		s = s.concat("Time  = " + df.format(this.time) + " || ");
		s = s.concat("Time cost  = " + df.format(this.economicCost) + " || ");
		s = s.concat("Social Cost = " + df.format(this.socialCost) + " || ");
		s = s.concat("CO2 Cost = " + df.format(this.co2Cost) + " || ");
		s = s.concat("Demand  = " + this.demand + "\n");
		s = s.concat("Exp. optimization costs = " + df.format(this.expOptCost) + " || ");
		s = s.concat("\nExp. total costs = " + df.format(this.expTotalCost) + " || ");
		s = s.concat("Exp. distance  = " + df.format(this.expDist) + " || ");
		s = s.concat("Exp. time  = " + df.format(this.expTime) + " || ");
		s = s.concat("Exp. distance Cost = " + df.format(this.expDistCost) + " || ");
		s = s.concat("Exp. time cost  = " + df.format(this.expTimeCost) + " || ");
		s = s.concat("Exp. economic cost  = " + df.format(this.expEconomicCost) + " || ");
		s = s.concat("Exp. social Cost = " + df.format(this.expSocialCost) + " || ");
		s = s.concat("Exp. CO2 Cost = " + df.format(this.expCo2Cost) + " || ");
		s = s.concat("Exp. demand = " + df.format(this.expDemand) + " || ");
		s = s.concat("Rel. demand = " + df.format(this.relDemand) + " || ");
		s = s.concat("Rel. time = " + df.format(this.relTime) + " || ");
		s = s.concat("Rel. = " + df.format(this.rel) + "\n");
		return s;}

	public void reset() {
		this.expOptCost = 0;
		this.expTotalCost = 0;
		this.expDist = 0;
		this.expDistCost = 0;
		this.expTime = 0;
		this.expTimeCost = 0;
		this.expEconomicCost = 0;
		this.expSocialCost = 0;
		this.expCo2Cost = 0;
		this.expDemand = 0;
		this.relDemand = 0;
		this.relTime = 0;
		this.rel = 0;}

	public void addExpDist(double d) {this.expDist += d;}
	public void addExpFuelCost(double d) {this.expDistCost += d;}
	public void addExpTime(double d) {this.expTime += d;}
	public void addExpTimeCost(double d) {this.expTimeCost += d;}
	public void addExpSocialCost(double d) {this.expSocialCost += d;}
	public void addExpCo2Cost(double d) {this.expCo2Cost += d;}
	public void addExpDemand(double d) {this.expDemand += d;}
}