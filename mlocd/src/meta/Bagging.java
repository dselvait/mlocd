package meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import utils.FileWorker;
import utils.Utils;
import classifiers.Classifier;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class Bagging extends Classifier{
  private Classifier learner;
  private String location = null;
  private Classifier[] learners = null;
  private final int baseClassifiersNum = 3;
		  
  public boolean surfixName(){return false;}
  public String getSurfixName(){return null;}
		  
  public Bagging(Classifier learner){
		this.learner = learner;
		learners = new Classifier[baseClassifiersNum];
  }
	  
  public void setLocation(String location){ this.location = location;}
	
 
  public void build(String[] datasets) throws Exception {
		try {
			initEnvironment();
			int index = 0;
			this.learners = new Classifier[datasets.length];
			for (String dataset : datasets) {
//				double[] weights = initDataWeights(num);
				// Process
				// Utils.resampleWithWeights(dataset, weights, sampled);
				learner.build(dataset);
				// rename the trained weak learner
//				String newname = this.location + "/classifier"
//						+ Integer.toString(index++);
//				rename(trained, newname);
//				boolean[] evaluations = utils.Evaluation.testClassifier(
//						trained, dataset);
				// for(boolean b:evaluations) System.out.println(b);//debug code
//				updateDataWeights(weights, evaluations);
		    	this.learners[index++] = learner.copy();
			}
		} catch (Exception e) {
			cleanup();
			throw e;
		}
	}

public void build(String dataset) { }
  
  public void build(String dataset, int times) throws Exception{
	
	learners = new Classifier[times];
	String name = dataset.split("\\.")[0];    
	
	try{	
      initEnvironment();
      int num=Utils.totalInstances(dataset);
      boolean c45TypeFile = new File(name+".names").exists();
      String sampleData = this.location + "/sampleData.data";
      double[] weights = initDataWeights(num);
      if(c45TypeFile)
    	FileWorker.cp(name+".names", this.location+"/sampleData.names");	     	   
    	// Processing
      for(int i=0;i<times;i++){
    	Utils.resampleWithWeights(dataset, weights, sampleData);
    	learner.build(sampleData);
    	learners[i] = learner.copy();
    	// rename the trained weak learner
    	String newname = this.location+"/classifier"+Integer.toString(i+1);
    	rename(learners[i],newname);
       }	  
    }catch(Exception e){
       e.printStackTrace();
       cleanup();
       throw e;
	  }
  }

  public String classifyInstance(String instance) throws Exception{
		String hypothesis = ""; // The return value

		HashMap<String, Integer> listResults = new HashMap<String, Integer>();
		for (Classifier aLearner : learners) {
			String result = aLearner.classifyInstance(instance);
			if (!listResults.containsKey(result)) {
				listResults.put(result, 1);
			} else {
				int newValue = listResults.get(result) + 1;
				listResults.put(result, newValue);
			}
		}

		Iterator iterator = listResults.entrySet().iterator();
		int max = 0;
		if(!iterator.hasNext())
			 System.out.println("No element");
		Map.Entry me = (Map.Entry) iterator.next();
		max = (Integer) me.getValue();
		hypothesis = (String) me.getKey();

		while (iterator.hasNext()) {
			me = (Map.Entry) iterator.next();
			int cur = (Integer) me.getValue();
			if (max < cur) {
				cur = max;
				hypothesis = (String) me.getKey();
			}
		}

		return hypothesis;
	}
  
  public String[] classifyData(String dataset) throws Exception {
		String[] results = null;
		BufferedReader in = new BufferedReader(new FileReader(dataset));
		try {
			results = new String[Utils.totalInstances(dataset)];
			int i = 0;
			while (in.ready()) {
				String instance = in.readLine();
				results[i++] = classifyInstance(instance);
			}
		} finally {
			in.close();
		}
		return results;
	}

  public Bagging copy(){
	Bagging cp = new Bagging(this.learner);
	cp.location=this.location;
	System.arraycopy(this.learners, 0, cp.learners, 0, this.learners.length);
	return cp; 
  }

  private void rename(Classifier classifier, String newname)throws FileNotFoundException{	 
	int i=0;
	boolean success=false;
	while(i++<10){ 
	  try{
		 classifier.reLocation(newname);
		 success=true;
		  break; // If the FileNotFoundException is not thrown, then the skip the loop
	   }catch(FileNotFoundException e){
		  try{
		    System.out.println("Waiting for gernating a weak learner");
			Thread.sleep(1000);   //sleep for 1 seconds to give time to generate the classifier
			 continue;
		  }catch(InterruptedException ie){}
		}
	 }
	if(!success)throw new FileNotFoundException("Fail to generate the weak learner");
  }  
	  
  private void initEnvironment(){
	if(this.location==null) 
	  this.location=System.getProperty("user.dir")+"/"+"BaggingClassifier";
	else 
	  this.location=this.location+"BaggingClassifier";
	File dir = new File(this.location);
	String location=this.location;
	int i=1;
	while(dir.exists()){    	
	  this.location=location+Integer.toString(i++);
	  dir = new File(this.location);
	}
    dir.mkdir();    
  }
  private double[] initDataWeights(int size){	
		double weight=(double)1/size;
		double[] weights = new double[size];
		for(int i=0;i<weights.length;++i) weights[i]=weight;
		return weights;
	  } 
  
  private void cleanup(){
	try{
	  File dir = new File(this.location);
	  for(File f:dir.listFiles())
		f.delete();
	  dir.delete();
	  System.out.println("The Bagging building environment has been cleaned up.");
	}catch(Exception e){
	   System.err.println("Fail to clean up the Bagging building environment.");
	   e.printStackTrace();
	   //System.exit(1);
	 }    
  }

}
