import umontreal.iro.lecuyer.rng.RandomStream;

public class Test {
	private String instanceName;
	public boolean solveDet, optDist;
	private double maxRouteCosts;
	private double maxTime;
	private double firstParam, secondParam, maxP, minP; // parameters of distribution and percentages for routes destruction
	private int seed;
	private double weightEconomic, weightEnvironment, weightSocial;
	public boolean preventive, corrective;
	public double varD, varT;
	public int simC, simL;
	private RandomStream rng;
	private double pDemand, pTime;
	private double safetyStockC1, safetyStockC2, safetyStockB;
	
	public Test(String name, boolean optDist, boolean solveDet, double rCosts, double t, double p1, double p2, double maxpdest, double minpdest,
			int s, int SC, int SL, double vD,double vT, double weightEconomic, double weightEnvironment, double weightSocial, boolean preventive, boolean corrective, double pc, double pb,
			double safetyStockC1, double safetyStockC2, double safetyStockB) {
		instanceName = name;
		this.optDist = optDist;
		this.solveDet = solveDet;
		maxRouteCosts = rCosts;
		maxTime = t;
		firstParam = p1;
		secondParam = p2;
		maxP = maxpdest;
		minP = minpdest;
		seed = s;
		simC = SC;
		simL = SL;
		varD = vD;
		varT = vT;
		this.weightEconomic = weightEconomic;
		this.weightEnvironment = weightEnvironment;
		this.weightSocial = weightSocial; 
		this.preventive = preventive;
		this.corrective = corrective;
		this.pDemand = pc;
		this.pTime = pb;
		this.safetyStockC1 = safetyStockC1;
		this.safetyStockC2 = safetyStockC2; 
		this.safetyStockB = safetyStockB;}

	public String getInstanceName() {return instanceName;}
	public boolean getOptDist() {return optDist;}
	public boolean getSolveDet() {return solveDet;}
	public double getMaxRouteCosts() {return maxRouteCosts;}
	public double getMaxTime() {return maxTime;}
	public double getFirstParam() {return firstParam;}
	public double getSecondParam() {return secondParam;}
	public double getminDest() {return minP;}
	public double getmaxDest() {return maxP;}
	public int getSeed() {return seed;}
	public double getWeightEconomic() {return weightEconomic;}
	public double getWeightEnvironment() {return weightEnvironment;}
	public double getWeightSocial() {return weightSocial;}
	public double getVarD() {return varD;}
	public double getVarT() {return varT;}
	public int getSimC() {return simC;}
	public int getSimL() {return simL;}
	public RandomStream getRandomStream() {return rng;}
	public boolean getpreventive() {return preventive;}
	public boolean getcorrective() {return corrective;}
	public double getPDemand() {return pDemand;}
	public double getPTime() {return pTime;}	
	public double getSafetyStockC1() {return this.safetyStockC1;}
	public double getSafetyStockC2() {return this.safetyStockC2;}
	public double getSafetyStockB() {return this.safetyStockB;}
	public void setSafetyStockC1(double aux) {this.safetyStockC1 = aux;}
	public void setSafetyStockC2(double aux) {this.safetyStockC2 = aux;}
	public void setSafetyStockB(double aux) {this.safetyStockB = aux;}	
	
	public void setPreventive(boolean s){this.preventive = s;}
	public void setCorrective(boolean s){this.corrective = s;}
		
	public String getSumary(Outputs output){
		String result = instanceName + "_" +  seed + "_" + weightEconomic  + "_" + weightEnvironment + "_" + weightSocial;
		if (output == null) result += " No feasible solution was found";
		else result += " DISTANCE: " + output.getOBSol().getDistance();
		return result;}

	public void setRandomStream(RandomStream stream) {rng=stream;}

	public String getOutputsFilePath() {
		String aux = this.instanceName + "_"  + this.optDist + "_" + this.solveDet + "_" + this.firstParam + "_" +  
				this.secondParam + "_" + this.maxP + "_" + this.minP + "_" +  
				this.seed + "_" + this.simC + "_" + this.simL + "_" +
				this.varD + "_" + this.varT + "_" + 
				this.weightEconomic + "_" + this.weightEnvironment + "_" + this.weightSocial + "_" +
				this.preventive + "_" + this.corrective + "_" +
				this.pDemand + "_" + this.pTime;		
		return aux;}
}