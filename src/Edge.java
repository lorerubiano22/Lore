public class Edge implements Comparable<Edge> {
	private Node origin, end;
	private double optimizationCost = 0.0;
	private double distance = 0.0, fuelCost = 0.0;
	private double time, economicCost = 0.0;
	private double socialCost = 0.0;
	private double co2Cost = 0.0;
	private double savings = 0.0;
	private Route inRoute = null; //route containing this edge
	private Edge inverseEdge = null; //edge with inverse direction

	public Edge(Node originNode, Node endNode) {
		origin = originNode;
		end = endNode;
		distance = distance(origin, end); }

	public Edge(Edge edge) {
		origin = new Node(edge.origin);
		end = new Node(edge.end);
		distance = distance(origin, end); }

	public void calcCO2Cost(double cE, double gamma) {
		this.co2Cost = cE * gamma * this.distance;}
	
	public void calcFuelCost(double cF, double kpl) {
		this.fuelCost = cF * kpl * this.distance;}

	public void calcEconomicCost(double DW) {
		this.economicCost = this.time * DW + this.fuelCost;}

	public void calcSocialCost(double aSocial) {
		double idemand = origin.getDemand();
		double jdemand = end.getDemand();
		this.socialCost = (idemand + jdemand) / 2 * this.distance * aSocial;}
	
	public void calcOptCosts(Costs cost, Test test) {
		double costs = 0;
		if(test.getOptDist()){
			costs += this.distance;
		}else{
			if (test.getWeightEnvironment() > 0) {
				calcCO2Cost(cost.getCE(), cost.getGamma());
				costs += test.getWeightEnvironment() * this.co2Cost;}
			if (test.getWeightEconomic() > 0) {
				calcFuelCost(cost.getCF(), cost.getKpl());
				calcEconomicCost(cost.getDriverCost());
				costs += test.getWeightEconomic() * this.economicCost;}
			if (test.getWeightSocial() > 0) {
				calcSocialCost(cost.getSocialCost());
				costs += test.getWeightSocial() * this.socialCost;}}
		this.optimizationCost = costs;}

	public void calcSavings(Node depot, Costs cost, double timeOriginDepot, double timeEndDepot, Test test) {
		double costOE = this.optimizationCost;
		Edge DO = new Edge(depot, this.origin);
		Edge ED = new Edge(this.end, depot);
		DO.setTime(timeOriginDepot);
		ED.setTime(timeEndDepot);
		DO.calcOptCosts(cost, test);
		ED.calcOptCosts(cost, test);
		double costDO = DO.optimizationCost;
		double costED = ED.optimizationCost;
		this.savings = costDO + costED - costOE;}

	public int compareTo(Edge otherEdge) {
		Edge other = otherEdge;
		double s1 = this.getSavings();
		double s2 = other.getSavings();
		if (s1 < s2) return 1;
		if (s1 > s2) return -1;
		return 0;}
		
	private double distance(Node o, Node e) {
		double X1 = o.getX();
		double Y1 = o.getY();
		double X2 = e.getX();
		double Y2 = e.getY();
		return Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));}

	public double computeDistance() {
		double X1 = origin.getX();
		double Y1 = origin.getY();
		double X2 = end.getX();
		double Y2 = end.getY();
		return Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));}
	
	public void setDist(double dist) {this.distance = dist;}
	public void setOptimizationCost(double c) {optimizationCost = c;}
	public void setSocialCost(double s) {socialCost = s;}
	public void setTime(double time) {this.time = time;}
	public void setInRoute(Route r) {inRoute = r;}
	public void setInverse(Edge e) {inverseEdge = e;}
	
	public Node getOrigin() {return origin;}
	public Node getEnd() {return end;}
	public double getOptCost() {return optimizationCost;}
	public double getDist() { return distance;}
	public double getco2Cost() { return co2Cost;}
	public double getTime() {return time;}
	public double getSocial() {return socialCost;}
	public double getSavings() {return savings;}
	public Route getInRoute() {return inRoute;}
	public Edge getInverseEdge() {return inverseEdge;}
}