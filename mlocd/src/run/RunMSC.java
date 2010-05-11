package run;
import meta.MSC;
import classifiers.NaiveWeka;
import utils.Evaluation;
import utils.FileWorker;

import data.generator.Sea;
import data.generator.Testset;

public class RunMSC {
	
	public static void main(String[] args)throws Exception{
		String base = System.getProperty("user.dir")+"\\lab\\";
		String stadata = "sea";
		Sea sea = new Sea(base+stadata);
		String[] datasets = new String[4]; // Change the batch number
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + i +".data";
			sea.setLocation(dataset);
			sea.generateData(500);
			sea.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = base+"seatest.data";
		Testset.make(datasets, testset, 600);
//		// Need to manully copy the names file
//		String jdata = base+"jtest.data";
//		String[] oldsets = new String[datasets.length-1];
//		System.arraycopy(datasets, 0, oldsets, 0, datasets.length-1);
//		FileWorker.combineFile(datasets, jdata);
//		
//		
		NaiveWeka learner = new NaiveWeka();
//		learner.build(jdata);
//		String[] labels = learner.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		
		
		MSC classifier = new MSC(learner);
		classifier.setLocation(base+"sea");
		classifier.build(datasets);
		
		String[] labels = classifier.classifyData(testset);
		double accuracy = Evaluation.accuracy(testset, labels);
		System.out.println("The accuracy of msc is "+accuracy);
		System.out.println("System End");
	}
	
}
