package dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import data.Row;
import enums.AttributeType;
import gui.Frame;
import gui.TableView;
import model.Attribute;
import model.Entity;


public class SearchRowDialog extends JDialog {
	
	private String tableName;
	private TableView tv;
	private String expression;
	private JPanel centerPanel;
	private JPanel southPanel;
	private List<Object> vals;
	private SearchRowDialog jd;
	
	public SearchRowDialog(TableView tv) {
		this.setPreferredSize(new Dimension(500,300));
		this.tv=tv;
		this.tableName=tv.getEntity().toString();
		vals = new ArrayList<Object>();
		expression = "";
		this.setTitle("SEARCH " + tableName);
		jd=this;
		pickAttribute();
	}
	
	public void pickAttribute() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton finish = new JButton("Pick");
		JLabel jlb = new JLabel("Current expression: " + expression);
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((SearchRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		southPanel = new JPanel();
		southPanel.add(finish);
		southPanel.add(discard);
		this.setLayout(new BorderLayout());
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(jlb,BorderLayout.NORTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(1,2));
		JLabel exp = new JLabel("Pick attribute:");
		String options[] = new String[n];
		for(int i=0;i<n;i++) {
			Attribute at = (Attribute)ent.getChildAt(i);
			options[i]=at.toString();
		}
		JComboBox jcb = new JComboBox(options);
		if(n>0) jcb.setSelectedIndex(0);
		centerPanel.add(exp);
		centerPanel.add(jcb);
		finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(jcb.getSelectedIndex()==-1) {
					((SearchRowDialog)(finish.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
					return;
				}
				Attribute at = (Attribute)ent.getChildAt(jcb.getSelectedIndex());
				AttributeType a = at.getAttributeType();
				//System.out.println(a.toString());
				
				if(a == AttributeType.VARCHAR || a == AttributeType.CHAR || a == AttributeType.NVARCHAR) {
					expression += at.toString();
					expression += " LIKE";
					jd.remove(southPanel);
					jd.remove(centerPanel);
					jd.remove(jlb);
					typeValue(true);
				}
				else {
					if(a == AttributeType.NUMERIC || a == AttributeType.INT || a == AttributeType.BIGINT || a == AttributeType.FLOAT) {
						expression += at.toString();
						jd.remove(southPanel);
						jd.remove(centerPanel);
						jd.remove(jlb);
						pickSign();
					}
					else {
						expression += at.toString();
						expression += " =";
						jd.remove(southPanel);
						jd.remove(centerPanel);
						jd.remove(jlb);
						typeValue(false);
					}
				}
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(300,100));
		this.setSize(new Dimension(3000,1000));
		this.setSize(new Dimension(600,150));
		this.repaint();
	}
	
	public void pickSign() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton finish = new JButton("Pick");
		JLabel jlb = new JLabel("Current expression: " + expression);
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((SearchRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		southPanel = new JPanel();
		southPanel.add(finish);
		southPanel.add(discard);
		this.setLayout(new BorderLayout());
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(jlb,BorderLayout.NORTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(1,2));
		JLabel exp = new JLabel("Pick sign:");
		String options[] = {"<","<=","=",">=",">"};
		JComboBox jcb = new JComboBox(options);
		if(n>0) jcb.setSelectedIndex(0);
		centerPanel.add(exp);
		centerPanel.add(jcb);
		finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(jcb.getSelectedIndex()==-1) {
					((SearchRowDialog)(finish.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
					return;
				}
				expression += " " + options[jcb.getSelectedIndex()];
				jd.remove(southPanel);
				jd.remove(centerPanel);
				jd.remove(jlb);
				typeValue(false);
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(300,100));
		this.setSize(new Dimension(3000,1000));
		this.setSize(new Dimension(600,150));
		this.repaint();
	}
	
	public void typeValue(boolean like) {
		this.repaint();
		JButton discard = new JButton("Discard");
		JLabel jlb = new JLabel("Current expression: " + expression);
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((SearchRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		southPanel.add(discard);
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(jlb,BorderLayout.NORTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		centerPanel = new JPanel(new GridLayout(1,2));
		JTextArea jta = new JTextArea();
		centerPanel.add(jta);
		JButton jb = new JButton("Finish");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if(like) expression += " \"?\"";
				expression += " ?";
				jd.remove(southPanel);
				jd.remove(centerPanel);
				jd.remove(jlb);
				vals.add(jta.getText());
				pickOperator();
			}
		});
		centerPanel.add(jb);
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(300,80));
		this.setSize(new Dimension(3000,1000));
		this.setSize(new Dimension(600,120));
		this.repaint();
	}
	
	public void pickOperator() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton finish = new JButton("Pick");
		JLabel jlb = new JLabel("Current expression: " + expression);
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((SearchRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		southPanel = new JPanel();
		southPanel.add(finish);
		southPanel.add(discard);
		this.setLayout(new BorderLayout());
		this.add(southPanel,BorderLayout.SOUTH);
		this.add(jlb,BorderLayout.NORTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		int n = ent.getChildCount();
		centerPanel = new JPanel(new GridLayout(1,2));
		JLabel exp = new JLabel("Pick logical operator or finish the expression:");
		String options[] = {"AND","OR","Finish"};
		JComboBox jcb = new JComboBox(options);
		if(n>0) jcb.setSelectedIndex(0);
		centerPanel.add(exp);
		centerPanel.add(jcb);
		finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(options[jcb.getSelectedIndex()].equals("Finish")) {
					try {
						List<String> columns = Frame.getInstance().getInformationResource().getEntity(tableName).getColumns();
						List<Row> l = Frame.getInstance().getIDatabase().search(tableName,expression, vals);
						tv.queryData(columns,l);
						((SearchRowDialog)(finish.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
					}
					catch(Exception er) {
						Frame.getInstance().getIExceptionHandler().handleException(er.getMessage());
					}
				}
				else {
					expression += " " + options[jcb.getSelectedIndex()]+" ";
					jd.remove(southPanel);
					jd.remove(centerPanel);
					jd.remove(jlb);
					pickAttribute();
				}
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(400,150));
		this.setSize(new Dimension(3000,1000));
		this.setSize(new Dimension(600,150));
		this.repaint();
	}
}
