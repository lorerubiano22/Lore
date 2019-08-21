public class Node{
	private int id;
	private double x, y; 
	private int demand;
	private Route inRoute = null; // route containing the node
	private boolean isInterior = false; // interior node in a route
	private Edge diEdge = null; // edge from depot to node
	private Edge idEdge = null; // edge from node to depot

	public Node(int nodeId, double nodeX, double nodeY, int nodeDemand){
		id = nodeId;
		x = nodeX;
		y = nodeY;
		demand = nodeDemand;}

	public Node(Node node) {
		this.id = node.id;
		this.x = node.x;
		this.y = node.y;
		this.demand = node.demand;
		this.inRoute = node.inRoute;
		this.isInterior = node.isInterior;
		this.diEdge = node.diEdge;
		this.idEdge = node.diEdge;}
	
	public void setInRoute(Route r){inRoute = r;}
	public void setIsInterior(boolean value){isInterior = value;}
	public void setDiEdge(Edge e){diEdge = e;}
	public void setIdEdge(Edge e){idEdge = e;}

	public int getId(){return id;}
	public double getX(){return x;}
	public double getY(){return y;}
	public int getDemand(){return demand;}
	public Route getInRoute(){return inRoute;}
	public boolean getIsInterior(){return isInterior;}
	public Edge getDiEdge(){return diEdge;}
	public Edge getIdEdge(){return idEdge;}

	@Override
	public String toString(){ 
		String s = "";
		s = s.concat(this.id + " ");
		s = s.concat(this.x + " ");
		s = s.concat(this.y + " ");
		s = s.concat(this.demand + "");
		return s;}
}