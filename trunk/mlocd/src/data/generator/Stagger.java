package data.generator;
//import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import utils.RandomMachine;
import java.io.File;

public class Stagger {

	public final String attribute_1 = "color";
	public final String attribute_2 = "shape";
	public final String attribute_3 = "size";
	public final int positive_label = 1; 
	public final int nagative_label = 0;
	
	public enum Color{GREEN,BLUE,RED};
	public enum Shape{TRIANGLE,CIRCLE,RECTANGLE};
	public enum Size{SMALL,MEDIUM,LARGE};
	
	public String seperator = ",";
	private String location;
   	private int seed=0; // mod 3 to select the rules;
	
	public Stagger(String location){
		this.location = location.split("\\.")[0];
	}
	
	public void setLocation(String location){ this.location = location;}
	
	public void reset(){seed = 0;}
	
	public void generateData(int instancesNum) throws java.io.IOException{
		
		if(new File(location+".data").exists()){
			error_message();
		}
		
		int rule_index = seed++ % 3;
		Color color=null; Shape shape=null; Size size=null;
		Concept concept = null;
		switch(rule_index){
		case 0: { concept = new Concept(color=Color.RED,shape=null,size=Size.SMALL); 
		          concept.relation = 1;}
		
		case 1: { concept = new Concept(color=Color.GREEN,shape=Shape.CIRCLE,null); 
                   concept.relation = 0;}
		
		case 3: { concept = new Concept(color=Color.RED,shape=null,size=Size.MEDIUM); 
        concept.relation = 0;}
		}
		
		PrintWriter writer = new PrintWriter(new FileOutputStream(location+".data"));
		RandomMachine rand = new RandomMachine();
		Color[] allColor = Color.values();
		Shape[] allShape = Shape.values();  
		Size[] allSize = Size.values();   
		//String value = null;
		int i = 0;
		while( i++ < instancesNum ){
			StringBuilder line = new StringBuilder("");
			int label = rand.getRandomInt(0, 2);		
		    if(label == 1){
		      line.append( makeData(concept,i));
		    }	else{
		    	int j ;
		    	j = rand.getRandomInt(0,3);
		    	line = line.append(allColor[j].toString()+ this.seperator);
		    	j = rand.getRandomInt(0,3);
		    	line = line.append(allShape[j].toString()+ this.seperator);
		    	j = rand.getRandomInt(0, 3);
		    	line = line.append(allSize[j].toString() + this.seperator);
		    }
		    writer.println(line.toString() + label + ".");
		  }
		writer.close();
		}
		

	public void generateData(Concept concept, int instancesNum)
			throws java.io.IOException {

		if (new File(location + ".data").exists()) {
			error_message();
		}

		PrintWriter writer = new PrintWriter(new FileOutputStream(location
				+ ".data"));
		RandomMachine rand = new RandomMachine();
		Color[] allColor = Color.values();
		Shape[] allShape = Shape.values();
		Size[] allSize = Size.values();
		// String value = null;
		int i = 0;
		while (i++ < instancesNum) {
			StringBuilder line = new StringBuilder("");
			int label = rand.getRandomInt(0, 2);
			if (label == 1) {
				 line.append( makeData(concept,i));
			} else {
				int j;
				j = rand.getRandomInt(0, 3);
				line = line.append(allColor[j].toString() + this.seperator);
				j = rand.getRandomInt(0, 3);
				line = line.append(allShape[j].toString() + this.seperator);
				j = rand.getRandomInt(0, 3);
				line = line.append(allSize[j].toString() + this.seperator);
			}
			writer.println(line.toString() + label + ".");
		}
		writer.close();
	}
	
	public void makeNamesFile() throws java.io.IOException {
		
		if(new File(location+".names").exists()){
			error_message();
		}
		
		PrintWriter writer = new PrintWriter(new FileOutputStream(location+".names"));
		writer.println(positive_label+","+nagative_label +".");
		writer.println(attribute_1+":"+Color.RED+seperator+Color.GREEN+seperator+Color.BLUE+".");
		writer.println(attribute_2+":"+Shape.CIRCLE+seperator+Shape.TRIANGLE+seperator+Shape.RECTANGLE+".");
		writer.println(attribute_3+":"+Size.SMALL+seperator+Size.MEDIUM+seperator+Size.LARGE+".");
		writer.close();
	}	
	
	private String makeData(Concept concept, int num) {
		Color[] allColor = Color.values();
		Shape[] allShape = Shape.values();
		Size[] allSize = Size.values();
		StringBuilder line = new StringBuilder("");
		if (concept.color != null && concept.relation == 0 && num++ % 2 == 0)
			line = line.append(concept.color.toString() + this.seperator);
		else {
			if (concept.color != null && concept.relation == 1) {
				line = line.append(concept.color.toString() + this.seperator);
			} else
				line = line.append(allColor[num % 3].toString()
						+ this.seperator);
		}
		if (concept.shape != null && concept.relation == 0 && num++ % 2 == 0)
			line = line.append(concept.shape.toString() + this.seperator);
		else {
			if (concept.shape != null && concept.relation == 1) {
				line = line.append(concept.shape.toString() + this.seperator);
			} else
				line = line.append(allShape[num % 3].toString()+ this.seperator);
		}
		if (concept.size != null && concept.relation == 0 && num++ % 2 == 0)
			line = line.append(concept.size.toString() + this.seperator);
		else {
			if (concept.size != null && concept.relation == 1) {
				line = line.append(concept.size.toString() + this.seperator);
			} else
				line = line.append(allSize[num % 3].toString() + this.seperator);
		}
		return line.toString();
	}
	
	private void error_message(){
		throw new UnsupportedOperationException("Stagger can't overwrite existing file," +
		"use setLocation method to define a new data file.");
	}
	
	public class Concept{
		public Color color = null;
		public Shape shape = null;
		public Size size = null;
		// Relation is 1 means the relation is "and", else if is 0 means the relation is "or";
		public int relation = 1;
		public Concept(Color color, Shape shape, Size size){
			this.color = color; 
			this.shape = shape;
			this.size = size;
		}
	}
}
