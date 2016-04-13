import java.text.*;
import java.util.Date;

public class InteractionHandler {

	/**
	 * Calculates the distance in km between two lat/long points
	 * using the haversine formula.
	 */
	public double haversine(double lat1, double long1, double lat2, double long2){
	    int r = 6371; // average radius of the earth in km
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLong = Math.toRadians(long2 - long1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	       		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
	      		* Math.sin(dLong / 2) * Math.sin(dLong / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double d = r * c;
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