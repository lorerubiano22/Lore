import java.util.*;

public class RouteCache {
	private final int MAX_CACHE_CAPACITY = 5000000;
	private LinkedHashMap<Set<Node>,Route> cache;
	int hits = 0;
	int update = 0;
	
	public RouteCache(){
		cache = new LinkedHashMap<>();}

	public Route improveRoute(Route route){
		Route cachedRoute = cache.get(route.getNodes());
		if(cachedRoute == null || cachedRoute.getOptCost() > route.getOptCost()){
			if(cachedRoute == null && cache.size() < MAX_CACHE_CAPACITY) {
				cache.put(route.getNodes(), route);
				System.out.println("cache " + cachedRoute == null);
				if(cache.size() >= MAX_CACHE_CAPACITY){
					System.out.println("FULL!");}}
			else{
				++update;}
			return route;}
		else{
			++hits;
			return cachedRoute;}}

	public Individual improveRouteUsingCache(Individual baseSol, Inputs inputs, Test test, Costs costs) {
		Individual sol = new Individual(baseSol);
		for(int i = 0; i < sol.getRoutes().size(); i++){
			Route iRoute = sol.getRoutes().get(i);
			Route jRoute = new Route(iRoute);
			Route betterRoute = improveRouteUsingCache(iRoute, inputs, test, costs);
			betterRoute.updateRoute(costs, test);
			if(betterRoute.getOptCost() < jRoute.getOptCost()){
				sol.replaceRoute(i, betterRoute, test);}}
		return sol;}

	private Route improveRouteUsingCache(Route aRoute, Inputs inputs, Test aTest, Costs costs) {
		Set<Node> nodes = new LinkedHashSet<>();
		nodes = aRoute.getNodes();
		Route cacheRoute = cache.get(nodes);
		if (cacheRoute != null && aRoute.getEdges().size() != cacheRoute.getEdges().size()) {
			System.err.println("(mismatch sizes) before: " + aRoute.getEdges().size() + ", after cacheroute is " + cacheRoute.getEdges().size());}			
		if (cacheRoute == null){
			aRoute.repairRoute(inputs, aTest, costs);			
			cache.put(nodes, aRoute);						
			return aRoute;}
		else{			
			if(aRoute.getOptCost() < cacheRoute.getOptCost()){			
				aRoute.repairRoute(inputs,aTest,costs);
				cache.put(nodes, aRoute);				
				return aRoute;}
			else return cacheRoute;}}

	public Individual fastLS(Individual baseSol, Test test, Inputs inputs, Costs costs, Random rng) {
		Individual bestSol = new Individual(baseSol);
		Individual bestsol = improveRouteUsingCache(bestSol, inputs, test, costs);
		if(bestsol != null && bestsol.getOptCost() < baseSol.getOptCost()) 
			return bestsol;
		return baseSol;}
}