package templates;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import templates.retrieving.TemplateLoaderHandler;
import templates.retrieving.TemplatesLoadUtils;
import templates.utils.TemplatesMapRetriever;
import de.fau.cs.osr.ptk.common.ast.AstNode;

/**
 * Used for Sweable project.
 * 
 * Methods described by {@link ExpansionCallback} are called during Mediawiki text parsing for 
 * retrieving templates (their body)
 * @author Ivan
 *
 */
public class MyExpansionCallback implements ExpansionCallback, TemplatesMapRetriever{
	private final static Logger log = Logger.getLogger(MyExpansionCallback.class.getName());

	private HashMap<String, String> templateMap;
	
	
	public MyExpansionCallback(String templatesFile){
		this.templateMap = new HashMap<String, String>();
		if(templatesFile != null){
			templateMap = TemplatesLoadUtils.getTemplatesMap(templatesFile);
			if(templateMap == null){
				this.templateMap = new HashMap<String, String>();
			}
		}
	}
	
	/**
	 * Method is called when template body is required.
	 * 
	 * @param title - tile of the template
	 * @return template body
	 */
	@Override
	public FullPage retrieveWikitext(ExpansionFrame frame, PageTitle pageTitle) throws Exception {
		String title = pageTitle.getFullTitle();
		if(Main.DEBUG) log.info("Template requst for - " + title);
		String article = null;
		//If template with name is found (exclude "Template:")
		if(title.startsWith("Template:")&&(title.length()>9)){
			article = templateMap.get(title);
			if (article == null) {
				if(Main.DEBUG) log.info("Downloading new template");
				try{
					article = TemplateDownloader.download(title);
					//article = TemplateDownloader.download(pageTitle);
				}
				catch(Exception e){
					if(Main.DEBUG) log.warning("Error occured while downloading");
					article = null;
				}
				//article = DocumentUtils.getWikiMediaProcessedTemplate(title);
				if (article != null && article.length() != 0) {
					templateMap.put(title, article);
					if(Main.DEBUG) log.info("new template - " + article);
				}
				
			}
		}
		if(article == null){
			if(Main.DEBUG) log.warning("No article found, trying to get parsed content");
			article = TemplateDownloader.getWikiMediaProcessedTemplate("{{" + title + "}}");
			
			if (article != null && article.length() != 0) {
				templateMap.put(title, article);
				if(Main.DEBUG) log.info("new parsed template - " + article);
			}
			else{
				if(Main.DEBUG) log.warning("No article found, returning null");
				article = "<div class=\"hatnote relarticle mainarticle\">NULL</div>";
				//return null;
			}
		}
		
		PageId pageId = new PageId(pageTitle, 1);
		return new FullPage(pageId,  article);
	}

	public HashMap<String, String> getTemplatesMap(){
		return this.templateMap;
	}

}
