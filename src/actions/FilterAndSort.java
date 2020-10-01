package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.FilterSortRowDialog;
import dialogs.InsertRowDialog;
import dialogs.RemoveRowDialog;
import dialogs.UpdateRowDialog;
import gui.TableView;


public class FilterAndSort extends AbstractAction{
	
	private TableView tv;
	private FilterSortRowDialog filterSortRowDialog;
	
	public FilterAndSort(TableView tv) {
		this.tv = tv;
		putValue(NAME, "Filter & Sort");
		putValue(SHORT_DESCRIPTION, "Filter & sort selected table");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		filterSortRowDialog = new FilterSortRowDialog(tv);
		filterSortRowDialog.setVisible(true);
	}
}
