package run;
import meta.MSC;
import meta.DMW;
import meta.SEAlearner;
import classifiers.NaiveWeka;
import utils.Evaluation;
import utils.FileWorker;

import data.generator.Sea;
import data.generator.Testset;

public class OnSea {
	
	public static String base = System.getProperty("user.dir")+"\\lab\\";
	
	
	public static void main(String[] args)throws Exception{
		
		String stadata = "sea";
		Sea sea = new Sea(base+stadata);
		String[] datasets = new String[10]; // Change the batch number
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + i +".data";
			sea.setLocation(dataset);
			sea.generateData(500);
			sea.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = base+"seatest.data";
		Testset.make(datasets, testset, 300);

//		runMsc(datasets,testset);
//		runNaive(datasets, testset);
//		runDMW(datasets, testset);
		runSEAlearner(datasets, testset);
		System.out.println("System End");
	}
	
	public static void runMsc(String[] datasets, String testset)throws Exception{
		NaiveWeka learner = new NaiveWeka();
		MSC classifier = new MSC(learner);
		classifier.setLocation(base+"sea");
		classifier.build(datasets);
		String[] labels = classifier.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The accuracy of msc is "+errorRate);
	}
	
	public static void runNaive(String[] datasets, String testset)throws Exception{
	    //Need to manully copy the names file
//		String[] oldsets = new String[datasets.length-1];
//		System.arraycopy(datasets, 0, oldsets, 0, datasets.length-1);
		String jdata = base+"jtest.data";
		FileWorker.combineFile(datasets, jdata);
		NaiveWeka learner = new NaiveWeka();
//		learner.build(datasets[0]);
		learner.build(jdata);
		String[] labels = learner.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of naive method is "+accuracy);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The error rate of naive is "+errorRate);
	}
	
	public static void runDMW(String[] datasets, String testset)throws Exception{
		NaiveWeka learner = new NaiveWeka();
		DMW classifier = new DMW(learner);
		classifier.setLocation(base+"sea");
		classifier.build(datasets);
		String[] labels = classifier.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The error rate of Boosting is "+errorRate);
	}
	
	public static void runSEAlearner(String[] datasets, String testset)throws Exception{
		NaiveWeka learner = new NaiveWeka();
		SEAlearner classifier = new SEAlearner(learner);
		classifier.setLocation(base+"sea");
		classifier.build(datasets);
		String[] labels = classifier.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The error rate of Boosting is "+errorRate);
	}
	
}
