package templates.visual;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import templates.MyParser;
import templates.utils.ParsingType;

/**
 * Main application Window
 * @author Ivan Martos
 *
 */
public class MainWindow extends FileUsingDialog{
	/** frame of the window */
	private JFrame frmTemplateParser;
	/** text field for path to input file */
	private JTextField inputTextField;
	/** text field for path to templates file */
	private JTextField templateTextField;
	/** Radio button for bliki parsing method */
	private JRadioButton blikiRdbtn;
	/** Radio button for sweble parsing method */
	private JRadioButton swebleRdbtn;
	/** Log text pane */
	private JTextPane logTextPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmTemplateParser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplateParser = new JFrame();
		frmTemplateParser.setResizable(false);
		frmTemplateParser.setTitle("Template parser");
		frmTemplateParser.setBounds(100, 100, 450, 379);
		frmTemplateParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTemplateParser.getContentPane().setLayout(null);
		
		JLabel lblInputFile = new JLabel("Input file :");
		lblInputFile.setBounds(10, 11, 76, 14);
		frmTemplateParser.getContentPane().add(lblInputFile);
		
		inputTextField = new JTextField();
		inputTextField.setBounds(96, 8, 229, 20);
		frmTemplateParser.getContentPane().add(inputTextField);
		inputTextField.setColumns(10);
		
		JButton inputBtnChange = new JButton("Change");
		inputBtnChange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFile(frmTemplateParser, inputTextField);
			}
		});
		inputBtnChange.setBounds(335, 7, 89, 23);
		frmTemplateParser.getContentPane().add(inputBtnChange);
		
		JLabel lblTemplateFile = new JLabel("Template file:");
		lblTemplateFile.setBounds(10, 36, 76, 14);
		frmTemplateParser.getContentPane().add(lblTemplateFile);
		
		templateTextField = new JTextField();
		templateTextField.setBounds(96, 33, 229, 20);
		frmTemplateParser.getContentPane().add(templateTextField);
		templateTextField.setColumns(10);
		
		JButton templateBtnChange = new JButton("Change");
		templateBtnChange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFile(frmTemplateParser, templateTextField);
			}
		});
		templateBtnChange.setBounds(335, 32, 89, 23);
		frmTemplateParser.getContentPane().add(templateBtnChange);
		
		JLabel lblParsingMethod = new JLabel("Parsing method:");
		lblParsingMethod.setBounds(10, 120, 121, 14);
		frmTemplateParser.getContentPane().add(lblParsingMethod);
		
		swebleRdbtn = new JRadioButton("sweble");
		swebleRdbtn.setBounds(137, 116, 94, 23);
		frmTemplateParser.getContentPane().add(swebleRdbtn);
		
		blikiRdbtn = new JRadioButton("bliki");
		blikiRdbtn.setSelected(true);
		blikiRdbtn.setBounds(233, 116, 109, 23);
		frmTemplateParser.getContentPane().add(blikiRdbtn);
		
		JButton ParseBtn = new JButton("Parse");
		ParseBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				parse();
			}
		});
		ParseBtn.setBounds(10, 146, 414, 23);
		frmTemplateParser.getContentPane().add(ParseBtn);
		
		logTextPane = new JTextPane();
		logTextPane.setBounds(10, 178, 414, 152);
		frmTemplateParser.getContentPane().add(logTextPane);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(swebleRdbtn);
	    group.add(blikiRdbtn);
	    
	    JMenuBar menuBar = new JMenuBar();
	    frmTemplateParser.setJMenuBar(menuBar);
	    
	    JMenu mnParseTemplates = new JMenu("Tools");
	    menuBar.add(mnParseTemplates);
	    
	    JMenuItem mntmParseTemplates = new JMenuItem("Parse templates");
	    mntmParseTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startMenuParsingDialog();
			}
		});
	    mnParseTemplates.add(mntmParseTemplates);
	}
	
	/**
	 * Start parsing
	 */
	private void parse(){
		String inputFileName = inputTextField.getText();
		String templateFileName = templateTextField.getText();
		
		ParsingType parsingType = getParsingType();
		
		if(inputFileName == null || inputFileName.isEmpty()){
			addLogText("Missing input file name!");
			return;
		}
		else{
			addLogText("Input file name - " + inputFileName);
		}
		
		boolean storeTemplates;
		
		if(templateFileName == null || templateFileName.isEmpty()){
			addLogText("Missing template file name. Templates won't be loaded and saved.");
			storeTemplates = false;
		}
		else{
			addLogText("Template file name - " + templateFileName);
			storeTemplates = true;
		}
		
		
		addLogText("Starting to parse input");
		MyParser parser = new MyParser();
		parser.run(inputFileName,templateFileName,storeTemplates,parsingType);
		addLogText("Parsing stopped");
		
	}
	
	/** 
	 * Get {@link ParsingType} from radio buttons group
	 * @return method of parsing
	 */
	private ParsingType getParsingType(){
		if(blikiRdbtn.isSelected()){
			return ParsingType.TextConverter;
		}
		else{
			return ParsingType.ExpansionCallback;
		}
	}
	
	/**
	 * Append text to logTextPane
	 * @param text - log to append
	 */
	private void addLogText(String text){
		logTextPane.setText(logTextPane.getText() + "\n" + text);
	}
	
	/**
	 * Open dialog for template parsing
	 */
	private void startMenuParsingDialog(){
		TemplatesWindow window = new TemplatesWindow();
		window.setVisible(true);
	}
}
