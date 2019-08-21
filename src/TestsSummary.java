import java.io.BufferedWriter;
import java.io.FileWriter;

public class TestsSummary {
	private static String fileName;

	public static void initTestsSumary(String fileNameLog){
		fileName = fileNameLog;}

	public static void setTestSumary(String message){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName, true));
			bw.write(message);
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();} catch (Exception e) {}}}}