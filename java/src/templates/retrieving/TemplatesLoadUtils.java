package templates.retrieving;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import templates.Main;

/**
 * Class from loading templates from file
 * @author Ivan Martos
 *
 */
public class TemplatesLoadUtils {
	private final static Logger log = Logger.getLogger(TemplatesLoadUtils.class.getName());
	
	/**
	 * Parse input file, retrieve templates stored inside file and return them as HashMap
	 * @param templatesFile - source of templates
	 * @return hashMap of tempaltes. Key - templateName. Value -templateBody
	 */
	public static HashMap<String, String> getTemplatesMap(String templatesFile){
		if(Main.DEBUG) log.info("Loading templates from - " + templatesFile);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			TemplateLoaderHandler templateHandler = new TemplateLoaderHandler();
			File xmlFile = new File(templatesFile);	
			if (!xmlFile.exists()) {
				log.warning("No templates file found - " + templatesFile);
				return null;
			}
			if(Main.DEBUG) log.info("Loading templates started");
			saxParser.parse(xmlFile, templateHandler);
			if(Main.DEBUG) log.info("Loading templates done");
			return templateHandler.getTemplatesMap();
			

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
