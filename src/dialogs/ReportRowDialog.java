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


public class ReportRowDialog extends JDialog {
	
	private String tableName;
	private String function;
	private String column;
	private List<String> group;
	private TableView tv;
	private JPanel centerPanel;
	private JPanel southPanel;
	private ReportRowDialog jd;
	
	public ReportRowDialog(TableView tv) {
		this.setPreferredSize(new Dimension(500,300));
		this.tv=tv;
		this.tableName=tv.getEntity().toString();
		group = new ArrayList<String>();
		this.setTitle("REPORT IN " + tableName);
		jd=this;
		pickFunction();
	}
	
	public void pickFunction() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton pick = new JButton("Pick");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ReportRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		southPanel.add(pick);
		southPanel.add(discard);
		this.add(southPanel,BorderLayout.SOUTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		centerPanel = new JPanel(new GridLayout(1,2));
		JLabel jlb = new JLabel("Pick function for report: ");
		String options[] = {"AVG","COUNT"};
		JComboBox jcb = new JComboBox(options);
		jcb.setSelectedIndex(0);
		centerPanel.add(jlb);
		centerPanel.add(jcb);
		pick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int c = jcb.getSelectedIndex();
				function = options[c];
				jd.remove(centerPanel);
				jd.remove(southPanel);
				pickAttribute();
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.repaint();
		this.setSize(new Dimension(2000,1000));
		this.setMinimumSize(new Dimension(300,100));
		this.setSize(new Dimension(300,100));
	}
	
	public void pickAttribute() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton pick = new JButton("Pick");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ReportRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		southPanel.add(pick);
		southPanel.add(discard);
		int n = Frame.getInstance().getInformationResource().getEntity(tableName).getChildCount();
		String options [];
		int m=0;
		if(function.equals("COUNT")) {
			m=n;
			options = new String[m];
			for(int i=0;i<n;i++) options[i]=((Attribute)Frame.getInstance().getInformationResource().getEntity(tableName).getChildAt(i)).toString();
		}
		else {
			
			for(int i=0;i<n;i++) {
				Attribute at = (Attribute)Frame.getInstance().getInformationResource().getEntity(tableName).getChildAt(i);
				AttributeType atp = at.getAttributeType();
				if(atp == AttributeType.BIGINT || atp == AttributeType.INT || atp == AttributeType.SMALLINT || atp==AttributeType.NUMERIC || atp == AttributeType.FLOAT || atp == AttributeType.DECIMAL) {
					m++;
				}
			}
			options = new String[m];
			m=0;
			for(int i=0;i<n;i++) {
				Attribute at = (Attribute)Frame.getInstance().getInformationResource().getEntity(tableName).getChildAt(i);
				AttributeType atp = at.getAttributeType();
				if(atp == AttributeType.BIGINT || atp == AttributeType.INT || atp == AttributeType.SMALLINT || atp==AttributeType.NUMERIC || atp == AttributeType.FLOAT || atp == AttributeType.DECIMAL) {
					options[m]=at.toString();
					m++;
				}
			}
		}
		this.add(southPanel,BorderLayout.SOUTH);
		Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
		centerPanel = new JPanel(new GridLayout(1,2));
		JLabel jlb = new JLabel("Pick attribute for report: ");
		JComboBox jcb = new JComboBox(options);
		if(m!=0) jcb.setSelectedIndex(0);
		centerPanel.add(jlb);
		centerPanel.add(jcb);
		pick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int c = jcb.getSelectedIndex();
				if(c!=-1) {
					column = options[c];
					jd.remove(centerPanel);
					jd.remove(southPanel);
					pickGroup();
				}
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.repaint();
		this.setSize(new Dimension(2000,1000));
		this.setMinimumSize(new Dimension(300,100));
		this.setSize(new Dimension(300,100));
	}
	
	public void pickGroup() {
		this.repaint();
		JButton discard = new JButton("Discard");
		JButton pick = new JButton("Finish");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ReportRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);
			}
		});
		this.setLayout(new BorderLayout());
		southPanel = new JPanel();
		southPanel.add(pick);
		southPanel.add(discard);
		int n = Frame.getInstance().getInformationResource().getEntity(tableName).getChildCount();
		JCheckBox options [] = new JCheckBox[n];
		this.add(southPanel,BorderLayout.SOUTH);
		centerPanel = new JPanel(new GridLayout(n,1));
		for(int i=0;i<n;i++) {
			options[i]=new JCheckBox(((Attribute)Frame.getInstance().getInformationResource().getEntity(tableName).getChildAt(i)).toString());
			centerPanel.add(options[i]);
		}
		JLabel jlb = new JLabel("Pick group for report: ");
		this.add(jlb,BorderLayout.NORTH);
		pick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					for(int i=0;i<n;i++) {
						if(options[i].isSelected()) group.add(options[i].getText());
					}
					List<String> columns = new ArrayList<String>();
					//columns.add(function + "(" + column + ")");
					List<Row> l = Frame.getInstance().getIDatabase().report(tableName,function,column,group);
					for(String s: l.get(0).getColumns()) columns.add(s);
					tv.queryData(columns,l);
					((ReportRowDialog)(discard.getParent().getParent().getParent().getParent().getParent())).setVisible(false);		
				}
				catch(Exception er) {
					er.printStackTrace();
				}
			}
		});
		this.add(centerPanel,BorderLayout.CENTER);
		this.setSize(new Dimension(2000,1000));
		this.setMinimumSize(new Dimension(300,100+20*n));
		this.setSize(new Dimension(300,100+20*n));
		this.repaint();
	}
}
