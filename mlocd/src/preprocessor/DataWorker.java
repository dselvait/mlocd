package preprocessor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;

public class DataWorker{

	
  //joinDataset method horizontally join two files together
  public static void joinDataset(String dataset1, String dataset2, String jointSet){
    try{
       PrintWriter out = new PrintWriter(new FileOutputStream(jointSet));
	   BufferedReader in1 = new BufferedReader(new FileReader(dataset1));
	   BufferedReader in2 = new BufferedReader(new FileReader(dataset2));
	   String seperator = System.getProperty("line.seperator");
	    	   
	   String line1 = "";
	   String line2 = "";
	   while(in1.ready() && in2.ready()){
	     line1 = in1.readLine();
	     line2 = in2.readLine();  
	     System.out.println(line1 + "," + line2);
	     out.write(line1+","+line2+seperator);
       }
	   in1.close();
	   in2.close();
	   out.close();
    }catch( Exception e){
	   e.printStackTrace();
	   System.exit(1);
	 } 	  
  }

  //combineDataset method vertically combine two files together
  public static void combineDatset(String dataset1 ,String dataset2, String combinedSet){
	try{
	  PrintWriter out = new PrintWriter(combinedSet);
	  BufferedReader in1 = new BufferedReader(new FileReader(dataset1));
	  BufferedReader in2 = new BufferedReader(new FileReader(dataset2));
	  String line = "";
	  while((line=in1.readLine()) != null){
		out.println(line);  
	  }
	  in1.close();
	  while((line=in2.readLine()) != null){
		out.println(line);  
	  }
	  in2.close();
	  out.close();
	}catch(Exception e){
	   e.printStackTrace();
	   System.exit(1);
	}  
	  
  }  
  
  public static String getRecordClass(String record){
	String label = null;
	String [] tmp = record.split(",");
    label = tmp[tmp.length-1];
    return label;
  }
  
  
  public static void removeField(String source, int index,String output)throws Exception {
      BufferedReader in = new BufferedReader(new FileReader(source));
      PrintWriter out = new PrintWriter(new FileOutputStream(output));          
      while(in.ready()){
       String line = removeFieldInLine(in.readLine(),index);
       out.println(line);
      }
      in.close();
      out.close();
    }
  
    public static void rename(String source, int index, String output)throws Exception{
	  BufferedReader in = new BufferedReader(new FileReader(source));
	  PrintWriter out = new PrintWriter(new FileOutputStream(output));
	  int count = 0;	  
	  String p="";
	  
	  while(in.ready()){		
		String[] fields = in.readLine().split(DELIMITER); 
	    if(!p.equals(fields[index])) { p=fields[index]; count++; }  
	    fields[index] = Integer.toString(count);
	    out.println(joinFields(fields));
	 }
	    in.close();
	    out.close();
}
  
  // Precondition the makeFields method assume the id is listed at the first column 
    public static void makeFields(String source, String[] columns,int index, String output)throws Exception{
      BufferedReader in = new BufferedReader(new FileReader(source));
  	  PrintWriter out = new PrintWriter(new FileOutputStream(output));
      String[] lines = null;
  	  String[] newFields = null;      	  
  	  String nextLine=in.readLine();
  	  Boolean next=true;
  	// Group the lines belong to identical id 	
  	  while(in.ready()){
  		 int i = 1;
    	 String id=""; 
    	 lines = new String[columns.length+5];  
  		 // 
    	 while(in.ready()){  	    		    	
  	    	if(next==true) { 
  	    		lines[0]=nextLine;
  	    		String[] fields = nextLine.split(DELIMITER);
  	    		id=fields[0];
  	    		next=false;
  	    		continue;
  	    	} 	    	
  	        String line = in.readLine();
  	    	String[] fields = line.split(DELIMITER);
  	        if( id.equals(fields[0])) lines[i++]=line;   
  	         else {nextLine=line; next=true; break;}
  	       }  // Group the lines belong to identical id 	     	        	      	        	       
    	  newFields = new String[columns.length+1];  //changed
    	  newFields[0]=id;
  	       for(String line:lines){
  	          if(line == null) break;	          
  	          String[] values = line.split(DELIMITER); 	         
  	          String tag = values[index].substring(1,values[index].length()-1);
  	          //System.out.println(tag);
  	          for(int p=0; p<columns.length; p++){  	          
  	          if(tag.equals(columns[p])) 
  	           newFields[p+1]=values[index+1].substring(1,values[index+1].length()-1);//changed	
  	          else if(newFields[p+1]==null) newFields[p+1]="0";  //changed
  	                  } //second for
  	                }  // first for     	       	      	     
  	   out.println(joinFields(newFields));
  	   //System.out.println(joinFields(newFields));
  	     } // while     
 	   in.close();
       out.close();
      }
       
 
    
    public static void makeDifference(String source, String output)throws Exception {
      BufferedReader in = new BufferedReader(new FileReader(source));
      PrintWriter out = new PrintWriter(new FileOutputStream(output));     
      while(in.ready()){
       int num=10;
       String[] values = in.readLine().split(DELIMITER);	
       for(int i=0; i<values.length; i++){
        int value=Integer.parseInt(values[i]);
        value=num+value;
        values[i]=Integer.toString(value);
        num=num+10;
    	}	//for
        out.println(joinFields(values));
      } //while
     in.close();
     out.close();
    }
    
    public static void joinMatched(String source1, int idx1, String source2, int idx2, String jointFile) throws Exception {
      BufferedReader in1 = new BufferedReader(new FileReader(source1));
      BufferedReader in2 = new BufferedReader(new FileReader(source2));
      PrintWriter out = new PrintWriter(new FileOutputStream(jointFile)); 
      in2.mark((int)(new File(source2).length())+1);
      while(in1.ready()){
       String line1 = in1.readLine();      
       System.out.println("Searching "+line1);
       String line2=null;
       if(line1.equals("")) continue;
       while((line2=in2.readLine()) != null){
    	if(line2.equals("")) continue;
    	if(matchedKey(line1,idx1,line2,idx2)) {    	 
    	 String key = line1.split(DELIMITER)[idx1];  
    	 String newline1 = removeFieldInLine(line1,idx1);
    	 String newline2 = removeFieldInLine(line2,idx2);
    	 out.println(key+","+newline1+","+newline2); 
    	 break;    
         }//     
        } // inner while
        in2.reset(); 
      }//while 
      in1.close();
      in2.close();
      out.close();
    }
    
    private static String joinFields(String[] fields){
      StringBuilder line=new StringBuilder("");
       for(int i=0; i<fields.length-1; i++){
    	line = line.append(fields[i]+DELIMITER); 
    	}
    	line = line.append(fields[fields.length-1]);
    	return line.toString();
    }
    
    public static String removeFieldInLine(String line, int index){
        String newline=null;
        String[] fields = line.split(DELIMITER);
        String[] newFields = new String[fields.length-1];
        int i=0;
        for(int j=0; j<fields.length; j++){
         if(i == index){ 
       	  if (j+1 != fields.length) {newFields[i++]=fields[j+1]; j++;}         
         }
         else newFields[i++]=fields[j];
        }  
        newline=joinFields(newFields);
        return newline;
       }
    
    private static boolean matchedKey(String line1, int idx1, String line2, int idx2){
      String[] fields1 = line1.split(DELIMITER);
      String[] fields2 = line2.split(DELIMITER);
      return fields1[idx1].equals(fields2[idx2]);
    }
    
    public static boolean equalValues(String str1, String str2){
      if(str1.equals(str2)) return true;      
      String str11=str1.substring(1,str1.length()-1);
      if(str2.equals(str11)) return true;
      String str21=str2.substring(1,str2.length()-1);
      if(str1.equals(str21)) return true;
      return false;   	
    }
    
    public static String DELIMITER = ","; 
  }
