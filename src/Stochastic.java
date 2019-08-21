import java.io.IOException;
import java.io.PrintWriter;

import umontreal.iro.lecuyer.randvar.*;
import umontreal.iro.lecuyer.rng.*;

public class Stochastic{
	public Stochastic(){}

	public static double std(double[] vector, double mean) {
		double var = 0;
		for(int i = 0; i < vector.length; i++) {
			var += Math.pow(vector[i] - mean, 2);}
		return var/vector.length;}

	public static double generate(RandomStream stream, double mean, double variance) {
		double var = variance * mean; //0.05f; 
		double factor = Math.log(1 + var / Math.pow(mean, 2));
		double mu = Math.log(mean) - 0.5 * factor;
		double sigma = Math.sqrt(factor);
		if(variance != 0 && mean != 0) return LognormalGen.nextDouble(stream, mu, sigma);
		return  mean;}

	public static void printSim(double[] custo, double[] dist, Test t, int type, int num){
		try{
			PrintWriter out = null;
			String filename = t.getInstanceName() + ".txt";
			out = new PrintWriter(filename);
			for (int i = 0; i < custo.length; i++){
				out.printf("%.4f \t %.4f\n",custo[i],dist[i]);}
			out.close();}
		catch (IOException exception){   
			System.out.println("Error processing output file: " + exception);}}
}