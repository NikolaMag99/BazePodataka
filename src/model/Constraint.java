package model;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import enums.ConstraintType;
import observer.interfaces.IObservable;

public class Constraint implements TreeNode {
	
	private Attribute parrent;
	private ConstraintType constraintType;
	
	public Constraint(Attribute att,ConstraintType constraintType) {
		this.parrent=att;
		this.constraintType=constraintType;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parrent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return null;
	}
	
	@Override
	public String toString() {
		return constraintType.name();
	}
}
