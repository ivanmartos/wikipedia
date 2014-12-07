package templates;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.HtmlPrinter;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.LinkTargetException;

import templates.storing.XMLCreator;
import templates.utils.ParsingFinishedHandler;
import templates.utils.ParsingType;
import templates.utils.TemplatesMapRetriever;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;



/**
 * Class for parsing wiki pages
 * @author Ivan
 *
 */
public class MyPageCallbackHandler implements PageCallbackHandler, ParsingFinishedHandler{
	private final static Logger log = Logger.getLogger(MyPageCallbackHandler.class.getName());
	
	private SimpleWikiConfiguration config;
	private Compiler compiler;
	/** max number of columns inside result file, if not only templates are exported */
	private final int wrapCol;
	private TextConverter textConverter;
	private MyExpansionCallback expansionCallback;
	private ParsingType parsingType;
	private boolean storeTemplate;
	private String templatesFile;
	
	public MyPageCallbackHandler(String templatesFile, ParsingType parsingType, boolean storeTemplate) throws FileNotFoundException, JAXBException{
		// Set-up a simple wiki configuration
		try {
			config = new SimpleWikiConfiguration("classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Instantiate a compiler for wiki pages
		compiler = new Compiler(config);
		wrapCol = 80;
	
		//instantiate converter depending on parsing type
		switch(parsingType){
		case ExpansionCallback:
			expansionCallback = new MyExpansionCallback(templatesFile);
			break;
		case TextConverter:
			textConverter = new TextConverter(config, wrapCol, templatesFile);
			break;
		}
		this.parsingType = parsingType;
		this.storeTemplate = storeTemplate;
		this.templatesFile = templatesFile;
	}
	
	/**
	 * Method that is called for parsing each {@link WikiPage} (Article) separately.
	 */
	@Override
	public void process(WikiPage page) {
		//parsed wikimedia text from dump
		String wikiText = page.getWikiText();
		//article title
		String title = page.getTitle();
		title = title.trim();

		try {
			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(config, title);

			PageId pageId = new PageId(pageTitle, -1);

			CompiledPage cp = null;
			String parsedContent = null;
			
			// Compile the retrieved page and convert to String
			switch(parsingType){
			case ExpansionCallback:
				cp = compiler.postprocess(pageId, wikiText, expansionCallback);
				
				StringWriter writer = new StringWriter();
				
				HtmlPrinter htmlPrinter = new HtmlPrinter(writer, pageTitle.getFullTitle());
				htmlPrinter.setCssResource("/org/sweble/wikitext/engine/utils/HtmlPrinter.css", "");
				htmlPrinter.setStandaloneHtml(true, "");
				htmlPrinter.go(cp.getPage()); 
				
				parsedContent = writer.toString();
				break;
			case TextConverter:
				cp = compiler.postprocess(pageId, wikiText, null);
				parsedContent = (String) textConverter.go(cp.getPage());
				break;
			}

			//write parsed result to File
			FileUtils.writeStringToFile(new File("ParsedResult/" + title + ".text"), parsedContent);
		} catch (IOException | LinkTargetException | CompilerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void finished() {
		if(storeTemplate){
			if(Main.DEBUG) log.info("Storing parsed template");
			TemplatesMapRetriever retriever = null;
			switch(parsingType){
			case ExpansionCallback:
				retriever = expansionCallback;
				break;
			case TextConverter:
				retriever = textConverter;
				break;
			}
			
			if(templatesFile == null){
				templatesFile = "templateDefaultBackup.xml";
			}
			
			XMLCreator.createXMLSAX(retriever, templatesFile);
		}
	}

}
