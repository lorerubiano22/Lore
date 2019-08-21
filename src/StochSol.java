import java.util.TreeSet;

public class StochSol implements Comparable<StochSol> {
	private static int serial = 0;
	private final int id = serial++;
	private Individual sol;
	private double expOptCost;

	public StochSol(Individual sol, double optCost) {
		this.sol = sol;
		this.expOptCost = optCost;}

	public Individual getkey(){return sol;}	
	public double getValue(){return expOptCost;}
	public void setValue(double aux){this.expOptCost = aux;}
			
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((sol == null) ? 0 : sol.hashCode());
		long temp;
		temp = Double.doubleToLongBits(expOptCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StochSol other = (StochSol) obj;
		if (id != other.id)
			return false;
		if (sol == null) {
			if (other.sol != null)
				return false;
		} else if (!sol.equals(other.sol))
			return false;
		if (Double.doubleToLongBits(expOptCost) != Double.doubleToLongBits(other.expOptCost))
			return false;
		return true;}

	@Override
	public int compareTo(StochSol o) {
		if (o.getValue() == this.getValue()) {
			return 0;
		} else if (o.getValue()> this.getValue()) {
			return -1;
		} else {
			return 1;}}
}