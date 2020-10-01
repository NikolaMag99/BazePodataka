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


public class RemoveRowDialog extends JDialog {
	
	private String tableName;
	private JButton remove;
	private JButton discard;
	private JPanel southPanel;
	private JPanel centerPanel;
	
	public RemoveRowDialog(String tableName) {
		this.setPreferredSize(new Dimension(500,300));
		this.tableName=tableName;
		initialize();
	}
	
	public void initialize() {
		remove = new JButton("Remove");
		discard = new JButton("Discard");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((RemoveRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(2*n,1));
		ArrayList<JTextArea> al = new ArrayList<JTextArea>();
		for(int i=0;i<n;i++) {
			String s = ((Attribute)ent.getChildAt(i)).toString();
			JLabel jl = new JLabel(s+": ");
			JTextArea jtb = new JTextArea();
			centerPanel.add(jl);
			centerPanel.add(jtb);
			al.add(jtb);
		}
		southPanel.add(remove);
		southPanel.add(discard);
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(200,80+2*n*15));
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Row q = new Row();
					for(int i=0;i<n;i++) {
						Attribute at = (Attribute)ent.getChildAt(i);
						String s = at.toString();
						if(al.get(i).getText().equals("")) continue;
						if(at.getAttributeType()==AttributeType.NUMERIC) {
							Double d = null;
							if(!al.get(i).getText().equals("")) d=Double.valueOf(al.get(i).getText());
							q.addField(s, d);
						}
						else q.addField(s, al.get(i).getText());
					}
					Frame.getInstance().getIDatabase().delete(tableName, q);
					Frame.getInstance().refreshData();
					ent.tableModified();
					((RemoveRowDialog)(remove.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
				
				}
				catch(Exception er) {
					Frame.getInstance().getIExceptionHandler().handleException(er.getMessage());
				}
			}
		});
		this.setTitle("REMOVE FROM " + tableName);
	}
}
