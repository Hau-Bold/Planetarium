package contextmenu;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuItemMouseListener extends AbstractMenuItemMouseListener {

	public MenuItemMouseListener(JMenuItem iconMenuItem) {
		super(iconMenuItem);
	}

	public MenuItemMouseListener(JPanel panel) {
		super(panel);
	}

}
