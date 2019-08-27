package mapper.models.math;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LatLong {

	private final static int R = 6371; // Radius of the earth
	private LineMode lineMode = LineMode.F;
	private double latitude;
	private double longitude;

	
	@JsonCreator
	public LatLong(Object[] coordinates) {
		if (coordinates.length == 3) {
			lineMode = LineMode.valueOf((String)coordinates[0]);
			latitude = (Double)coordinates[1];
			longitude = (Double)coordinates[2];
		}
		else {
			latitude = (Double)coordinates[0];
			longitude = (Double)coordinates[1];
		}
	}
	
	@JsonCreator
	public LatLong(double[] coordinates) {
		if (coordinates.length != 2)
			throw new IllegalArgumentException();
		latitude = coordinates[0];
		longitude = coordinates[1];
	}

	@JsonValue
	public Object[] toArray() {
		return new Object[] { lineMode.name(), latitude, longitude };
	}

	public double distanceTo(LatLong p2) {
		return distance(latitude, p2.latitude, longitude, p2.longitude);
	}
	
	public static double distance(double lat1, double lat2, double lon1, double lon2) {
		

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters
		return distance;
	}

	public static long generateRandomSeed(LatLong a, LatLong b) {
		return Double.doubleToLongBits(a.latitude)
			+ Double.doubleToLongBits(a.longitude)
			+ Double.doubleToLongBits(b.latitude)
			+ Double.doubleToLongBits(b.longitude);
	}
}
