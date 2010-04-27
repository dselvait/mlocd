package data.generator;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;

//import data.preprocessor.DataWorker;

public class DataGenerator {

  public static void generator(int min, int max, int lines, String output)
                              throws Exception{
	Random random = new Random();
    PrintWriter out = new PrintWriter(new FileOutputStream(output));
    String line = null;
    int i=0;
    while(i++<lines){
      int value = random.nextInt(max-min+1)+min;
      line = Integer.toString(value);
      out.println(line);
    }
	out.close();
  }
  
// It always add data at head, so the class attributes will not be necessary moved
  public static void addData(String dataset, int min, int max, String output)
                       throws Exception{
    Random random = new Random();
    BufferedReader in = new BufferedReader(new FileReader(dataset));
    PrintWriter out = new PrintWriter(new FileOutputStream(output));
    String line = null;
    while((line=in.readLine()) != null){
      int value = random.nextInt(max-min+1)+min;
      line = Integer.toString(value)+","+line;
      out.println(line);
    }
   in.close();
   out.close();
  }
	 
}
