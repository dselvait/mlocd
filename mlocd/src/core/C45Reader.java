package core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import core.Attribute.Type;



public class C45Reader {

	public static Attribute[] parseAttributes(String names_file)throws Exception{
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		BufferedReader reader = new BufferedReader(new FileReader(names_file));
		
		while(reader.ready()){
			String line = reader.readLine();
			//line = parseLine(line);
			if(!attributeLine(line))
				continue;
			Attribute attribute = parseLine(line);
			attributes.add(attribute);
		}
		return (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
	}
	
	public static String[] getClassValues(String names_file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(names_file));
		ArrayList<String> list = new ArrayList<String>();
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				StringBuilder str = new StringBuilder();
				outer: for (int i = 0; i < line.length(); ++i) {
					char c = line.charAt(i);
					switch (c) {
					case ' ':
						break;
					case '|':
						break outer;
					case '.': {
						break outer;
					}
					case '\t':
						break;
					case ',': {
						if (!str.equals("")) {
						list.add(str.toString());
						str = new StringBuilder();
						}
						break;
					}
					default:
						str.append(c);
					}
				}
				if (!str.equals("")) 
					list.add(str.toString());
				if (list.size() != 0) {
					break;
				}
			}
		} finally {
			reader.close();
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	public static String[] getC45FileNames(String filename){
		String[] names = new String[2];
		String[] tmp = filename.split("\\.");
		if(tmp.length == 0){
			names = new String[2];
			names[0] = filename + ".data";
			names[1] = filename + ".names";
		}
		else{
		  names[0] = filename;
		  names[1] = tmp[0] + ".names";
		}
		return names;
	}
	
	private static boolean attributeLine(String line){
	  if(!line.contains(":"))
		 return false;
	  else return true;	
	}
	
	private static Attribute parseLine(String line){
	   Attribute attribute = null;
	   ArrayList<String> list = new ArrayList<String>();
	   StringBuilder str = new StringBuilder("");
	   
	   if(line.contains("continuous")){
		  if(!line.contains("|")) 
		   attribute = new Attribute(Type.CONTINUOUS);
		  else if(line.indexOf("continuous") < line.indexOf("|"))
			  attribute = new Attribute(Type.CONTINUOUS);
		  else
			   attribute = new Attribute(Type.CATEGORICAL);
	   } else
		   attribute = new Attribute(Type.CATEGORICAL);
	   
	   outer:for(int i = 0 ; i < line.length(); ++i){
		 char c = line.charAt(i);
		 switch (c){
		 case ' ' : break;   
		 case '\t' : break;
		 case ',' : {list.add(str.toString()); str = new StringBuilder(""); break;}
		 case ':' : {list.add(str.toString()); str = new StringBuilder(""); break;}
		 case '.' : {list.add(str.toString()); str = new StringBuilder(""); break outer;}
		 default : str.append(c);
	     }
	   }
	   
	   str = null;
	   String[] classes = new String[list.size()-1];
	   for(int i = 1 ; i < list.size(); ++i)
	      classes[i-1] = list.get(i); 
	   attribute.setName(list.get(0));
	   attribute.setAttributeClasses(classes);
	   return attribute;
	}
		
}
