import java.io.IOException;
import java.io.PrintWriter;

public class Outputs{
	private Individual bestSol;
	private Individual bestStochSol;

	public Outputs(){
		bestSol = null;}

	public void setOBSol(Individual obSol){bestSol = obSol;}
	public void setOBStochSol(Individual obSol){bestStochSol = obSol;}

	public Individual getOBSol(){return bestSol;}
	public Individual getOBStochSol(){return bestStochSol;}

	public void sendToFile(String outFile, Inputs inputs, Costs costs, Test test, String time){
		try{ 
			PrintWriter out = new PrintWriter(outFile);
			out.println("***************************************************");
			out.println("*                      OUTPUTS                    *");
			out.println("***************************************************");
			out.println("--------------------------------------------");
			out.println("BDS:");
			out.println("--------------------------------------------");
			out.println(bestSol.toString(inputs, costs, test));
			out.println("--------------------------------------------");
			out.println("BSS:");
			out.println("--------------------------------------------");
			out.println(bestStochSol.toString(inputs, costs, test));
			out.println("--------------------------------------------");
			out.println("Time: " + time);
			out.close();
		} catch (IOException exception){ 
			System.out.println("Error processing output file: " + exception);}}
}