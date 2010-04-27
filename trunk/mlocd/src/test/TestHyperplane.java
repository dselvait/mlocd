package test;

import data.generator.Hyperplane;

public class TestHyperplane {

	public static void main(String[] args) throws Exception {
		String base = "";
		String hyperdata = "hyper1";
		Hyperplane hyper = new Hyperplane(base + hyperdata);
		hyper.generateData(50);
		hyper.makeNamesFile();
		hyper.setLocation("hyper2");
		hyper.generateData(80);
		hyper.makeNamesFile();
		System.out.println("System End");
	}

}