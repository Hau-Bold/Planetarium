package client;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import com.jogamp.opengl.GLAutoDrawable;

import universe.Planet;
import universe.Point3D;
import universe.Star;
import universe.Utils;

public class App extends AppRoot {

	private static final double ORBIT = 0.007;
	private final float RADIUS_OF_STARS = 1.0f;

	float time = 0.0f;
	Point3D actual = null;
	private double PHI = Math.PI / 2;

	private Point3D currentLocation;

	@Override
	public void display(GLAutoDrawable d) {

		clearCanvas();

		if (sceneIsFrozen) {
			fpsAnimator.pause();
		}

		if (!getCameraWillBeMoved()) {
			if (useArcBall == null) {
				camera.reset();
			}
			camera.apply(gl);
		} else {
			Point3D locationOnEarth = Utils.computeLocationOnEarth(earth, gps);

			currentLocation = earth.getCurrentLocation();

			if (!cameraIsSet && (planetToOrbit == null)) {
				animateOnlyEarth = Boolean.TRUE;
				renderSuns = Boolean.FALSE;
				actual = camera.getPosition();

				if (z_NEAR > 0.01) {
					z_NEAR -= 0.01;
				}

				if (Point3D.norm(currentLocation.minus(actual)) > earth.getRadius() + 0.01) {

					if (currentLocation.getX() > currentX) {
						currentX += 0.01;
					}

					if (currentLocation.getX() < currentX) {
						currentX -= 0.01;
					}

					if (currentLocation.getY() > currentY) {
						currentY += 0.01;
					}

					if (currentLocation.getY() < currentY) {
						currentY -= 0.01;
					}

					if (currentLocation.getZ() > currentZ) {
						currentZ += 0.01;
					}

					if (currentLocation.getZ() < currentZ) {
						currentZ -= 0.01;
					}
					camera.setPosition(new Point3D(currentX, currentY, currentZ));
					camera.setViewDirection(locationOnEarth);
					camera.setZ_NEAR(z_NEAR);

				} else {
					cameraIsSet = Boolean.TRUE;
					animateOnlyEarth = Boolean.FALSE;
					renderSuns = Boolean.TRUE;
				}
			}
			if (cameraIsSet && (planetToOrbit == null)) {
				camera.setPosition(earth.getCurrentLocation());
				camera.setViewDirection(locationOnEarth);
				camera.setZ_NEAR(0.1f);
			}

			if (!cameraIsSet && (planetToOrbit != null)) {

				if (planetToOrbitAround == null) {
					for (Planet planet : lstPlanet) {
						if (planet.getName().equals(planetToOrbit)) {
							planetToOrbitAround = planet;
						}
					}
				}

				actual = camera.getPosition();
				if (planetToOrbitAround != null) {
					currentLocation = planetToOrbitAround.getCurrentLocation();

					if (Point3D.norm(currentLocation.minus(actual)) <= ORBIT) {
						isInOrbit = Boolean.TRUE;
					}

					if (!isInOrbit) {

						if (z_NEAR > 0.01) {
							z_NEAR -= 0.01;
						}

						if (currentLocation.getX() >= currentX) {
							currentX += 0.01;
						}

						if (currentLocation.getX() < currentX) {
							currentX -= 0.01;
						}

						if (currentLocation.getY() >= currentY) {
							currentY += 0.01;
						}

						if (currentLocation.getY() < currentY) {
							currentY -= 0.01;
						}

						if (currentLocation.getZ() >= currentZ) {
							currentZ += 0.01;
						}

						if (currentLocation.getZ() < currentZ) {
							currentZ -= 0.01;
						}
						camera.setPosition(new Point3D(currentX, currentY, currentZ));
						camera.setViewDirection(currentLocation);
						camera.setZ_NEAR(z_NEAR);
					} else {

						for (Planet planet : lstPlanet) {
							planet.setRenderMe(Boolean.TRUE);
						}

						sun.setRenderMe(Boolean.TRUE);

						/** being in orbit */
						double orbit = planetToOrbitAround.getRadius() + ORBIT;
						double y_new = orbit * Math.sin(PHI);
						double z_new = orbit * Math.cos(PHI);
						Point3D locationOfCameraWithOrignIsZero = new Point3D(0.0, y_new, z_new);
						Point3D locationOfCameraWithOrignIsPlanet = currentLocation
								.plus(locationOfCameraWithOrignIsZero);
						camera.setPosition(locationOfCameraWithOrignIsPlanet);
						camera.setViewDirection(currentLocation);
						camera.setZ_NEAR(0.001f);

						PHI += 0.001;

						renderSuns = Boolean.TRUE;

					}

				}

			}

			camera.apply(gl);
		}

		if (useArcBall != null) {
			if (useArcBall) {
				camera.installTrackball(glCanvas, gl);
			}

			else {
				camera.deinstallArcBall();
				useArcBall = null;
			}
		}

		/** Animation of Orbits */
		if (getOrbitsAreVisible()) {
			ellipseOfMercury.animate(gl);
			ellipseOfVenus.animate(gl);
			ellipseOfEarth.animate(gl);
			ellipseOfMars.animate(gl);
			ellipseOfJupiter.animate(gl);
			ellipseOfSaturn.animate(gl);
			ellipseOfUranus.animate(gl);
			ellipseOfNeptune.animate(gl);
			ellipseOfPluto.animate(gl);
		}

		/** Animation of Planets */
		Point3D position = camera.getPosition();
		earth.animate(gl, Point3D.norm(position.minus(earth.getCurrentLocation())));

		if (!animateOnlyEarth) {
			/** Animation of sun */
			sun.animate(gl);
			/** planets */
			mercury.animate(gl, Point3D.norm(position.minus(mercury.getCurrentLocation())));
			venus.animate(gl, Point3D.norm(position.minus(venus.getCurrentLocation())));
			mars.animate(gl, Point3D.norm(position.minus(mars.getCurrentLocation())));
			jupiter.animate(gl, Point3D.norm(position.minus(jupiter.getCurrentLocation())));
			saturn.animate(gl, Point3D.norm(position.minus(saturn.getCurrentLocation())));
			uranus.animate(gl, Point3D.norm(position.minus(uranus.getCurrentLocation())));
			neptune.animate(gl, Point3D.norm(position.minus(neptune.getCurrentLocation())));
			pluto.animate(gl, Point3D.norm(position.minus(pluto.getCurrentLocation())));
		}

		/** renders the stars */
		try {
			Star.renderStars1(gl, lstStars, RADIUS_OF_STARS, camera.getPosition(), cameraWillBeMoved, cameraIsSet,
					isInOrbit);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/** render suns */

		if (renderSuns) {
			Star.renderSuns(gl, lstSuns, RADIUS_OF_STARS, camera.getPosition(), cameraWillBeMoved, cameraIsSet,
					isInOrbit);
		}

		if (getUseAngularDefaultValues()) {
			camera.reset();
			setUseAngularDefaultValues(Boolean.FALSE);
		}
	}

	// String vendor = gl.glGetString(GL.GL_VENDOR);
	//
	// System.out.println("Vendor: " + vendor);
	// String renderer = gl.glGetString(GL.GL_RENDERER);
	// System.out.println("Renderer: " + renderer);

	@Override
	public void registerAllKeyAction() {
		registerKeyAction(KeyEvent.VK_UP, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				setViewChanged(Boolean.TRUE);

			}
		});

		registerKeyAction(KeyEvent.VK_DOWN, new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (useArcBall == null) {
					setViewChanged(Boolean.TRUE);
				}

			}
		});

		registerKeyAction(KeyEvent.VK_LEFT, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (useArcBall == null) {
					setViewChanged(Boolean.TRUE);
				}

			}
		});

		registerKeyAction(KeyEvent.VK_RIGHT, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (useArcBall == null) {
					setViewChanged(Boolean.TRUE);
				}

			}
		});

		registerKeyAction(KeyEvent.VK_PLUS, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (useArcBall == null) {
					setViewChanged(Boolean.TRUE);
				}

			}
		});

		registerKeyAction(KeyEvent.VK_MINUS, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (useArcBall == null) {
					setViewChanged(Boolean.TRUE);
				}

			}
		});

	}

	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

}