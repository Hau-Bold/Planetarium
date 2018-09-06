package contextmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class CommonContextMenu extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2796536710016330518L;
	private final int HEIGHTOFITEM = 25;
	private final int WIDTH = 140;
	private List<JMenuItem> iconMenuItems = new ArrayList<JMenuItem>();
	private JPanel panel;

	/**
	 * Constructor.
	 * 
	 * @param event
	 *            - the event
	 * @param routeplaner
	 *            - the routeplaner
	 * @param iconMenuItems
	 *            - list of IconMenuItems
	 */
	CommonContextMenu(MouseEvent event) {
		initComponent(event);
	}

	private void initComponent(MouseEvent event) {

		this.setUndecorated(true);
		this.setLocation(event.getX(), event.getY());
		this.setLayout(new BorderLayout());

	}

	protected void showMenu() {
		this.setVisible(true);
	}

	/**
	 * moves the items to the context menu
	 */
	protected void activate() {
		int countOfItem = iconMenuItems.size();
		this.setSize(WIDTH, countOfItem * HEIGHTOFITEM);
		panel = new JPanel(new GridLayout(countOfItem, 1));
		panel.setSize(WIDTH, countOfItem * HEIGHTOFITEM);

		this.add(panel, BorderLayout.CENTER);

		for (JMenuItem item : iconMenuItems) {

			panel.add(getPanelWithItem(item));
		}
	}

	private JPanel getPanelWithItem(JMenuItem item) {

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(WIDTH, HEIGHTOFITEM);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		if (item instanceof IconCheckBoxItem) {
			panel.add(IconCheckBoxItem.getCheckBox(), BorderLayout.WEST);
			panel.addMouseListener(new MenuItemMouseListener(panel));
		} else if (item instanceof IconComboboxItem) {
			panel.add(IconComboboxItem.getCombobox(), BorderLayout.EAST);
			panel.addMouseListener(new MenuItemMouseListener(panel));

		} else {
			item.addMouseListener(new MenuItemMouseListener(item));
		}
		panel.add(item, BorderLayout.CENTER);
		panel.setOpaque(Boolean.FALSE);

		return panel;
	}

	protected void addIconMenuItem(JMenuItem... varargs) {
		for (JMenuItem item : varargs) {
			iconMenuItems.add(item);
		}
	}

}
