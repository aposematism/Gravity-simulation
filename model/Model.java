package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
public class Model {
  public static final double size=900;
  public static final double gravitationalConstant=0.002;
  public static final double lightSpeed=10;//the smaller, the larger is the chunk of universe we simulate
  public static final double timeFrame=20;//the bigger, the shorter is the time of a step
  public List<Particle> p=new ArrayList<Particle>();
  public volatile List<DrawableParticle> pDraw=new ArrayList<DrawableParticle>();
  private List<Particle> removalList = new ArrayList<Particle>();

  /** 
   * This is the main step loop for the particles.
   * */
  public void step() {
    for(Particle p:this.p){p.interact(this);}
    mergeParticles();
    for(Particle p:this.p){p.move(this);}
    updateGraphicalRepresentation();
  }
  
  /** 
   * This is the 4th step in the step method;
   * It draws particles which expand by how their mass grows.
   * */
  private void updateGraphicalRepresentation() {
    ArrayList<DrawableParticle> d=new ArrayList<DrawableParticle>();
    Color c=Color.ORANGE;
    for(Particle p:this.p){
      d.add(new DrawableParticle((int)p.x, (int)p.y, (int)Math.sqrt(p.mass),c ));
    }
    this.pDraw=d;//atomic update
  }
  
  /** 
   * This is the 2nd step in the step method.
   * It takes all the particles which are going to be interacting
   * with each other, creating a stack of soon to be dead particles.
   * 
   * For each dead particle, out of the stack, it merges particles into new particles.
   * 
   * Bug: If three particles are touch. 1 touching 2, 2 touching 3 but 1 and 3 are not touching.
   * There will be a bug where if 1 is executed before 2, 1 and 2 will merge while 3 will not merge
   * as it will not know the new object. This is due to iterating through impacting rather than tmp.
   * 
   * Then it will then create a new particle in duplication of 3.
   * */
  public void mergeParticles(){
    Stack<Particle> deadPs=new Stack<Particle>();
    for(Particle p:this.p){ 
    	if(!p.impacting.isEmpty()){
    		deadPs.add(p);
    	};
    }
    this.p.removeAll(deadPs);
    removalList.addAll(deadPs);
    while(!deadPs.isEmpty()){
      Particle current=deadPs.pop();
      Set<Particle> ps=getSingleChunck(current);
      deadPs.removeAll(ps);//There is no removeAll for a stack. Probably intended for ps.
      this.p.add(mergeParticles(ps));
    }
  }
  
  /** 
   * This method takes a single particle, adds it to the impacting set.
   * It essentially attempts to find every single particle in the universe which is impacting with this particle.
   * It returns a hashset of those particles.
   * */
  private Set<Particle> getSingleChunck(Particle current){
    Set<Particle> impacting=new HashSet<Particle>();
    impacting.add(current);
    while(true){
      Set<Particle> tmp=new HashSet<Particle>();
      for(Particle pi:impacting){//This means it doesn't check any connecting objects.
    	  tmp.addAll(pi.impacting);
      }
      boolean changed=impacting.addAll(tmp);
      if(!changed){break;}//strange way to control this. 
      }
    //now impacting have all the chunk of collapsing particles
    return impacting;
  }
  
  /** 
   * This method seems to be designed to create a sort of gravitational accumulation of particles.
   * Creates new, larger particles from existing particles.
   * */
  public Particle mergeParticles(Set<Particle> ps){
	  double speedX=0;
      double speedY=0;
      double x=0;
      double y=0;
      double mass=0;
      for(Particle p:ps){  mass+=p.mass; }
      for(Particle p:ps){
        x+=p.x*p.mass;//Why is it mass for x. Why not (p.x + x)/2 since it is merging.
        y+=p.y*p.mass;//same for here.
        speedX+=p.speedX*p.mass;
        speedY+=p.speedY*p.mass;
        }
      x/=mass;
      y/=mass;
      speedX/=mass;
      speedY/=mass;
      return new Particle(mass,speedX,speedY,x,y);
  }
  
  public int getPNum() {
	  return p.size();
  }
  
  public List<Particle> getRemovalList(){
	  return removalList;
  }
}
