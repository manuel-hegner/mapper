package mapper;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import mapper.models.Jackson;
import mapper.models.description.Landmasses;

public class Landmasses2SVG {
	
	public static final String SVG = "http://www.w3.org/2000/svg";
	
	public static void main(String... args) throws Exception {
		new Landmasses2SVG().create(5000);
	}
	
	public void create(int width) throws Exception {
		System.out.println("Creating output of size "+width);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		Document doc = domImpl.createDocument(SVG, "svg", null);
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);
		
		SVGGraphics2D g = new SVGGraphics2D(ctx, false);
		
		//draw landmasses
		System.out.println("\tdraw landmasses");
		Jackson.MAPPER
			.readValue(new File("coordinates.json"), Landmasses.class)
			.toSVG(g, width, width);
		
		//store svg
		System.out.println("\tstore svg");
		File resFile = new File("../results/landmasses_editable_"+width+".svg");
		try(BufferedWriter writer = Files.newBufferedWriter(resFile.toPath(), StandardCharsets.UTF_8)) {
			g.stream(writer, false);
		}
        
        System.out.println("done");
	}
}
