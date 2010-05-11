package data.generator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat; 
import java.text.DecimalFormat;
import utils.RandomMachine;

public class Sea {

	public final String attribute_1 = "A1";
	public final String attribute_2 = "A2";
	public final String attribute_3 = "A3";
	public final int positive_label = 1; 
	public final int nagative_label = 0;
	
	public double x1;
	public double x2;
	public double x3;
	
	public String seperator = ",";
	private String location;
   	private int seed=0; // mod 3 to select the rules;
	
	public Sea(String location){
		this.location = location.split("\\.")[0];
	}
	
	public void setLocation(String location){ 
		this.location = location.split("\\.")[0];
		}
	
	public void reset(){seed = 0;}
	
	public void setSeed(int seed){ this.seed = seed;}
	
	public void generateData(int instancesNum) throws java.io.IOException{
		
		if(new File(location+".data").exists()){
			error_message();
		}
		
		int rule_index = seed++ % 5;
		double rule = 0.0;
		
		switch(rule_index){
		case 0: { rule = 8;  break; }
		case 1: { rule = 28; break; }
		case 2: { rule = 48; break; }
		case 3: { rule = 58; break; }
		case 4: { rule = 78; break; }
		}
		
		PrintWriter writer = new PrintWriter(new FileOutputStream(location+".data"));
		RandomMachine rand = new RandomMachine();
		NumberFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(2);
		int i = 0;
		while( i++ < instancesNum ){
			StringBuilder line = new StringBuilder("");
			int label = rand.getRandomInt(0, 2);		
		    if(label == 1){
		    	x1 = rand.getRandomFloat(0,rule);
		    	x2 = rule - x1;
		    	x3 = rand.getRandomFloat(0, 99);
		    }	else{
		    	x1 = rand.getRandomFloat(0,99);
		    	x2 = rand.getRandomFloat(0, 99);
		    	x3 = rand.getRandomFloat(0, 99);
		    }
		    line=line.append(format.format(x1)+seperator+format.format(x2)+seperator+format.format(x3)+seperator);
		    writer.println(line.toString() + label);
		  }
		writer.close();
		}
	

	public void makeNamesFile() throws java.io.IOException {
		
		if(new File(location+".names").exists()){
			error_message();
		}
		
		PrintWriter writer = new PrintWriter(new FileOutputStream(location+".names"));
		writer.println(positive_label+","+nagative_label +".");
		writer.println(attribute_1+": continuous.");
		writer.println(attribute_2+": continuous.");
		writer.println(attribute_3+": continuous.");
		writer.close();
	}	
	
	private void error_message(){
		throw new UnsupportedOperationException("SEA concept can't overwrite existing file," +
		"use setLocation method to define a new data file.");
	}
	
}
