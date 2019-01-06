package mapper.models.math;

public class Cap {
	public static double cap(double value, double width) {
		return Math.min(Math.max(value, 0), width);
	}
}
