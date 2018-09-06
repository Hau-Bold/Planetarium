package camera;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import client.AppRoot;

public class TrackballMouseListener implements MouseListener, MouseMotionListener {

	private boolean dragging;
	private double[] prevRay;
	private Component trackballComponent;
	private Camera camera;
	private AppRoot appRoot;

	public TrackballMouseListener(Component trackballComponent, Camera camera, AppRoot appRoot) {
		this.trackballComponent = trackballComponent;
		this.camera = camera;
		this.appRoot = appRoot;
	}

	public void mousePressed(MouseEvent e) {
		if (dragging) {
			return;
		}
		dragging = true;
		prevRay = mousePointToRay(e.getX(), e.getY());
		trackballComponent.addMouseMotionListener(this);
	}

	public void mouseReleased(MouseEvent e) {
		if (!dragging) {
			return;
		}
		dragging = false;
		trackballComponent.removeMouseMotionListener(this);
	}

	public void mouseDragged(MouseEvent e) {
		if (!dragging)
			return;
		double[] thisRay = mousePointToRay(e.getX(), e.getY());
		camera.applyTransvection(prevRay, thisRay);
		prevRay = thisRay;
		trackballComponent.repaint();
		appRoot.setViewChanged(Boolean.TRUE);
	}

	private double[] mousePointToRay(int x, int y) {
		double dx, dy, dz, norm;
		int centerX = trackballComponent.getWidth() / 2;
		int centerY = trackballComponent.getHeight() / 2;
		double scale = 0.8 * Math.min(centerX, centerY);
		dx = (x - centerX);
		dy = (centerY - y);
		norm = Math.sqrt(dx * dx + dy * dy);
		if (norm >= scale)
			dz = 0;
		else
			dz = Math.sqrt(scale * scale - dx * dx - dy * dy);
		double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
		return new double[] { dx / length, dy / length, dz / length };
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

}
