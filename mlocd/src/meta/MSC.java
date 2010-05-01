package meta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import utils.Utils;
import utils.FileWorker;
import classifiers.*;
import java.io.IOException;

public class MSC {
  private Classifier learner;
  private String location = null;
  private Classifier[] learners = null;
	  
  public boolean surfixName(){return false;}
  public String getSurfixName(){return null;}
	  
  public MSC(Classifier learner){
	this.learner=learner;
	learners = new Classifier[3];
  }
  
  public void setLocation(String location){ this.location = location;}
	  
/**
 * It needs to generate a couple of instances of a specified classifier. 
 * Classifier.build 
 * 
 */ 
 
  public void build(String[] batches){
    try{	
	  initEnvironment();
	  String name = batches[0].split("\\.")[0];
	  boolean c45TypeFile = new File(name+".names").exists(); // check if it is c4.5 file, if so .names file need to be coped with	 
	  String hardset = this.location+"/"+"hardset";
	  String agent = this.location+"/"+"agent";
	  learner.build(batches[0]);  // batches 0
	  learners[0] = learner.copy();
	  learners[0].setLocation(this.location);
	  //rename the trained weak learner
//	  String newname = this.location+"/classifiers";
	  //rename(learners[0],newname);
	  boolean[] evaluations=utils.Evaluation.testClassifier(learners[0],batches[1]); //batches[1]
	  //Create hardset
	  BufferedReader in = new BufferedReader(new FileReader(batches[1])); //batches[1]
	  PrintWriter out1 = new PrintWriter(new FileOutputStream(hardset+".data"));
	  PrintWriter out2 = new PrintWriter(new FileOutputStream(agent+".data"));
	  if(c45TypeFile){
		FileWorker.cp(name+".names", hardset+".names");  
		makeC45NamesFile(name+".names", agent+".names");
	  }
	  for(boolean test : evaluations){
		String line = in.readLine();
		if(!test){
		  out2.println(line+",0");
		}  
		else out2.println(line+",1");
		out1.println(line);
	  }
	  out1.close(); out2.close();in.close();
	  learner.build(hardset+".data");
	  learners[1] = learner.copy();
	  learners[1].setLocation(this.location);
	  //newname = this.location+"/hardset";
	  //rename(learners[1],newname);
	  learner.build(agent+".data");
	  learners[2] = learner.copy();
	  learners[2].setLocation(this.location);
	  //newname = this.location+"/agent";
	  //rename(learners[2],newname);
	  //learner.classifyData();
     }catch(Exception e){
		e.printStackTrace();
		//cleanup();
		System.exit(1);
	  }
	}

   public String classifyInstance(String instance)throws Exception{	
		if(learners[2] == null){
			return learners[0].classifyInstance(instance);
		}
	   	String hypothesis="";  // The return value
		String agentInstance=instance+",1";
		String indicate = learners[2].classifyInstance(agentInstance);
		Classifier selectedClassifier = indicate.equals("1")?learners[0]:learners[1];
		hypothesis = selectedClassifier.classifyInstance(instance);
	    return hypothesis;
	  }
	  
   public String[] classifyData(String dataset)throws Exception{
	   if(learners[2] == null){
			return learners[0].classifyData(dataset);
		}
	 String[] results = null;
	 BufferedReader in = new BufferedReader(new FileReader(dataset)); 
	 try{
	   results=new String[Utils.totalInstances(dataset)];		   
	   String instance;
	   int i=0;
	   while((instance=in.readLine())!=null){
		 String agentInstance=instance+",1";
		 String indicate = learners[2].classifyInstance(agentInstance);
		 Classifier selectedClassifier = indicate.equals("1")?learners[0]:learners[1];
	     results[i++] = selectedClassifier.classifyInstance(instance);  
	   }	 
	 }catch(Exception e){
		e.printStackTrace();
		System.exit(1);
	 }
	 finally{
		 in.close();	 
	 }
	 return results;
   }	  

  public MSC copy(){
	MSC cp = new MSC(this.learner);
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
	  this.location=System.getProperty("user.dir")+"/"+"MSCclassifier";
	else 
	  this.location=this.location+"MSCclassifier";
	File dir = new File(this.location);
	String location=this.location;
	int i=1;
	while(dir.exists()){    	
	  this.location=location+Integer.toString(i++);
	  dir = new File(this.location);
	    }
	dir.mkdir();    
  }
	
  private void makeC45NamesFile(String names, String newnames) throws Exception{
	BufferedReader in = new BufferedReader(new FileReader(names));
	PrintWriter out = new PrintWriter(new FileOutputStream(newnames));
	out.println("0,1.");
	String endline = in.readLine();
	endline = "class_attribute:"+ (endline.endsWith(".")?endline:endline+".");
	String line = null;
	while((line = in.readLine()) != null){
	  out.println(line);	
	}
	out.println(endline);
	in.close();
	out.close();
  }
  
  private void cleanup(){
	try{
	  File dir = new File(this.location);
	  for(File f:dir.listFiles())
	    f.delete();
	dir.delete();
	System.out.println("The MSC building environment has been cleaned up.");
	  }catch(Exception e){
		 System.err.println("Fail to clean up the MSC building environment.");
		 e.printStackTrace();
	   }    
  }
	  
}
