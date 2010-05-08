package run;

import utils.Evaluation;
import classifiers.NaiveWeka;
import data.generator.Sea;
import meta.*;

public class RunBoost {

	public static void main(String[] args)throws Exception{
		String base = System.getProperty("user.dir")+"\\lab\\";
		String stadata = "sea";
		Sea sea = new Sea(base+stadata);
		String[] datasets = new String[2];
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + (i+1) +".data";
			sea.setLocation(dataset);
//			sea.generateData(100);
//			sea.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = datasets[datasets.length-1];
	
		NaiveWeka learner = new NaiveWeka();
		learner.build(datasets[0]);
		String[] labels = learner.classifyData(testset);
		double accuracy = Evaluation.accuracy(testset, labels);
		System.out.println("The accuracy of msc is "+accuracy);
		
		
		Boosting classifier = new Boosting(learner);
		classifier.setLocation(base+"sea");
		classifier.build(datasets);
		
		labels = classifier.classifyData(testset);
		accuracy = Evaluation.accuracy(testset, labels);
		System.out.println("The accuracy of msc is "+accuracy);
		System.out.println("System End");
	}
	
}
