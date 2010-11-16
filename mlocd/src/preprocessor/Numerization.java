package preprocessor;
import io.C45Reader;

import java.io.BufferedReader;
import java.io.FileReader;

import core.*;

public class Numerization {

	/**
	 * The method read in the c45 file and get the categorical values and change them 
	 * to digital values depending on their sequences shown on the names file. 
	 *
	 */
	public static Attribute digitalAttribute(String filename, int index)throws Exception{
	   BufferedReader reader = new BufferedReader(new FileReader(filename));	
	   Attribute[] attributes = C45Reader.parseAttributes(filename);
	   Attribute aAttribute = attributes[index].clone();
	   String[] classes = aAttribute.getAttributeClasses();
	   for(int i=0; i < classes.length; ++i){
		   classes[i] = Integer.toString(i);
	   }
	   aAttribute.setAttributeClasses(classes);
	   return aAttribute;
	}
	
}
