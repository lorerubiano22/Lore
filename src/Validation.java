import java.util.ArrayList;
import java.util.Arrays;

import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStream;

public class Validation {

	public static boolean checkList(Costs costs, Test test, Individual sol, Inputs inputs){
		String text = "START VALIDATION\n";
		
		// 1. All customers served
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Route r:sol.getRoutes()){
			ids.addAll(r.extractnodesFromRoute());}
		int n = inputs.getNumNodes();
		for(int i = 1; i < n; i++){
			if(!ids.contains(new Integer(i))){
				text += "C1 (all customers served) fails";
				System.exit(0);}}
		
		// 2. Capacity
		for(Route r:sol.getRoutes()){
			if (r.getDemand() > inputs.getVehCap()){
				text += "C2 (capacity excedeed) fails";
				System.out.println(text);
				System.exit(0);}}
		
		// 3. Dist, time and dda (routes)
		for(Route r:sol.getRoutes()){
			double dist = 0, time = 0, dda = 0;	
			for(Edge e:r.getEdges()){
				dist += e.computeDistance();
				time += e.getTime();
				dda += e.getEnd().getDemand();}
			if(Math.abs(dist - r.getDist()) > 0.0001){
				text += "C3 (distance per route wrong specified)";
				System.out.println(text);
				System.exit(0);}
			if(Math.abs(time - r.getTime()) > 0.0001){
				text += "C3 (time per route wrong specified)";
				System.out.println(text);
				System.exit(0);}
			if(dda != r.getDemand()){
				text += "C3 (dda per route wrong specified)";
				System.out.println(text);
				System.exit(0);}}
		
		// 4. impacts costs, optimization cost (routes)
		Individual newSol = new Individual(sol);
		newSol.computeOptCost(costs, test);
		if(Math.abs(newSol.getOptCost() - sol.getOptCost()) > 0.0001 || 
		Math.abs(newSol.getDistanceCosts() - sol.getDistanceCosts()) > 0.0001 ||
		Math.abs(newSol.getTimeCosts() - sol.getTimeCosts()) > 0.0001 ||
		Math.abs(newSol.getEconomicCosts() - sol.getEconomicCosts()) > 0.0001 ||
		Math.abs(newSol.getSocialCosts() - sol.getSocialCosts()) > 0.0001 ||
		Math.abs(newSol.getCo2Costs() - sol.getCo2Costs()) > 0.0001){
			text += "C4 impacts costs, optimization cost (routes)";
			System.out.println(text);
			System.exit(0);}
		
		// 5. Dist and time (aggr)
		double agDist = 0, agTime = 0;
		for(Route r:sol.getRoutes()){
			agDist += r.getDist();
			agTime += r.getTime();}
		if(Math.abs(agDist - sol.getDistance()) > 0.0001){
			text += "C5 Dist";
			System.out.println(text +": " + agDist + " - " + sol.getDistance());
			System.exit(0);}
		if(Math.abs(agTime - sol.getTime()) > 0.0001){
			System.out.println(agTime);
			System.out.println(sol.getTime());
			text += "C5 Time";
			System.out.println(text);
			System.exit(0);} 
				
		// 6. impacts costs, optimization cost (aggr)		
		double optCost = 0, distCost = 0, timeCost = 0, economicCost = 0, socialCost = 0, co2Cost = 0;  
		for(Route r:sol.getRoutes()){
			optCost += r.getOptCost();
			distCost += r.getDistsCost();
			timeCost += r.getTimeCost();
			economicCost += r.getEconomicCost();
			socialCost += r.getSocialCost();
			co2Cost  += r.getCo2Cost();}
		if(Math.abs(optCost - sol.getOptCost()) > 0.0001 || 
		Math.abs(distCost - sol.getDistanceCosts()) > 0.0001 ||
		Math.abs(timeCost - sol.getTimeCosts()) > 0.0001 ||
		Math.abs(economicCost - sol.getEconomicCosts()) > 0.0001 ||
		Math.abs(socialCost - sol.getSocialCosts()) > 0.0001 ||
		Math.abs(co2Cost - sol.getCo2Costs()) > 0.0001){
			text += "C6 impacts costs, optimization cost (routes)";
			System.out.println(text);
			System.exit(0);}
		return true;}

	public static void checkSim(Test test){
		MRG32k3a stream = new MRG32k3a(); // L'Ecuyer stream
	    long[] seeds = new long[6];
	    for (int i = 0; i < seeds.length; i++) seeds[i] = test.getSeed() + i;
	    stream.setSeed(seeds);
        double[] vec = new double[10000];
        for(int i = 0; i < 10000; i++){
        	vec[i] = Stochastic.generate(stream, 20, 1);}
        Arrays. sort(vec);
        System.out.println(vec[0]);
        System.out.println(vec[5*10000/100]); // 5%
        System.out.println(vec[95*10000/100]); // 95%
        System.out.println(vec[9999]);}
}