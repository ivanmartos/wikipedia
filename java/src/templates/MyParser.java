package templates;
import templates.utils.ParsingType;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

/**
 * Class for parsing execution
 * @author Ivan
 *
 */
public class MyParser {
	/** 
	 * Method parses input stored inside given file
	 * 
	 * Result is stored to Files  - one for each Article
	 * @param fileName - file to parse
	 */
	public void runBasic(String fileName) {
		String templatesSource = null;
		ParsingType parsingType = ParsingType.TextConverter;
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(fileName);
		MyPageCallbackHandler handler = null;

		try {
			wxsp.setPageCallback((handler = new MyPageCallbackHandler(templatesSource, parsingType, false)));
			wxsp.parse();
			handler.finished();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(String fileName, String templatesFileName, boolean storeTemplates, ParsingType parsingType){
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(fileName);
		MyPageCallbackHandler handler = null;

		try {
			wxsp.setPageCallback((handler = new MyPageCallbackHandler(templatesFileName, parsingType, storeTemplates)));
			wxsp.parse();
			handler.finished();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
