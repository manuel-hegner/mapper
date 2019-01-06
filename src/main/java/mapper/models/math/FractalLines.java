package mapper.models.math;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mapper.models.description.Landmass;

public class FractalLines {

	public static List<LatLong> interpolate(Landmass.Type type, Random r, List<LatLong> points, boolean closed, int width) {
		ArrayDeque<LatLong> open = new ArrayDeque<>(points);
		//for interpolation between first and last point
		if(closed)
			open.add(open.getFirst());
		List<LatLong> result = new ArrayList<>();
		result.add(open.pop());
		LatLong a = result.get(0);
		
		while(!open.isEmpty()) {
			LatLong b = open.getFirst();
			
			double distance = a.distanceTo(b);
			if(distance > 10_000) {
				LatLong c = fractal(type, r, a, b, width);
				open.push(c);
			}
			else {
				a = b;
				result.add(open.pop());
			}
		}
		//if closed remove the last point again
		if(closed)
			result.remove(result.size()-1);
		return result;
	}

	private static LatLong fractal(Landmass.Type type, Random r, LatLong a, LatLong b, int width) {
		double d = type.getFractalStrength()*(r.nextDouble()-0.5);
		double aY = Mercator.latitudeToPixelY(a.getLatitude(), width);
		double aX = Mercator.longitudeToPixelX(a.getLongitude(), width);
		double bY = Mercator.latitudeToPixelY(b.getLatitude(), width);
		double bX = Mercator.longitudeToPixelX(b.getLongitude(), width);
		
		double ma = r.nextDouble()*0.5+0.25;
		double mb = 1-ma;
		
		return new LatLong(
			Mercator.pixelYToLatitude(Cap.cap(ma*aY+mb*bY - (aX-bX)*d, width), width), 
			Mercator.pixelXToLongitude(Cap.cap(ma*aX+mb*bX + (aY-bY)*d, width), width)
		);
	}

}
