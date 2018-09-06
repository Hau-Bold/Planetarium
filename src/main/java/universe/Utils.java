package universe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Utils {

	/**
	 * rotates a received point3d by angle radiantX
	 * 
	 * @param point3d
	 *            - the point3d
	 * @param radiant
	 *            - the radiant
	 * @return transformed point
	 */
	public static Point3D rotateX(Point3D point3d, float radiant) {

		radiant = (float) Math.toRadians(radiant);

		double rotatedY = Math.cos(radiant) * point3d.getY() - Math.sin(radiant) * point3d.getZ();
		double rotatedZ = Math.sin(radiant) * point3d.getY() + Math.cos(radiant) * point3d.getZ();

		return new Point3D(point3d.getX(), rotatedY, rotatedZ);
	}

	/**
	 * rotates a received point3d by angle radiant
	 * 
	 * @param point3d
	 *            - the point3d
	 * @param radiant
	 *            - the radiant
	 * @return transformed point
	 */
	private static Point3D rotateY(Point3D point3d, float radiant) {

		radiant = (float) Math.toRadians(radiant);

		double rotatedX = Math.cos(radiant) * point3d.getX() + Math.sin(radiant) * point3d.getZ();
		double rotatedZ = -Math.sin(radiant) * point3d.getX() + Math.cos(radiant) * point3d.getZ();

		return new Point3D(rotatedX, point3d.getY(), rotatedZ);
	}

	/**
	 * rotates a received point3d by angle radiant
	 * 
	 * @param point3d
	 *            - the point3d
	 * @param radiant
	 *            - the radiant
	 * @return transformed point
	 */
	public static Point3D rotateZ(Point3D point3d, float radiant) {

		radiant = (float) Math.toRadians(radiant);

		double rotatedX = Math.cos(radiant) * point3d.getX() - Math.sin(radiant) * point3d.getY();
		double rotatedY = Math.sin(radiant) * point3d.getX() + Math.cos(radiant) * point3d.getY();

		return new Point3D(rotatedX, rotatedY, point3d.getZ());
	}

	/**
	 * rotate a received point around the coordinate axis
	 * 
	 * @param inclination
	 *            - array of radiant's
	 * @param point3d
	 * @return the transformed point
	 */
	public static Point3D rotatePoint3DInSpace(float[] inclination, Point3D point3d) {

		if (Integer.compare(inclination.length, 3) == 0) {
			/** since matrix-multiplication is associative */
			point3d = rotateX(point3d, inclination[0]);
			point3d = rotateY(point3d, inclination[1]);
			point3d = rotateZ(point3d, inclination[2]);

			return point3d;
		} else {
			throw new IndexOutOfBoundsException(
					String.format("inclination < %s > is incomplete", inclination.toString()));
		}
	}

	public static boolean isStringValid(String string) {
		return (string != null) && (!string.trim().equals(""));
	}

	/**
	 * to remove not accepted chars
	 * 
	 * @param receiving
	 *            - the String to examine
	 * @return - the transformed String
	 */
	public static String replaceUnusableChars(String receiving) {
		if (receiving != null) {
			receiving = receiving.replaceAll("ä", "ae");
			receiving = receiving.replaceAll("ü", "ue");
			receiving = receiving.replaceAll("ö", "oe");
			receiving = receiving.replaceAll("ß", "ss");
			receiving = receiving.replaceAll(" ", "");
		}
		return receiving;
	}

	public static List<Star> initializeStars(String path) {

		path = "C:/Users/Haubold/workspace/workspace/OPENGL/src/main/java/srcStarsystem/ngc.txt";

		List<Star> response = new ArrayList<Star>();

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String line = null;

		try {
			while ((line = bufferedReader.readLine()) != null) {
				String ar[] = line.split(" ");

				String rectaszension = ar[0];
				String declination = ar[1];

				response.add(new Star(transformRectaszensationToGrade(rectaszension),
						transformDeclinationToGrade(declination)));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * renders the stars
	 * 
	 * @param gl
	 *            - the GLAutoDrawable
	 * @param lstStars
	 *            - the receiving
	 * @param radius
	 *            - the radius of the stars
	 * @return
	 * @throws IOException
	 */
	public static FloatBuffer getFloatBufferForStars(GL2 gl, List<Star> lstStars, float radius) throws IOException {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_FLAT);

		// First: only one triangle
		FloatBuffer vertices = Buffers.newDirectFloatBuffer(/** lstStars.size() * */
				3);

		if (lstStars.size() > 0) {
			gl.glEnable(GL2.GL_COLOR_MATERIAL);

			for (Star item : lstStars) {

				float x = (float) (radius * Math.cos(item.getRectaszension()) * Math.cos(item.getDeclination()));
				float z = (float) (radius * Math.cos(item.getRectaszension()) * Math.sin(item.getDeclination()));
				float y = (float) (radius * Math.sin(item.getRectaszension()));

				// Defines a triangle
				vertices.put(x - 0.002f);
				vertices.put(y + 0.002f);
				vertices.put(z + 0.002f);

				break;

			}

			vertices.rewind();
		}

		return vertices;
	}

	public static double transformRectaszensationToGrade(String rectaszensation) {

		String hour_First = rectaszensation.substring(1, 2);
		String hour_Second = rectaszensation.substring(2, 3);
		String minute_First = rectaszensation.substring(3, 4);
		String minute_Second = rectaszensation.substring(4, 5);
		String sec_first = rectaszensation.substring(5, 6);
		String sec_second = rectaszensation.substring(6, 7);
		String sec_third = rectaszensation.substring(7, 8);

		int hour_FirstAsInt = Integer.valueOf(hour_First);
		int hour_SecondAsInt = Integer.valueOf(hour_Second);

		int minute_FirstAsInt = Integer.valueOf(minute_First);
		int minute_SecondAsInt = Integer.valueOf(minute_Second);

		int sec_firstAsInt = Integer.valueOf(sec_first);
		int sec_secondAsInt = Integer.valueOf(sec_second);
		int sec_thirdAsInt = Integer.valueOf(sec_third);

		double hours = (10 * hour_FirstAsInt + hour_SecondAsInt) * 15;
		double minutes = (10 * minute_FirstAsInt + minute_SecondAsInt) * (double) 1 / 60;
		double seconds = (100 * sec_firstAsInt + 10 * sec_secondAsInt + sec_thirdAsInt) * (double) 1 / 3600;

		return hours + minutes + seconds;

	}

	public static double transformDeclinationToGrade(String declination) {

		String degree = declination.substring(1, 3);
		String minute_First = declination.substring(3, 4);
		String minute_Second = declination.substring(4, 5);

		double degreeAsDouble = Double.valueOf(degree);

		double minute_FirstAsDouble = Double.valueOf(minute_First);
		double minute_SecondAsDouble = Double.valueOf(minute_Second);
		return degreeAsDouble + (1.0 / 10 * minute_FirstAsDouble + 1.0 / 100 * minute_SecondAsDouble);
	}

	public static Point3D computeLocationOnEarth(Planet planet, GpsCoordinate gps) {
		Point3D computedLocation;
		Point3D position = planet.getCurrentLocation();
		double radius = planet.getRadius();

		float xRotatio = planet.getXrotatio();
		float yRotatio = planet.getYrotatio();
		float zRotatio = planet.getZrotatio();

		float[] inclination = new float[] { xRotatio, yRotatio, zRotatio };

		double x = radius * Math.cos(gps.getLatitude()) * Math.cos(gps.getLongitude());
		double z = radius * Math.cos(gps.getLatitude()) * Math.sin(gps.getLongitude());
		double y = radius * Math.sin(gps.getLatitude());

		/** point on unity sphere */
		computedLocation = new Point3D(x, y, z);
		/** rotate point on sphere */
		computedLocation = rotatePoint3DInSpace(inclination, computedLocation);

		/** point on earth */
		return computedLocation.plus(position);
	}

	public static void animateSun(GL2 gl, double radius) // Draw The Blurred Image
	{

		int[] psiFloat = new int[10000];
		int[] phiFloat = new int[10000];
		Random psi = new Random();
		Random phi = new Random();

		for (int i = 0; i < 10000; i++) {
			psiFloat[i] = psi.nextInt(360);
			phiFloat[i] = phi.nextInt(180) - 180;
		}

		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glPushMatrix();
		for (int i = 0; i < 10000; i++) {

			double x = radius * Math.cos(psiFloat[i] / (Math.PI * 2)) * Math.cos(phiFloat[i] / (Math.PI * 2));
			double xStroke = (radius + 0.01) * Math.cos(psiFloat[i] / (Math.PI * 2))
					* Math.cos(phiFloat[i] / (Math.PI * 2));
			double z = radius * Math.cos(psiFloat[i] / (Math.PI * 2)) * Math.sin(phiFloat[i] / (Math.PI * 2));
			double zStroke = (radius + 0.01) * Math.cos(psiFloat[i] / (Math.PI * 2))
					* Math.sin(phiFloat[i] / (Math.PI * 2));
			double y = radius * Math.sin(psiFloat[i] / (Math.PI * 2));
			double yStroke = (radius + 0.01) * Math.sin(psiFloat[i] / (Math.PI * 2));

			gl.glColor3d(255, 255, 0);
			gl.glVertex3d(x + 0.001, y, z);
			gl.glVertex3d(x - 0.001, y, z);
			gl.glVertex3d(xStroke, yStroke, zStroke);

		}

		gl.glEnd();
		gl.glFlush();
		gl.glPopMatrix();
	}

	public static List<Star> initializeSuns(List<Star> lstStars) {
		List<Star> lstSuns = new ArrayList<Star>();

		int counterOfSuns = 0;

		Iterator<Star> iterator = lstStars.iterator();

		while (iterator.hasNext()) {
			Star current = iterator.next();
			if (Integer.compare(counterOfSuns % 20, 0) == 0) {
				lstSuns.add(current);
				iterator.remove();
			}
			counterOfSuns++;
		}

		return lstSuns;
	}

}
