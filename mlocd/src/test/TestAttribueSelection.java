package test;
import data.preprocessor.AttributeSelection;

public class TestAttribueSelection {

	public static void main(String[] args)throws Exception{
		String base = ".\\";
		String dataset = "german.data";
		AttributeSelection as = new AttributeSelection();
		int[] atts = as.recommandAttributes(base+dataset);
	    for(int s:atts)
	    	System.out.println(s);
	    System.out.println("System End");
	}
}
