package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import model.DrawableParticle;

public class ModelParallel {
  public static final double size=900;
  public static final double gravitationalConstant=0.002;
  public static final double lightSpeed=10;//the smaller, the larger is the chunk of universe we simulate
  public static final double timeFrame=20;//the bigger, the shorter is the time of a step
  public volatile List<ParticleParallel> pL=new ArrayList<ParticleParallel>();
  public volatile List<DrawableParticle> pDraw=new ArrayList<DrawableParticle>();
  
  private List<ParticleParallel> removalList = new ArrayList<ParticleParallel>();
  
  /** 
   * This is the main step loop for the particles.
   * */
  public void step() {
	  try {
		  interactions();
		  updateGraphicalRepresentation();
		  mergeParticles();
		  movements();
	  }
	  catch(InterruptedException e){
		  e.printStackTrace();
	  }
  }
  
  /** 
   * This method is supposed to run a synchronized thread safe list to go through all interact operations.
   * */
  private List<ParticleParallel> interactions() throws InterruptedException{
	  List<ParticleParallel> dL = duplicateList();
	  synchronized(dL) {
		  for(ParticleParallel p:dL){
			  p.interact(dL);
		  }
	  }
	  return dL;
  }
  
  /** 
   * This method is supposed to run a synchronized thread safe list to go through all the move operations/.
   * */
  private void movements() throws InterruptedException{
	  List<ParticleParallel> dL = duplicateList();
	  synchronized(dL) {
		  for(ParticleParallel p: dL){
			  p.move();
		  }
	  }
  }
  
  /** 
   * This is the 4th step in the step method;
   * It draws particles which expand by how their mass grows.
   * */
  private void updateGraphicalRepresentation() throws InterruptedException{
    ArrayList<DrawableParticle> d=new ArrayList<DrawableParticle>();
    Color c=Color.ORANGE;
    List<ParticleParallel> dL = duplicateList();
    synchronized(dL) {
    	for(ParticleParallel p:dL){
    		d.add(new DrawableParticle((int)p.getPosX(), (int)p.getPosY(), (int)Math.sqrt(p.getMass()),c ));
    	}
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
  public void mergeParticles() throws InterruptedException{
	List<ParticleParallel> dL = duplicateList();
    Stack<ParticleParallel> deadPs=new Stack<ParticleParallel>();
    for(ParticleParallel p:dL){ 
    	if(!p.getImpacting().isEmpty()){
    		deadPs.add(p);
    	};
    }
    dL.removeAll(deadPs);
    removalList.addAll(deadPs);
    while(!deadPs.isEmpty()){
      ParticleParallel current=deadPs.pop();
      Set<ParticleParallel> ps=getSingleChunck(current);
      deadPs.removeAll(ps);//There is no removeAll for a stack. Probably intended for ps.
      dL.add(mergeParticles(ps));
    }
  }
  
  /** 
   * This method takes a single particle, adds it to the impacting set.
   * It essentially attempts to find every single particle in the universe which is impacting with this particle.
   * It returns a hashset of those particles.
   * 
   * I ignored the race condition here. There is a possibility of the retrieved particle which is impacting being unavailable.
   * However, my conclusion was that there are two chances to retrieve it and it is unlikely that it will be locked on the second try.
   * */
  private Set<ParticleParallel> getSingleChunck(ParticleParallel current){
    Set<ParticleParallel> impacting=new HashSet<ParticleParallel>();
    impacting.add(current);
    while(true){
      Set<ParticleParallel> tmp=new HashSet<ParticleParallel>();
      for(ParticleParallel pi:impacting){//This means it doesn't check any connecting objects.
    	  tmp.addAll(pi.getImpacting());
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
  public ParticleParallel mergeParticles(Set<ParticleParallel> ps){
	  double speedX=0;
      double speedY=0;
      double x=0;
      double y=0;
      double mass=0;
      for(ParticleParallel p:ps){  mass+=p.getMass(); }
      for(ParticleParallel p:ps){
    	  x+=p.getPosX()*p.getMass();//Why is it mass for x. Why not since it is merging.
    	  y+=p.getPosY()*p.getMass();//same for here.
    	  speedX+=p.getVeloX()*p.getMass();
    	  speedY+=p.getVeloY()*p.getMass();
      }
      x/=mass;
      y/=mass;
      speedX/=mass;
      speedY/=mass;
      //System.out.println("New Particle created.");
      return new ParticleParallel(mass,speedX,speedY,x,y);
  }
  
  /** 
   * 
   * */
  private List<ParticleParallel> duplicateList() {
	  List<ParticleParallel> dupList = Collections.synchronizedList(pL);
	  return dupList;
  }
  
  public int getPNum() {
	  return duplicateList().size();
  }
  
  public List<ParticleParallel> getRemovalList(){
	  return removalList;
  }
}
