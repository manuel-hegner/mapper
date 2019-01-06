package mapper;

import java.io.File;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import mapper.models.Jackson;
import mapper.models.description.LandmassParser;
import mapper.models.description.Landmasses;

public class SVG2Landmasses {
	
	public static final String SVG = "http://www.w3.org/2000/svg";
	
	public static void main(String... args) throws Exception {
		new SVG2Landmasses().create(5000);
	}
	
	public void create(int width) throws Exception {
		System.out.println("Creating output of size "+width);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
		Document doc = factory.createDocument(new File("landmasses_editable_"+width+".svg").toURI().toString());

		//draw landmasses
		Jackson.MAPPER
			.writerWithDefaultPrettyPrinter()
			.writeValue(new File("../results/coordinates_parsed.json"), LandmassParser.from(doc, width));

        System.out.println("done");
	}
}
