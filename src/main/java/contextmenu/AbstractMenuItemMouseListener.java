package contextmenu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class AbstractMenuItemMouseListener implements MouseListener {

	private JMenuItem iconMenuItem = null;
	private JPanel panel = null;

	public AbstractMenuItemMouseListener(JMenuItem iconMenuItem) {
		this.iconMenuItem = iconMenuItem;
	}

	public AbstractMenuItemMouseListener(JPanel panel) {
		this.panel = panel;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {

		if (iconMenuItem != null) {
			iconMenuItem.setBackground(Color.LIGHT_GRAY);
		}
		if (panel != null) {
			panel.setBackground(Color.LIGHT_GRAY);
		}

	}

	public void mouseExited(MouseEvent e) {
		if (iconMenuItem != null) {
			iconMenuItem.setBackground(Color.WHITE);
		}
		if (panel != null) {
			panel.setBackground(Color.WHITE);
		}

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
