package universe;

import java.io.IOException;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/** the class star */
public class Star {

	private double rectaszension;
	private double declination;
	private static int starCounter = 0;
	protected final static GLU glu = new GLU();

	/** Constructor. */
	public Star(double rectaszension, double declination) {
		this.rectaszension = rectaszension;
		this.declination = declination;
	}

	public double getRectaszension() {
		return rectaszension;
	}

	public void setRectaszension(double rectaszension) {
		this.rectaszension = rectaszension;
	}

	public double getDeclination() {
		return declination;
	}

	public void setDeclination(double declination) {
		this.declination = declination;
	}

	/**
	 * renders the stars
	 * 
	 * @param gl
	 *            - the GLAutoDrawable
	 * @param lstStars
	 *            - the receiving
	 * @param radius
	 *            - the radius of the stars
	 * @param cameraIsSet
	 * @param isInOrbit
	 * @param cameraWillBeMoved
	 * @param vector3d
	 * @throws IOException
	 */
	public static void renderStars1(GL2 gl, List<Star> lstStars, float radius, Point3D positionOfCamera,
			Boolean cameraIsMovedToEarth, Boolean cameraIsSet, Boolean isInOrbit) throws IOException {

		gl.glPushMatrix();

		if (cameraIsMovedToEarth && !cameraIsSet && !isInOrbit) {
			gl.glLoadIdentity();
		}
		gl.glBegin(GL2.GL_TRIANGLES);

		if (cameraIsMovedToEarth && !cameraIsSet) {
			double eyex = positionOfCamera.getX();
			double eyey = positionOfCamera.getY();
			double eyez = positionOfCamera.getZ();
			gl.glTranslated(-eyex, -eyey, -eyez);
		}

		if (lstStars.size() > 0) {

			for (Star item : lstStars) {

				double x = radius * Math.cos(item.getRectaszension()) * Math.cos(item.getDeclination());
				double z = radius * Math.cos(item.getRectaszension()) * Math.sin(item.getDeclination());
				double y = radius * Math.sin(item.getRectaszension());
				if (starCounter % 10 == 0) {
					gl.glColor3d(255, 48, 48);

				} else if (starCounter % 15 == 0) {

					gl.glColor3d(32, 178, 170);
				} else if (starCounter % 35 == 0) {

					gl.glColor3d(0, 0, 139);
				} else if (starCounter % 37 == 0) {
					gl.glColor3d(255, 0, 0);
				}

				else {
					gl.glColor3d(255, 255, 0);
				}
				gl.glVertex3d(x - 0.001, y, z);
				gl.glVertex3d(x, y + 0.001, z);
				gl.glVertex3d(x, y, z + 0.001);

				starCounter++;

			}

			// gl.glDisable(GL2.GL_COLOR_MATERIAL);
		}
		gl.glEnd();
		gl.glFlush();
		gl.glPopMatrix();

		starCounter = 0;
	}

	public static void renderSuns(GL2 gl, List<Star> lstSuns, float radius, Point3D positionOfCamera,
			Boolean cameraWillBeMoved, Boolean cameraIsSet, Boolean isInOrbit) {
		gl.glPushMatrix();

		if (cameraWillBeMoved && !cameraIsSet && !isInOrbit) {
			gl.glLoadIdentity();
		}
		gl.glBegin(GL2.GL_QUADS);

		if (cameraWillBeMoved && !cameraIsSet) {
			double eyex = positionOfCamera.getX();
			double eyey = positionOfCamera.getY();
			double eyez = positionOfCamera.getZ();
			gl.glTranslated(-eyex, -eyey, -eyez);
		}

		if (lstSuns.size() > 0) {

			gl.glColor3d(100, 0, 255);
			for (Star item : lstSuns) {

				double x = radius * Math.cos(item.getRectaszension()) * Math.cos(item.getDeclination());
				double z = radius * Math.cos(item.getRectaszension()) * Math.sin(item.getDeclination());
				double y = radius * Math.sin(item.getRectaszension());
				gl.glTranslated(x, y, z);
				GLUquadric glUquadric = glu.gluNewQuadric();
				glu.gluQuadricDrawStyle(glUquadric, GLU.GLU_FILL);
				glu.gluQuadricTexture(glUquadric, true);
				glu.gluQuadricNormals(glUquadric, GLU.GLU_SMOOTH);

				float[] matAmbient = { 0.0f, 0.0f, 0.0f, 1.0f };
				float[] matDiffuse = { 0.0f, 0.0f, 0.0f, 1.0f };
				float[] matSpecular = { 0.0f, 0.0f, 0.0f, 1.0f };
				float[] matEmission = { 0.0f, 0.0f, 0.0f, 1.0f };
				float matShininess = 0.0f;
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmbient, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDiffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, matEmission, 0);
				gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess);

				glu.gluSphere(glUquadric, 0.006f, 20, 30);
			}

		}

		gl.glEnd();
		gl.glFlush();
		gl.glPopMatrix();

		starCounter = 0;

	}

}
