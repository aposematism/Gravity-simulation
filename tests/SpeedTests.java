package tests;

import org.junit.jupiter.api.Test;

import datasets.DataSetLoader;
import datasetsParallel.DataSetLoaderParallel;
import model.Model;
import model.ModelParallel;

public class SpeedTests {
	@Test
	public void SingleRun() {
		double ut=System.currentTimeMillis();
		double t10 = 0, t5 = 0, t3 = 0, t1 = 0;
		boolean b10 = false; boolean b5 = false; boolean b3 = false; boolean b1 = false;
		Model m = DataSetLoader.getRandomSet(100, 800, 100);
		while(m.getPNum() > 1) {
			m.step();
			if(m.getPNum() == 10 && b10 != true) {
				t10 = System.currentTimeMillis()-ut;
				b10 = true;
			}
			if(m.getPNum() == 5 && b5 != true) {
				t5 = System.currentTimeMillis()-ut;
				b5 = true;
			}
			if(m.getPNum() == 3 && b3 != true) {
				t3 = System.currentTimeMillis()-ut;
				b3 = true;
			}
			if(m.getPNum() == 1 && b1 != true) {
				t1 = System.currentTimeMillis()-ut;
				b1 = true;
			}
		}
		System.out.println("Running a single sequential test");
		System.out.printf("Time till 10 particles left: %f \n", (t10));
		System.out.printf("Time till 5 particles left: %f \n", (t5));
		System.out.printf("Time till 3 particles left: %f \n", (t3));
		System.out.printf("Time till 1 particles left: %f \n", (t1));
	}
	
	@Test
	public void SingleRunConcurrent() {
		double ut=System.currentTimeMillis();
		double t10 = 0, t5 = 0, t3 = 0, t1 = 0;
		boolean b10 = false; boolean b5 = false; boolean b3 = false; boolean b1 = false;
		ModelParallel m = DataSetLoaderParallel.getRandomSet(100, 800, 100);
		while(m.getPNum() > 1) {
			m.step();
			if(m.getPNum() == 10 && b10 != true) {
				t10 = System.currentTimeMillis()-ut;
				b10 = true;
			}
			if(m.getPNum() == 5 && b5 != true) {
				t5 = System.currentTimeMillis()-ut;
				b5 = true;
			}
			if(m.getPNum() == 3 && b3 != true) {
				t3 = System.currentTimeMillis()-ut;
				b3 = true;
			}
			if(m.getPNum() == 1 && b1 != true) {
				t1 = System.currentTimeMillis()-ut;
				b1 = true;
			}
		}
		System.out.println("Running a single concurrent test");
		System.out.printf("Time till 10 particles left: %f \n", (t10));
		System.out.printf("Time till 5 particles left: %f \n", (t5));
		System.out.printf("Time till 3 particles left: %f \n", (t3));
		System.out.printf("Time till 1 particles left: %f \n", (t1));
	}
}
