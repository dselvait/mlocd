package test;
import classifiers.NaiveWeka;
import utils.Evaluation;
import classifiers.C45;

public class TestNaiveWeka {

	public static void main(String[] args)throws Exception{
	String base = ".\\";
	String dataset = "sta1.data";
	String testset = "sta2.data";
	String dataset2 = "sea1.data";
	String testset2 = "sea2.data";
	
	NaiveWeka classifier = new NaiveWeka();
//	classifier.build(base+dataset);
//	String[] labels = classifier.classifyData(base+testset);
//	for(String s:labels)
//		System.out.println(s);
//	double acc = Evaluation.accuracy(base+testset, labels);
//	System.out.println("The accuracy is "+acc);
//	System.out.println("System End");
//	
	classifier.build(base+dataset2);
	String[] label2 = classifier.classifyData(base+testset2);
	for(String s:label2)
		System.out.println(s);
	double acc2 = Evaluation.accuracy(base+testset2, label2);
	System.out.println("The accuracy is "+acc2);
	System.out.println("System End");
	
	}
}
