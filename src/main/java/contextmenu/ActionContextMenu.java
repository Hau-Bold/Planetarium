package contextmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import cameraToEarth.CameraToEarth;
import client.AppRoot;
import universe.Planet;

/**
 * the class ActionContextMenu
 *
 */
public class ActionContextMenu extends CommonContextMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private IconMenuItem cameraToEarthIconButton, resetIconButton, showOrbitsIconButton, hideOrbitsIconButton,
			resetAngularsIconButton, freezeSceneIconButton, continueSceneIconButton;
	IconComboboxItem orbitRoundPlanet;
	private AppRoot appRoot;
	private IconCheckBoxItem useArcBallCheckBox;

	public ActionContextMenu(MouseEvent event, AppRoot appRoot) {
		super(event);
		this.appRoot = appRoot;
		initComponent();
		showMenu();
	}

	private void initComponent() {

		this.setModal(Boolean.TRUE);

		if (!appRoot.getCameraWillBeMoved()) {
			cameraToEarthIconButton = new IconMenuItem("setLocation.png", Constants.SET_LOCATION);
			cameraToEarthIconButton.addActionListener(this);
			super.addIconMenuItem(cameraToEarthIconButton);
		} else {
			resetIconButton = new IconMenuItem("resetLocation.png", Constants.RESET_LOCATION);
			resetIconButton.addActionListener(this);
			super.addIconMenuItem(resetIconButton);

		}
		if (!appRoot.getOrbitsAreVisible()) {
			showOrbitsIconButton = new IconMenuItem("showOrbits.png", Constants.SHOW_ORBITS);
			showOrbitsIconButton.addActionListener(this);
			super.addIconMenuItem(showOrbitsIconButton);
		} else {
			hideOrbitsIconButton = new IconMenuItem("hideOrbits.png", Constants.HIDE_ORBITS);
			hideOrbitsIconButton.addActionListener(this);
			super.addIconMenuItem(hideOrbitsIconButton);
		}

		if (appRoot.getViewChanged()) {
			resetAngularsIconButton = new IconMenuItem("resetAngulars.png", Constants.RESET_ANGULARS);
			resetAngularsIconButton.addActionListener(this);
			super.addIconMenuItem(resetAngularsIconButton);
		}

		if (!appRoot.getSceneIsFrozen()) {
			freezeSceneIconButton = new IconMenuItem("freezeIcon.png", Constants.FREEZE_SCENE);
			freezeSceneIconButton.addActionListener(this);
			super.addIconMenuItem(freezeSceneIconButton);
		} else {
			continueSceneIconButton = new IconMenuItem("continueIcon.jpg", Constants.CONTINUE);
			continueSceneIconButton.addActionListener(this);
			super.addIconMenuItem(continueSceneIconButton);
		}

		useArcBallCheckBox = new IconCheckBoxItem(null, Constants.ARCBALL);
		useArcBallCheckBox.getCheckBox().setSelected(appRoot.getUseArcBall());
		useArcBallCheckBox.getCheckBox().addActionListener(this);

		super.addIconMenuItem(useArcBallCheckBox);

		if (!appRoot.getCameraIsOrbitingRoundPlanet()) {
			orbitRoundPlanet = new IconComboboxItem("spaceShuttle.png", Constants.ORBIT);
			orbitRoundPlanet.getCombobox().addActionListener(this);
			super.addIconMenuItem(orbitRoundPlanet);
		}

		super.activate();
	}

	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(cameraToEarthIconButton)) {
			CameraToEarth moveCameraToPlanet = CameraToEarth.getInstance(appRoot);
			moveCameraToPlanet.showFrame();
		}

		else if (o.equals(showOrbitsIconButton)) {
			appRoot.setOrbitsAreVisible(Boolean.TRUE);
		}

		else if (o.equals(hideOrbitsIconButton)) {
			appRoot.setOrbitsAreVisible(Boolean.FALSE);
		}

		else if (o.equals(resetIconButton)) {
			appRoot.setCameraWillBeMoved(Boolean.FALSE);
			appRoot.setUseArcBall(null);
			appRoot.setCameraIsSet(Boolean.FALSE);
			appRoot.setPlanetToOrbit(null);
			appRoot.setIsInOrbit(Boolean.FALSE);
			appRoot.setPlanetToOrbitAround(null);
			appRoot.resetStaticParameters();
		}

		else if (o.equals(resetAngularsIconButton)) {
			appRoot.setUseAngularDefaultValues(Boolean.TRUE);
			appRoot.setViewChanged(Boolean.FALSE);
		}

		else if (o.equals(useArcBallCheckBox.getCheckBox())) {

			JCheckBox checkbox = useArcBallCheckBox.getCheckBox();

			if (checkbox.isSelected()) {
				appRoot.setUseArcBall(Boolean.TRUE);
			} else {
				appRoot.setUseArcBall(Boolean.FALSE);
			}
		}

		else if (o.equals(freezeSceneIconButton)) {
			appRoot.setSceneIsFrozen(Boolean.TRUE);
		}

		else if (o.equals(continueSceneIconButton)) {
			appRoot.setSceneIsFrozen(Boolean.FALSE);
			appRoot.getFpsAnimator().resume();
		}

		else if (o.equals(orbitRoundPlanet.getCombobox())) {

			JComboBox<String> comboBox = orbitRoundPlanet.getCombobox();
			String planetToOrnit = orbitRoundPlanet.getPlanetsToSelect()[comboBox.getSelectedIndex()];

			appRoot.getSun().setRenderMe(Boolean.FALSE);
			appRoot.setRenderSuns(Boolean.FALSE);

			for (Planet planet : appRoot.getLstPlanet()) {
				if (!(planet.getName() == planetToOrnit)) {
					planet.setRenderMe(Boolean.FALSE);
				}
			}

			appRoot.setPlanetToOrbit(planetToOrnit);
			appRoot.setCameraWillBeMoved(Boolean.TRUE);
		}

		this.dispose();

	}

}
