package io;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

public class FileWorker {
  
  public static void cp(String file1, String file2) throws Exception {
	BufferedReader in = new BufferedReader(new FileReader(file1));
	PrintWriter out=new PrintWriter(new FileOutputStream(file2)); 
	String line;
	while((line=in.readLine())!=null){
	out.println(line);  
	 }
	out.close();
	in.close();
  }
		
  public static void remove(String filename) {
	 File target = new File(filename);
	 // Make sure the file or directory exists and isn't write protected
	 if (!target.exists())
	    throw new IllegalArgumentException(
	          "Delete: no such file or directory: " + filename);

	 if (!target.canWrite())
	      throw new IllegalArgumentException("Delete: write protected: "
	          + filename);

	    // If it is a directory, make sure it is empty
	 if (target.isDirectory()) {
	    String[] files =target.list();
	    if (files.length > 0)
	       throw new IllegalArgumentException(
	            "Delete: directory not empty: " + filename);
	 }

	 boolean success = target.delete();

//	 if (!success)
//	    throw new IllegalArgumentException("Delete: deletion failed");
  }
  
  public static void combineFile(String[] filenames, String target)throws java.io.IOException{
	  PrintWriter out=new PrintWriter(new FileOutputStream(target)); 
	  for(String filename : filenames){
	  BufferedReader in = new BufferedReader(new FileReader(filename));
	  while(in.ready()){
		  out.println(in.readLine());
	  }
	  in.close();
	  }
	  out.close();
  }

}