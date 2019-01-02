package mapper.models.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class LatLong {

	private double latitude;
	private double longitude;
	
	@JsonCreator
	public LatLong(double[] coordinates) {
		if(coordinates.length != 2)
			throw new IllegalArgumentException();
		latitude = coordinates[0];
		longitude = coordinates[1];
	}
	
	@JsonValue
	public double[] toArray() {
		return new double[] {latitude, longitude};
	}
}
