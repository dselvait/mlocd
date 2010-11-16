package utils.text;
import java.io.*;

public class MkComma{

public static void main(String[] args) throws Exception {
  File file = new File(args[1]);  
  BufferedReader reader = new BufferedReader(new FileReader(file));

  String str = "";
  String[] holder = null; 

   while( (str = reader.readLine()) != null){
   if(args[0].equals("-c")) holder = str.split(",");
     
   if(args[0].equals("-s")) holder = str.split(" ");
     
     StringBuilder line = new StringBuilder("");   
     
     int i=0;  // index head for the purpose not to add comma at the end 
     for(String s:holder){
     if( i == 0){
     line.append(s);
     i=1;
      }
     else line.append(","+s);
     }
   
    System.out.println(line);

        } // while

}
 
 }
