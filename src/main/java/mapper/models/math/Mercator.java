package mapper.models.math;

import mapper.models.raw.RawPoint;

public class Mercator {
	public static final double EARTH_CIRCUMFERENCE = 40000000;
	public static final double LATITUDE_MAX = 85.05112877980659;
	public static final double LATITUDE_MIN = -LATITUDE_MAX;
	
	public static LatLong fromPixels(RawPoint p, int mapSize) {
		return fromPixels(p.getX(), p.getY(), mapSize);
	}
	
	public static LatLong fromPixels(Point p, int mapSize) {
		return fromPixels(p.getX(), p.getY(), mapSize);
	}

	public static LatLong fromPixels(double x, double y, int mapSize) {
		return new LatLong(pixelYToLatitude(y, mapSize), pixelXToLongitude(x, mapSize));
	}

	public static double pixelXToLongitude(double x, int mapSize) {
		if (x < 0 || x > mapSize) {
			throw new IllegalArgumentException("invalid pixelX coordinate " + mapSize + ": " + x);
		}
		return 360 * ((x / mapSize) - 0.5);
	}

	public static double pixelYToLatitude(double pixelY, int mapSize) {
		if (pixelY < 0 || pixelY > mapSize) {
			throw new IllegalArgumentException("invalid pixelY coordinate " + mapSize + ": " + pixelY);
		}
		double y = 0.5 - (pixelY / mapSize);
		return 90 - 360 * Math.atan(Math.exp(-y * (2 * Math.PI))) / Math.PI;
	}

	public static Point getPixel(LatLong latLong, int mapSize) {
		double pixelX = longitudeToPixelX(latLong.getLongitude(), mapSize);
		double pixelY = latitudeToPixelY(latLong.getLatitude(), mapSize);
		return new Point(pixelX, pixelY);
	}

	public static double longitudeToPixelX(double longitude, long mapSize) {
		return (longitude + 180) / 360 * mapSize;
	}

	public static double latitudeToPixelY(double latitude, long mapSize) {
		double sinLatitude = Math.sin(latitude * (Math.PI / 180));
		// FIXME improve this formula so that it works correctly without the clipping
		double pixelY = (0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI)) * mapSize;
		return Math.min(Math.max(0, pixelY), mapSize);
	}
}
