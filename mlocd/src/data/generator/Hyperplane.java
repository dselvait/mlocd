package data.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import utils.RandomMachine;

public class Hyperplane {

	public final String attribute_1 = "A1";
	public final String attribute_2 = "A2";
	public final String attribute_3 = "A3";
	public double weight1 = 0.0;
	public double weight2 = 0.0;
	public double weight3 = 0.0;
	public final int positive_label = 1; 
	public final int nagative_label = 0;
	
	public double x1;
	public double x2;
	public double x3;
	
	public String seperator = ",";
	private String location;
   	private int seed=0; // mod 3 to select the rules;
	
	public Hyperplane(String location){
		this.location = location.split("\\.")[0];
	}
	
	public void setLocation(String location){ 
		this.location = location.split("\\.")[0];
	}
	
	public void reset(){seed = 0;}
	
	public void generateData(int instancesNum) throws java.io.IOException{
		
		if(new File(location+".data").exists()){
			error_message();
		}
		
		int rule_index = seed++ % 4;
		double rule = 1;
		
		switch(rule_index){
		case 0: { weight1 = 2; weight2 = 3; weight3 = 4; break; }
		case 1: { weight1 = 6; weight2 = 7; weight3 = 8; break; }
		case 2: { weight1 = 9; weight2 = 11; weight3 = 12; break; }
		case 3: { weight1 = 20; weight2 = 21; weight3 = 22; break; }
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
		    	double remain = rule - weight1*x1;
		    	x2 = rand.getRandomFloat(0,remain);
		    	remain = rule - weight2*x2 - weight1*x1;
		    	x3 = remain / weight3;
		    }	else{
		    	x1 = rand.getRandomFloat(0,1);
		    	x2 = rand.getRandomFloat(0,1);
		    	x3 = rand.getRandomFloat(0,1);
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
		throw new UnsupportedOperationException("Hyperplane concept can't overwrite existing file," +
		"use setLocation method to define a new data file.");
	}
}
