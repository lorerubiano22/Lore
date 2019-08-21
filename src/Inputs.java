import java.util.LinkedList;

public class Inputs{
	private Node[] nodes;
	private Edge[][] edges;
	private double vCap = 0.0F; 
	private int nVeh = 0; 
	private LinkedList<Edge> savings = null;
	private double[][]  times = null;
	private double[] vrpCenter; // (x-bar, y-bar) is a geometric VRP center
	
	public Inputs(int n){
	    nodes = new Node[n]; // n nodes, including the depot
		vrpCenter = new double[2];}

	/* GET METHODS */
	public Node[] getNodes(){return nodes;}
	public LinkedList<Edge> getSavings(){return savings;}
	public double getVehCap(){return vCap;}
	//public int getVehNum(){return nVeh;}
	public double[] getVrpCenter(){return vrpCenter;}
	public double getTime(int i, int j) {return this.times[i][j];}
	public double[][] getTimes() {return this.times;}
	public Edge getEdge(int i, int j) {return this.edges[i][j];}
	public int getNumNodes(){return nodes.length;}

	/* SET METHODS */
	public void setVrpCenter(double[] center){vrpCenter = center;}
	public void setVehCap(double c){vCap = c;}
	public void setList(LinkedList<Edge> sList){savings = sList;}
	public void setTimes(double[][] tList){times = tList;}
	public void setEdges(Edge[][] edgesList){edges = edgesList;}
	public void setVehNum(int v){nVeh= v;}
}