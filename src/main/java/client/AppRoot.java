package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import camera.Camera;
import cameraToEarth.ActionContextMenuMouseListener;
import contextmenu.ActionContextMenu;
import contextmenu.Constants;
import universe.Ellipse;
import universe.GpsCoordinate;
import universe.Planet;
import universe.Star;
import universe.Sun;
import universe.Utils;

public abstract class AppRoot implements GLEventListener {

	protected Vector<Integer> shaders = new Vector<Integer>();
	protected GL2 gl;
	private static GLProfile glProfile;
	private static GLCapabilities glCapabilities;

	protected FPSAnimator fpsAnimator;
	protected GLCanvas glCanvas;

	private JFrame frame;
	private JPanel panel;
	private static ActionMap actionMap;
	private static InputMap inputMap;
	private ActionContextMenu actionContextMenu;
	int fps = 10;

	/** Booleans */
	private Boolean orbitsAreVisible = Boolean.FALSE;
	protected Boolean cameraWillBeMoved = Boolean.FALSE;
	protected Boolean animateOnlyEarth = Boolean.FALSE;
	private Boolean useAngularDefaultValues = Boolean.FALSE;
	private Boolean viewChanged = Boolean.FALSE;
	protected Boolean useArcBall;
	protected Boolean sceneIsFrozen = Boolean.FALSE;
	protected Boolean cameraIsSet = Boolean.FALSE;
	protected Boolean cameraIsOrbitingRoundPlanet = Boolean.FALSE;
	protected Boolean renderSuns = Boolean.TRUE;

	/** Objects to animate */
	protected final float RADIUS_OF_SUN = 0.05f;
	protected Sun sun;
	protected Ellipse ellipseOfMercury, ellipseOfVenus, ellipseOfEarth, ellipseOfMars, ellipseOfJupiter,
			ellipseOfSaturn, ellipseOfUranus, ellipseOfNeptune, ellipseOfPluto;
	protected Planet mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, pluto;
	protected List<Planet> lstPlanet = new ArrayList<Planet>();
	protected String planetToOrbit = null;
	protected Planet planetToOrbitAround = null;
	protected Boolean isInOrbit = Boolean.FALSE;

	protected Camera camera = new Camera(glu, this, RADIUS_OF_SUN);
	protected static float currentX = 0, currentY = 0, currentZ = 0;

	protected List<Star> lstStars;
	protected List<Star> lstSuns;
	protected int positionAttribute;
	protected int colorAttribute;
	protected int programId;

	// float z = +0.5f;
	protected GpsCoordinate gps = new GpsCoordinate(13.404954, 52.5200066);
	protected float z_NEAR = 0.1f;
	private FloatBuffer starVertexBuffer;
	private IntBuffer indexBuffer;
	private FloatBuffer colorBuffer;
	private int[] starIndices;
	// private float[] starColor;
	private float[] starVertices;

	protected static final GLU glu = new GLU();

	public abstract void registerAllKeyAction();

	public abstract void display(GLAutoDrawable d);

	/** to create Objects */
	private void initGLObjects() {
		glProfile = GLProfile.get(GLProfile.GL2);
		glCapabilities = new GLCapabilities(glProfile);

		ellipseOfMercury = new Ellipse(6f * RADIUS_OF_SUN, 0.205f, Color.MAGENTA, 88,
				new float[] { .0f, .0f, 7.00487f });
		ellipseOfVenus = new Ellipse(10f * RADIUS_OF_SUN, 0.0067f, Color.ORANGE, 224, new float[] { .0f, .0f, 3.395f });
		ellipseOfEarth = new Ellipse(12 * RADIUS_OF_SUN, 0.016f, Color.GREEN, 365, null);
		ellipseOfMars = new Ellipse(16 * RADIUS_OF_SUN, 0.094f, Color.RED, 687, new float[] { .0f, .0f, 1.6f });
		ellipseOfJupiter = new Ellipse(20 * RADIUS_OF_SUN, 0.048f, Color.YELLOW, 4330,
				new float[] { .0f, .0f, 1.305f });
		ellipseOfSaturn = new Ellipse(22 * RADIUS_OF_SUN, 0.056f, Color.GRAY, 10751, new float[] { .0f, .0f, 2.484f });
		ellipseOfUranus = new Ellipse(24 * RADIUS_OF_SUN, 0.0472f, Color.BLUE, 30660, new float[] { .0f, .0f, 0.770f });
		ellipseOfNeptune = new Ellipse(28 * RADIUS_OF_SUN, 0.008f, Color.CYAN, 60225, new float[] { .0f, .0f, 1.769f });
		ellipseOfPluto = new Ellipse(30 * RADIUS_OF_SUN, 0.2488f, Color.PINK, 90520, new float[] { .0f, .0f, 17.16f });

		sun = new Sun(Constants.Sun, "sunTest.png", RADIUS_OF_SUN, 0.5f, 1.0f, 0.0f);
		mercury = new Planet(Constants.Mercury, "mercury_FAR.jpg", "mercury_NEAR.jpg", 0.004, 0.0f, 0.01f, 0.02f,
				ellipseOfMercury.getCoordinate());
		venus = new Planet(Constants.Venus, "venus_FAR.jpg", "venus_NEAR1.jpg", 0.003, 0.0f, 2.0f, 0.0f,
				ellipseOfVenus.getCoordinate());
		earth = new Planet(Constants.Earth, "earth_FAR.jpg", "earth_NEAR.jpg", 0.004, 0.0f, 1.0f, 1.2f,
				ellipseOfEarth.getCoordinate());
		mars = new Planet(Constants.Mars, "mars_FAR.jpg", "mars_NEAR1.jpg", 0.004, 0.0f, 2.0f, 0.0f,
				ellipseOfMars.getCoordinate());
		jupiter = new Planet(Constants.Jupiter, "jupiter_FAR.jpg", "jupiter_NEAR1.jpg", 0.006, 0.0f, 2.0f, 0.0f,
				ellipseOfJupiter.getCoordinate());
		saturn = new Planet(Constants.Saturn, "saturn_FAR.jpg", "saturn_NEAR.jpg", 0.004, 0.0f, 2.0f, 0.0f,
				ellipseOfSaturn.getCoordinate());
		uranus = new Planet(Constants.Uranus, "uranus_FAR.jpg", "uranus_NEAR.jpg", 0.004, 0.0f, 2.0f, 0.0f,
				ellipseOfUranus.getCoordinate());
		neptune = new Planet(Constants.Neptune, "neptune_FAR.jpg", "neptune_NEAR.jpg", 0.005, 0.0f, 2.0f, 0.0f,
				ellipseOfNeptune.getCoordinate());
		pluto = new Planet(Constants.Pluto, "pluto_FAR.jpg", "pluto_NEAR.jpg", 0.004, 0.0f, 0.0000000f, 0.0f,
				ellipseOfPluto.getCoordinate());

		lstPlanet.add(mercury);
		lstPlanet.add(venus);
		lstPlanet.add(earth);
		lstPlanet.add(mars);
		lstPlanet.add(saturn);
		lstPlanet.add(jupiter);
		lstPlanet.add(uranus);
		lstPlanet.add(neptune);
		lstPlanet.add(pluto);

		lstStars = Utils.initializeStars("../srcStarsystem/ngc.txt");
		lstSuns = Utils.initializeSuns(lstStars);

	}

	public void dispose(GLAutoDrawable arg0) {
		fpsAnimator.stop();
	}

	private void createWindows() {
		glCanvas = new GLCanvas(glCapabilities);

		glCanvas.addGLEventListener(this);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		glCanvas.setSize(new Dimension(width, height));

		frame = new JFrame();

		frame.getContentPane().add(glCanvas);

		frame.setSize(frame.getContentPane().getPreferredSize());

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		int x = (d.width - frame.getWidth()) / 2;
		int y = (d.height - frame.getHeight()) / 2;

		frame.setLocation(x, y);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 0));

		actionMap = panel.getActionMap();
		inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		frame.addWindowListener(new WindowAdapter() {

			@SuppressWarnings("unused")
			public void windowIsClosing() {
				if (fpsAnimator.isStarted()) {
					fpsAnimator.stop();
					System.exit(0);
				}
			}
		});
		glCanvas.addMouseListener(new ActionContextMenuMouseListener(this));

	}

	public void registerKeyAction(Integer key, AbstractAction a) {
		inputMap.put(KeyStroke.getKeyStroke(key, 0), key.toString());
		actionMap.put(key.toString(), a);
	}

	/**
	 * to start the fpsAnimator.
	 */
	public void start() {
		fpsAnimator.start();
	}

	public void clearCanvas() {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

	}

	public void init(GLAutoDrawable d) {
		gl = d.getGL().getGL2();

		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearDepth(1);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		/** Setting the background color */
		gl.glClearColor(0.15f, 0.15f, 0.15f, 0);

	}

	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		gl.glViewport(x, y, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(1.0f, (float) width / (float) height, 1, 500);

		gl.glMatrixMode(GL2.GL_PROJECTION);
	}

	public AppRoot() {
		initGLObjects();
		createWindows();
		fpsAnimator = new FPSAnimator(glCanvas, fps, true);
		frame.setVisible(true);
		registerAllKeyAction();
	}

	/**
	 * to reset the parameters for moving the camera statically.
	 */
	public void resetStaticParameters() {
		currentX = 0;
		currentY = 0;
		currentZ = 0;
		camera.setZ_NEAR(0.1f);

		for (Planet planet : lstPlanet) {
			planet.setRenderMe(Boolean.TRUE);
		}
		sun.setRenderMe(Boolean.TRUE);
		renderSuns = Boolean.TRUE;

	}

	/** set & get follows below here */
	public ActionContextMenu getActionContextMenu() {
		return actionContextMenu;
	}

	public void setActionContextMenu(ActionContextMenu actionContextMenu) {
		this.actionContextMenu = actionContextMenu;
	}

	public Boolean getOrbitsAreVisible() {
		return orbitsAreVisible;
	}

	public void setOrbitsAreVisible(Boolean orbitsAreVisible) {
		this.orbitsAreVisible = orbitsAreVisible;
	}

	public Boolean getCameraWillBeMoved() {
		return cameraWillBeMoved;
	}

	public void setCameraWillBeMoved(Boolean cameraWillBeMoved) {
		this.cameraWillBeMoved = cameraWillBeMoved;
	}

	public Boolean getUseAngularDefaultValues() {
		return useAngularDefaultValues;
	}

	public void setUseAngularDefaultValues(Boolean useAngularDefaultValues) {
		this.useAngularDefaultValues = useAngularDefaultValues;
	}

	public Boolean getViewChanged() {
		return viewChanged;
	}

	public void setViewChanged(Boolean viewChanged) {
		this.viewChanged = viewChanged;
	}

	public void setUseArcBall(Boolean useArcBall) {
		this.useArcBall = useArcBall;
	}

	public Boolean getUseArcBall() {

		if (useArcBall == null) {
			return Boolean.FALSE;
		}
		return useArcBall;
	}

	public void setGPS(GpsCoordinate gps) {
		this.gps = gps;

	}

	public Boolean getSceneIsFrozen() {
		return sceneIsFrozen;
	}

	public void setSceneIsFrozen(Boolean sceneIsFrozen) {
		this.sceneIsFrozen = sceneIsFrozen;
	}

	public FPSAnimator getFpsAnimator() {
		return fpsAnimator;
	}

	public void setCameraIsSet(Boolean cameraIsSet) {
		this.cameraIsSet = cameraIsSet;
	}

	public Boolean getCameraIsOrbitingRoundPlanet() {
		return cameraIsOrbitingRoundPlanet;
	}

	public void setCameraIsOrbitingRoundPlanet(Boolean cameraIsOrbitingRoundPlanet) {
		this.cameraIsOrbitingRoundPlanet = cameraIsOrbitingRoundPlanet;
	}

	public String getPlanetToOrbit() {
		return planetToOrbit;
	}

	public void setPlanetToOrbit(String planetToOrbit) {
		this.planetToOrbit = planetToOrbit;
	}

	public void setPlanetToOrbitAround(Planet planetToOrbitAround) {
		this.planetToOrbitAround = planetToOrbitAround;
	}

	public void setIsInOrbit(Boolean isInOrbit) {
		this.isInOrbit = isInOrbit;
	}

	public List<Planet> getLstPlanet() {
		return lstPlanet;
	}

	public void setLstPlanet(List<Planet> lstPlanet) {
		this.lstPlanet = lstPlanet;
	}

	public Sun getSun() {
		return sun;
	}

	public void setSun(Sun sun) {
		this.sun = sun;
	}

	public Boolean getRenderSuns() {
		return renderSuns;
	}

	public void setRenderSuns(Boolean renderSuns) {
		this.renderSuns = renderSuns;
	}

}
