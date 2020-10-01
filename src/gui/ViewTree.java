package gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


public class ViewTree extends JTree {

	public ViewTree() {
		super();
		addMouseListener(new gui.listeners.mouse.EntitySelectionListener());
	}

}
