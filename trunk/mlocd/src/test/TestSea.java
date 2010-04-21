package test;

import data.generator.Sea;

public class TestSea {

	public static void main(String[] args)throws Exception{
		String base = "";
		String stadata = "sea1";
		Sea sea = new Sea(base+stadata);
		sea.generateData(50);
		sea.setLocation("sea2");
		sea.generateData(80);
		sea.makeNamesFile();
		System.out.println("System End");
	}
	
}
