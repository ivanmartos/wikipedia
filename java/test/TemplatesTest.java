package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import templates.MyParser;

/**
 * Class for jUnit testing
 * @author Ivan
 */
public class TemplatesTest {
	
	/**
	 * Method for testing basic functionality - parsing templates.
	 * Test is performed by comparing stored result and new result using {@link MyParser}
	 * Result of parsing is stored inside File, so result Files are compared
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException{
		String file = "enwiki-latest-pages-articles1.xml";
		MyParser parser  = new MyParser();
		parser.runBasic(file);

		
		File fileExpected1 = new File("AccessibleComputing_cmp.text");
		File fileActual1 = new File("AccessibleComputing.text");
		
		File fileExpected2 = new File("Atomic theory_cmp.text");
		File fileActual2 = new File("Atomic theory.text");
		
		
		Assert.assertEquals("The files differ!", 
			    FileUtils.readFileToString(fileExpected1, "utf-8"), 
			    FileUtils.readFileToString(fileActual1, "utf-8"));
		Assert.assertEquals("The files differ!", 
			    FileUtils.readFileToString(fileExpected2, "utf-8"), 
			    FileUtils.readFileToString(fileActual2, "utf-8"));
		
	}
}
