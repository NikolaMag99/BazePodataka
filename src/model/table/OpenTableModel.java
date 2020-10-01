/**
 * 
 */
package model.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import data.Row;
import gui.Frame;
import model.Attribute;
import model.Entity;


public class OpenTableModel implements TableModel {

	private List<String> columns;
	
	private List<Row> data;

	public OpenTableModel(Entity ent) {
		String name = ent.toString();
		data = Frame.getInstance().getIDatabase().readDataFromTable(name);
		int n=ent.getChildCount();
		columns = new ArrayList<String>();
		for(int i=0;i<n;i++) {
			columns.add(((Attribute)ent.getChildAt(i)).toString());
		}
	}
	
	public OpenTableModel(List<Row> data,List<String> columns) {
		this.data = new ArrayList<Row>();
		this.columns = new ArrayList<String>();
		for(Row r:data) this.data.add(r);
		for(String s:columns) this.columns.add(s);
	}
	
	public void setData(List<Row> data,List<String> columns) {
		this.data = new ArrayList<Row>();
		this.columns = new ArrayList<String>();
		for(Row r:data) this.data.add(r);
		for(String s:columns) this.columns.add(s);
	}

	@Override
	public int getRowCount() {
		if(data==null) return 0;
		return data.size();
	}


	@Override
	public int getColumnCount() {
		if(columns==null) return 0;
		return columns.size();
	}


	@Override
	public String getColumnName(int columnIndex) {
		if(data==null || columns==null) return null;
		return columns.get(columnIndex);
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(data==null || columns==null) return columns.getClass();
		int i=0;
		while(i<getRowCount() && data.get(i).getObject(getColumnName(columnIndex))==null) i++;
		if(i<getRowCount()) return data.get(i).getObject(getColumnName(columnIndex)).getClass();
		return columns.getClass();
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(data==null || columns==null) return null;
		return data.get(rowIndex).getObject(getColumnName(columnIndex));
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(data==null || columns==null) return;
		data.get(rowIndex).removeField((getColumnName(columnIndex)));
		data.get(rowIndex).addField((getColumnName(columnIndex)),aValue);
	}


	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}

}
