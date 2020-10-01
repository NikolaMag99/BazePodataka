package gui.listeners.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import gui.Frame;
import model.Entity;

public class EntitySelectionListener implements MouseListener{

	public void mouseClicked(MouseEvent arg0) {

		if (arg0.getClickCount() == 2) {
			Object node = Frame.getInstance().getTree().getLastSelectedPathComponent();

			if(node != null) {
				if(node instanceof Entity) {
					Entity ent = (Entity)node;
					ent.select();
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
