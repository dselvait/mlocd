package test.data;
import data.generator.*;

public class TestStagger {

	public static void main(String[] args)throws Exception{
		String base = "";
		String stadata = "sta1";
		Stagger sta = new Stagger(base+stadata);
		sta.generateData(80);
		sta.makeNamesFile();
		sta.setLocation("sta2");
		sta.generateData(40);
		sta.makeNamesFile();
		Stagger.Concept concept = sta.new Concept();
		System.out.println("System End");
	}
}
