package run;
//import io.*;
//import classifiers.*;
import io.FileWorker;
import utils.*;

public class DoneData {

	public static void main(String[] args)throws Exception{

		 //String base = "C:\\workspace\\DMtools\\dataset\\";
		 //String base = "E:\\kdd-workspace\\datasets\\original\\";
		 String base = "/home/runxin/kdd-workspace/datasets/original/";
		//String base = "/home/runxin/kdd-workspace/DMtools/dataset/original/debug/"; 
		
		 String c45_db = "/home/runxin/Desktop/Datasets/C45/";
		 //String bayes_home = "E:\\kdd-workspace\\datasets\\bayes\\";
		 String dataset = "car.data";
		 String newset = "car.data";
		 String names = dataset.split("\\.")[0]+".names";
		 FileWorker.cp(base+dataset,c45_db+newset);
		 FileWorker.cp(base+names,c45_db+names);
		 
		 System.out.println("Dataset is available.");		
		
		 for(int i = 1; i < 6 ; ++i){
			 String newset2 = dataset.split("\\.")[0] + i;
			 io.FileWorker.cp(c45_db+newset, c45_db+newset2+".data");
			 FileWorker.cp(base+names,c45_db+newset2+".names");
		 }
		 
		 System.out.println("System End");		
	}                      
	
}
