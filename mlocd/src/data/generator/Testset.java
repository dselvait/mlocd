package data.generator;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import utils.FileWorker;
import utils.RandomMachine;

public class Testset {

	public static void make(String[] datasets, String testset, int instances)throws Exception{
		int part = instances / datasets.length;
		RandomMachine rand = new RandomMachine();
		
		for(String dataset : datasets){
			int i = 0;
			BufferedReader reader = new BufferedReader(new FileReader(dataset));
			PrintWriter writer = new PrintWriter(new FileOutputStream(testset));
			while( i++ < part && reader.ready()){
				String line = reader.readLine();
				int j = rand.getRandomInt(0, 10);
				if(j % 2 == 0){
					writer.println(line);
				}else
					continue;
			}
			reader.close();
			writer.close();
		}
		// cp names file
		String dbase = datasets[0].split("\\.")[0];
		String tbase = testset.split("\\.")[0];
		FileWorker.cp(dbase+".names", tbase+".names");
		
	}
}
