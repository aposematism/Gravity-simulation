package model;

public class Position {
	private volatile double posX;
	private volatile double posY;
	Position(double pX, double pY){
		posX = pX;
		posY = pY;
	}
	
	public double getX() {return posX;}
	public double getY() {return posY;}
	
	public boolean equals(Position p) {
		if(posX == p.getX()) {
			if(posY == p.getY()) {
				return true;
			}
		}
		return false;
	}
}
