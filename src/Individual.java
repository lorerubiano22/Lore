import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Individual{
	private static long nInstances = 0;
	private long id;
	private double runTime = 0;
	
	private double optCost = 0, totalCost = 0;
	private double distance = 0, FuelCost = 0;
	private double time = 0, timeCost = 0, economicCost = 0;
	private double socialCost = 0, co2Cost = 0;
	private int demand = 0;
	private LinkedList<Route> routes;
		
	private double expOptCost = 0, expTotalCost = 0;
	private double expDistance = 0, expFuelCost = 0;
	private double expTime = 0, expTimeCost = 0, expEconomicCost = 0;
	private double expSocialCost = 0, expCo2Cost = 0;
	private double expDemand = 0;
	private double relDemand = 0, relTime = 0, rel = 0;
	private double remCustomers = 0;
	
	public Individual(){  
		nInstances++;
		id = nInstances;
		routes = new LinkedList<Route>();}

	public Individual(Individual sol){   
		id = sol.id;
		routes = new LinkedList<Route>();
		for (Route r:sol.getRoutes()){
			Route rSol = new Route(r);
			routes.add(rSol);}
		this.runTime = sol.runTime;
		this.optCost = sol.optCost;
		this.totalCost = sol.totalCost;
		this.distance = sol.distance;
		this.FuelCost = sol.FuelCost;
		this.time = sol.time;
		this.timeCost = sol.timeCost;
		this.economicCost = sol.economicCost;
		this.socialCost = sol.socialCost;
		this.co2Cost = sol.co2Cost;
		this.demand = sol.demand;
			
		this.expOptCost = sol.expOptCost;
		this.expTotalCost = sol.expTotalCost;
		this.expDistance = sol.expDistance;
		this.expFuelCost = sol.expFuelCost;
		this.expTime = sol.expTime;
		this.expTimeCost = sol.expTimeCost;
		this.expEconomicCost = sol.expEconomicCost;
		this.expSocialCost = sol.expSocialCost;
		this.expCo2Cost = sol.expCo2Cost;
		this.expDemand = sol.expDemand;
		this.relDemand = sol.relDemand;
		this.relTime = sol.relTime;
		this.rel = sol.rel;}

	private void reset() {
		this.expOptCost = 0;
		this.expTotalCost = 0;
		this.expDistance = 0;
		this.expFuelCost = 0;
		this.expTime = 0;
		this.expTimeCost = 0;
		this.expEconomicCost = 0;
		this.expSocialCost = 0;
		this.expCo2Cost = 0;
		this.expDemand = 0;
		this.relDemand = 0;
		this.relTime = 0;
		this.rel = 0;
		for(Route r:this.routes){
			r.reset();}}
	
	public void setOptimizationCost() {
		double optimizationCost=0;
		for (Route r: this.routes) {
			optimizationCost+= r.getOptCost();}
		this.optCost = optimizationCost;
		System.out.println("this.optimizationCost " + this.optCost);}    

	public void setDistance() {
		double aux = 0;
		for(Route r:this.routes) {
			aux += r.getDist();}
		this.distance = aux;}

	public void setDistancesCost() {
		double aux=0;
		for (Route r: this.routes) {
			aux += r.getDistsCost();}
		this.FuelCost=aux;}

	public void setCO2Cost() {
		double aux=0;
		for (Route r: this.routes) {
			aux += r.getCo2Cost();}
		this.co2Cost=aux;}

	public void setTime() {
		double aux=0;
		for (Route r: this.routes) {
			aux += r.getTime();}
		this.time = aux;}

	public void setTimeCost() {
		setDistance();
		setDistancesCost();
		double aux=0;
		for (Route r: this.routes) {
			aux += r.getEconomicCost();}
		this.timeCost=aux;}

	public void setSocialCost() {
		double aux = 0;
		for (Route r: this.routes) {
			aux += r.getSocialCost();}
		this.socialCost=aux;}

	public void setDemand(int d) {demand = d;}

	public void computeOptCost(Costs costs, Test aTest) {
		double aux = 0, auxFuelCost = 0, auxTimeCost = 0, auxEconomicCost = 0, auxCo2 = 0, auxSocial = 0;
		for(Route r:routes){
			r.setOptCost(costs, aTest);
			aux += r.getOptCost();
			auxFuelCost += r.getDistsCost();
			auxTimeCost += r.getTimeCost();
			auxEconomicCost += r.getEconomicCost();
			auxCo2 += r.getCo2Cost();
			auxSocial += r.getSocialCost();}
		this.optCost = aux;
		this.FuelCost = auxFuelCost;
		this.timeCost = auxTimeCost;
		this.economicCost = auxEconomicCost;
		this.co2Cost = auxCo2;
		this.socialCost = auxSocial;}

	public void updateSol(){
		setDistance();
		setTime();}

	public void localSearch(Test test, Inputs inputs, Costs costs) {
		boolean changes = false;
		for(Route r:this.routes){
			Route aux = new Route(r);
			changes = r.repairRoute(inputs, test, costs);
			if(changes){
				this.demand += r.getDemand() - aux.getDemand();
				this.distance += r.getDist() - aux.getDist();
				this.FuelCost += r.getDistsCost() - aux.getDistsCost();
				this.time += r.getTime() - aux.getTime();
				this.timeCost += r.getTimeCost() - aux.getTimeCost();
				this.economicCost += r.getEconomicCost() - aux.getEconomicCost();
				this.co2Cost += r.getCo2Cost() - aux.getCo2Cost();
				this.socialCost += r.getSocialCost() - aux.getSocialCost();
				this.optCost += r.getOptCost() - aux.getOptCost();}}}

	public void replaceRoute(int i, Route newRoute, Test test) {
		Route oldRoute = routes.remove(i);
		routes.add(i, newRoute);
		this.distance += newRoute.getDist()-oldRoute.getDist(); 
		this.time += newRoute.getTime() - oldRoute.getTime();
		if(test.getWeightEconomic() > 0){ 
			this.FuelCost += newRoute.getDistsCost()-oldRoute.getDistsCost();
			this.timeCost += newRoute.getTimeCost() - oldRoute.getTimeCost();
			this.economicCost += newRoute.getEconomicCost() - oldRoute.getEconomicCost();}
		if(test.getWeightEnvironment() > 0) 
			this.co2Cost += newRoute.getCo2Cost() - oldRoute.getCo2Cost();
		if(test.getWeightSocial() > 0) 
			this.socialCost += newRoute.getSocialCost() - oldRoute.getSocialCost();
		this.optCost += newRoute.getOptCost() - oldRoute.getOptCost();}

	public static LinkedList<Node> extractNodes(Route route) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		for (Edge e : route.getEdges()) {
			Node current = e.getEnd();
			nodes.add(current);}
		return nodes;}

	public static LinkedList<Node> customerNodes(Route route) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		for(int e=1;e<route.getNodes().size();e++) {
			Node current = route.getEdges().get(e).getOrigin();
			nodes.add(current);}
		return nodes;}
	
	public void finalUpdate(Costs costs, Test test) {
		if(test.getWeightEconomic()==0){
			computeDistanceCost(costs);
			computeTimeCost(costs);
			computeEconomicCost(costs);}
		if(test.getWeightEnvironment()==0)
			computeEnviromentCost(costs);
		if(test.getWeightSocial()==0)
			computeSocialCost(costs);
			computeTotalCost();}

	private void computeDistanceCost(Costs costs){
		double aux = 0;
		for(Route r:this.routes){
			r.calcFuelCost(costs);
			aux += r.getDistsCost();}
		this.FuelCost = aux;}
	
	private void computeTimeCost(Costs costs){
		double aux = 0;
		for(Route r:this.routes){
			r.calcTimeCost(costs);
			aux += r.getTimeCost();}
		this.timeCost = aux;}
	
	private void computeEconomicCost(Costs costs){
		double aux = 0;
		for(Route r:this.routes){
			r.calcEconomicCost(costs);
			aux += r.getEconomicCost();}
		this.economicCost = aux;}
	
	private void computeSocialCost(Costs costs) {
		double aux = 0;
		for(Route r:this.routes){
			r.calcSocialCost(costs);
			aux += r.getSocialCost();}
		this.socialCost = aux;	}

	private void computeEnviromentCost(Costs costs) {
		double aux = 0;
		for(Route r:this.routes){
			r.calcCo2Cost(costs);
			aux += r.getCo2Cost();}
		this.co2Cost = aux;}
	
	private void computeTotalCost() {
		double aux = 0;
		for(Route r:this.routes){
			r.totalCost();
			aux += r.getTotalCosts();}
		this.totalCost = aux;}
	
	public LinkedList<Node> removeFrom(Random rng, int n, Costs costs, Test test, Inputs inputs) {
		LinkedList<Node> selectedNodes = new LinkedList<Node>();
		int tries = 0;
		while(selectedNodes.size() < n && tries < n*5){
			tries++;
			int r = rng.nextInt(this.routes.size());
			Route route = this.getRoutes().get(r);
			if(route.getEdges().size() > 2) {				
				int pos = rng.nextInt(route.getEdges().size()-1);
				Node selected = route.getEdges().get(pos).getEnd();
				selectedNodes.add(selected);
				this.getRoutes().get(r).delete(pos);}}		
		return selectedNodes;}
	
	public void MCS(boolean large, Test test, Inputs inputs, Costs costs) {
		int nSim = test.simC;
		if(large) nSim = test.simL;
		this.reset();
		int remCust = 0;
		
		double auxRelDemand = 0, auxRelTime = 0, auxRel = 0;
		Node d = this.getRoutes().get(0).getEdges().get(0).getOrigin();
		if(!test.preventive && !test.corrective){
			for(int i = 0; i < nSim; i++){
				boolean iCapacity = false;
				boolean iBattery = false;
				boolean irel = false;
				for(Route r:this.routes){
					double auxExpDistance = 0, auxExpFuelCost = 0;
					double auxExpTime = 0, auxExpTimeCost = 0;
					double auxExpSocialCost = 0, auxExpCo2Cost = 0;
					double auxExpDemand = 0;
					boolean rCapacity = false;
					boolean rBattery = false;
					boolean rel = false;
					double remCapacity = Math.min(r.getDemand() * (1 + test.getSafetyStockC2()), inputs.getVehCap());
					double remBattery = costs.getBattery();
					auxExpTimeCost += costs.getFixedCost();
					for(Edge e:r.getEdges()){
						double meanTime = e.getTime();
						double meanDemand = e.getEnd().getDemand();
						double stochTime = Stochastic.generate(test.getRandomStream(), meanTime, test.varT);
						double stochDemand = Stochastic.generate(test.getRandomStream(), meanDemand, test.varD);
						auxExpDistance += e.getDist();
						auxExpTime += stochTime;
						auxExpDemand += stochDemand;
						auxExpFuelCost += costs.getCF() * costs.getKpl() * e.getDist();
						auxExpTimeCost += stochTime * costs.getDriverCost();
						auxExpSocialCost += costs.getSocialCost() * remCapacity * e.getDist();
						auxExpCo2Cost += costs.getCE() * costs.getGamma() * e.getDist();
						remCapacity -= stochDemand;
						remBattery -= stochTime;
						if(remBattery <= 0){
							if(!(remBattery == 0 && e.getEnd().getId() == d.getId())){
								iBattery = true;
								rBattery = true;}}
						if(remCapacity < 0){
							iCapacity = true;
							rCapacity = true;}
						if(remBattery <= 0 || remCapacity < 0){
							if(!(remBattery == 0 && e.getEnd().getId() == d.getId() && remCapacity >= 0)){
								irel = true;
								rel = true;
								remCust += 1 + remC(e, r);
								if(remBattery == 0 && remCapacity >= 0) remCust--;
								break;}}}					
					r.addExpDist(auxExpDistance/nSim);
					r.addExpFuelCost(auxExpFuelCost/nSim);
					r.addExpTime(auxExpTime/nSim);
					r.addExpTimeCost(auxExpTimeCost/nSim);
					r.addExpSocialCost(auxExpSocialCost/nSim);
					r.addExpCo2Cost(auxExpCo2Cost/nSim);
					r.addExpDemand(auxExpDemand/nSim);
					if(rCapacity) r.setRelDemand(r.getRelDemand() + 1);
					if(rBattery) r.setRelTime(r.getRelBattery() + 1);
					if(rel) r.setRel(r.getRel() + 1);}
				if(iCapacity) auxRelDemand += 1;
				if(iBattery) auxRelTime += 1;
				if(irel) auxRel += 1;};
		
		}else if(!test.preventive && test.corrective){
			for(int i = 0; i < nSim; i++){
				boolean iCapacity = false;
				boolean iBattery = false;
				boolean irel = false;
				for(Route r:this.routes){
					double auxExpDistance = 0, auxExpFuelCost = 0;
					double auxExpTime = 0, auxExpTimeCost = 0;
					double auxExpSocialCost = 0, auxExpCo2Cost = 0;
					double auxExpDemand = 0;
					boolean rCapacity = false;
					boolean rBattery = false;
					boolean rel = false;
					double remCapacity = Math.min(r.getDemand() * (1 + test.getSafetyStockC2()), inputs.getVehCap());
					double remBattery = costs.getBattery();
					auxExpTimeCost += costs.getFixedCost();
					for(Edge e:r.getEdges()){
						double meanTime = e.getTime();
						double meanDemand = e.getEnd().getDemand();
						double stochTime = Stochastic.generate(test.getRandomStream(), meanTime, test.varT);
						double stochDemand = Stochastic.generate(test.getRandomStream(), meanDemand, test.varD);
						auxExpDistance += e.getDist();
						auxExpTime += stochTime;
						auxExpDemand += stochDemand;
						auxExpFuelCost += costs.getCF() * costs.getKpl() * e.getDist();
						auxExpTimeCost += stochTime * costs.getDriverCost();
						auxExpSocialCost += costs.getSocialCost() * remCapacity * e.getDist();
						auxExpCo2Cost += costs.getCE() * costs.getGamma() * e.getDist();
						remCapacity -= stochDemand;
						remBattery -= stochTime;
						if(remBattery <= 0){
							if(!(remBattery == 0 && e.getEnd().getId() == d.getId())){
								iBattery = true;
								rBattery = true;
								irel = true;
								rel = true;
								double auxTime1 = 0;
								double auxTime2 = 0;
								if(e.getOrigin().getId() != d.getId()) auxTime1 = inputs.getEdge(e.getOrigin().getId(), d.getId()).getTime();
								if(e.getEnd().getId() != d.getId()) auxTime2 = inputs.getEdge(e.getEnd().getId(), d.getId()).getTime();
								double auxTime = (auxTime1 + auxTime2)/2; 
								remBattery += costs.getBattery();
								auxExpTime += auxTime * 2;
								auxExpTimeCost += auxTime * costs.getDriverCost();}}
						if(remCapacity < 0){
							iCapacity = true;
							rCapacity = true;
							irel = true;
							rel = true;
							Edge j = inputs.getEdge(e.getEnd().getId(), d.getId());
							auxExpDistance += j.getDist();
							double auxTime1 = Stochastic.generate(test.getRandomStream(), j.getTime(), test.varT);
							auxExpTime += auxTime1;
							auxExpFuelCost += costs.getCF() * costs.getKpl() * j.getDist();
							auxExpTimeCost += auxTime1 * costs.getDriverCost();
							auxExpCo2Cost += costs.getCE() * costs.getGamma() * j.getDist();
							// No social cost because the vehicle goes empty.
							double auxCapacity = remCapacity;
							remCapacity = requiredCapacity(r, e) + Math.abs(remCapacity);
							remCapacity = Math.min(remCapacity * (1 + test.getSafetyStockC2()), inputs.getVehCap());
							remBattery -= auxTime1;								
							if(remBattery <= 0){
								iBattery = true;
								rBattery = true;
								irel = true;
								rel = true;
								remBattery += costs.getBattery();}
							double auxTime2 = Stochastic.generate(test.getRandomStream(), j.getTime(), test.varT);
							auxExpTime += auxTime2;
							auxExpFuelCost += costs.getCF() * costs.getKpl() * j.getDist();
							auxExpTimeCost += auxTime2 * costs.getDriverCost();
							auxExpSocialCost += costs.getSocialCost() * remCapacity * j.getDist();
							auxExpCo2Cost += costs.getCE() * costs.getGamma() * j.getDist();
							remCapacity -= auxCapacity;
							remBattery -= auxTime2;								
							if(remBattery <= 0){
								iBattery = true;
								rBattery = true;
								irel = true;
								rel = true;								
								double auxTime = inputs.getEdge(e.getEnd().getId(), d.getId()).getTime();
								remBattery += costs.getBattery();
								auxExpTime += auxTime; // 2 * mean(0, auxTime)
								auxExpTimeCost += auxTime * costs.getDriverCost();}}}	
						r.addExpDist(auxExpDistance/nSim);
						r.addExpFuelCost(auxExpFuelCost/nSim);
						r.addExpTime(auxExpTime/nSim);
						r.addExpTimeCost(auxExpTimeCost/nSim);
						r.addExpSocialCost(auxExpSocialCost/nSim);
						r.addExpCo2Cost(auxExpCo2Cost/nSim);
						r.addExpDemand(auxExpDemand/nSim);
						if(rCapacity) r.setRelDemand(r.getRelDemand() + 1);
						if(rBattery) r.setRelTime(r.getRelBattery() + 1);
						if(rel) r.setRel(r.getRel() + 1);}
				if(iCapacity) auxRelDemand += 1;
				if(iBattery) auxRelTime += 1;
				if(irel) auxRel += 1;}					
		}
				
		else{ 
			for(int i = 0; i < nSim; i++){
				boolean iCapacity = false;
				boolean iBattery = false;
				boolean irel = false;
				for(Route r:this.routes){
					double auxExpDistance = 0, auxExpFuelCost = 0;
					double auxExpTime = 0, auxExpTimeCost = 0;
					double auxExpSocialCost = 0, auxExpCo2Cost = 0;
					double auxExpDemand = 0;
					boolean rCapacity = false;
					boolean rBattery = false;
					boolean rel = false;
					double remCapacity = Math.min(r.getDemand() * (1 + test.getSafetyStockC2()), inputs.getVehCap());
					double remBattery = costs.getBattery();
					auxExpTimeCost += costs.getFixedCost();
					for(Edge e:r.getEdges()){
						boolean preventiveCapPolicy = false; 
						double meanTime;
						double meanDemand;
						double stochTime = 0;
						double stochDemand = 0;									
						if(e.getEnd().getId() != d.getId()){
							if(e.getEnd().getDemand() > remCapacity) {
								preventiveCapPolicy = true;
								Edge j = inputs.getEdge(e.getOrigin().getId(), d.getId());
								meanTime = j.getTime();
								stochTime = Stochastic.generate(test.getRandomStream(), meanTime, test.varT);
								auxExpDistance += e.getDist();
								auxExpTime += stochTime;
								auxExpFuelCost += costs.getCF() * costs.getKpl() * e.getDist();
								auxExpTimeCost += stochTime * costs.getDriverCost();
								auxExpSocialCost += costs.getSocialCost() * remCapacity * e.getDist();
								auxExpCo2Cost += costs.getCE() * costs.getGamma() * e.getDist();
								remCapacity = requiredCapacity(r, e) + e.getEnd().getDemand();
								remCapacity = Math.min(remCapacity * (1 + test.getSafetyStockC2()), inputs.getVehCap());
								remBattery -= stochTime;
								if(remBattery < 0 || requiredBattery(r, e) + j.getTime() < remBattery){
									remBattery = costs.getBattery();}
								Edge j2 = inputs.getEdge(d.getId(), e.getEnd().getId());
								meanTime = j2.getTime();
								stochTime = Stochastic.generate(test.getRandomStream(), meanTime, test.varT);}
							
							else if (e.getTime() > remBattery){
								double auxTime1 = inputs.getEdge(e.getOrigin().getId(), d.getId()).getTime();
								double auxTime2 = inputs.getEdge(e.getEnd().getId(), d.getId()).getTime();
								double auxTime = (auxTime1 + auxTime2)/2; 
								remBattery += costs.getBattery();
								auxExpTime += auxTime * 2;
								auxExpTimeCost += auxTime * costs.getDriverCost();}}
						
						meanDemand = e.getEnd().getDemand();
						stochDemand = Stochastic.generate(test.getRandomStream(), meanDemand, test.varD);						
						if(!preventiveCapPolicy){
							meanTime = e.getTime();
							stochTime = Stochastic.generate(test.getRandomStream(), meanTime, test.varT);}
						
						auxExpDistance += e.getDist();
						auxExpTime += stochTime;
						auxExpDemand += stochDemand;
						auxExpFuelCost += costs.getCF() * costs.getKpl() * e.getDist();
						auxExpTimeCost += stochTime * costs.getDriverCost();
						auxExpSocialCost += costs.getSocialCost() * remCapacity * e.getDist();
						auxExpCo2Cost += costs.getCE() * costs.getGamma() * e.getDist();
						remCapacity -= stochDemand;
						remBattery -= stochTime;
						
						if(remBattery <= 0){
							iBattery = true;
							rBattery = true;
							irel = true;
							rel = true;
							double auxTime1 = 0;
							if(e.getOrigin().getId() != d.getId()) auxTime1 = inputs.getEdge(e.getOrigin().getId(), d.getId()).getTime();
							Edge aux = inputs.getEdge(e.getEnd().getId(), d.getId());
							double auxTime2 = 0;
							if(e.getEnd().getId() != d.getId()) auxTime2 = aux.getTime();
							double auxTime = (auxTime1 + auxTime2)/2; 
							remBattery += costs.getBattery();
							auxExpTime += auxTime * 2;
							auxExpTimeCost += auxTime * costs.getDriverCost();}
						
						if(remCapacity < 0){
							iCapacity = true;
							rCapacity = true;
							irel = true;
							rel = true;
							Edge j = inputs.getEdge(e.getEnd().getId(), d.getId());
							auxExpDistance += j.getDist();
							double auxTime1 = Stochastic.generate(test.getRandomStream(), j.getTime(), test.varT);
							auxExpTime += auxTime1;
							auxExpFuelCost += costs.getCF() * costs.getKpl() * j.getDist();
							auxExpTimeCost += auxTime1 * costs.getDriverCost();
							auxExpCo2Cost += costs.getCE() * costs.getGamma() * j.getDist();
							double auxCapacity = remCapacity;
							remCapacity = requiredCapacity(r,e) + Math.abs(remCapacity);
							remCapacity = Math.min(remCapacity * (1 + test.getSafetyStockC2()), inputs.getVehCap());
							remBattery -= auxTime1;								
							if(remBattery <= 0){
								iBattery = true;
								rBattery = true;
								irel = true;
								rel = true;
								remBattery += costs.getBattery();}
							if(remBattery < this.requiredBattery(r, e) + j.getTime()){
								remBattery = costs.getBattery();}
							double auxTime2 = Stochastic.generate(test.getRandomStream(), j.getTime(), test.varT);
							auxExpTime += auxTime2;
							auxExpFuelCost +=  costs.getCF() * costs.getKpl() * j.getDist();
							auxExpTimeCost += auxTime2 * costs.getDriverCost();
							auxExpSocialCost += costs.getSocialCost() * remCapacity * j.getDist();
							auxExpCo2Cost += costs.getCE() * costs.getGamma() * j.getDist();
							remCapacity -= auxCapacity;
							remBattery -= auxTime2;								
							if(remBattery <= 0){
								iBattery = true;
								rBattery = true;
								irel = true;
								rel = true;
								double auxTime = inputs.getEdge(e.getEnd().getId(), d.getId()).getTime();
								remBattery += costs.getBattery();
								auxExpTime += auxTime;
								auxExpTimeCost += auxTime * costs.getDriverCost();}}}
					
						r.addExpDist(auxExpDistance/nSim);
						r.addExpFuelCost(auxExpFuelCost/nSim);
						r.addExpTime(auxExpTime/nSim);
						r.addExpTimeCost(auxExpTimeCost/nSim);
						r.addExpSocialCost(auxExpSocialCost/nSim);
						r.addExpCo2Cost(auxExpCo2Cost/nSim);
						r.addExpDemand(auxExpDemand/nSim);
						if(rCapacity) r.setRelDemand(r.getRelDemand() + 1);
						if(rBattery) r.setRelTime(r.getRelBattery() + 1);
						if(rel) r.setRel(r.getRel() + 1);}
				if(iCapacity) auxRelDemand += 1;
				if(iBattery) auxRelTime += 1;
				if(irel) auxRel += 1;}}
		
		for(Route r:this.routes){
			r.setExpEconomicCost();
			r.setExpTotalCost();
			r.setExpOptCost(test);
			r.setRelDemand(1 - r.getRelDemand()/(1.0 * nSim));
			r.setRelTime(1 - r.getRelBattery()/(1.0 * nSim));
			r.setRel(1 - r.getRel()/(1.0 * nSim));
			this.expDistance += r.getExpDist(); 
			this.expFuelCost += r.getExpFuelCost();
			this.expTime += r.getExpTime(); 
			this.expTimeCost += r.getExpTimeCost();
			this.expSocialCost += r.getExpSocialCost();
			this.expCo2Cost += r.getExpCo2Cost();
			this.expDemand += r.getExpDemand();}
		this.expEconomicCost += this.expFuelCost + this.expTimeCost;
		this.expTotalCost = this.expEconomicCost + this.expSocialCost + this.expCo2Cost;
		if(test.getOptDist()){
			this.expOptCost = this.expDistance;
		}else{
			this.expOptCost = test.getWeightEconomic() * this.expEconomicCost + 
				test.getWeightSocial() * this.expSocialCost + 
				test.getWeightEnvironment() * this.expCo2Cost;}
		this.relDemand = 1 - 1.0 * auxRelDemand/nSim;
		this.relTime = 1 - auxRelTime/(1.0 * nSim);
		this.rel = 1 - auxRel/(1.0 * nSim);
		this.remCustomers = remCust/(1.0 * nSim); 	
	}

	private int remC(Edge e, Route r) {
		int aux = 0;
		boolean add = false;
		for(Edge i:r.getEdges()){
			if(add) aux ++;
			if(i.getOrigin().getId() == e.getOrigin().getId()) add = true;}
		return aux;
	}

	private double requiredBattery(Route r, Edge e) {
		double aux = 0;
		boolean add = false;
		for(Edge i:r.getEdges()){
			if(add) aux += i.getTime();
			if(i.getOrigin().getId() == e.getOrigin().getId()) add = true;}
		return aux;}

	private double requiredCapacity(Route r, Edge e) {
		double aux = 0;
		boolean add = false;
		for(Edge i:r.getEdges()){
			if(add) aux += i.getEnd().getDemand();
			if(i.getOrigin().getId() == e.getOrigin().getId()) add = true;}
		return aux;}
	
	public LinkedList<Route> getRoutes() {return this.routes;}
	public int getNumRoutes() {return this.routes.size();}
	public long getId() {return id;}
	public double getOptCost() {return optCost;} 
	public double getExpOptCost() {return expOptCost;}
	public double getTotalCost() {return totalCost;}
	public double getExpTotalCost() {return expTotalCost;}
	public double getDistance() {return distance;}
	public double getTime() {return time;}
	public int getDemand() {return demand;}
	public double getDistanceCosts() {return FuelCost;}
	public double getTimeCosts() {return timeCost;}
	public double getEconomicCosts() {return economicCost;}
	public double getSocialCosts() {return socialCost;}
	public double getCo2Costs() {return co2Cost;}
	public double getCompTime() {return runTime;}
	public double getRelDemand() {return relDemand;}
	public double getRelTime() {return relTime;}
	
	public void setElapsedTime(double t) {this.runTime=t;}
	
	@Override
	public String toString(){
		String s = "";
		Route aRoute;
		s = s.concat("List of routes: \r");
		for (int i = 0; i < routes.size(); i++){   
			aRoute = routes.get(i);
			s = s.concat("\nRoute " + (i+1) + ": ");
			for(Edge e : aRoute.getEdges()) {
				if(e.getOrigin().getId() == 0)
					s = s.concat("0" + "-" + e.getEnd().getId());
				else s = s.concat("-" + e.getEnd().getId());}}
		return s;}
	
	public String toString(Inputs inputs, Costs costs, Test test){
		Route aRoute;
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.####", otherSymbols);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		String s = "";
		s = s.concat("ID : " + this.id + "\r\n");
		s = s.concat("DETERMINISTIC INDICATORS"+ "\r\n");
		s = s.concat("Optimization costs: " + df.format(this.optCost) + "\r\n");
		s = s.concat("Total costs: " + df.format(this.totalCost) + "\r\n");
		s = s.concat("Dist: " + df.format(this.distance) + "\r\n");
		s = s.concat("Time: " + df.format(this.time) + "\r\n");
		s = s.concat("Fuel cost: " + df.format(this.FuelCost) + "\r\n");
		s = s.concat("Time cost: " + df.format(this.timeCost) + "\r\n");
		s = s.concat("Economic cost: " + df.format(this.economicCost) + "\r\n");
		s = s.concat("Co2 cost: " + df.format(this.co2Cost) + "\r\n");
		s = s.concat("Social cost: " + df.format(this.socialCost) + "\r\n");
		s = s.concat("CheckList: " + Validation.checkList(costs, test, this, inputs) + "\r\n");
		s = s.concat("Stochastic Indicators" + "\r\n");
		s = s.concat("Exp. optimization costs: " + df.format(this.expOptCost) + "\r\n");
		s = s.concat("Exp. total costs: " + df.format(this.expTotalCost) + "\r\n");
		s = s.concat("Exp. distance: " + df.format(this.expDistance) + "\r\n");
		s = s.concat("Exp. time: " + df.format(this.expTime) + "\r\n");
		s = s.concat("Exp. fuel costs: " + df.format(this.expFuelCost) + "\r\n");
		s = s.concat("Exp. time costs: " + df.format(this.expTimeCost) + "\r\n");
		s = s.concat("Exp. economic costs: " + df.format(this.expEconomicCost) + "\r\n");
		s = s.concat("Exp. co2 costs: " + df.format(this.expCo2Cost) + "\r\n");
		s = s.concat("Exp. social costs: " + df.format(this.expSocialCost) + "\r\n");
		s = s.concat("Exp. demand: " + df.format(this.expDemand) + "\r\n");
		s = s.concat("Rel. demand: " + df.format(this.relDemand) + "\r\n");
		s = s.concat("Rel. time: " + df.format(this.relTime) + "\r\n");
		s = s.concat("Rel.: " + df.format(this.rel) + "\r\n");
		s = s.concat("SafetyStockC1: " + df.format(test.getSafetyStockC1()) + "\r\n");
		s = s.concat("SafetyStockC2: " + df.format(test.getSafetyStockC2()) + "\r\n");
		s = s.concat("SafetyStockB: " + df.format(test.getSafetyStockB()) + "\r\n");
		s = s.concat("Customers unserved: " + df.format(this.remCustomers) + "\r\n");
		s = s.concat("Sol computing time: " + this.runTime + "\r\n");
		s = s.concat("# of routes in sol: " + this.routes.size() + "\n");
		s = s.concat("Capacity of the vehicle: " + inputs.getVehCap());
		s = s.concat("\r\n\r\n");
		s = s.concat("List of routes: \r");
		for (int i = 0; i < routes.size(); i++){   
			aRoute = routes.get(i);
			s = s.concat("\nRoute " + (i+1) + ": ");
			for(Edge e : aRoute.getEdges()) {
				if(e.getOrigin().getId() == 0)
					s = s.concat("0" + "-" + e.getEnd().getId());
				else s = s.concat("-" + e.getEnd().getId());}            
			s = s.concat(aRoute.toString(df));}        
		return s;}}