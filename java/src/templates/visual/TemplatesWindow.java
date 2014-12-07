package templates.visual;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import templates.retrieving.TemplateDumpExtractor;
import templates.storing.XMLCreator;

/**
 * Window for parsing templates from dump file
 * @author Ivan Martos
 *
 */
public class TemplatesWindow extends FileUsingDialog{
	/** Window frame */
	private JFrame frmTemplatesParsing;
	/** text field containing path to output templateFile */
	private JTextField templateTextField;
	/** text field containing path to source dump file */
	private JTextField dumpTextField;

	/**
	 * Create the application.
	 */
	public TemplatesWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplatesParsing = new JFrame();
		frmTemplatesParsing.setTitle("Templates Parsing");
		frmTemplatesParsing.setBounds(100, 100, 450, 300);
		frmTemplatesParsing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTemplatesParsing.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("Template file:");
		label.setBounds(10, 15, 76, 14);
		frmTemplatesParsing.getContentPane().add(label);
		
		templateTextField = new JTextField();
		templateTextField.setColumns(10);
		templateTextField.setBounds(96, 12, 229, 20);
		frmTemplatesParsing.getContentPane().add(templateTextField);
		
		JButton buttonChange = new JButton("Change");
		buttonChange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFile(frmTemplatesParsing, templateTextField);
			}
		});
		buttonChange.setBounds(335, 11, 89, 23);
		frmTemplatesParsing.getContentPane().add(buttonChange);
		
		JLabel label_1 = new JLabel("Template source dump:");
		label_1.setBounds(10, 55, 139, 14);
		frmTemplatesParsing.getContentPane().add(label_1);
		
		dumpTextField = new JTextField();
		dumpTextField.setColumns(10);
		dumpTextField.setBounds(159, 52, 166, 20);
		frmTemplatesParsing.getContentPane().add(dumpTextField);
		
		JButton buttonSet = new JButton("Set");
		buttonSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFile(frmTemplatesParsing, dumpTextField);
			}
		});
		buttonSet.setBounds(335, 51, 89, 23);
		frmTemplatesParsing.getContentPane().add(buttonSet);
		
		JButton btnParse = new JButton("Parse");
		btnParse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				parse();
			}
		});
		btnParse.setBounds(236, 228, 89, 23);
		frmTemplatesParsing.getContentPane().add(btnParse);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		btnCancel.setBounds(335, 228, 89, 23);
		frmTemplatesParsing.getContentPane().add(btnCancel);
	}
	
	private void parse(){
		
		String templateDumpSource = dumpTextField.getText();
		String templateStorageFile = templateTextField.getText();
		
		//templateStorageFile = "templates2.xml";
		
		if(templateDumpSource == null  || templateDumpSource.isEmpty() || templateStorageFile == null || templateStorageFile.isEmpty()){
			return;
		}
		else{
			//hash map of templates
			HashMap<String, String> templateMap = new HashMap<String, String>();
			
			String templatesFile = templateDumpSource;
			
			//String templatesFile ="enwiki-latest-pages-articles.xml";
			
			templateMap = new TemplateDumpExtractor().extract(templatesFile, templateMap);
			XMLCreator.createXMLSAX(templateMap, templateStorageFile);
		}
		setVisible(false);
	}
	
	/**
	 * Set visibility of this window
	 * @param visible
	 */
	public void setVisible(boolean visible){
		frmTemplatesParsing.setVisible(visible);
	}
}
