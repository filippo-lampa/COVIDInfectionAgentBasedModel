package covidModelMP3DCC;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Ribosome {
	
	ContinuousSpace<Object> space;
	Grid<Object> grid;
	
	public Ribosome(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	/*@ScheduledMethod(start = 1, interval = 1)
	public void step() {

	}*/
	
}
