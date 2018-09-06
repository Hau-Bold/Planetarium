package cameraToEarth;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdom.JDOMException;

import client.AppRoot;
import contextmenu.Constants;
import contextmenu.IconButton;
import universe.GPS;
import universe.GpsCoordinate;
import universe.Utils;

public class CameraToEarth extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6471958532391000723L;
	private final int X = 10;
	private final int Y = 10;
	private final int WIDTH = 500;
	private final int HEIGHT = 160;

	private static CameraToEarth instance = null;

	private JPanel panel;

	private JLabel lblLongitude, lblLatitude, lblstreet, lblcity, lblcountry;
	private JTextField txtStreet, txtCity, txtCountry, txtLatitude, txtLongitude;
	private IconButton confirmIconButton;

	private AppRoot appRoot;

	/**
	 * Constructor.
	 * 
	 * @param appRoot
	 */
	private CameraToEarth(AppRoot appRoot) {

		this.appRoot = appRoot;

		// Setting the Layout
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		initComponent();
	}

	private void initComponent() {

		this.setTitle(Constants.SET_LOCATION);
		this.setBounds(X + 100, Y, WIDTH, HEIGHT);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {

			@SuppressWarnings("unused")
			public void windowIsClosing() {
				instance.dispose();
			}
		});
		/** orders when window is closing */

		panel = new JPanel();
		panel.setLayout(null);
		this.getContentPane().add(panel);

		/** Street */
		lblstreet = new JLabel(Constants.STREET);
		lblstreet.setOpaque(true);
		lblstreet.setBounds(20, 20, 50, 20);
		lblstreet.setVisible(Boolean.TRUE);
		panel.add(lblstreet);

		txtStreet = new JTextField();
		txtStreet.setBounds(80, 20, 100, 20);
		txtStreet.setFocusable(true);
		txtStreet.setBackground(Color.GREEN);
		txtStreet.setVisible(Boolean.TRUE);
		panel.add(txtStreet);

		/** City */
		lblcity = new JLabel(Constants.CITY);
		lblcity.setOpaque(true);
		lblcity.setBounds(20, 50, 50, 20);
		lblcity.setVisible(Boolean.TRUE);
		panel.add(lblcity);

		txtCity = new JTextField();
		txtCity.setBounds(80, 50, 100, 20);
		txtCity.setFocusable(true);
		txtCity.setBackground(Color.GREEN);
		txtCity.setVisible(Boolean.TRUE);
		panel.add(txtCity);

		/** Country */
		lblcountry = new JLabel(Constants.COUNTRY);
		lblcountry.setOpaque(true);
		lblcountry.setBounds(20, 80, 50, 20);
		lblcountry.setVisible(Boolean.TRUE);
		panel.add(lblcountry);

		txtCountry = new JTextField();
		txtCountry.setBounds(80, 80, 100, 20);
		txtCountry.setFocusable(true);
		txtCountry.setBackground(Color.GREEN);
		txtCountry.setVisible(Boolean.TRUE);
		panel.add(txtCountry);

		/** Longitude */
		lblLongitude = new JLabel(Constants.LONGITUDE);
		lblLongitude.setOpaque(true);
		lblLongitude.setBounds(200, 20, 50, 20);
		lblLongitude.setVisible(Boolean.TRUE);
		panel.add(lblLongitude);

		txtLongitude = new JTextField();
		txtLongitude.setBounds(260, 20, 100, 20);
		txtLongitude.setFocusable(true);
		txtLongitude.setBackground(Color.GREEN);
		txtLongitude.setVisible(Boolean.TRUE);
		panel.add(txtLongitude);

		/** Latitude */
		lblLatitude = new JLabel(Constants.LATITUDE);
		lblLatitude.setOpaque(true);
		lblLatitude.setBounds(200, 50, 50, 20);
		lblLatitude.setVisible(Boolean.TRUE);
		panel.add(lblLatitude);

		txtLatitude = new JTextField();
		txtLatitude.setBounds(260, 50, 100, 20);
		txtLatitude.setFocusable(true);
		txtLatitude.setBackground(Color.GREEN);
		txtLatitude.setVisible(Boolean.TRUE);
		panel.add(txtLatitude);

		confirmIconButton = new IconButton("confirm.png", 200, 80);
		confirmIconButton.setMnemonic(KeyEvent.VK_C);
		confirmIconButton.addActionListener(this);
		panel.add(confirmIconButton);

	}

	/**
	 * yields an instance of the class CameraToEarth
	 * 
	 * @param appRoot
	 * 
	 * @return - the instance
	 */
	public static CameraToEarth getInstance(AppRoot appRoot) {
		if (instance == null) {
			instance = new CameraToEarth(appRoot);
		}
		return instance;
	}

	public void showFrame() {
		this.setVisible(true);
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(confirmIconButton)) {
			if (Utils.isStringValid(txtCity.getText())) {
				StringBuilder builder = new StringBuilder();

				if (Utils.isStringValid(txtStreet.getText())) {
					builder.append(txtStreet.getText().trim());
					builder.append(",");
				}
				builder.append(txtCity.getText().trim());

				if (Utils.isStringValid(txtCountry.getText())) {
					builder.append(",");
					builder.append(txtCountry.getText().trim());
				}

				GpsCoordinate gps = null;

				/** to request the coordinates */
				try {
					gps = GPS.requestGPS(Utils.replaceUnusableChars(builder.toString()));
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				} catch (JDOMException e1) {
					e1.printStackTrace();
				}

				if (gps != null) {
					/** saving the address */
					gps.setStreet(txtStreet.getText());
					gps.setCity(txtCity.getText());
					gps.setCountry(txtCountry.getText());

					txtLongitude.setText(String.valueOf(gps.getLongitude()));
					txtLatitude.setText(String.valueOf(gps.getLatitude()));

					appRoot.setGPS(gps);
					appRoot.setCameraWillBeMoved(Boolean.TRUE);

				}

			}
		}

	}

}
