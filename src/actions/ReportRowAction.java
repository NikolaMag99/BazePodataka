package actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dialogs.InsertRowDialog;
import dialogs.RemoveRowDialog;
import dialogs.ReportRowDialog;
import dialogs.SearchRowDialog;
import dialogs.UpdateRowDialog;
import gui.TableView;


public class ReportRowAction extends AbstractAction{
	
	private TableView tv;
	private ReportRowDialog reportRowDialog;
	
	public ReportRowAction(TableView tv) {
		this.tv=tv;
		putValue(NAME, "Report");
		putValue(SHORT_DESCRIPTION, "Reports of table");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		reportRowDialog  = new ReportRowDialog(tv);
		reportRowDialog.setVisible(true);
	}
}
