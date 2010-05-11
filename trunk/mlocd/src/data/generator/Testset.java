package data.generator;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import utils.FileWorker;
import utils.RandomMachine;

public class Testset {

	public static void make(String[] datasets, String testset, int instances)throws Exception{
		int part1 = instances / 10 * 7;
		int part2 = (instances - part1) / (datasets.length-1);
		RandomMachine rand = new RandomMachine();
		
			int i = 0;
			BufferedReader reader = new BufferedReader(new FileReader(datasets[datasets.length-1]));
			PrintWriter writer = new PrintWriter(new FileOutputStream(testset));
			while( i < part1 ){
				if(!reader.ready()){
					reader = new BufferedReader(new FileReader(datasets[datasets.length-1]));;
				}
				String line = reader.readLine();
				int k = rand.getRandomInt(0, 30);
				if(k % 2 == 0){
					writer.println(line);
					i++;
				}else
					continue;
			}
			reader.close();
//			writer.close();
		
		for(int d =0; d < datasets.length-1; ++d){
			int j = 0;
			reader = new BufferedReader(new FileReader(datasets[d]));
			while( j < part2 ){
				if(!reader.ready()){
					reader = new BufferedReader(new FileReader(datasets[d]));;
				}
				String line = reader.readLine();
				int k = rand.getRandomInt(0, 30);
				if(k % 2 == 0){
					writer.println(line);
					j++;
				}else
					continue;
			}
			reader.close();
		}
		writer.close();
		// cp names file
		String dbase = datasets[0].split("\\.")[0];
		String tbase = testset.split("\\.")[0];
		FileWorker.cp(dbase+".names", tbase+".names");
	}
}
