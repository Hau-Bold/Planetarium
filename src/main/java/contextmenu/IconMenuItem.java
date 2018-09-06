package contextmenu;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import universe.Utils;

public class IconMenuItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon icon = null;

	/**
	 * Constructor.
	 * 
	 * @param pathOfIcon
	 *            - the path of the icon.
	 * @param text
	 *            - the text that should be displayed.
	 */
	IconMenuItem(String pathOfIcon, String text) {

		if (Utils.isStringValid(pathOfIcon)) {
			this.icon = new ImageIcon(getClass().getResource("../images/" + pathOfIcon));
			this.icon.setImage(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			this.setIcon(icon);
		}

		this.setSize(20, 150);
		this.setText(text);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);

	}

	public IconMenuItem(String pathOfIcon) {
		this(pathOfIcon, null);
	}

}
