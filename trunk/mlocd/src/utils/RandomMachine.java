package utils;


public class RandomMachine {

	
	  public  int[] getRandomIntWithoutReduplicate( int min, int max, int size )  
	     {  
	         int[] result = new int[size];    
         int arraySize = max - min;  
	         int[] intArray = new int[arraySize];  
	         // init intArray  
	         for( int i = 0 ; i < intArray.length ; i++ ){  
	             intArray[i] = i + min;  
	         }  
	         // get randome interger without reduplicate  
	         for( int i = 0 ; i < size ; i++ )  {  
	             int c = getRandomInt( min, max - i );  
	             int index = c - min;  
	             swap( intArray, index, arraySize - 1 - i );  
	             result[i] = intArray[ arraySize - 1 - i ];  
	         }  
	                   
	         return result;  
	     }  
	      
	     private  void swap( int[] array, int x, int y ){  
	         int temp = array[x];  
	         array[x] = array[y];  
	         array[y] = temp;  
	     }  
	     
	      public  int getRandomInt( double min, double max )  {  
	          // include min, exclude max  
	          int result = (int)min + new Double( Math.random() * ( max - min ) ).intValue();  
	        
	          return result;  
	      }  
	  
	      public  double getRandomDouble( double min, double max )  {  
	          // include min, exclude max  
	          double result = (int)min + new Double( Math.random() * ( max - min ) ).doubleValue();  
	        
	          return result;  
	      }  
	      
	      public float getRandomFloat( double min, double max )  {  
	          // include min, exclude max  
	          float result = (int)min + new Double( Math.random() * ( max - min ) ).floatValue();  
	        
	          return result;  
	      }  
	      
	      public  String getRandomNormalString( int length ) {  
	               // include 0-9,a-z,A-Z  
	               StringBuffer result = new StringBuffer();  
	               for( int i = 0; i < length; i++) {  
	                 result.append( getRandomNormalChar() );  
	              }  
	               return result.toString();  
	      }  
	      
	      

      public  char getRandomNormalChar() {  
	             // include 0-9,a-z,A-Z  
	               int number = getRandomInt( 0, 62 );  
	               int zeroChar = 48;  
	               int nineChar = 57;  
	               int aChar = 97;  
	               int zChar = 122;  
	               int AChar = 65;  
	               int ZChar = 90;  
	                 
	               char result;  
	                 
	              if( number < 10 )  {  
	                  result =  ( char ) ( getRandomInt( zeroChar, nineChar + 1 ) );  
	                  return result;  
	               }  
	             else if( number >= 10 && number < 36 ) {  
	                   result = ( char ) ( getRandomInt( AChar, ZChar + 1 ) );  
	                   return result;  
	               }  
	             else if( number >= 36 && number < 62 )  {  
	                   result =  ( char ) ( getRandomInt( aChar, zChar + 1 ) );  
	                   return result;  
	               }  
	             else {  
	                 return 0;  
	               }  
	         }    
      
      public  char getRandomChar(char[] chars) {  
          // include 0-9,a-z,A-Z  
            int index = getRandomInt( 0, chars.length);  
            return chars[index];
      }    
      
}
