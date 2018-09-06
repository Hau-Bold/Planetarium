package universe;

public class Point3D {

	/** Coordinates corresponding to R^3 */
	private double x;
	private double y;
	private double z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Point3D minus(Point3D subtrahend) {

		return new Point3D(this.getX() - subtrahend.getX(), this.getY() - subtrahend.getY(),
				this.getZ() - subtrahend.getZ());
	}

	public Point3D plus(Point3D summand) {
		return new Point3D(this.getX() + summand.getX(), this.getY() + summand.getY(), this.getZ() + summand.getZ());
	}

	public static double norm(Point3D receiving) {
		double norm2 = receiving.getX() * receiving.getX() + receiving.getY() * receiving.getY()
				+ receiving.getZ() * receiving.getZ();
		if (Double.isNaN(norm2) || Double.isInfinite(norm2))
			throw new NumberFormatException("Vector length undefined, or infinite.");
		return Math.sqrt(norm2);
	}

	@Override
	public String toString() {
		return " [X: " + x + " Y: " + y + " Z: " + z + " ]";

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point3D other = (Point3D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

}
