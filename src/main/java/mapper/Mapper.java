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

public class Mapper {
	
	public static final String SVG = "http://www.w3.org/2000/svg";
	
	public static void main(String... args) throws Exception {
		new Mapper().create(500);
		new Mapper().create(2000);
		//new Mapper().create(10_000);
	}
	
	public void create(int width) throws Exception {
		System.out.println("Creating output of size "+width);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		Document doc = domImpl.createDocument(SVG, "svg", null);
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);
		//ctx.setGraphicContextDefaults(new GraphicContextDefaults());
		//ctx.getGraphicContextDefaults().setStroke();
		
		SVGGraphics2D g = new SVGGraphics2D(ctx, false);
		
		//background
		BufferedImage img = ImageIO.read(new File("background.png"));
		
		//scale background
		img = Scalr.resize(img, Method.ULTRA_QUALITY, width, width);
		int height = img.getHeight();
		g.setSVGCanvasSize(new Dimension(width, img.getHeight()));
		doc.getDocumentElement().setAttribute("width", Integer.toString(width));
		doc.getDocumentElement().setAttribute("height", Integer.toString(height));
		g.drawImage(img, 0, 0, null);
		
		//draw landmasses
		System.out.println("\tdraw landmasses");
		Jackson.MAPPER
			.readValue(new File("coordinates.json"), Landmasses.class)
			.interpolate(width)
			.render(g, width, height);
		
		//store svg
		System.out.println("\tstore svg");
		File resFile = new File("../results/result_"+width+".svg");
		try(BufferedWriter writer = Files.newBufferedWriter(resFile.toPath(), StandardCharsets.UTF_8)) {
			g.stream(writer, false);
		}

		//render svg to output png
		/*
		System.out.println("\trender image");
		g.getRoot(doc.getDocumentElement());
        PNGTranscoder png = new PNGTranscoder();
        png.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)width);
        png.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)g.getSVGCanvasSize().getHeight());
        png.addTranscodingHint(SVGAbstractTranscoder.KEY_AOI, new Rectangle2D.Double(0, 0, width, height));
        TranscoderInput input = new TranscoderInput(resFile.toURL().toString());
        try(OutputStream ostream = Files.newOutputStream(new File("../results/result_"+width+".png").toPath())) {
	        TranscoderOutput output = new TranscoderOutput(ostream);
	        png.transcode(input, output);
        }
        */
        //render svg with inkscape
        System.out.println("\trender image with inkscape");
        new ProcessBuilder()
        	.command(
        		"C:\\Program Files\\WindowsApps\\25415Inkscape.Inkscape_0.92.3.0_x64__9waqn51p1ttv2\\inkscape.exe",
        		"-f",
        		resFile.getAbsolutePath(),
        		"-e",
        		new File("../results/result_"+width+".png").getAbsolutePath(),
        		"-w",
        		Integer.toString(width),
        		"-h",
        		Integer.toString(height)
        	)
        	.inheritIO()
        	.start()
        	.waitFor();
        
        System.out.println("done");
	}
}
