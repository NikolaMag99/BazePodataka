package model;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import enums.AttributeType;
import enums.ConstraintType;

public class Attribute implements TreeNode {
	
	private String name;
	private Entity ent;
	private int size;
	private AttributeType atp;
	private ArrayList<Constraint> constraints;
	private Attribute from;

	public Attribute(String name,Entity ent,AttributeType atp,int size) {
		this.name=name;
		this.ent=ent;
		this.atp=atp;
		this.size=size;
		constraints = new ArrayList<Constraint>();
		this.from=null;
	}
	
	public void addChild(Constraint con) {
		constraints.add(con);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if(constraints.isEmpty() || childIndex<0 || childIndex>=constraints.size()) return null;
		return constraints.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return constraints.size();
	}

	@Override
	public TreeNode getParent() {
		return ent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if(node instanceof Constraint) {
			for(int i=0;i<constraints.size();i++) {
				if(constraints.get(i)==node) return i;
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
	
	public boolean isPrimaryKey() {
		for(Constraint c:constraints) {
			if(c.toString().equals(ConstraintType.PRIMARY_KEY.toString())) return true;
		}
		return false;
	}
	
	public Attribute getFrom() {
		return from;
	}
	
	public void setFrom(Attribute from) {
		this.from=from;
	}
	
	public AttributeType getAttributeType() {
		return atp;
	}
}
