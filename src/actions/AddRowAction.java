package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.InsertRowDialog;


public class AddRowAction extends AbstractAction{
	
	private String tableName;
	private InsertRowDialog insertRowDialog;
	
	public AddRowAction(String tableName) {
		this.tableName = tableName;
		putValue(NAME, "Insert");
		putValue(SHORT_DESCRIPTION, "Insert new row");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		insertRowDialog  = new InsertRowDialog(tableName);
		insertRowDialog.setVisible(true);
	}
}
