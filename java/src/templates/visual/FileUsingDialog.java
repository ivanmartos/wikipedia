package templates.visual;

import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Abstract basic class containing utilities for working with paths for folders
 * @author Ivan Martos
 *
 */
public abstract class FileUsingDialog {
	/**
	 * Opens {@link FileDialog} and sets path to chosen file to given textField
	 * @param frame - frame of the application
	 * @param textField - field that will contain path to file
	 */
	protected void setFile(JFrame frame, JTextField textField){
		FileDialog fileDialog  = new FileDialog(frame, "Select file", FileDialog.LOAD);
		fileDialog.setDirectory("C:/");
		fileDialog.show();
		String selected = fileDialog.getFiles()[0].getAbsolutePath();
		textField.setText(selected);
	}
}
