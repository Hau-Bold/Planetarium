package camera;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * the class Camera.
 *
 */
//public class Camera {
//
//	
//
//}

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import cameraToEarth.ActionContextMenuMouseListener;
import client.AppRoot;
import universe.Point3D;

public class Camera {

	private Point3D up = new Point3D(0.0d, 1.0d, 0.0d);

	private double size_X = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private double size_Y = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

	private double eyex = 0;
	private double eyey = 0;
	private double eyez = 1;
	private double refx = 0;
	private double refy = 0;
	private double refz = 0;
	private double upx, upy = 0.5, upz;

	private boolean orthographic;
	private boolean preserveAspect = true;

	private TrackballMouseListener trackballMouseListener; // handles the mouse for the simulated trackball
	private Component trackballComponent; // if not-null, component where trackball is installed

	private GLU glu;
	private GL2 gl;

	private AppRoot appRoot;

	private float z_NEAR = 0.1f;
	private float z_FAR = 10.0f;

	public Camera(GLU glu, AppRoot appRoot, float radius_Of_Sun) {
		this.glu = glu;
		this.appRoot = appRoot;
	}

	public void setPosition(Point3D position) {
		eyex = position.getX();
		eyey = position.getY();
		eyez = position.getZ();
	}

	public Point3D getPosition() {
		return new Point3D(eyex, eyey, eyez);
	}

	public void setViewDirection(Point3D viewDirection) {
		this.refx = viewDirection.getX();
		this.refy = viewDirection.getY();
		this.refz = viewDirection.getZ();
	}

	public Point3D getViewDirection() {
		return new Point3D(refx, refy, refz);
	}

	public Point3D getUp() {
		return up;
	}

	/**
	 * Returns the view information -- the 9 parameters of lookAt(), in an array.
	 */
	public void getViewParameters() {
		System.out.println(eyex + ";" + eyey + ";" + eyez + ";" + refx + ";" + refy + ";" + refz + ";" + upx + ";" + upy
				+ ";" + upz);
	}

	public void apply(GL2 gl) {

		gl.glOrtho(-(int) (size_X) / 2.0, (int) (size_X) / 2.0, (int) (size_Y) / 2.0, -(int) (size_Y) / 2.0, 0.0, 1.0);
		gl.glViewport(0, 0, (int) (size_Y), (int) (size_X));

		/** Change back to projection matrix. */
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		/** first: */
		/**
		 * specifies the angle in degrees how much you will see
		 */
		glu.gluPerspective(100, (float) size_Y / (float) size_X, z_NEAR, z_FAR);
		// gl.glFrustum(-(int) (size_X) / 2.0, (int) (size_X) / 2.0, -(int) (size_Y) /
		// 2.0, (int) (size_Y) / 2.0, 0.0,
		// 1.0);

		/** Change back to model view matrix. */
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(eyex, eyey, eyez, refx, refy, refz, upx, upy, upz);

		// z-Buffer clearing
		// gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// Light computation is on
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT_MODEL_TWO_SIDE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glShadeModel(GL2.GL_SHADE_MODEL);
		gl.glPushMatrix();

		//
		float[] lightAmbient = { 1.0f, 1.0f, 0.0f, 1.0f };
		float[] LightDiffuse = { 1.0f, 1.0f, 0.0f, 1.0f };
		float[] LightSpecular = { 1.0f, 1.0f, 0.0f, 1.0f };
		//
		float[] LightPosition0 = { 0.0f, 0.0f, 0.0f, 0.0f };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LightPosition0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, LightDiffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, LightSpecular, 0);

		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_CONSTANT_ATTENUATION, 0.5f);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, 0.0f);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, 0.0f);

		gl.glPopMatrix();

	}

	/**
	 * Installs a simulated trackball for an OpenGL component, which must be the
	 * same component on which this camera is used. The user can rotate the view by
	 * dragging on the component. This will only work if the camera's apply() method
	 * is called at the start of the component's display function to set up the
	 * viewing and projection transformations. The camera words by modifying the
	 * camera's viewing parameters.
	 * 
	 * @param c
	 *            The OpenGL drawing surface where the trackball will be installed.
	 *            This is probably a GLJPanel or GLCanvas. Passing a null as the
	 *            parameter will remove the trackball from the component (if it has
	 *            been installed there).
	 * @param gl
	 * @param
	 */
	public void installTrackball(Component c, GL2 gl) {
		this.gl = gl;
		if (trackballComponent != null && trackballComponent != c) {
			trackballComponent.removeMouseListener(trackballMouseListener);
		}
		// if (trackballComponent == c) {
		// return;
		// }
		trackballComponent = c;
		if (trackballComponent == null) {
			return;
		}
		if (trackballMouseListener == null) {
			trackballMouseListener = new TrackballMouseListener(trackballComponent, this, appRoot);
		}
		trackballComponent.addMouseListener(trackballMouseListener);
	}

	private double norm(double[] v) {
		double norm2 = v[0] * v[0] + v[1] * v[1] + v[2] * v[2];
		if (Double.isNaN(norm2) || Double.isInfinite(norm2) || norm2 == 0)
			throw new NumberFormatException("Vector length zero, undefined, or infinite.");
		return Math.sqrt(norm2);
	}

	private void normalize(double[] v) {
		double norm = norm(v);
		v[0] /= norm;
		v[1] /= norm;
		v[2] /= norm;
	}

	public void applyTransvection(double[] e1, double[] e2) {
		// rotate vector e1 onto e2; must be 3D *UNIT* vectors.
		double[] zDirection = new double[] { eyex - refx, eyey - refy, eyez - refz };
		double viewDistance = norm(zDirection);
		normalize(zDirection);
		double[] yDirection = new double[] { upx, upy, upz };
		double upLength = norm(yDirection);
		double proj = yDirection[0] * zDirection[0] + yDirection[1] * zDirection[1] + yDirection[2] * zDirection[2];
		yDirection[0] = yDirection[0] - proj * zDirection[0];
		yDirection[1] = yDirection[1] - proj * zDirection[1];
		yDirection[2] = yDirection[2] - proj * zDirection[2];
		normalize(yDirection);
		double[] xDirection = new double[] { yDirection[1] * zDirection[2] - yDirection[2] * zDirection[1],
				yDirection[2] * zDirection[0] - yDirection[0] * zDirection[2],
				yDirection[0] * zDirection[1] - yDirection[1] * zDirection[0] };
		e1 = transformToViewCoords(e1, xDirection, yDirection, zDirection);
		e2 = transformToViewCoords(e2, xDirection, yDirection, zDirection);
		double[] e = new double[] { e1[0] + e2[0], e1[1] + e2[1], e1[2] + e2[2] };
		normalize(e);
		double[] temp = new double[3];
		reflectInAxis(e, zDirection, temp);
		reflectInAxis(e1, temp, zDirection);
		reflectInAxis(e, xDirection, temp);
		reflectInAxis(e1, temp, xDirection);
		reflectInAxis(e, yDirection, temp);
		reflectInAxis(e1, temp, yDirection);
		eyex = refx + viewDistance * zDirection[0];
		eyey = refy + viewDistance * zDirection[1];
		eyez = refz + viewDistance * zDirection[2];
		upx = upLength * yDirection[0];
		upy = upLength * yDirection[1];
		upz = upLength * yDirection[2];
	}

	private void reflectInAxis(double[] axis, double[] source, double[] destination) {
		double s = 2 * (axis[0] * source[0] + axis[1] * source[1] + axis[2] * source[2]);
		destination[0] = s * axis[0] - source[0];
		destination[1] = s * axis[1] - source[1];
		destination[2] = s * axis[2] - source[2];
	}

	private double[] transformToViewCoords(double[] v, double[] x, double[] y, double[] z) {
		double[] w = new double[3];
		w[0] = v[0] * x[0] + v[1] * y[0] + v[2] * z[0];
		w[1] = v[0] * x[1] + v[1] * y[1] + v[2] * z[1];
		w[2] = v[0] * x[2] + v[1] * y[2] + v[2] * z[2];
		return w;
	}

	public void reset() {
		eyex = 0;
		eyey = 0;
		eyez = 1.0;
		refx = 0.0;
		refy = 0.0;
		refz = 0.0;
		upx = 0;
		upy = 1;
		upz = 0;
	}

	public void deinstallArcBall() {
		MouseListener[] arMouseListeners = trackballComponent.getMouseListeners();
		MouseMotionListener[] arMouseMotionListeners = trackballComponent.getMouseMotionListeners();

		for (int i = 0; i < arMouseListeners.length; i++) {

			if (!(arMouseListeners[i] instanceof ActionContextMenuMouseListener)) {
				trackballComponent.removeMouseListener(arMouseListeners[i]);
			}
		}

		for (int i = 0; i < arMouseMotionListeners.length; i++) {
			trackballComponent.removeMouseMotionListener(arMouseMotionListeners[i]);
		}

		this.trackballMouseListener = null;
	}

	public void setZ_NEAR(float z_NEAR) {
		this.z_NEAR = z_NEAR;
	}

	public void setZ_FAR(float z_FAR) {
		this.z_FAR = z_FAR;
	}

}
