package cameraToEarth;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.AppRoot;
import contextmenu.ActionContextMenu;

public class ActionContextMenuMouseListener implements MouseListener {

	private AppRoot appRoot;

	/**
	 * Constructor.
	 * 
	 * @param appRoot
	 *            - the appRoot
	 */
	public ActionContextMenuMouseListener(AppRoot appRoot) {
		this.appRoot = appRoot;
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent event) {

		if (event.getButton() == MouseEvent.BUTTON3) {

			if (appRoot.getActionContextMenu() != null) {

				appRoot.getActionContextMenu().dispose();
			}

			appRoot.setActionContextMenu(new ActionContextMenu(event, appRoot));
		} else {
			if (appRoot.getActionContextMenu() != null) {
				appRoot.getActionContextMenu().dispose();
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
	}

}
