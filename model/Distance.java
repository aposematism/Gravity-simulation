package model;

public class Distance {
	private double dX;
	private double dY;
	
	public Distance(double x, double y) {
		dX = x;
		dY = y;
	}
	
	public double getX() {
		return dX;
	}
	
	public double getY() {
		return dY;
	}
	
	public double getDist() {
		return Math.sqrt(dX+dY);
	}

}
