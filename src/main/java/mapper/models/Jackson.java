package mapper.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson {
	public static final ObjectMapper MAPPER;
	
	static {
		DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(new DefaultIndenter("\t", "\n"));
		
		MAPPER = new ObjectMapper()
			.setDefaultPrettyPrinter(printer)
			.enable(JsonParser.Feature.ALLOW_COMMENTS);
	}
}
