import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Costs {
	private double driverCost, fixedCost, cF, socialCost;
	private double battery, gamma, cE, kpl;

	public Costs(double socialCost, double driverCost, double fixedCost, double cF, double battery, double gamma, double cE, double kpl) {
		this.socialCost = socialCost;
		this.driverCost = driverCost;
		this.fixedCost = fixedCost;
		this.cF = cF;
		this.battery = battery;
		this.gamma = gamma;
		this.cE = cE;
		this.kpl = kpl;}

	public Costs(String parametersFilePath) {
		System.out.println(parametersFilePath);
		try {
			FileReader reader = new FileReader(parametersFilePath);
			Scanner in = new Scanner(reader);
			in.useLocale(Locale.US);
			while (in.hasNextLine()) {
				String s = in.next();
				if (s.charAt(0) == '#') in.nextLine();
				else {
					socialCost = Double.parseDouble(s);
					driverCost = in.nextDouble();
					fixedCost = in.nextDouble();
					cF = in.nextDouble();
					battery = in.nextDouble();
					gamma = in.nextDouble();
					cE = in.nextDouble();
					kpl = in.nextDouble();}}
			in.close();
		} catch (IOException exception) {
			System.out.println("Error processing tests file: " + exception);}}

	public double getDriverCost() {return driverCost;}
	public double getFixedCost() {return fixedCost;}
	public double getCF() {return this.cF;}
	public double getSocialCost() {return socialCost;}
	public double getBattery() { return battery;}
	public double getGamma() {return gamma;}
	public double getCE() {return this.cE;}
	public double getKpl() {return this.kpl;}
}