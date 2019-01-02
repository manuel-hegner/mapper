package mapper.models.math;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FractalLines {

	public static List<LatLong> interpolate(Random r, List<LatLong> points) {
		ArrayDeque<LatLong> open = new ArrayDeque<>(points);
		List<LatLong> result = new ArrayList<>();
		result.add(open.pop());
		LatLong a = result.get(0);
		
		while(!open.isEmpty()) {
			LatLong b = open.getFirst();
			
			double distance = a.distanceTo(b);
			if(distance > 1500) {
				double d = r.nextDouble()-0.5;
				LatLong c = new LatLong(
					(a.getLatitude()+b.getLatitude())/2		+ (a.getLongitude()-b.getLongitude())*d, 
					(a.getLongitude()+b.getLongitude())/2	+ (a.getLatitude()-b.getLatitude())*d
				);
				open.push(c);
			}
			else {
				a = b;
				result.add(open.pop());
			}
		}
		return result;
	}

}
