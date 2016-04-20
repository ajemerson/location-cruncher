import java.io.FileWriter;
import java.io.IOException;

public class CSVFileWriter {
    //Delimiters used in CSV file.
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final String FILE_HEADER = 
    		"user1,user2,time1,time2,lat1,lat2,long1,long2,interaction";
    
    public static void writeCSVFile(String fileName) {
    	//Sample interaction data (NOT REAL DATA)
    	String user1 = "Brady Coye";
    	String user2 = "Andrew Emerson";
    	String time1 = "11:26:14";
    	String time2 = "11:25:59";
    	String lat1 = "34.9246";
    	String lat2 = "34.9246";
    	String long1 = "82.4391";
    	String long2 = "82.4391";
    	String interaction = "true";
    	
    	FileWriter fileWriter = null;
    	
    	try {
    		fileWriter = new FileWriter(fileName);
    		//Write the CSV file header
    		fileWriter.append(FILE_HEADER.toString());
    		//Add a new line separator after the header
    		fileWriter.append(NEW_LINE_SEPARATOR);
    		fileWriter.append(user1);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(user2);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(time1);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(time2);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(lat1);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(lat2);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(long1);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(long2);
    		fileWriter.append(COMMA_DELIMITER);
    		fileWriter.append(interaction);
    		fileWriter.append(NEW_LINE_SEPARATOR);
    		System.out.println("CSV file was created successfully !!!");
    	}
    	catch (Exception e) {
    		System.out.println("Error in CsvFileWriter !!!");
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			fileWriter.flush();
    			fileWriter.close();
    		} catch (IOException e) {
    			System.out.println("Error while flushing/closing fileWriter !!!");
    			e.printStackTrace();
    		}
    	}
    	
    }
}