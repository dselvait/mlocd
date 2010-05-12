package run;
import meta.MSC;
import classifiers.NaiveWeka;
import utils.Evaluation;
import utils.FileWorker;
import data.generator.Hyperplane;
import data.generator.Stagger;
import data.generator.Testset;

public class OnHyperplane {
	
	public static void main(String[] args)throws Exception{
		String base = System.getProperty("user.dir")+"\\lab\\";
		String hypdata = "hyp";
		Hyperplane hyp = new Hyperplane(base+hypdata);
		String[] datasets = new String[10]; // Change the batch number
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + hypdata + i +".data";
			hyp.setLocation(dataset);
			hyp.generateData(500);
			hyp.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = base+"hyptest.data";
		Testset.make(datasets, testset, 300);
		// Need to manully copy the names file
//		String[] oldsets = new String[datasets.length-1];
//		System.arraycopy(datasets, 0, oldsets, 0, datasets.length-1);
//		String jdata = base+"jhyp.data";
//		FileWorker.combineFile(datasets, jdata);
		NaiveWeka learner = new NaiveWeka();
		learner.build(datasets[0]);
		String[] labels = learner.classifyData(testset);
		double errorRate = Evaluation.errorRate(testset, labels);
		System.out.println("The error rate of naive method is "+errorRate);
		
//		NaiveWeka learner = new NaiveWeka();
//		MSC classifier = new MSC(learner);
//		classifier.setLocation(base+"hyp");
//		classifier.build(datasets);
//		
//		String[] labels = classifier.classifyData(testset);
//		double errorRate = Evaluation.errorRate(testset, labels);
//		System.out.println("The error rate of MSC is "+errorRate);
		System.out.println("System End");
	}
	
}
