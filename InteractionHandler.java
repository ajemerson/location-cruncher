import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import org.json.*;


public class InteractionHandler {
	double PHYSICAL_DISTANCE_LIMIT;	//in km, not set in stone
	long TIME_DIFFERENCE_LIMIT;		//in seconds, not set in stone
	ArrayList<String> inputUsers = new ArrayList<String>();
	ArrayList<String> inputTimes = new ArrayList<String>();
	ArrayList<Double> inputLats = new ArrayList<Double>();
	ArrayList<Double> inputLongs = new ArrayList<Double>();
	ArrayList<Double> inputAltitudes = new ArrayList<Double>();
	ArrayList<String> interactionsUser1 = new ArrayList<String>();
	ArrayList<String> interactionsUser2 = new ArrayList<String>();
	ArrayList<Long> interactionsTime1 = new ArrayList<Long>();
	ArrayList<Long> interactionsTime2 = new ArrayList<Long>();
	ArrayList<Double> interactionsAlt1 = new ArrayList<Double>();
	ArrayList<Double> interactionsAlt2 = new ArrayList<Double>();
	ArrayList<Double> interactionsLat1 = new ArrayList<Double>();
	ArrayList<Double> interactionsLat2 = new ArrayList<Double>();
	ArrayList<Double> interactionsLong1 = new ArrayList<Double>();
	ArrayList<Double> interactionsLong2 = new ArrayList<Double>();
	ArrayList<Boolean> isInteractions = new ArrayList<Boolean>();
	
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
		Scanner scan = new Scanner("exportedData.json");	//building JSON into String
		String str = new String();
		while (scan.hasNext()) {
			str += scan.nextLine();
		}
		scan.close();
		
		JSONObject obj = new JSONObject(str);
		JSONArray users = obj.getJSONArray("displayName");
		for (int i = 0; i < users.length(); i++) {
			JSONObject locations = obj.getJSONObject("locations");
			ArrayList<String> keys = (ArrayList<String>) locations.keys();
			for (int j = 0; j < keys.size(); j++) {
				inputUsers.add(users.getString(i));
				String[] dateAndTime = keys.get(i).split(" ");
				inputTimes.add(dateAndTime[1]);
				JSONObject data = locations.getJSONObject(keys.get(j));
				inputLats.add(Double.parseDouble(data.getString("latitude")));
				inputLongs.add(Double.parseDouble(data.getString("longitude")));
				inputAltitudes.add(Double.parseDouble(data.getString("altitude")));
			}	
		}
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
}