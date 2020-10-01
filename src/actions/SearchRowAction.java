package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.InsertRowDialog;
import dialogs.RemoveRowDialog;
import dialogs.SearchRowDialog;
import dialogs.UpdateRowDialog;
import gui.TableView;


public class SearchRowAction extends AbstractAction{
	
	private TableView tv;
	private SearchRowDialog searchRowDialog;
	
	public SearchRowAction(TableView tv) {
		this.tv=tv;
		putValue(NAME, "Search");
		putValue(SHORT_DESCRIPTION, "Search table");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		searchRowDialog  = new SearchRowDialog(tv);
		searchRowDialog.setVisible(true);
	}
}
