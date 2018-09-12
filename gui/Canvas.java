package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import model.ModelParallel;
import model.DrawableParticle;
/** 
 * This method is the main GUI panel container for the game. 
 * Creating the canvas and using the drawable particles to draw each particle to the Jpanel.
 * */
public class Canvas extends JPanel{
  ModelParallel m; Canvas(ModelParallel m){this.m=m;}
  @Override public void paint(Graphics gg){
    Graphics2D g=(Graphics2D)gg;
    g.setBackground(Color.DARK_GRAY);
    g.clearRect(0, 0, getWidth(),getHeight());
    for(DrawableParticle p: m.pDraw){p.draw(g);}
  }
  @Override public Dimension getPreferredSize(){
    return new Dimension((int)ModelParallel.size, (int)ModelParallel.size);
    }
}
