package contextmenu;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;

import universe.Utils;

public class IconCheckBoxItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JCheckBox checkBox;

	/**
	 * Constructor.
	 * 
	 * @param pathOfIcon
	 *            - the path of the icon.
	 * @param text
	 *            - the text that should be displayed.
	 */
	IconCheckBoxItem(String pathOfIcon, String text) {

		if (Utils.isStringValid(pathOfIcon)) {
			ImageIcon icon = new ImageIcon(getClass().getResource("../images/" + pathOfIcon));
			icon.setImage(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			this.setIcon(icon);
		}

		this.setSize(20, 150);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		checkBox = new JCheckBox(text);

	}

	public IconCheckBoxItem(String pathOfIcon) {
		this(pathOfIcon, null);
	}

	public static JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

}
