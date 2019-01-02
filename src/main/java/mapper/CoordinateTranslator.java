package mapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mapper.models.Jackson;
import mapper.models.description.Landmass;
import mapper.models.description.Landmass.Type;
import mapper.models.description.Landmasses;
import mapper.models.math.Mercator;
import mapper.models.raw.RawLandmass;
import mapper.models.raw.RawPoint;

public class CoordinateTranslator {

	public static void main(String... args) throws IOException {
		RawLandmass[] landmasses = Jackson.MAPPER.readerFor(RawLandmass[].class).readValue(new File("raw.json"));
		
		Landmasses l = new Landmasses();
		
		for(RawLandmass landmass : landmasses) {
			Landmass lm = new Landmass();
			lm.setType(Type.LAND);
			lm.setPoints(new ArrayList<>(landmass.getPoints().length));
			l.getLandmasses().add(lm);
			
			System.out.println(Arrays.stream(landmass.getPoints()).mapToLong(p->p.getY()).summaryStatistics());
			int c=0;
			int countOverflows=0;
			for(int i=0;i<landmass.getPoints().length;i++) {
				RawPoint p = landmass.getPoints()[i];
				if(p.getY() < 0)
					lm.setType(Type.ICE);
				
				if(i > 1) {
					RawPoint p2 = landmass.getPoints()[i-2];
					RawPoint p1 = p;
					RawPoint p0 = landmass.getPoints()[i-1];
					double distance = Math.abs((p2.getY()-p1.getY())*p0.getX()-(p2.getX()-p1.getX())*p0.getY()+p2.getX()*p1.getY()-p2.getY()*p1.getX())
							/ Math.sqrt(Math.pow(p2.getY()-p1.getY(),2)+Math.pow(p2.getX()-p1.getX(),2));
					if(distance < 50)
						c++;
				}
				
				//wrap around coordinates
				if(p.getX()<0 || p.getX() > 10_000_000)
					countOverflows++;
				p.setX(Math.max(Math.min(p.getX(), 10_000_000),0));
				p.setY(Math.max(Math.min(p.getY() + 1_000_000, 8_000_000),0));
				
				lm.getPoints().add(Mercator.fromPixels(p, 10_000_000));
			}
			System.out.println(lm+" "+c+"/"+lm.getPoints().size()+"\t"+countOverflows);
		}
		
		Jackson.MAPPER
			.writerWithDefaultPrettyPrinter()
			.writeValue(new File("coordinates.json"), l);
	}
}
