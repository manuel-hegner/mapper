package mapper.models.description;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.svggen.DOMGroupManager;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.base.Strings;

import lombok.Getter;
import lombok.Setter;
import mapper.Mapper;
import mapper.models.description.Landmass.Type;
import mapper.models.math.FractalLines;
import mapper.models.math.LatLong;
import mapper.models.math.Mercator;
import mapper.models.math.Point;

@Getter @Setter
public class Landmasses {

	private List<Landmass> landmasses = new ArrayList<>();

	
	public Landmasses interpolate(int width) {
		Random master = new Random(17);
		for(Landmass lm : landmasses) {
			Random r = new Random(master.nextLong());
			lm.setPoints(FractalLines.interpolate(lm.getType(), r, lm.getPoints(), true, width));
		}
		return this;
	}
	
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
		        e.setAttribute("mapper_type", lm.getType().name());
		        container.appendChild(e);
			}
		}
		
		//soft-light-blend
		container = g.getDOMFactory().createElementNS(Mapper.SVG, "g");
        container.setAttribute("style", "mix-blend-mode:soft-light;opacity:0.8;");
		group.addElement(container, DOMGroupManager.FILL);
		
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
	
	public void toSVG(SVGGraphics2D g, int width, int height) {
		DOMGroupManager group = new DOMGroupManager(new GraphicContext(new AffineTransform()), g.getDOMTreeManager());
		
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
			e.setAttribute("fill", "black");
	        e.setAttribute("d", path.toString());
	        e.setAttribute("type", lm.getType().name());
	        group.addElement(e, DOMGroupManager.FILL);
		}
	}
}
