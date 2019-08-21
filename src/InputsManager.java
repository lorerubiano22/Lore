import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InputsManager{
	public static Inputs readInputs(String nodesFilePath, String vehiclesFilePath){
		Inputs inputs = null;
		try{   
			BufferedReader br = new BufferedReader(new FileReader(nodesFilePath));
			String f = null;
			int nNodes = 0;
			while((f = br.readLine()) != null){
			    if(f.charAt(0) != '#')
				nNodes++;}
			inputs = new Inputs(nNodes);
			FileReader reader = new FileReader(nodesFilePath);
			Scanner in = new Scanner(reader);
			in.useLocale(Locale.US);
			String s = null;
			int k = 0;
			while(in.hasNextLine()){
			    s = in.next();
				if(s.charAt(0) == '#') in.nextLine(); // skip comment line
				else{
				    double x = Double.parseDouble(s); 
				    double y = in.nextDouble();
				    int demand = in.nextInt();
				    Node node = new Node(k, x, y, demand);
				    inputs.getNodes()[k] = node;
				    k++;}}
			in.close(); br.close();			
			reader = new FileReader(vehiclesFilePath);
			in = new Scanner(reader);
			in.useLocale(Locale.US);
			int nVehInType=0;
			while(in.hasNextLine()){
				s = in.next();
				nVehInType++;
				if(s.charAt(0) == '#') in.nextLine();
			else{
				double vCap = Double.parseDouble(s);
				inputs.setVehCap(vCap);
				inputs.setVehNum(nVehInType);}}
			in.close();}
		catch (IOException exception){
		    System.out.println("Error processing inputs files: " + exception);}
		return inputs;}

	public static void readTimes(Inputs inputs, String inputTimePath){
		int n = inputs.getNumNodes();
		double[][] timelist = new double[n][n];
		for(int i = 0; i < n; i++) timelist[i][i] = 0;
		try{
			FileReader reader = new FileReader(inputTimePath);
			Scanner in = new Scanner(reader);
			in.useLocale(Locale.US);
			for(int i=0; i<(n-1)*n/2;i++){
				int a = in.nextInt();
				int b = in.nextInt();
				double time = in.nextDouble();                    
				timelist[a][b] = time;
				timelist[b][a] = time;}
			in.close();}
		catch(IOException exception){
			System.out.println("Error processing inputs files: " + exception);}
		inputs.setTimes(timelist);}

	public static void generateSavings(Inputs inputs, Costs cost, Test test) {
		double[][] times = inputs.getTimes();
		int nNodes = inputs.getNodes().length;
		Edge[] savingsArray = new Edge[(nNodes - 1) * (nNodes - 2) / 2];
		Node depot = inputs.getNodes()[0];
		int k = 0;
		for (int i = 1; i < nNodes - 1; i++) { // node 0 is the depot
			for (int j = i + 1; j < nNodes; j++) {
				Node iNode = inputs.getNodes()[i];
				Node jNode = inputs.getNodes()[j];
				Edge ijEdge = new Edge(iNode, jNode);
				double timeOriginDepot = times[0][iNode.getId()];
				double timeEndDepot = times[0][jNode.getId()];
				ijEdge.setTime(times[i][j]);
				ijEdge.calcOptCosts(cost, test);
				ijEdge.calcSavings(depot, cost, timeOriginDepot, timeEndDepot, test);
				Edge jiEdge = new Edge(jNode, iNode);
				jiEdge.setTime(times[i][j]);
				jiEdge.calcOptCosts(cost, test);
				jiEdge.calcSavings(depot, cost, timeEndDepot, timeOriginDepot, test);
				ijEdge.setInverse(jiEdge);
				jiEdge.setInverse(ijEdge);
				savingsArray[k] = ijEdge;
				k++;}}
		// Construct the savingsList by sorting the edgesList. Uses the compareTo()
		Arrays.sort(savingsArray,Collections.reverseOrder());
		List sList = Arrays.asList(savingsArray);
		LinkedList savingsList = new LinkedList(sList);
		inputs.setList(savingsList);}

	/*
	 * Creates the list of paired edges connecting node i with the depot,
	 *  i.e., it creates the edges (0,i) and (i,0) for all i > 0.
	 */
	public static void generateDepotEdges(Inputs inputs, Costs cost, Test test) {
		double[][] times = inputs.getTimes();
		Edge[][] edges = new Edge[inputs.getNodes().length][inputs.getNodes().length];
		Node[] nodes = inputs.getNodes();
		Node depot = nodes[0];
		for (int i = 1; i < nodes.length; i++) {
			Node iNode = nodes[i];
			Edge diEdge = new Edge(depot, iNode);
			diEdge.setTime(times[0][iNode.getId()]);
			diEdge.calcOptCosts(cost, test);
			iNode.setDiEdge(diEdge);
			edges[0][iNode.getId()] = diEdge;
			Edge idEdge = new Edge(iNode, depot);
			idEdge.setTime(times[0][iNode.getId()]);
			idEdge.calcOptCosts(cost, test);
			iNode.setIdEdge(idEdge);
			edges[iNode.getId()][0] = idEdge;
			idEdge.setInverse(diEdge);
			diEdge.setInverse(idEdge);}
		for (int i = 0; i < nodes.length; i++) { 
			for(int j = i+1; j < nodes.length; ++j){
				Node Node_i = nodes[i];
				Node Node_j = nodes[j];
				Edge ijEdge = new Edge(Node_i, Node_j);
				ijEdge.setTime(times[Node_i.getId()][Node_j.getId()]);
				ijEdge.calcOptCosts(cost, test);
				edges[Node_i.getId()][Node_j.getId()] = ijEdge;
				Edge jiEdge = new Edge(Node_j, Node_i);
				jiEdge.setTime(times[Node_j.getId()][Node_i.getId()]);
				jiEdge.calcOptCosts(cost, test);
				edges[Node_j.getId()][Node_i.getId()]= jiEdge;
				ijEdge.setInverse(jiEdge);
				jiEdge.setInverse(ijEdge);}}
		inputs.setEdges(edges);}

	public static double[] calcGeometricCenter(List<Node> nodesList){
		Node[] nodesArray = new Node[nodesList.size()];
		nodesArray = nodesList.toArray(nodesArray);
		return calcGeometricCenter(nodesArray);}

	public static double[] calcGeometricCenter(Node[] nodes){
		double sumX = 0.0F;
		double sumY = 0.0F;
		double[] center = new double[2];
		Node iNode; // iNode = ( x[i], y[i] )
		for( int i = 0; i < nodes.length; i++ ){   
			iNode = nodes[i];
			sumX = sumX + iNode.getX();
			sumY = sumY + iNode.getY();}
		center[0] = sumX / nodes.length; // mean for x[i]
		center[1] = sumY / nodes.length; // mean for y[i]
		return center;}
}