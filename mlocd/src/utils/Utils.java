package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

public class Utils {
  
  public static double normalize(String result){
	return 0.0;  
  }
  
  public static int totalInstances(String dataset)throws Exception {
    int size=0;
	BufferedReader in = new BufferedReader(new FileReader(dataset));
	String line=null;
	while((line=in.readLine())!=null){
	  if(line.equals("")) continue;
	  size++;
	}
	in.close();
	return size;
  }
  
  // return the location of the temporary sampled dataset 
  public static void sampleTo(String dataset, double[] weights, String sampled)throws Exception{
	 if(Utils.totalInstances(dataset) != weights.length)
		  throw new IllegalArgumentException(
			"The weighted instances don't matched the training instances");
		// Configure the program
	    BufferedReader in = new BufferedReader(new FileReader(dataset));
		PrintWriter out = new PrintWriter(new FileOutputStream(sampled));
		//double mean= Math.sum(weights)/weights.length;
		in.mark((int)(new File(dataset).length())+1);
		Random random = new Random();
		int i=0;
		int factor=1;
		int volume=0;
		String line;
		// start the process
		while((line=in.readLine())!= null){
		  out.println(line);	
		}
		out.close();
  }
  
  public static void resampleWithWeights(String dataFile, double[] weights ,String sampleFile) 
           throws Exception {
     
	int total = totalInstances(dataFile);
	
	if(weights.length != total) {
	    throw new IllegalArgumentException("weights.length != numInstances.");
	}	
	if(total == 0) {
	   dataFile = sampleFile;
	   return;
	}
    
    String[] dataset = new String[total]; 
	BufferedReader in = new BufferedReader(new FileReader(dataFile));
	PrintWriter out = new PrintWriter(new FileOutputStream(sampleFile));
	Random random = new Random();
    double[] probabilities = new double[total];
    double sumProbs = 0, sumOfWeights = sum(weights);
	try{
	//Populate the data into to file
	  for(int i = 0; i<total;++i ){
	    dataset[i]= in.readLine();	
	  }	
    
      for(int i = 0; i < total; i++) {
        sumProbs += random.nextDouble();
        probabilities[i] = sumProbs;
      }
      Utils.normalize(probabilities, sumProbs / sumOfWeights);

// Make sure that rounding errors don't mess things up
      probabilities[total - 1] = sumOfWeights;
      int k = 0; int l = 0;
      sumProbs = 0;
      while(k < total && l < total) {
        if(weights[l] < 0) {
          throw new IllegalArgumentException("Weights have to be positive.");
        } 
        sumProbs += weights[l];
        while((k < total) &&
             (probabilities[k] <= sumProbs)){ 
     //newData.add(data.instance(l));
    	  out.println(dataset[l]);  
          k++;
        }
        l++;
      }
	}finally{
	   in.close();
	   out.close();
	 } 
  }

  public static void normalize(double[] doubles, double sum) {
    
	if(Double.isNaN(sum)) {
	  throw new IllegalArgumentException("Can't normalize array. Sum is NaN.");
	}
	if(sum == 0){
	      // Maybe this should just be a return.
	  throw new IllegalArgumentException("Can't normalize array. Sum is zero.");
	}
	for(int i = 0; i < doubles.length; i++) {
	  doubles[i] /= sum;
	}
  }
  
  public static double sum(double[] elements){
	double sum = 0;
	for(int i = 0; i < elements.length; i++)
	  sum += elements[i];
	//float a = (float) sum;   
	return sum;
  }
		  
  public static double randomDouble(double[] elements){
	Random random = new Random();
	int i=random.nextInt(5);
	if(i>3) i=i-1;
	double t=i/sum(elements);
	return t;
  }
    
}
