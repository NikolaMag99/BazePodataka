package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.InsertRowDialog;
import dialogs.RemoveRowDialog;
import dialogs.UpdateRowDialog;


public class UpdateRowAction extends AbstractAction{
	
	private String tableName;
	private UpdateRowDialog updateRowDialog;
	
	public UpdateRowAction(String tableName) {
		this.tableName = tableName;
		putValue(NAME, "Update");
		putValue(SHORT_DESCRIPTION, "Update selected row");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateRowDialog  = new UpdateRowDialog(tableName);
		updateRowDialog.setVisible(true);
	}
}
