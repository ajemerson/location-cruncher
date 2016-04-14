import java.text.*;
import java.util.Date;

public class InteractionHandler {
	double PHYSICAL_DISTANCE_LIMIT = 0.1;	//in km
	long TIME_DIFFERENCE_LIMIT = 120;		//in seconds
	
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
	public double haversine(double lat1, double long1, double lat2, double long2){
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
}