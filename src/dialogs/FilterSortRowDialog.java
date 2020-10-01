package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Row;
import gui.Frame;
import gui.TableView;
import model.Attribute;
import model.Entity;


public class FilterSortRowDialog extends JDialog {
	
	private TableView tv;
	private JButton filterSort;
	private JButton discard;
	private JPanel southPanel;
	private JPanel centerPanel;
	private String tableName;
	
	public FilterSortRowDialog(TableView tv) {
		this.setPreferredSize(new Dimension(500,300));
		this.tv=tv;
		initialize();
	}
	
	public void initialize() {
		filterSort = new JButton("Filter & Sort");
		discard = new JButton("Discard");
		tableName = tv.getEntity().toString();
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((FilterSortRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		Entity ent = tv.getEntity();
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(n,3));
		ArrayList<JCheckBox> inFilter = new ArrayList<JCheckBox>();
		ArrayList<JCheckBox> inSort = new ArrayList<JCheckBox>();
		ArrayList<JCheckBox> descOrder = new ArrayList<JCheckBox>();
		for(int i=0;i<n;i++) {
			String s = ((Attribute)ent.getChildAt(i)).toString();
			JCheckBox jcb1 = new JCheckBox("Show column " + s,true);
			JCheckBox jcb2 = new JCheckBox("Sort by column " + s,false);
			JCheckBox jcb3 = new JCheckBox("Descending order " + s,false);
			centerPanel.add(jcb1);
			centerPanel.add(jcb2);
			centerPanel.add(jcb3);
			inFilter.add(jcb1);
			inSort.add(jcb2);
			descOrder.add(jcb3);
		}
		southPanel.add(filterSort);
		southPanel.add(discard);
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(600,80+2*n*20));
		filterSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<String> aC = new ArrayList<String>();
					ArrayList<Boolean> filterList,sortList,descendingList;
					List<String> columns = new ArrayList<String>();;
					filterList = new ArrayList<Boolean>();
					sortList = new ArrayList<Boolean>();
					descendingList = new ArrayList<Boolean>();
					for(int i=0;i<n;i++) {
						aC.add(((Attribute)ent.getChildAt(i)).toString());
						filterList.add(inFilter.get(i).isSelected());
						sortList.add(inSort.get(i).isSelected());
						if(inFilter.get(i).isSelected()) columns.add(((Attribute)ent.getChildAt(i)).toString());
						descendingList.add(descOrder.get(i).isSelected());
					}
					List<Row> l = Frame.getInstance().getIDatabase().filterAndSort(tableName, aC,filterList,sortList,descendingList);
					tv.queryData(columns,l);
					
					((FilterSortRowDialog)(filterSort.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
				
				}
				catch(Exception er) {
					er.printStackTrace();
				}
			}
		});
		this.setTitle("FILTER & SORT " + tableName);
	}
}
