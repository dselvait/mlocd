package run;
import io.FileWorker;
import classifiers.NaiveWeka;
import classifiers.meta.DMW;
import classifiers.meta.MSC;
import classifiers.meta.SEAlearner;
import utils.Evaluation;
import data.generator.Sea;
import data.generator.Stagger;
import data.generator.Testset;

public class OnStagger {
	
	public static String base = System.getProperty("user.dir")+"\\lab\\";
	
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
	
		runDMW(datasets, testset);
		runSEAlearner(datasets, testset);
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
		System.out.println("The error rate of DMW is "+errorRate);
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
		System.out.println("The error rate of SEAlearner is "+errorRate);
	}
	
	
}
