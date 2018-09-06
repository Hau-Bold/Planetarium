package contextmenu;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import universe.Utils;

public class IconComboboxItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JComboBox<String> combobox;
	private List<String> lstPlanets = new ArrayList<String>();
	private String[] planetsToSelect;

	private List<String> lstPlanetsToSelect = new ArrayList<String>();

	/**
	 * Constructor.
	 * 
	 * @param pathOfIcon
	 *            - the path of the icon.
	 * @param text
	 *            - the text that should be displayed.
	 */
	IconComboboxItem(String pathOfIcon, String text) {

		if (Utils.isStringValid(pathOfIcon)) {
			ImageIcon icon = new ImageIcon(getClass().getResource("../images/" + pathOfIcon));
			icon.setImage(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			this.setIcon(icon);
		}

		this.setSize(20, 150);
		this.setOpaque(true);
		this.setText(text);
		this.setBackground(Color.WHITE);

		lstPlanets.add(Constants.Mercury);
		lstPlanets.add(Constants.Venus);
		lstPlanets.add(Constants.Earth);
		lstPlanets.add(Constants.Mars);
		lstPlanets.add(Constants.Jupiter);
		lstPlanets.add(Constants.Saturn);
		lstPlanets.add(Constants.Uranus);
		lstPlanets.add(Constants.Neptune);
		lstPlanets.add(Constants.Pluto);

		planetsToSelect = new String[lstPlanets.size()];

		for (int i = 0; i < lstPlanets.size(); i++) {
			String planet = lstPlanets.get(i);
			lstPlanetsToSelect.add(planet);
		}

		for (int i = 0; i < lstPlanetsToSelect.size(); i++) {
			planetsToSelect[i] = lstPlanets.get(i);
		}

		combobox = new JComboBox<String>(planetsToSelect);
		combobox.setSize(80, 20);

	}

	public IconComboboxItem(String pathOfIcon) {
		this(pathOfIcon, null);
	}

	public static JComboBox<String> getCombobox() {
		return combobox;
	}

	public static void setCombobox(JComboBox<String> combobox) {
		IconComboboxItem.combobox = combobox;
	}

	public String[] getPlanetsToSelect() {
		return planetsToSelect;
	}

}
