package data.generator;

import java.io.*;
import java.util.Vector;

import utils.RandomMachine;

public class MakeFile {

	public static int remove(Vector<Integer> pToBeRemovedItems, String pSourceFile,
			String pOutputFile) throws IOException {

		            int fileLen = 0;
					PrintWriter myFile = new PrintWriter(new FileOutputStream(pOutputFile));
					BufferedReader bufferedReader = new BufferedReader(new FileReader(pSourceFile));
					
					try{
					String str;
					String[] strings;
					while ((str = bufferedReader.readLine()) != null) {
						fileLen++;
						strings = str.split(",");
						String result = new String();
						for (int i = 0; i < pToBeRemovedItems.size(); i++) {
							int position = ((Integer) (pToBeRemovedItems.get(i)))
									.intValue() - 1;

							strings[position] = "";
						}

						for (int j = 0; j < strings.length; j++) {
							if (strings[j] != "") {
								if (j != strings.length - 1)
									result = result + strings[j] + ",";
								else
									result = result + strings[j];
							}

						}
						/*
						 * if(pClasses.size() != 0) { for(int i=0;
						 * i<pClasses.size(); i++) { if(strings[strings.length
						 * -1 ] != pClasses.elementAt(i))
						 * pClasses.add(strings[strings.length - 1]); } } else
						 * pClasses.add(strings[strings.length - 1]);
						 */
						// System.out.println(result);
						myFile.println(result);

					}
					
				} finally{ 
					bufferedReader.close();
					//fileReader.close();
					myFile.close();
					//resultFile.close();
				}
		
	
		return fileLen;

	}

	public static void insert_num(String pRemovedFile, String pInsertedFile,
			Vector<String> pClasses, int[] bounds, int range)throws Exception {
			
		RandomMachine rig = new RandomMachine();
					// initialise a target file
		PrintWriter writer = new PrintWriter(new FileOutputStream(pInsertedFile));
		BufferedReader reader = new BufferedReader(new FileReader(pRemovedFile));

		try{
			while(reader.ready()){
				String line = reader.readLine();
				String[] values = line.split("\\,");
				String class_value = values[values.length-1];
				int index = pClasses.indexOf(class_value);
			    int new_value = rig.getRandomInt(bounds[index]-range, bounds[index]);
			    String new_line = new_value + "," + line;
			    writer.println(new_line);
			}
				
		}finally{
					writer.close();
			//		resultFile.close();
					reader.close();
			//		fileReader.close();
				}
	}

	public static void insert_char(String source, String newfile, Vector<String> pClasses, char[] chars) throws Exception{
		RandomMachine rig = new RandomMachine();
		// initialise a target file
		PrintWriter writer = new PrintWriter(new FileOutputStream(newfile));
		BufferedReader reader = new BufferedReader(new FileReader(source));
	 try{
		while(reader.ready()){
			String line = reader.readLine();
			String[] values = line.split(",");
			String class_value = values[values.length-1];
			int index = pClasses.indexOf(class_value);
			char new_value = chars[index];
			if( new_value == 'R'){
				char[] tmp = new char[chars.length];
				int k = 0;
				for(int i = 0; i < chars.length; ++i)
				  if(chars[i] != 'R')
					  tmp[k++] = chars[i];
				char[] cs = new char[k];
				System.arraycopy(tmp,0,cs,0,k);
				new_value = rig.getRandomChar(cs);
				}
			String new_line = new_value + ","  + line;
			writer.println(new_line);
		}
	 } finally{
		 reader.close();
		 writer.close();
	 }
	}
}