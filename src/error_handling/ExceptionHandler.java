package error_handling;

import javax.swing.JOptionPane;

public class ExceptionHandler implements IExceptionHandler {
	
	public void handleException(String s) {
		System.out.println(s);
		JOptionPane.showMessageDialog(gui.Frame.getInstance(), s, "Error!", JOptionPane.ERROR_MESSAGE);
	}
}
