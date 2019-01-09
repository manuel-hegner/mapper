package mapper.models.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Point {
	private double x;
	private double y;
	
	@JsonCreator
	public Point(double[] coordinates) {
		if(coordinates.length != 2)
			throw new IllegalArgumentException();
		x = coordinates[0];
		y = coordinates[1];
	}
	
	@JsonValue
	public double[] toArray() {
		return new double[] {x, y};
	}
	
	public static long generateRandomSeed(Point a, Point b) {
		return Double.doubleToLongBits(a.y)
			+ Double.doubleToLongBits(a.x)
			+ Double.doubleToLongBits(b.y)
			+ Double.doubleToLongBits(b.x);
	}

	public double distanceTo2(Point b) {
		return (x-b.x)*(x-b.x)+(y-b.y)*(y-b.y);
	}
}
