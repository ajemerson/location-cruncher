import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import org.json.*;


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
	static ArrayList<Long> interactionsTime1 = new ArrayList<Long>();
	static ArrayList<Long> interactionsTime2 = new ArrayList<Long>();
	static ArrayList<Double> interactionsAlt1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsAlt2 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLat1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLat2 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLong1 = new ArrayList<Double>();
	static ArrayList<Double> interactionsLong2 = new ArrayList<Double>();
	static ArrayList<Boolean> isInteractions = new ArrayList<Boolean>();
	
	public InteractionHandler(double distance, long time) {
		PHYSICAL_DISTANCE_LIMIT = distance;
		TIME_DIFFERENCE_LIMIT = time;
	}
	
	/**
	 * Reads output JSON from Firebase and puts appropriate data
	 * into ArrayLists.
	 */
	@SuppressWarnings("unchecked")
	public void readJSON() {
		File exportData = new File("exportedData.txt");
		Scanner dataScan = null;
		String str = "";
		try {
			dataScan = new Scanner(exportData);	//building JSON into String
			while (dataScan.hasNext()) {
				str += dataScan.nextLine();
			}
			System.out.println(str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		finally {dataScan.close();}
		
		String[] jsonData = str.split("[{},\"\\s+]+");
		System.out.println(Arrays.toString(jsonData));
		int counter = 1;
		while (jsonData[counter] != null) {
			if (jsonData[counter].equals("displayName")) {
				int firstNameCount = counter + 2;
				int lastNameCount = counter + 3;
				while (!jsonData[counter+1].equals("displayName")){
					if (jsonData[counter+1].contains("-")) {
						inputUsers.add(jsonData[firstNameCount]+" "+jsonData[lastNameCount]);
						System.out.println(inputUsers.toString());
						inputTimes.add(jsonData[counter+3]);
						System.out.println(inputTimes.toString());
						inputAltitudes.add(Double.parseDouble(jsonData[counter+10]));
						System.out.println(inputAltitudes.toString());
						inputLats.add(Double.parseDouble(jsonData[counter+13]));
						System.out.println(inputLats.toString());
						inputLongs.add(Double.parseDouble(jsonData[counter+16]));
						System.out.println(inputLongs.toString());
					}
					if ((counter+16) < jsonData.length) {
							counter += 17;
					}
					else {
						break;
					}
					System.out.println(counter);
				}
			}
			if ((counter+1)<jsonData.length){
				counter++;
			}
			else {
				break;
			}
		}
//		JSONObject obj = new JSONObject(str);
//		JSONArray users = obj.getJSONArray("users");
//		for (int i = 0; i < users.length(); i++) {
//			JSONObject locations = obj.getJSONObject("locations");
//			ArrayList<String> keys = (ArrayList<String>) locations.keys();
//			for (int j = 0; j < keys.size(); j++) {
//				inputUsers.add(users.getString(i));
//				String[] dateAndTime = keys.get(i).split(" ");
//				inputTimes.add(dateAndTime[1]);
//				JSONObject data = locations.getJSONObject(keys.get(j));
//				inputLats.add(Double.parseDouble(data.getString("latitude")));
//				inputLongs.add(Double.parseDouble(data.getString("longitude")));
//				inputAltitudes.add(Double.parseDouble(data.getString("altitude")));
//			}	
//		}
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
				if (isInteraction(distanceDifference, timeDifference)) {
					interactionsUser1.add(inputUsers.get(i));
					interactionsUser2.add(inputUsers.get(j));
					interactionsTime1.add(Long.parseLong(inputTimes.get(i)));
					interactionsTime2.add(Long.parseLong(inputTimes.get(j)));
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
		return timeDifference;
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
		    		fileWriter.append(Long.toString(interactionsTime1.get(i)));
		    		fileWriter.append(COMMA_DELIMITER);
		    		fileWriter.append(Long.toString(interactionsTime2.get(i)));
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