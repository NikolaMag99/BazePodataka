package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.InsertRowDialog;
import dialogs.RemoveRowDialog;


public class RemoveRowAction extends AbstractAction{
	
	private String tableName;
	private RemoveRowDialog removeRowDialog;
	
	public RemoveRowAction(String tableName) {
		this.tableName = tableName;
		putValue(NAME, "Remove");
		putValue(SHORT_DESCRIPTION, "Remove selected row");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		removeRowDialog  = new RemoveRowDialog(tableName);
		removeRowDialog.setVisible(true);
	}
}
