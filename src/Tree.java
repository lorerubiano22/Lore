import java.util.TreeSet;

public class Tree{
	private TreeSet<StochSol> arbol; 
	private static final int MAX_SOLS = 5;
	
    public Tree(){ 
    	arbol = new TreeSet<>();}
	
	public void addSolution(StochSol sol){
		arbol.add(sol);
		if(arbol.size() > MAX_SOLS) {
			arbol.pollLast();
		}
	}
	public TreeSet<StochSol> getTree(){return arbol;}	
	public int getSize(){return arbol.size();}
}