package model;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class InformationResource implements TreeNode{
	
	private ArrayList<Entity> entities;
	private String name;
	
	public InformationResource(String name) {
		entities = new ArrayList<Entity>();
		this.name=name;
	}

	public void addChild(Entity ent) {
		entities.add(ent);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		if(entities.isEmpty() || childIndex<0 || childIndex>=entities.size()) return null;
		return entities.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return entities.size();
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		if(node instanceof Entity) {
			for(int i=0;i<entities.size();i++) {
				if(entities.get(i)==node) return i;
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
	
	public Entity getEntity(String name) {
		for(Entity ent: entities) {
			if(ent.toString().contentEquals(name)) return ent;
		}
		return null;
	}
}
