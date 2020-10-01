package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import data.Row;
import gui.listeners.change.EntityViewsSelectionListener;
import model.Entity;
import model.table.OpenTableModel;

public class TablesView extends JPanel {
	
	private JTabbedPane tables;
	
	private boolean mainView;
	
	public TablesView (boolean mainView) {
		this.mainView = mainView;
		tables = new JTabbedPane(JTabbedPane.TOP);
		tables.addChangeListener(new EntityViewsSelectionListener(mainView));
		add(tables);
	}
	
	public void addTable(Entity ent) {
		TableView tv = new TableView(ent,mainView);
		ent.addObserver(tv);
		for(Entity e:ent.getRelationsFrom()) e.addObserver(tv);
		tables.add(ent.toString(), tv);
		this.repaint();
	}
	
	public void addTable(Entity ent,List<Row> data) {
		TableView tv = new TableView(ent,data,mainView);
		ent.addObserver(tv);
		for(Entity e:ent.getRelationsFrom()) e.addObserver(tv);
		tables.add(ent.toString(), tv);
		this.repaint();
	}
	
	public void clear() {
		remove(tables);
		tables = new JTabbedPane(JTabbedPane.TOP);
		tables.addChangeListener(new EntityViewsSelectionListener(mainView));
		add(tables);
	}
	
	public void refreshData() {
		int n=tables.getComponentCount();
		if(n==0) return;
        int selectedIndex = tables.getSelectedIndex();
        ((TableView)tables.getComponent(selectedIndex)).refreshData();
		this.repaint();
	}
}
