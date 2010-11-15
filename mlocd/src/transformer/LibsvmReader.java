package transformer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import core.*;

public class LibsvmReader {

	 private final String filename;
	 public String seperator = ",";
	 public String libsvm_seperator = " ";
		
	  public LibsvmReader(String filename){
		this.filename = filename;  
	  }
	  
	  public void setSeperator(String seperator){
		  this.seperator = seperator;
	  }
	  
	  public void convertTo(String location) throws Exception {
		    
		    String[] names = C45Reader.getC45FileNames(filename);
			BufferedReader reader = new BufferedReader(new FileReader(names[0]));
			PrintWriter writer = new PrintWriter(new FileOutputStream(location));
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			boolean integer_class_value = true;
			
			try {
				// Process the classes into integer value, if necessary.
				String[] class_values = C45Reader.getClassValues(names[1]);
				try{
					Integer.parseInt(class_values[0]);
				}catch(NumberFormatException e){
					integer_class_value = false;
				}
				
				Attribute[] attributes = C45Reader.parseAttributes(names[1]);
				for (Attribute att : attributes) {
					if (att.type == Attribute.Type.CATEGORICAL) {
						String[] classes = att.getAttributeClasses();
						HashMap<String,String> map = new HashMap<String,String>();
						for (int i = 0; i < classes.length; ++i) {
							map.put(classes[i], Integer.toString(i+1));
						}
						list.add(map);
					}
				}
			while(reader.ready()){
				String line = reader.readLine();
				String[] values = line.split(this.seperator);
				String label = values[values.length-1];
				if(!integer_class_value){
					int idx = indexOf(class_values,label);
					if( idx == -1 ) 
						throw new Exception("Fail to analyse the class values.");
					else 
					  label = Integer.toString(idx);
				}
				StringBuilder newline = new StringBuilder(label);
				
				int list_index = 0;
				for(int i = 0 ; i < values.length - 1; ++i){
				   String value = values[i]; 
				   if(attributes[i].type == Attribute.Type.CATEGORICAL){
	                  HashMap<String,String> map = list.get(list_index++);
				   	  value = (String) map.get(values[i]);
				   		}
				   if(attributes[i].type == Attribute.Type.CONTINUOUS){
				     if(Double.parseDouble(values[i]) == 0.0)
				    	 continue;
				   }
				   if(value == null || value == "?")
					   continue;
				    String attIndex = Integer.toString(i+1)+":";
				   	newline.append(libsvm_seperator+attIndex+value);
				}
			 writer.println(newline);	
			}
			} finally {
				reader.close();
				writer.close();
			}
		}
	  
	  private int indexOf(String[] elements, String element){
		  for(int i = 0 ; i < elements.length; ++i)
			  if(element.equals(elements[i]))
					  return i;
			  return -1;
	  }
}
