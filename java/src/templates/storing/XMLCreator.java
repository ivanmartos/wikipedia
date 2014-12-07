package templates.storing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import templates.utils.TemplatesMapRetriever;

/**
 * Class for storing templates HashMap into XML file
 * 
 * @author Ivan Martos - from provided sources
 */
public class XMLCreator {
	
	
	public static void createXMLSAX(TemplatesMapRetriever mapSource, String templateFile){
		HashMap<String, String> input = mapSource.getTemplatesMap();
		createXMLSAX(input, templateFile);
	}
	
	/**
	 * Method for storing map of templates to file
	 * @param input HashMap of Templates
	 * @param templateFile XML file where to store map
	 */
	public static void createXMLSAX(HashMap<String, String> input, String templateFile){
        try {
        	Set<String> keys = input.keySet();
        	XMLOutputFactory xof = XMLOutputFactory.newInstance();
        	XMLStreamWriter streamWriter = xof.createXMLStreamWriter(new FileOutputStream(templateFile), "UTF-8");
        	streamWriter.writeStartDocument("utf-8", "1.0");
        	streamWriter.writeStartElement("wikipedia");
        	
        	
        	int i = 0;
			for(String key : keys){
				i++;
				streamWriter.writeStartElement("template");
				streamWriter.writeAttribute("id", String.valueOf(i));
				
				streamWriter.writeStartElement("name");
				streamWriter.writeCharacters(key);
				streamWriter.writeEndElement();	
				
				streamWriter.writeStartElement("value");
				streamWriter.writeCharacters(input.get(key));
				streamWriter.writeEndElement();					
		       	streamWriter.writeEndElement();
			}
        	streamWriter.writeEndElement();
        	streamWriter.writeEndDocument();
	        streamWriter.flush();
	        streamWriter.close();
			
		} catch (XMLStreamException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
