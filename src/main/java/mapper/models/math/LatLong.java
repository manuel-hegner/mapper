package mapper.models.math;

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

	private double latitude;
	private double longitude;

	@JsonCreator
	public LatLong(double[] coordinates) {
		if (coordinates.length != 2)
			throw new IllegalArgumentException();
		latitude = coordinates[0];
		longitude = coordinates[1];
	}

	@JsonValue
	public double[] toArray() {
		return new double[] { latitude, longitude };
	}

	public double distanceTo(LatLong p2) {
		return distance(latitude, p2.latitude, longitude, p2.longitude);
	}
	
	public static double distance(double lat1, double lat2, double lon1, double lon2) {
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters
		return distance;
		
		/* if you want a height difference
		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);*/
	}
}
