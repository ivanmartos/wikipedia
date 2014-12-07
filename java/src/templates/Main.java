package templates;

import java.util.HashMap;

import templates.retrieving.TemplateDumpExtractor;
import templates.storing.XMLCreator;
import templates.utils.ParsingType;
/**
 * 
 */

/**
 * @author Ivan Marts
 *
 */
public class Main {
	public static final boolean DEBUG = true;
	
	private static final void printHelp(){
		System.err.println("Usage: java -jar program.jar [-temp templates_file_name] [-st] [-f file_name] [-textConvert|-expCallback] [-dump dumpSourceFile|templatesTargetFile]");
		System.err.println("-temp templates_file_name File name from which tempaltes should be loaded");
		System.err.println("-st store templates after parsing");
		System.err.println("-f file_name dump file");
		System.err.println("-textConvert|-expCallback action type");
		System.err.println("-dump dumpSourceFile|templatesTargetFile load templates from dumpSourceFile (wiki dump) to templatesTargetFile");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		boolean storeTemplate = false;
		//ParsingType type = ParsingType.TextConverter;
		ParsingType type = ParsingType.ExpansionCallback;
		String templateFile = null;
		//String templateFile = "templateDefaultBackup.xml";
		//String templateFile = "templates.xml";
		//String templateFile = "templates_new.xml";
		String fileName;
		
		//fileName = "enwiki-latest-pages-articles.xml";
		fileName = "enwiki-latest-pages-articles1.xml";
		//fileName = "enwiki-latest-pages-articles1_origin.xml";
		
		if (args.length < 1) {
			printHelp();
			return;
		}
		
		int argsCounter = 0;
		while (argsCounter != (args.length)) {
			switch (args[argsCounter]) {
			case "-temp":
				templateFile = args[argsCounter + 1];
				argsCounter = argsCounter + 2;
				break;
			case "-st":
				storeTemplate = true;
				argsCounter++;
				break;
			case "-f":
				fileName = args[argsCounter + 1];
				argsCounter = argsCounter + 2;
				break;
			case "-expCallback": {
				type = ParsingType.ExpansionCallback;
				argsCounter++;
				break;
			}
			case "-textConvert": {
				type = ParsingType.TextConverter;
				argsCounter++;
				break;
			}
			case "-h": {
				printHelp();
				return;
			}
			case "-dump":{
				String templateDumpSource = args[argsCounter + 1];
				String templateStorageFile = args[argsCounter + 2];
				//templateStorageFile = "templates2.xml";
				HashMap<String, String> templateMap = new HashMap<String, String>();
				
				String templatesFile = templateDumpSource;
				
				//String templatesFile ="enwiki-latest-pages-articles.xml";
				
				templateMap = new TemplateDumpExtractor().extract(templatesFile, templateMap);
				XMLCreator.createXMLSAX(templateMap, templateStorageFile);
				return;
			}
			default: {
				System.err.println("Wrong switch, type -h for help");
				return;
			}
			}
		}

		
		MyParser parser = new MyParser();
		parser.run(fileName,templateFile,storeTemplate,type);
	}

}
