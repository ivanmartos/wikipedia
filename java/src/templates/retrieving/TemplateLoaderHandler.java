package templates.retrieving;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Custom DefaulHandler for parsing specific xml that stores parsed templates
 * @author Ivan
 *
 */
public class TemplateLoaderHandler extends DefaultHandler{
	private boolean name = false;
	private boolean value = false;
	private StringBuilder sbValue, sbName;
	
	private HashMap<String, String> templateMap;
	
	public TemplateLoaderHandler(){
		templateMap = new HashMap<String, String>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)	throws SAXException {

		if (qName.equalsIgnoreCase("name")) {
			sbName = new StringBuilder();
			name = true;
		}

		if (qName.equalsIgnoreCase("value")) {
			sbValue = new StringBuilder();
			value = true;
		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if (qName.equalsIgnoreCase("template")) {
			templateMap.put(sbName.toString(), sbValue.toString());
			if (qName.equalsIgnoreCase("value")) {
				value = false;
			}
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		if (name) {
			sbName.append(new String(ch, start, length));
			name = false;
		}

		if (value) {
			sbValue.append(new String(ch, start, length));
		}
	}
	
	public HashMap<String, String> getTemplatesMap(){
		return this.templateMap;
	}
}
