package gui.listeners.change;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.TableView;
import model.Entity;

public class EntityViewsSelectionListener implements ChangeListener {
    
	private boolean mainView;
	
	public EntityViewsSelectionListener(boolean mainView) {
		this.mainView=mainView;
	}
	
	public void stateChanged(ChangeEvent e) {
        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        int n = tabbedPane.getComponentCount();
        if(n==0) return;
        int selectedIndex = tabbedPane.getSelectedIndex();
        if(mainView) {
        	Entity ent = ((TableView)tabbedPane.getComponentAt(selectedIndex)).getEntity();
        	ent.focus();
        	((TableView)tabbedPane.getComponentAt(selectedIndex)).refreshData();
        }
    }
}