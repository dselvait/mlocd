package data.generator;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

import utils.text.DataWorker;

import data.generator.DataGenerator;

public class ConceptDriftData {

  public static void addConceptDriftData(String dataset, String targetClass, int min, int max, 
              String output){
    try{  
      Random random = new Random();
      BufferedReader in = new BufferedReader(new FileReader(dataset));
      PrintWriter out = new PrintWriter(new FileOutputStream(output));
      String line = "";
      while((line = in.readLine()) != null){
        String label = DataWorker.getRecordClass(line);
        if(label.equals(targetClass)){
          int value = random.nextInt(max-min+1)+min;
          line = Integer.toString(value)+","+line;    
      }
      out.println(line);
   }
   in.close();
   out.close();
   }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
  } 

  public static void main(String[] args)throws Exception{	  
    String base="c:\\Projects\\Boosting\\ArtificialData\\";
	String dataset="cmb.data";
	String middle="artificial.out";
	int[] removeAttributes={5,6,8,11,12};  // know from weka
	String artidata = "artidata.data";
	
	//Steps to generate the artificial data to simulate the concept drift, given a dataset
	//Use the feature subset selections to select out the irrelevant attributes.
	//Use the dataWorker remove field to remove all the useful attributes, therefore the 	
	//file only contains irrelevant attributes. 
	//Use addConceptDriftData method to add into the data that dominates the class distributions
	//Use datagenerator package to generate some data files with artificial data
	//Use dataworker package to join the concept drift dataset with the artificial dataset 
	// together
	
	DataWorker.removeField(base+dataset,removeAttributes[0],base+middle); // step1
	for(int i=1;i<removeAttributes.length;i++){
	  DataWorker.removeField(base+middle, i,base+middle);
	}
	addConceptDriftData(base+middle, "3", 10, 20, base+"conceptDrift1.data"); // step2
	addConceptDriftData(base+middle, "1", 1, 9, base+"conceptDrift1.data");
	addConceptDriftData(base+middle, "2", 21, 30, base+"conceptDrift1.data");
	DataGenerator.generator(1,20,3000,base+"artidata.data");	
	DataGenerator.addData(base+artidata,10,30,base+artidata);
	DataGenerator.addData(base+artidata,31,40,base+artidata);
	
	DataWorker.joinDataset(base+artidata, base+"conceptDrift1.data", base+"conceptDrift1.data");
  }	
	
}
