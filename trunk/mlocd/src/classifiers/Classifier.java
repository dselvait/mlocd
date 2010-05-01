package classifiers; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class Classifier {
  
  protected String buildCommand=null; 
  protected String classifyCommand=null;
  protected String model=null;
  
   /**
   * Generating a classifier is by using build method, provided with the 
   * taraining data.
   * @param data set of instances serving as training data 
   * @exception Exception if the classifier has not been 
   * generated successfully
   */
  public abstract void build(String traniningSet) throws Exception;  
  
  public abstract String classifyInstance(String instance) throws Exception;  
  
  public abstract String[] classifyData(String dataset) throws Exception;
  
  public abstract Classifier copy() throws Exception;
  
  public void setBuildCommand(String command){
	this.buildCommand=command; 
  }
    
  public void setClassifyCommand(String command){
	this.classifyCommand=command;  
  }
    
  public String getLocation(){
	return this.model;  
  }
  
  public void reLocation(String newname)throws FileNotFoundException{
	File learner = new File(this.model);
	if(!learner.exists())
	  throw new FileNotFoundException();
	File newfile = new File(newname);
	learner.renameTo(newfile);
  }
  
  public void setLocation(String location){
	  this.model = location;
  }
  
  /**
   * If the inherited class has speicial surfix name then the following two
   * methods: surfixName and getSurfixName need to be overwirte.
   * @return
   */
  public boolean surfixName(){return false;}
  public String getSurfixName(){return "";}
  
  public String toString(){
	    String name = this.getClass().getName();
		return name;	  
  }
  
  				 
}
