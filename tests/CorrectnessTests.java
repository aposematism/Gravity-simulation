package tests;
import java.util.List;
import org.junit.jupiter.api.Test;
import datasets.DataSetLoader;
import datasetsParallel.DataSetLoaderParallel;
import model.Model;
import model.ModelParallel;
import model.Particle;
import model.ParticleParallel;

public class CorrectnessTests {
	
	@Test
	public void compareRemovalOrderRandomParticles() {
		Model m = DataSetLoader.getRandomSet(100, 800, 100);
		ModelParallel mp = DataSetLoaderParallel.getRandomSet(100, 800, 100);
		while(mp.getPNum() > 1 && m.getPNum() > 1) {
			m.step(); mp.step();
		}
		List<Particle> mList = m.getRemovalList();
		List<ParticleParallel> mpList = mp.getRemovalList();
		for(int i = 0; i < mpList.size(); i++) {
			assert(mList.get(i).x == mpList.get(i).getPosX()); 
			assert(mList.get(i).y == mpList.get(i).getPosY()); 
		}
	}
}