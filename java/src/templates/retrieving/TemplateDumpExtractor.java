package templates.retrieving;

import java.util.HashMap;
import java.util.logging.Logger;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;

import org.xml.sax.SAXException;

import templates.Main;

/**
 * Class for extracting templates from Wiki DUMP
 * 
 * @author Ivan
 */
public class TemplateDumpExtractor {
	private static final boolean DEBUG_TEMPLATE = true;
	private final static Logger log = Logger.getLogger(TemplateDumpExtractor.class.getName());

	private HashMap<String, String> templates;
	
	/**
	 * Interface implementation for extracting templates only
	 * @author Ivan
	 *
	 */
	class TemplateFilter implements IArticleFilter {
		@Override
		public void process(WikiArticle page, Siteinfo siteinfo)throws SAXException {
			if (page.isTemplate()) {
				if (templates.get(page.getTitle()) == null) {
					String title = page.getTitle();
					String text = page.getText();
					
					if(Main.DEBUG && DEBUG_TEMPLATE){
						log.info("Extracting template - " + title);
						log.info("Template value - " + text);
					}
					
					templates.put(page.getTitle(), text);
				}
			}
		}
	}
	
	/**
	 * Extract templates from dump
	 * @param filename dump storage
	 * @param templates retrieved templates
	 * @return hashMap of extracted templates - key is title, content is template
	 */
	public HashMap<String, String> extract(String filename, HashMap<String, String> templates) {
		this.templates = templates;
		if (filename == null || !filename.endsWith(".xml")) {
			return templates;
		}
		try {
			IArticleFilter handler = new TemplateFilter();
			WikiXMLParser wxp = new WikiXMLParser(filename, handler);
			wxp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.templates;
	}
}
