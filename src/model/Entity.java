package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import gui.Frame;
import observer.interfaces.IOListener;
import observer.interfaces.IObservable;


public class Entity implements TreeNode, IObservable {
	
	private String name;
	private boolean selected;
	private InformationResource ir;
	private ArrayList<Attribute> attributes;
	private ArrayList<Entity> relationsFrom;
	private ArrayList<Entity> relationsTo;
	private ArrayList<IOListener> al;

	public Entity(String name,InformationResource ir) {
		attributes = new ArrayList<Attribute>();
		al = new ArrayList<IOListener>();
		this.name=name;
		this.ir=ir;
		this.selected = false;
		relationsFrom = new ArrayList<Entity>();
		relationsTo = new ArrayList<Entity>();
	}
	
	public void addChild(Attribute att) {
		attributes.add(att);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if(attributes.isEmpty() || childIndex<0 || childIndex>=attributes.size()) return null;
		return attributes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return attributes.size();
	}

	@Override
	public TreeNode getParent() {
		return ir;
	}
	
	public Attribute getChild(String t) {
		for(int i=0;i<attributes.size();i++) {
			if(attributes.get(i).toString().equals(t)) return attributes.get(i);
		}
		return null;
	}
	
	public void select() {
		if(this.selected) return;
		Frame.getInstance().getMainView().addTable(this);
		this.selected=true;
	}

	@Override
	public int getIndex(TreeNode node) {
		if(node instanceof Attribute) {
			for(int i=0;i<attributes.size();i++) {
				if(attributes.get(i)==node) return i;
			}
		}
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void addRelationTo(Entity ent) {
		if(!relationsTo.contains(ent)) relationsTo.add(ent);
	}
	
	public void addRelationFrom(Entity ent) {
		if(!relationsFrom.contains(ent)) relationsFrom.add(ent);
	}
	
	public void focus() {
		Frame.getInstance().getRelView().clear();
		for(Entity r:relationsTo) Frame.getInstance().getRelView().addTable(r);
		for(Entity r:relationsFrom) Frame.getInstance().getRelView().addTable(r);
	}
	
	public List<Attribute> getPrimaryKey() {
		List<Attribute> ret = new ArrayList<Attribute>();
		for(Attribute a:attributes) {
			if(a.isPrimaryKey()) ret.add(a);
		}
		return ret;
	}
	
	public List<Entity> getRelationsTo() {
		return relationsTo;
	}
	
	public List<Entity> getRelationsFrom() {
		return relationsFrom;
	}
	
	public List<String> getColumns() {
		List<String> l = new ArrayList<String>();
		for(Attribute a:attributes) l.add(a.toString());
		return l;
	}
	
	public void tableModified() {
		this.notifyObserver(this);
	}
	
	@Override
	public void addObserver(IOListener listener) {
		al.add(listener);
		
	}

	@Override
	public void removeObserver(IOListener listener) {
		al.remove(listener);
		
	}

	@Override
	public void notifyObserver(Object event) {
		for(IOListener listener: al) {
			listener.update(event);
		}
	}
}
