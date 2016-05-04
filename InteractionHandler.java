import java.io.FileWriter;
import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class InteractionHandler {
	double PHYSICAL_DISTANCE_LIMIT;	//in km, converted to m
	long TIME_DIFFERENCE_LIMIT;		//in ms, converted to s
	ArrayList<String> inputUsers = new ArrayList<String>();
	ArrayList<String> inputTimes = new ArrayList<String>();
	ArrayList<Double> inputLats = new ArrayList<Double>();
	ArrayList<Double> inputLongs = new ArrayList<Double>();
	ArrayList<Double> inputAltitudes = new ArrayList<Double>();
	static ArrayList<String> interactionsUser1 = new ArrayList<String>();
	static ArrayList<String> interactionsUser2 = new ArrayList<String>();
	static ArrayList<String> interactionsTime1 = new ArrayList<String>();
	static ArrayList<String> interactionsTime2 = new ArrayList<String>();
	static ArrayList<Double> interactionsAlt1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsAlt2 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLat1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLat2 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLong1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLong2 = new ArrayList<Double>();
	static ArrayList<Boolean> isInteractions = new ArrayList<Boolean>();
	String[] users = new String[] {"john dkljfs","john dkljfs","john dkljfs","john dkljfs",
			"john dkljfs","john dkljfs","john dkljfs","john dkljfs","john dkljfs","john dkljfs",
			"Brady Coye","Brady Coye","Brady Coye","Brady Coye","Brady Coye","Brady Coye",
			"Brady Coye","Brady Coye","Brady Coye","Brady Coye","Brady Coye","Brady Coye"};
	String[] times = new String[] {"20:02:20","20:02:33","20:02:37","20:02:40","20:02:43",
			"20:02:46","20:02:48","20:02:51","20:02:54","20:03:18","20:04:26","20:04:30",
			"20:01:11","19:59:46","19:59:22","19:58:45","20:02:49","20:01:52","20:01:55",
			"19:59:21","19:58:26","20:01:42"};
	Double[] lats = new Double[] {34.92450575813178,34.923121,34.92379,34.923759,34.925749,
			34.93081,34.924671,34.924799,34.923121,34.923121,34.924315,34.925749,34.925872,
			34.93081,34.923121,34.923121,34.92379,34.924315,34.925872,34.923759,34.925749,
			34.93081};
	Double[] longs = new Double[] {-82.43831584237742,-82.438259,-82.440137,-82.438817,
			-82.437723,-82.434483,-82.437758,-82.43709,-82.438259,-82.438259,-82.440787,
			-82.437723,-82.439411,-82.434483,-82.438259,-82.438259,-82.440137,-82.440787,
			-82.439411,-82.438817,-82.437723,-82.434483};
	Double[] alts = new Double[] {1.0,0.0,1.0,0.0,0.3,0.0,0.0,4.0,0.0,0.0,
			0.0,3.0,0.0,2.0,0.0,0.2,0.0,0.0,1.0,0.0,2.0,0.0};
	
	public InteractionHandler(double distance, long time) {
		PHYSICAL_DISTANCE_LIMIT = distance;
		TIME_DIFFERENCE_LIMIT = time;
	}
	
	/**
	 * Reads output JSON from Firebase and puts appropriate data
	 * into ArrayLists.
	 */
	public void readJSON() {
		inputUsers.addAll(Arrays.asList(users));
		inputTimes.addAll(Arrays.asList(times));
		inputLats.addAll(Arrays.asList(lats));
		inputLongs.addAll(Arrays.asList(longs));
		inputAltitudes.addAll(Arrays.asList(alts));
	}
	
	/**
	 * Determines where interactions for all input data are,
	 * and adds them to interaction ArrayLists.
	 * @throws ParseException
	 */
	public void buildInteractionArrays() throws ParseException {
		for (int i = 0; i < inputUsers.size(); i++) {
			for (int j = 0; j < inputUsers.size(); j++) {
				double distanceDifference = distanceApart(inputLats.get(i),inputLongs.get(i),
						inputLats.get(j),inputLongs.get(j));
				long timeDifference = timeDifference(inputTimes.get(i),inputTimes.get(j));
				if ((isInteraction(distanceDifference, timeDifference)) && i!=j) {
					interactionsUser1.add(inputUsers.get(i));
					interactionsUser2.add(inputUsers.get(j));
					interactionsTime1.add(inputTimes.get(i));
					interactionsTime2.add(inputTimes.get(j));
					interactionsAlt1.add(inputAltitudes.get(i));
					interactionsAlt2.add(inputAltitudes.get(j));
					interactionsLat1.add(inputLats.get(i));
					interactionsLat2.add(inputLats.get(j));
					interactionsLong1.add(inputLongs.get(i));
					interactionsLong2.add(inputLongs.get(j));
					isInteractions.add(true);
				}
			}
		}
	}
	
	/**
	 * Determines if the time difference and physical distance of
	 * two location logs are within a given range.
	 */
	public boolean isInteraction(double distance, long timeDifference) {
		boolean interaction = false;
		if ((distance < PHYSICAL_DISTANCE_LIMIT) && (timeDifference < TIME_DIFFERENCE_LIMIT)) {
			interaction = true;
		}
		return interaction;
	}
	
	/**
	 * Calculates the distance in km between two lat/long points
	 * using the haversine formula.
	 */
	public double distanceApart(double lat1, double long1, double lat2, double long2){
	    int earthRadius = 6371; 	// average in km
	    double differenceLat = Math.toRadians(lat2 - lat1);
	    double differenceLong = Math.toRadians(long2 - long1);
	    double a = Math.sin(differenceLat / 2) * Math.sin(differenceLat / 2) +
	       		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
	      		* Math.sin(differenceLong / 2) * Math.sin(differenceLong / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double d = earthRadius * c;
	    return d;
	}
	
	/**
	 *Determines Difference in time between 2 given times.
	 * @throws ParseException 
	 */
	public long timeDifference(String time1, String time2) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(time1);
		Date date2 = format.parse(time2);
		long timeDifference = date2.getTime() - date1.getTime();
		long diffSeconds = timeDifference / 1000 % 60;
		return diffSeconds;
	}
	
	public void createCSV() {
		String fileName = "C:\\Users\\Andrew\\Desktop"+"/interactions.csv";
		CSVFileWriter.writeCSVFile(fileName);
	}
	
	/**
	 * Assisting class to convert JSON processed data to CSV.
	 * @author Andrew
	 *
	 */
	private static class CSVFileWriter {
	    //Delimiters used in CSV file.
	    private static final String COMMA_DELIMITER = ",";
	    private static final String NEW_LINE_SEPARATOR = "\n";
	    //CSV file header
	    private static final String FILE_HEADER = 
	    		"user1,user2,time1,time2,alt1,alt2,lat1,lat2,long1,long2,interaction";
	    
	    public static void writeCSVFile(String fileName) {
	    	FileWriter fileWriter = null;
	    	
	    	try {
	    		fileWriter = new FileWriter(fileName);
	    		//Write the CSV file header
	    		fileWriter.append(FILE_HEADER.toString());
	    		//Add a new line separator after the header
	    		fileWriter.append(NEW_LINE_SEPARATOR);
	    		for (int i = 0; i < interactionsUser1.size(); i++) {
	    			fileWriter.append(interactionsUser1.get(i));
	    			fileWriter.append(COMMA_DELIMITER);
	    			fileWriter.append(interactionsUser2.get(i));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(interactionsTime1.get(i));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(interactionsTime2.get(i));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsAlt1.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsAlt2.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsLat1.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsLat2.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsLong1.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Double.toString(interactionsLong2.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Boolean.toString(isInteractions.get(i)));
		    		fileWriter.append(NEW_LINE_SEPARATOR);
	    		}
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
}