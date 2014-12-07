package templates;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sweble.wikitext.engine.PageTitle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Class for downloading templates from Internet
 * @author Ivan Martos -  inspired by provided resources
 *
 */
public class TemplateDownloader {
	private static final boolean DEBUG = true;
	
	/**
	 * Method for downloading template from internet
	 * @param templateName 
	 * @return Template body
	 */
	public static String download(String templateName){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;	
		Document doc;
		try {
			db = dbf.newDocumentBuilder();
			//doc = db.parse(new URL("http://en.wikipedia.org/w/api.php?action=query&titles="+URLEncoder.encode(templateName, "ISO-8859-1")+"&prop=revisions&rvprop=content&format=xml").openStream());
			doc = db.parse(new URL("http://en.wikipedia.org/w/api.php?action=query&continue=&titles="+URLEncoder.encode(templateName, "ISO-8859-1")+"&prop=revisions&rvprop=content&format=xml").openStream());
			return (doc.getDocumentElement().getTextContent().isEmpty() ? null : doc.getDocumentElement().getTextContent());
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	
	private static String URL_LINK = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=";
	
	/**
	 * 
	 * @param pageTitle
	 * @return
	 * @throws Exception
	 */
	public static String download(PageTitle pageTitle) throws Exception{
		String urlString = URL_LINK + URLEncoder.encode(pageTitle.getLinkString(),"UTF-8");
		if(DEBUG) System.out.println("URL - " + urlString);
		URL url = new URL(urlString);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(url.openStream());
		Node templateNode = doc.getElementsByTagName("rev").item(0);
		String templateText = templateNode.getTextContent();
		return templateText;
	}
	
	
	/**
	 * Method for downloading XML Document from internet
	 * 
	 * @param urlString URL of document
	 * @return downloaded XML document
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Document getDocument(String urlString) throws SAXException, IOException, ParserConfigurationException{
		URL url = new URL(urlString);
		//System.out.println("URL - " + urlString);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(url.openStream());
	}
	
	
	private static final String WIKIMEDIA = "http://en.wikipedia.org/w/api.php?action=expandtemplates&prop=wikitext&format=xml&text=";
	
	/**
	 * Method for downloading parsed template from URL
	 * 
	 * @param template - template to parse
	 * @return parsed template
	 */
	public static String getWikiMediaProcessedTemplate(String template){
		Node parsedNode = null;
		try {
			//encode template
			String parsedTemplate = URLEncoder.encode(template, "UTF-8");
			//Download Document containing template
			Document document = getDocument(WIKIMEDIA + parsedTemplate);
			
			//get required node
			parsedNode = document.getElementsByTagName("wikitext").item(0);
			
			return parsedNode.getTextContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsedNode.getTextContent();
		
	}
}
