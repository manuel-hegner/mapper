package mapper.models.math;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import mapper.models.description.Landmass;

public class FractalLines {
	
	private final static double RESOLUTION = 0.002;
	

	public static List<LatLong> interpolate(Landmass.Type type, List<LatLong> points, boolean closed, int width) {
		double maxDistance = width*width*RESOLUTION*RESOLUTION;
		
		Random r = new Random();
		ArrayDeque<Point> open = new ArrayDeque<>(points.stream().map(ll->Mercator.getPixel(ll, width)).collect(Collectors.toList()));
		//for interpolation between first and last point
		if(closed)
			open.add(open.getFirst());
		List<Point> result = new ArrayList<>();
		result.add(open.pop());
		Point a = result.get(0);
		
		while(!open.isEmpty()) {
			Point b = open.getFirst();
			if(b.getLineMode() == LineMode.F) {
				double distance = a.distanceTo2(b);
				if(distance > maxDistance) {
					Point c = fractal(type, r, a, b, width);
					open.push(c);
					continue;
				}
			}
			a = b;
			result.add(open.pop());
		}
		//if closed remove the last point again
		if(closed)
			result.remove(result.size()-1);
		return result
			.stream()
			.map(p->new LatLong(
				p.getLineMode(),
				Mercator.pixelYToLatitude(Cap.cap(p.getY(), width), width), 
				Mercator.pixelXToLongitude(Cap.cap(p.getX(), width), width)
			))
			.collect(Collectors.toList());
	}

	private static Point fractal(Landmass.Type type, Random r, Point a, Point b, int width) {
		r.setSeed(Point.generateRandomSeed(a, b));
		
		double d = type.getFractalStrength()*(0.2*r.nextGaussian()*(r.nextBoolean()?-1:1));
		double aY = a.getY();
		double aX = a.getX();
		double bY = b.getY();
		double bX = b.getX();
		
		double ma = 0.5;//r.nextDouble()*0.5+0.25;
		double mb = 0.5;//1-ma;
		
		double noiseX = type.getFractalStrength()*0.1*(r.nextDouble()-0.5);
		double noiseY = type.getFractalStrength()*0.1*(r.nextDouble()-0.5);
		
		return new Point( 
			b.getLineMode(),
			ma*aX+mb*bX + (aY-bY)*d,// + noiseX,
			ma*aY+mb*bY - (aX-bX)*d// + noiseY
		);
	}

}
