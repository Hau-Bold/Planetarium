package universe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/** the class Ellipse */
public class Ellipse {

	private float minorAxis;
	private float majorAxis;
	private Color color;
	private int countOfSteps;
	private List<Point3D> coordinates = new ArrayList<Point3D>();
	private float[] inclination;
	private final float compression = 0.5f;

	/**
	 * Constructor.
	 * 
	 * @param majorAxis
	 *            - the tall semi-major axis
	 * 
	 * @param eccentricity
	 *            - the numeric eccentricity
	 * @param color
	 *            - the color of the ellipse
	 * @param countOfSteps
	 *            - count of steps (the accuracy the ellipse is drawing with)
	 */
	public Ellipse(float majorAxis, float eccentricity, Color color, int countOfSteps, float[] inclination) {
		this.majorAxis = majorAxis;
		this.minorAxis = (float) Math.sqrt((Math.pow(majorAxis, 2.0f) - Math.pow(eccentricity, 2.0f)));
		this.color = color;
		this.countOfSteps = countOfSteps;
		this.inclination = inclination;

		initData();

	}

	private void initData() {
		for (int i = 0; i < countOfSteps; i += 1) {
			double infinitesimalArc = 2 * Math.PI / countOfSteps;
			float x = (majorAxis * (float) Math.cos((i * infinitesimalArc)) * compression);
			float z = (minorAxis * (float) Math.sin((i * infinitesimalArc)) * compression);
			float y = 0;

			/** saving the location */
			Point3D point3d = new Point3D(x, y, z);
			/** rotate the ellipse */
			if (inclination != null) {
				point3d = Utils.rotatePoint3DInSpace(inclination, point3d);
			}

			coordinates.add(point3d);
		}

	}

	public void animate(GL2 gl) {
		gl.glPushMatrix();
		gl.glLineWidth(0.00001f);
		gl.glColor3d(color.getRed(), color.getGreen(), color.getBlue());

		gl.glBegin(GL.GL_LINE_LOOP);

		for (int i = 0; i < countOfSteps; i++) {

			Point3D currentCoordinate = coordinates.get(i);
			gl.glVertex3d(currentCoordinate.getX(), currentCoordinate.getY(), currentCoordinate.getZ());
		}

		gl.glEnd();
		gl.glFlush();
		gl.glPopMatrix();

	}

	public List<Point3D> getCoordinate() {
		return coordinates;
	}

}
