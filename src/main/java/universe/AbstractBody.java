package universe;

import java.io.File;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;

public abstract class AbstractBody {

	protected String path;
	protected double radius;
	protected File file_FAR_Texture;
	protected File file_NEAR_Texture;
	protected static Texture texture;
	protected final GLU glu = new GLU();
	protected String name;
	protected boolean renderMe = Boolean.TRUE;

	public AbstractBody(String name, String path_FAR_Texture, String path_NEAR_Texture, double radius) {

		file_FAR_Texture = new File(getClass().getResource("../textures/" + path_FAR_Texture).getFile());
		if (path_NEAR_Texture != null) {
			file_NEAR_Texture = new File(getClass().getResource("../textures/" + path_NEAR_Texture).getFile());
		}
		this.radius = radius;
		this.name = name;

	}

	public AbstractBody(String name, String path, double radius) {
		this(name, path, null, radius);
	}

	abstract void animate(GL2 gl, double distance);

	abstract void animate(GL2 gl);

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getName() {
		return name;
	}

	public boolean isRenderMe() {
		return renderMe;
	}

	public void setRenderMe(boolean renderMe) {
		this.renderMe = renderMe;
	}

}
