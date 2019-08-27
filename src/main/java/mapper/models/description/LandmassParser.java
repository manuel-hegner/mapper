package mapper.models.description;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import mapper.models.description.Landmass.Type;
import mapper.models.math.LineMode;
import mapper.models.math.Mercator;

public class LandmassParser {
	
	public static enum ParseMode {
		m,M,L,v,V,h,H,l,z;
	}
	
	public static Landmasses from(Document doc, int width) {
		Landmasses result = new Landmasses();
		NodeList nodes = doc.getElementsByTagName("path");
		for(int i=0;i<nodes.getLength();i++) {
			if(nodes.item(i) instanceof Element) {
				Element e = (Element) nodes.item(i);
				if(e.hasAttribute("type")) {
					Landmass l = new Landmass();
					l.setType(Type.valueOf(e.getAttribute("type")));
					l.setPoints(new ArrayList<>());
					result.getLandmasses().add(l);
					
					String[] parts = e.getAttribute("d").split(" ");
					ParseMode mode = null;
					
					double x = 0;
					double y = 0;
					
					for(String p:parts) {
						if(p.length()==1 && Character.isLetter(p.charAt(0)))
							mode = ParseMode.valueOf(p);
						else {
							switch(mode) {
								case v: {
									y += Double.parseDouble(p);
									break;
								}
								case V: {
									y = Double.parseDouble(p);
									break;
								}
								case h: {
									x += Double.parseDouble(p);
									break;
								}
								case H: {
									x = Double.parseDouble(p);
									break;
								}
								case m:
								case l: {
									String[] sub = p.split(",");
									x += Double.parseDouble(sub[0]);
									y += Double.parseDouble(sub[1]);
									break;
								}
								case M:
								case L: {
									String[] sub = p.split(",");
									x = Double.parseDouble(sub[0]);
									y = Double.parseDouble(sub[1]);
									break;
								}
								default:
								case z: {
									throw new IllegalStateException();
								}
							}
							
							l.getPoints().add(Mercator.fromPixels(LineMode.F, Math.min(x,width), y, width));
						}
					}
				}
			}
		}
		return result;
	}
}
