package gui.listeners.list;


import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Row;
import gui.Frame;
import model.Entity;

public class RowSelectionListener implements ListSelectionListener{
	
	private String tableName;
	private JTable jtb;
	
	public RowSelectionListener(String tableName,JTable jtb) {
		this.tableName=tableName;
		this.jtb=jtb;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int r=jtb.getSelectedRow();
		if(r!=-1) {
			Row q = new Row();
			int c = jtb.getColumnCount();
			for(int i=0;i<c;i++) {
				Object o = jtb.getValueAt(r, i);
				q.addField(jtb.getColumnName(i),o);
			}
			Entity ent = Frame.getInstance().getInformationResource().getEntity(tableName);
			List<Entity> relationsTo = ent.getRelationsTo();
			List<Entity> relationsFrom = ent.getRelationsFrom();
			Frame.getInstance().getRelView().clear();
			for(Entity et:relationsTo) {
				String secondTable = et.toString();
				List<Row> data=Frame.getInstance().getIDatabase().leftJoin(tableName, secondTable, q);
				/*System.out.println("right " + secondTable + " " + data.size());
				String pt="";
				for(String s:et.getColumns()) pt+=s+" ";
				System.out.println("\t" + pt);
				for(Row ro:data) {
					pt="";
					for(String s:et.getColumns()) pt+=ro.getObject(s)+" ";
					System.out.println("\t" + pt);
				}*/
				Frame.getInstance().getRelView().addTable(et, data);
			}
			for(Entity et:relationsFrom) {
				String secondTable = et.toString();
				List<Row> data=Frame.getInstance().getIDatabase().rightJoin(tableName, secondTable, q);
				Frame.getInstance().getRelView().addTable(et, data);
				/*System.out.println("left " + secondTable + " " + data.size());
				String pt="";
				for(String s:et.getColumns()) pt+=s+" ";
				System.out.println("\t" + pt);
				for(Row ro:data) {
					pt="";
					for(String s:et.getColumns()) pt+=ro.getObject(s)+" ";
					System.out.println("\t" + pt);
				}*/
			}
			//System.out.println("Done");
			Frame.getInstance().getRelView().repaint();
		}
	}
}
