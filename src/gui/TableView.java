package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import actions.AddRowAction;
import actions.FilterAndSort;
import actions.RemoveRowAction;
import actions.ReportRowAction;
import actions.SearchRowAction;
import actions.UpdateRowAction;
import data.Row;
import gui.listeners.list.RowSelectionListener;
import model.Entity;
import model.table.OpenTableModel;
import observer.interfaces.IOListener;

public class TableView extends JPanel implements IOListener  {
	
	private Entity ent;
	
	private JTable jtb;
	
	private JScrollPane jsp;
	
	private OpenTableModel otm;
	
	private JButton aButton, uButton, dButton;
	
	private JButton fButton, sButton, rButton;
	
	private JPanel southPanel,northPanel;
	
	private boolean mainViewTableView;
	
	public TableView(Entity ent,boolean mainViewTableView) {
		this.ent=ent;
		otm=new OpenTableModel(ent);
		this.mainViewTableView=mainViewTableView;
		initialise();
	}
	
	public TableView(Entity ent,List<Row> data,boolean mainViewTableView) {
		this.ent=ent;
		otm=new OpenTableModel(ent);
		List<String> columns = ent.getColumns();
		otm.setData(data, columns);
		this.mainViewTableView=mainViewTableView;
		initialise();
		this.repaint();
	}
	
	public Entity getEntity() {
		return ent;
	}
	
	public void refreshData() {
		this.remove(jsp);
		this.remove(southPanel);
		this.remove(northPanel);
		otm=new OpenTableModel(ent);
		initialise();
		this.repaint();
	}
	
	public void initialise() {
		setLayout(new BorderLayout());
		aButton = new JButton("Add");
		uButton = new JButton("Update");
		dButton = new JButton("Delete");
		fButton = new JButton("Filter & Sort");
		sButton = new JButton("Search");
		rButton = new JButton("Report");
		jtb = new JTable(otm);
		jtb.setPreferredScrollableViewportSize(new Dimension(700, 150));
		if(mainViewTableView) jtb.getSelectionModel().addListSelectionListener(new RowSelectionListener(ent.toString(),jtb));
		jsp = new JScrollPane(jtb);
		southPanel = new JPanel();
		add(jsp,BorderLayout.CENTER);
		aButton.addActionListener(new AddRowAction(ent.toString()));
		dButton.addActionListener(new RemoveRowAction(ent.toString()));
		uButton.addActionListener(new UpdateRowAction(ent.toString()));
		fButton.addActionListener(new FilterAndSort(this));
		sButton.addActionListener(new SearchRowAction(this));
		rButton.addActionListener(new ReportRowAction(this));
		northPanel = new JPanel();
		northPanel.add(fButton);
		northPanel.add(sButton);
		northPanel.add(rButton);
		southPanel.add(aButton);
		southPanel.add(uButton);
		southPanel.add(dButton);
		add(northPanel,BorderLayout.NORTH);
		add(southPanel,BorderLayout.SOUTH);
	}
	
	public void queryData(List<String> columns,List<Row> data) {
		this.remove(jsp);
		this.remove(southPanel);
		this.remove(northPanel);
		otm=new OpenTableModel(ent);
		otm.setData(data,columns);
		initialise();
		this.repaint();
		this.getParent().repaint();
	}

	@Override
	public void update(Object event) {
		this.refreshData();
	}
}
