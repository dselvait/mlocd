package run;
import meta.MSC;
import classifiers.NaiveWeka;
import utils.Evaluation;

import data.generator.Sea;
import data.generator.Testset;

public class RunMSC {
	
	public static void main(String[] args)throws Exception{
		String base = System.getProperty("user.dir")+"\\lab\\";
		String stadata = "sea";
		Sea sea = new Sea(base+stadata);
		String[] datasets = new String[1];
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + (i+1) +".data";
			sea.setLocation(dataset);
			sea.generateData(500);
			sea.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = base+"seatest.data";
	
		NaiveWeka learner = new NaiveWeka();
		learner.build(datasets[0]);
		Testset.make(datasets, testset, 200);
		String[] labels = learner.classifyData(testset);
		double accuracy = Evaluation.accuracy(testset, labels);
		System.out.println("The accuracy of msc is "+accuracy);
		
		
//		MSC classifier = new MSC(learner);
//		classifier.setLocation(base+"sea");
//		classifier.build(datasets);
//		
//		labels = classifier.classifyData(testset);
//		accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		System.out.println("System End");
	}
	
}
