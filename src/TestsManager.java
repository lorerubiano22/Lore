import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class TestsManager {
	public static ArrayList<Test> getTestsList(String testsFilePath) {
		ArrayList<Test> list = new ArrayList<Test>();
		try {
			FileReader reader = new FileReader(testsFilePath);
			Scanner in = new Scanner(reader);
			in.useLocale(Locale.US);
			while(in.hasNextLine()) {
				String s = in.next();
				if(s.charAt(0) == '#') in.nextLine(); // skip comment line
				else {
					String instanceName = s; // e.g.: A-n32-k5
					boolean optDist = in.nextBoolean();
					boolean solveDet = in.nextBoolean();
					double maxRouteCosts = in.nextDouble(); // maxCosts per route
					double maxTime = in.nextDouble(); // max computational time (sec)
					double firstParam = in.nextDouble(); // geometric distribution parameter
					double secondParam = in.nextDouble(); 
					double maxP = in.nextDouble(); // max/med/min percentage to remove routes
					double minP = in.nextDouble();
					int seed = in.nextInt(); // seed for the RNG
					int simC = in.nextInt(); // short/large simulation
					int simL = in.nextInt();
					double varD = in.nextDouble(); // variance for demand/time
					double varT = in.nextDouble();
					double wT = in.nextDouble();
					double wD = in.nextDouble();
					double wS = in.nextDouble();
					boolean preventive = in.nextBoolean();
					boolean corrective = in.nextBoolean();
					double pc = in.nextDouble();
					double pb = in.nextDouble();
					double sftC1 = 0;
					double sftC2 = 1;
					double sftB = 0;
					Test aTest = new Test(instanceName, optDist, solveDet, maxRouteCosts, maxTime, firstParam, secondParam, maxP, minP, seed, simC, simL, varD, varT, wT, wD, wS, preventive, corrective, pc, pb, sftC1, sftC2, sftB);
					list.add(aTest);}}
			in.close();}
		catch (IOException exception) {
			System.out.println("Error processing tests file: " + exception);}
		return list;}}