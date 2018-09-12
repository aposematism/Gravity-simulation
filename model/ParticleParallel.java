package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * This class was converted to a monitor pattern.
 * 
 * But since there are only two classes which are accessed by Model, only move() and interact() matter. 
 * By synchronizing the methods themselves, we can only lock the particle object.
 * Move() does not require any other particle.
 * 
 * Interact() risks an occasional risk of race condition.
 * */
public class ParticleParallel {
  public ParticleParallel(double mass, double speedX, double speedY, double x, double y){
    this.mass = mass;
    velo = new Velocity(speedX, speedY);
    pos = new Position(x, y);
  }
  private Set<ParticleParallel> impacting = new HashSet<ParticleParallel>();
  private double mass;
  private volatile Velocity velo;
  private volatile Position pos;
  
  /** 
   * This method calculates the movement of the particle through some area of space.
   * */
  public synchronized void move(){
    //uncomment the following to have particle bouncing on the boundary
    if(this.pos.getX()<0){
    	if(Math.signum(this.velo.getX()) < 0) {
    		this.velo = new Velocity(-(this.velo.getX()), this.velo.getY());
    	}
    //System.out.printf("%f %f \n", this.pos.getX(), this.velo.getX());
    }
    if(this.pos.getY()<0){
    	if(Math.signum(this.velo.getY()) < 0) {
    		this.velo = new Velocity(this.velo.getX(), -(this.velo.getY()));
    	}
    //System.out.printf("%f %f \n", this.pos.getY(), this.velo.getY());
    }
    if(this.pos.getX()>ModelParallel.size){
    	if(Math.signum(this.velo.getX()) > 0){
    		this.velo = new Velocity(-(this.velo.getX()), this.velo.getY());	
    	}
    }
    if(this.pos.getY()>ModelParallel.size){
    	if(Math.signum(this.velo.getY()) > 0){
    		this.velo = new Velocity(this.velo.getX(), -(this.velo.getY()));
    	}
    }
    this.pos = new Position(pos.getX()+(this.velo.getX()/(ModelParallel.timeFrame)), pos.getY()+(velo.getY()/(ModelParallel.timeFrame)));
  }
  
  /** 
   * This method calculates the distance between particles.
   * if the masses cross over, then return true.
   * */
  private boolean isImpact(double dist,double otherMass){
    if(Double.isNaN(dist)){return true;}
    double distMass=Math.sqrt(mass)+Math.sqrt(otherMass);
    //System.out.printf("%f %f \n", distMass*distMass, dist);
    if(dist<distMass*distMass){
    	 //System.out.println("Particles merged!");
    	return true;}
    return false;
  }
  
  /** 
   * This method calculates the distance between particles using the iterable<particle>
   * */
  public boolean isImpact(Iterable<ParticleParallel> ps){
    for(ParticleParallel p:ps){
      if(this==p){continue;}
      Distance dist=distance2(p);
      if(isImpact(dist.getX()+dist.getY(),p.getMass())){
    	  System.out.println("Particles merged!");
    	  return true;}
      }
    return false;
  }
  
  /** 
   * Calculates the distances between this particle and another.
   * */
  private Distance distance2(ParticleParallel p){
    double distX=this.pos.getX()-p.getPosX();
    double distY=this.pos.getY()-p.getPosY();
    return new Distance(distX*distX, distY*distY);
  }
  
  /** 
   * So the interaction essentially calculates a gravity in respective directions
   * for each particle until it has found a general speed. 
   * If it is impacting another particle, it doesn't recalculate its velocity.
   * */
  public synchronized void interact(List<ParticleParallel> pL){//
    for(ParticleParallel p:pL){
      if(p==this) {continue;}
      //double dirX= -(Math.signum(this.getPosX()-p.getPosX()));
      //double dirY= -(Math.signum(this.getPosY()-p.getPosY()));
      Distance dist=distance2(p);
      //System.out.printf("%f \n", dist);
      if(isImpact(dist.getDist(),p.getMass())){this.impacting.add(p);continue;}
      //double vecX=(p.getMass()*ModelParallel.gravitationalConstant*dirX)/dist.getDist();
      //double vecY=(p.getMass()*ModelParallel.gravitationalConstant*dirY)/dist.getDist();
      //double newSpeedX=(this.getVeloX()+vecX)/(1+(this.getVeloX()*vecX)/ModelParallel.lightSpeed);
      //double newSpeedY=(this.getVeloY()+vecY)/(1+(this.getVeloY()*vecY)/ModelParallel.lightSpeed);
      //if(!Double.isNaN(newSpeedX) && !Double.isNaN(newSpeedY)){
    	  //this.velo = new Velocity(newSpeedX, newSpeedY);
    	  this.velo = new Velocity(this, p, dist);
      //} 
    }
  }
  
  public double getPosX() {
	  return pos.getX();
  }
  
  public double getPosY() {
	  return pos.getY();
  }
  
  public double getVeloX() {
	  return velo.getX();
  }
  
  public double getVeloY() {
	  return velo.getY();
  }
  
  public double getMass() {
	  return mass;
  }
  
  public Set<ParticleParallel> getImpacting(){
	  return impacting;
  }
}
