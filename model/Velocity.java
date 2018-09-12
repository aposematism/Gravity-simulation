package model;

/** 
 * This class essentially contains all the information of a velocity vector.
 * 
 * */
public class Velocity {
	private volatile double speedX;
	private volatile double speedY;
	Velocity(double pX, double pY){
		speedX = pX;
		speedY = pY;
	}
	
	Velocity(ParticleParallel a, ParticleParallel b, Distance d){
		double dirX= -(Math.signum(a.getPosX()-b.getPosX()));
	    double dirY= -(Math.signum(a.getPosY()-b.getPosY()));
		double vecX=(b.getMass()*ModelParallel.gravitationalConstant*dirX)/d.getDist();
	    double vecY=(b.getMass()*ModelParallel.gravitationalConstant*dirY)/d.getDist();
	    double newSpeedX=(a.getVeloX()+vecX)/(1+(a.getVeloX()*vecX)/ModelParallel.lightSpeed);
	    double newSpeedY=(a.getVeloY()+vecY)/(1+(a.getVeloY()*vecY)/ModelParallel.lightSpeed);
	    if(newSpeedX > ModelParallel.lightSpeed) {
	    	speedX = ModelParallel.lightSpeed;
		    speedY = newSpeedY;
	    }
	    else if(newSpeedY > ModelParallel.lightSpeed) {
	    	speedX = newSpeedX;
		    speedY = ModelParallel.lightSpeed;
	    }
	    else {
	    	speedX = newSpeedX;
		    speedY = newSpeedY;
	    }
	}
		
	public double getX() {return speedX;}
	public double getY() {return speedY;}
}
