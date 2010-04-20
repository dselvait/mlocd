package data.dbworker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileWriter;
import java.io.BufferedWriter;

public final class DBWorker{
  
	public static  synchronized DBWorker getInstance(String dbdriver) throws Exception {		
		if(dbworker != null) {
			System.err.println("Mutilple connections to database is not allowed");
			return null;
		}
		
		try{			
			dbworker = new DBWorker();
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB		
			connector = DriverManager.getConnection(dbdriver);			
		  }			
			catch(java.sql.SQLException e) {
			 e.printStackTrace();
			 connector.close();
			 throw e;
			}  
		return dbworker;		
	} 
	
  public void makeQuery(String query)throws Exception {
     if(resultSet != null) resultSet.close();	 
	  statement = connector.createStatement();
	  resultSet = statement.executeQuery(query);
  }

  public void writeMetaData(ResultSet resultSet) throws SQLException {
		// 	Now get some metadata from the database
		// Result set get the result of the SQL query 		
		System.out.println("The columns in the table are: ");		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

  
  /*
  public String[] getResults() throws SQLException {
		// ResultSet is initially before the first data set
		String[] results = null;
		while (resultSet.next()) {
			// It is possible to get the columns via name or column number
		
		  }
		return results;
	}
*/
   public void close() {
			try {
				if (resultSet != null) {
					resultSet.close();
				}

				if (statement != null) {
					statement.close();
				}

				if (connector != null) {
					connector.close();
				}
			} catch (Exception e) {

			}
		}
  
 public void makeData(String filename){	 
	 try{
		   FileWriter fstream = new FileWriter(filename);
		   BufferedWriter out = new BufferedWriter(fstream);
		     
		   // Configure the parameters
		   int columns = resultSet.getMetaData().getColumnCount();
		   String column_value="";
		   int i=1;		   
		   while(resultSet.next()){ 
		     column_value="";
		     i=1; // The column value in resultSet starts index with 1 
		     while(i < columns){
		      column_value=resultSet.getString(i++);
			  if((column_value == null || column_value.equals(""))) column_value=" ";
		       out.append(column_value+",");
		    	 }
		     // Printing the last value without printing the comma
		     column_value=resultSet.getString(i);
		     if((column_value == null || column_value.equals(""))) column_value=" ";
		      out.append(column_value);
		      out.newLine(); 
		     }
		    //Close the output stream
		    out.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
 }
   
 private DBWorker(){ dbworker = this; } 
   
 public void finalize() {
	 close();
	 try {
		super.finalize();
	} catch (Throwable e) {
				e.printStackTrace();
	}	 
 } 
   
private static DBWorker dbworker = null;
private static Connection connector = null;
private Statement statement = null;
private PreparedStatement preparedStatement = null;
private ResultSet resultSet = null;

}