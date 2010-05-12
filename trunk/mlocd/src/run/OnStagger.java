package run;
import meta.MSC;
import classifiers.NaiveWeka;
import utils.Evaluation;
import utils.FileWorker;
import data.generator.Sea;
import data.generator.Stagger;
import data.generator.Testset;

public class OnStagger {
	
	public static void main(String[] args)throws Exception{
		String base = System.getProperty("user.dir")+"\\lab\\";
		String stadata = "sta";
		Stagger sta = new Stagger(base+stadata);
		String[] datasets = new String[10]; // Change the batch number
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + i +".data";
			sta.setLocation(dataset);
			sta.generateData(500);
			sta.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = base+"statest.data";
		Testset.make(datasets, testset, 300);
		// Need to manully copy the names file
//		String[] oldsets = new String[datasets.length-1];
//		System.arraycopy(datasets, 0, oldsets, 0, datasets.length-1);
//		String jdata = base+"jsta.data";
//		FileWorker.combineFile(datasets, jdata);
		NaiveWeka learner = new NaiveWeka();
		learner.build(datasets[0]);
		String[] labels = learner.classifyData(testset);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The error rate of naive method is "+errorRate);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of naive method is "+accuracy);
		
//		NaiveWeka learner = new NaiveWeka();
//		MSC classifier = new MSC(learner);
//		classifier.setLocation(base+"sta");
//		classifier.build(datasets);
//		
//		String[] labels = classifier.classifyData(testset);
//		double accuracy = Evaluation.accuracy(testset, labels);
//		System.out.println("The accuracy of msc is "+accuracy);
		System.out.println("System End");
	}
	
}
