package universe;

import java.io.IOException;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.TextureIO;

public class Planet extends AbstractBody {

	private int counter = 0;
	private Point3D currentLocation = new Point3D(0.0f, 0.0f, 0.0f);

	private List<Point3D> coordinates;

	private int size;
	//
	private float xrotatio = 0;
	private float yrotatio = 0;
	private float zrotatio = 0;

	private float incrementalSpeedx;
	private float incrementalSpeedy;
	private float incrementalSpeedz;

	private int itexture;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            - the name of the planet
	 * @param path_FAR_Texture
	 *            - the path to the far texture
	 * @param path_NEAR_Texture
	 * @param radius
	 *            - the radius
	 * @param incrementalSpeedx
	 *            - rotation speed in lstStars direction
	 * @param incrementalSpeedy
	 *            - rotation speed in y direction
	 * @param incrementalSpeedz
	 *            - rotation speed in z direction
	 * @param coordinates
	 */
	public Planet(String name, String path_FAR_Texture, String path_NEAR_Texture, double radius,
			float incrementalSpeedx, float incrementalSpeedy, float incrementalSpeedz, List<Point3D> coordinates) {

		super(name, path_FAR_Texture, path_NEAR_Texture, radius);
		this.incrementalSpeedx = incrementalSpeedx;
		this.incrementalSpeedy = incrementalSpeedy;
		this.incrementalSpeedz = incrementalSpeedz;
		this.coordinates = coordinates;
		this.size = coordinates.size();
	}

	@Override
	public void animate(GL2 gl, double distance) {

		if (Integer.compare(counter, size - 1) == 0) {
			counter = 0;
		}
		gl.glPushMatrix();
		gl.glColor3d(255, 255, 255);

		if (size > 0) {
			Point3D currentCoordinate = coordinates.get(counter);
			setCurrentLocation(currentCoordinate);
			counter++;

			gl.glTranslated(currentCoordinate.getX(), currentCoordinate.getY(), currentCoordinate.getZ());

			xrotatio += incrementalSpeedx;
			yrotatio += incrementalSpeedy;
			zrotatio += incrementalSpeedz;

			gl.glRotatef(xrotatio, 1, 0, 0);
			gl.glRotatef(yrotatio, 0, 1, 0);
			gl.glRotatef(zrotatio, 0, 0, 1);

		}

		gl.glEnable(GL2.GL_TEXTURE_2D);
		try {

			if (distance > 0.1) {
				texture = TextureIO.newTexture(file_FAR_Texture, Boolean.FALSE);
			} else {
				texture = TextureIO.newTexture(file_NEAR_Texture, Boolean.FALSE);
			}

		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itexture = texture.getTextureObject(gl);

		// FIX ME
		// gl.glGenBuffers(n, buffers);

		GLUquadric glUquadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(glUquadric, GLU.GLU_FILL);
		glu.gluQuadricTexture(glUquadric, true);
		glu.gluQuadricNormals(glUquadric, GLU.GLU_SMOOTH);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, itexture);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		// If the u,v coordinates overflow the range 0,1 the image is repeated
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		// gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
		// GL2.GL_LINEAR);
		// gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
		// GL2.GL_LINEAR_MIPMAP_NEAREST);
		// gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
		// GL2.GL_LINEAR);
		// gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
		// GL2.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		// gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

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

		if (renderMe) {
			glu.gluSphere(glUquadric, radius, 20, 30);
		}
		gl.glEnd();

		gl.glFlush();

		gl.glPopMatrix();

		texture.destroy(gl);
		super.name = name;

	}

	public Point3D getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Point3D location) {
		this.currentLocation = location;
	}

	public float getXrotatio() {
		return xrotatio;
	}

	public float getYrotatio() {
		return yrotatio;
	}

	public float getZrotatio() {
		return zrotatio;
	}

	@Override
	void animate(GL2 gl) {
	}

}
