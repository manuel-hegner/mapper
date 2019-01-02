package mapper.models.description;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.svggen.DOMGroupManager;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Element;

import lombok.Getter;
import lombok.Setter;
import mapper.Mapper;
import mapper.models.description.Landmass.Type;
import mapper.models.math.LatLong;
import mapper.models.math.Mercator;
import mapper.models.math.Point;

@Getter @Setter
public class Landmasses {

	private List<Landmass> landmasses = new ArrayList<>();

	public void render(SVGGraphics2D g, int width, int height) {
		DOMGroupManager group = new DOMGroupManager(new GraphicContext(new AffineTransform()), g.getDOMTreeManager());
		
		//hue of ice
		Element container = g.getDOMFactory().createElementNS(Mapper.SVG, "g");
        container.setAttribute("style", "mix-blend-mode:hard-light;opacity:0.2;");
		group.addElement(container, DOMGroupManager.FILL);
		
		for(Landmass lm : landmasses) {
			if(lm.getType() == Type.ICE) {
				StringBuilder path = new StringBuilder();
				Iterator<LatLong> it = lm.getPoints().iterator();
				Point start = Mercator.getPixel(it.next(), width);
				path.append("M "+start.getX()+" "+start.getY());
				
				while(it.hasNext()) {
					Point p = Mercator.getPixel(it.next(), width);
					path.append(" L "+p.getX()+" "+p.getY());
				}
				path.append(" Z");
				Element e = g.getDOMFactory().createElementNS(Mapper.SVG, "path");
				e.setAttribute("fill", lm.getType().getCSSColor());
		        e.setAttribute("d", path.toString());
		        container.appendChild(e);
			}
		}
		
		//soft-light-blend
		container = g.getDOMFactory().createElementNS(Mapper.SVG, "g");
        container.setAttribute("style", "mix-blend-mode:soft-light;opacity:0.8;");
		group.addElement(container, DOMGroupManager.FILL);
		
		Element bg = g.getDOMFactory().createElementNS(Mapper.SVG, "rect");
		bg.setAttribute("fill", "7f7f7f");
		bg.setAttribute("x", "0");
		bg.setAttribute("y", "0");
		bg.setAttribute("width", Integer.toString(width));
		bg.setAttribute("height",Integer.toString(height));
		
		for(Landmass lm : landmasses) {
			StringBuilder path = new StringBuilder();
			Iterator<LatLong> it = lm.getPoints().iterator();
			Point start = Mercator.getPixel(it.next(), width);
			path.append("M "+start.getX()+" "+start.getY());
			
			while(it.hasNext()) {
				Point p = Mercator.getPixel(it.next(), width);
				path.append(" L "+p.getX()+" "+p.getY());
			}
			path.append(" Z");
			Element e = g.getDOMFactory().createElementNS(Mapper.SVG, "path");
			e.setAttribute("fill", lm.getType().getCSSColor());
	        e.setAttribute("d", path.toString());
	        container.appendChild(e);
		}
	}
}
