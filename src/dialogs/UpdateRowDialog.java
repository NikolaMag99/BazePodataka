package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Row;
import enums.AttributeType;
import gui.Frame;
import model.Attribute;
import model.Entity;


public class UpdateRowDialog extends JDialog {
	
	private String tableName;
	private JButton update;
	private JButton discard;
	private JPanel southPanel;
	private JPanel centerPanel;
	
	public UpdateRowDialog(String tableName) {
		this.setPreferredSize(new Dimension(500,300));
		this.tableName=tableName;
		initialize();
	}
	
	public void initialize() {
		update = new JButton("Update");
		discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((UpdateRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(2*n,2));
		ArrayList<JTextArea> ol = new ArrayList<JTextArea>();
		ArrayList<JTextArea> nl = new ArrayList<JTextArea>();
		for(int i=0;i<n;i++) {
			String s = ((Attribute)ent.getChildAt(i)).toString();
			JLabel ojl = new JLabel("old " + s + ": ");
			JTextArea ojtb = new JTextArea();
			JLabel njl = new JLabel("new " + s + ": ");
			JTextArea njtb = new JTextArea();
			centerPanel.add(ojl);
			centerPanel.add(njl);
			centerPanel.add(ojtb);
			centerPanel.add(njtb);
			ol.add(ojtb);
			nl.add(njtb);
		}
		southPanel.add(update);
		southPanel.add(discard);
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(400,80+2*n*15));
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Row p = new Row();
					Row q = new Row();
					for(int i=0;i<n;i++) {
						Attribute at = (Attribute)ent.getChildAt(i);
						String s = at.toString();
						if(ol.get(i).getText().equals("")) continue;
						if(at.getAttributeType()==AttributeType.NUMERIC) {
							Double d = null;
							if(!ol.get(i).getText().equals("")) d=Double.valueOf(ol.get(i).getText());
							p.addField(s, d);
						}
						else p.addField(s, ol.get(i).getText());
					}
					for(int i=0;i<n;i++) {
						Attribute at = (Attribute)ent.getChildAt(i);
						String s = at.toString();
						if(nl.get(i).getText().equals("")) continue;
						if(at.getAttributeType()==AttributeType.NUMERIC) {
							Double d = null;
							if(!nl.get(i).getText().equals("")) d=Double.valueOf(nl.get(i).getText());
							q.addField(s, d);
						}
						else q.addField(s, nl.get(i).getText());
					}
					Frame.getInstance().getIDatabase().update(tableName,p , q);
					Frame.getInstance().refreshData();
					ent.tableModified();
					((UpdateRowDialog)(update.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
				
				}
				catch(Exception er) {
					Frame.getInstance().getIExceptionHandler().handleException(er.getMessage());
				}
			}
		});
		this.setTitle("UPDATE " + tableName);
	}
}
