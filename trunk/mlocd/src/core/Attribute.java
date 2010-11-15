package core;
import java.util.Arrays;

public class Attribute implements java.lang.Cloneable {

	public enum Type{CATEGORICAL, CONTINUOUS}
	
	public final Type type;
	//private int id;
	private String[] attClasses = null;
	private String name;

	public Attribute(Type type){
		this.type = type;
	}
	
	public void setAttributeClasses(String[] attClasses){
		this.attClasses = attClasses;
	}
    
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getIndex(String aAttribute){
		if(type == Type.CATEGORICAL) {
			return 0;
		} 
		int i = 0;
		while(i < attClasses.length){
			if( attClasses.equals(aAttribute))
				return i+1;
			else i++;	
		}
		return -1;
	}
	
	public String getAttributeName(){
		return name;
	}
	
	public String[] getAttributeClasses(){
	  if(type == Type.CONTINUOUS)
		  return null;
	  String[] copy = Arrays.copyOf(attClasses, attClasses.length);
	  return copy;
	}
	
	public Attribute clone() {
		Attribute aAttribute = new Attribute(this.type);
		aAttribute.setName(this.name);
		aAttribute.setAttributeClasses(this.attClasses);
		return aAttribute;
	}
}

