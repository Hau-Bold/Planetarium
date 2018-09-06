package universe;

import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.TextureIO;

public class Sun extends AbstractBody {

	private static float xrotatio;
	private static float yrotatio;
	private static float zrotatio;
	private float incrementalSpeedx;
	private float incrementalSpeedy;
	private float incrementalSpeedz;
	private int itexture;

	public Sun(String name, String path, double radius, float incrementalSpeedx, float incrementalSpeedy,
			float incrementalSpeedz) {
		super(name, path, radius);
		this.incrementalSpeedx = incrementalSpeedx;
		this.incrementalSpeedy = incrementalSpeedy;
		this.incrementalSpeedz = incrementalSpeedz;

	}

	@Override
	public void animate(GL2 gl) {

		gl.glPushMatrix();
		gl.glColor3d(255, 255, 255);

		xrotatio += incrementalSpeedx;
		yrotatio += incrementalSpeedy;
		zrotatio += incrementalSpeedz;

		gl.glRotatef(xrotatio, 1, 0, 0);
		gl.glRotatef(yrotatio, 0, 1, 0);
		gl.glRotatef(zrotatio, 0, 0, 1);

		try {
			texture = TextureIO.newTexture(file_FAR_Texture, Boolean.FALSE);
		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		itexture = texture.getTextureObject(gl);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_COLOR_MATERIAL_FACE);
		GLUquadric glUquadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(glUquadric, GLU.GLU_FILL);
		glu.gluQuadricTexture(glUquadric, true);
		glu.gluQuadricNormals(glUquadric, GLU.GLU_SMOOTH);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, itexture);

		gl.glBegin(GL2.GL_LINE_LOOP);

		if (renderMe) {
			glu.gluSphere(glUquadric, radius, 20, 30);
		}
		gl.glEnd();
		gl.glFlush();
		// gl.glDisable(GL2.GL_COLOR_MATERIAL);
		gl.glPopMatrix();

		texture.destroy(gl);
	}

	public int getItexture() {
		return itexture;
	}

	public void setItexture(int itexture) {
		this.itexture = itexture;
	}

	@Override
	void animate(GL2 gl, double distance) {
	}

}
