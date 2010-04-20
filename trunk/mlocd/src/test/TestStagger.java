package test;
import data.generator.*;

public class TestStagger {

	public static void main(String[] args)throws Exception{
		String base = "";
		String stadata = "sta1";
		Stagger sta = new Stagger(base+stadata);
		sta.generateData(50);
		sta.setLocation("sta2");
		sta.generateData(80);
		sta.makeNamesFile();
		System.out.println("System End");
	}
}
