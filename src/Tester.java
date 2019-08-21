import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import umontreal.iro.lecuyer.rng.*;

public class Tester{
	final static String inputFolder = "inputs";
	final static String outputFolder = "outputs";
	final static String testFolder = "tests";
	final static String fileNameTest = "test2run.txt";
	final static String sufixFileNodes = "_nodes.txt";
	final static String sufixFileTimes = "_medium.txt";
	final static String sufixFileVehicules = "_vehicles.txt";
	final static String sufixFileOutput = "_outputs.txt";
	final static String sufixFileParameter = "Parameters.txt";

	public static void main(String[] args){
		System.out.println("****  WELCOME  ****");
		long programStart = Time.systemTime();
		TestsSummary.initTestsSumary(sufixFileOutput + File.separator + "testsSummary.txt");
		// GET THE LIST OF TESTS
		String testsFilePath = testFolder + File.separator + fileNameTest;
		ArrayList<Test> testsList = TestsManager.getTestsList(testsFilePath);
		String parametersFilePath = inputFolder + File.separator + sufixFileParameter;
		Costs costs = new Costs(parametersFilePath);
		// FOR EACH TEST...
		int nTests = testsList.size();
		for(int k = 0; k < nTests; k++){  
			Test test = testsList.get(k);
			//Validation.checkSim(test);
			System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);
			// GET THE INSTANCE INPUTS
			String inputNodesPath = inputFolder + File.separator + test.getInstanceName() + sufixFileNodes;
			String inputVehPath = inputFolder + File.separator + test.getInstanceName() + sufixFileVehicules;
			String inputTimePath = inputFolder + File.separator + test.getInstanceName() + sufixFileTimes;
			Inputs inputs = InputsManager.readInputs(inputNodesPath, inputVehPath);
			InputsManager.readTimes(inputs,inputTimePath);
			InputsManager.generateDepotEdges(inputs, costs, test);
			InputsManager.generateSavings(inputs, costs, test);				
			long[] seeds = new long[6];
		    for (int i = 0; i < seeds.length; i++) seeds[i] = test.getSeed() + i;
		    Random rng = new Random(test.getSeed());
            RandomStreamBase stream = new LFSR113(); // L'Ecuyer stream
            test.setRandomStream(stream);
			Algorithm algorithm = new Algorithm(test, inputs, rng, costs);
			Outputs output;
			if(test.getSolveDet()) {
				output = algorithm.detSolve();
			} else{
				output = algorithm.solve();}
			// PRINT OUT THE RESULTS
			if(output == null){
				continue;
			}
			long testEnd = Time.systemTime();
			String outputsFilePath = outputFolder + File.separator + test.getOutputsFilePath() + sufixFileOutput;
			output.sendToFile(outputsFilePath, inputs, costs, test, Time.calcElapsedHMS(programStart, testEnd));			
		}
		/* 3. END OF PROGRAM */
		System.out.println("\n****  END  ****");
		long programEnd = Time.systemTime();
		System.out.println("Total elapsed time = " + Time.calcElapsedHMS(programStart, programEnd));}
}