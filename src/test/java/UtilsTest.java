import static org.junit.Assert.assertEquals;

import org.junit.Test;

import universe.Point3D;
import universe.Utils;

public class UtilsTest {

	@Test
	public void rotateZTest() {
		Point3D point3d = new Point3D(1, 0, 0);

		float radiant = 90;
		Point3D expected = new Point3D(-4.371139000186241E-8, 0.999999999999999, 0);

		assertEquals(expected, Utils.rotateZ(point3d, radiant));
	}

}
