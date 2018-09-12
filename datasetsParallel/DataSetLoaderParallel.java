package datasetsParallel;

import java.util.Random;

import model.ModelParallel;
import model.ParticleParallel;

public class DataSetLoaderParallel {
  public static ModelParallel getRegularGrid(int min, int max,int distance){
    ModelParallel result=new ModelParallel();
    for(int i=min;i<max;i+=distance){
      for(int j=min;j<max;j+=distance){
        result.pL.add(new ParticleParallel(0.5,0,0,i,j));
        }
      }
    return result;
  }
  public static ModelParallel getRandomGrid(int min, int max,int distance){
    ModelParallel result=new ModelParallel();
    Random r=new Random(1);
    for(int i=min;i<max;i+=distance){
      for(int j=min;j<max;j+=distance){
        result.pL.add(new ParticleParallel(0.5,0,0,i+0.5-r.nextDouble(),j+0.5-r.nextDouble()));
        }
      }
    return result;
  }
  public static ModelParallel getRandomSet(int min, int max,int size){
    ModelParallel result=new ModelParallel();
    Random r=new Random(1);
    for(int i=0;i<size;i++){
      result.pL.add(new ParticleParallel(0.5,0,0,min+r.nextInt(max-min)+0.5-r.nextDouble(),min+r.nextInt(max-min)+0.5-r.nextDouble()));
      }
    return result;
  }
  public static ModelParallel getRandomRotatingGrid(int min, int max,int distance){
    ModelParallel result=new ModelParallel();
    Random r=new Random(1);
    for(int i=min;i<max;i+=distance){
      for(int j=min;j<max;j+=distance){
        result.pL.add(new ParticleParallel(0.5,Math.signum(j-450)*r.nextDouble()/10d,Math.signum(450-i)*r.nextDouble()/10d,i+r.nextDouble(),j+r.nextDouble()));
        }
      }
    return result;
  }

}
