package covidModelMP3DCC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class VirusCell {

	ContinuousSpace<Object>space;
	
	public VirusCell(ContinuousSpace<Object> space) {
		this.space = space;
	}
	
	// calculate the state for the next time tick for dead cells
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void step1() {
		this.lockToHostCellThroughSpikes();
	}
	
	private void lockToHostCellThroughSpikes() {
		int diffusionChoice = RandomHelper.nextIntFromTo(0, 1);
		if(diffusionChoice == 0) {
			this.fuseWithHostSurface();
		} else {
			this.getPulledInHostThroughSac();
		}
	}
	
	private void getPulledInHostThroughSac() {
		this.releaseGenes();
	}
	
	private void fuseWithHostSurface() {
		this.releaseGenes();
	}
	
	private void releaseGenes() {
		
	}
	
	private void move(){
		Context context = ContextUtils.getContext(this);
		Grid grid = (Grid)context.getProjection("Grid");
	
		List<GridPoint> emptySites = findEmptySites();
		
		// TODO add Grid.moveTo(Object o, GridPoint pt) to Repast API
		if (emptySites.size() > 0) grid.moveTo(this, emptySites.get(0).getX(), emptySites.get(0).getY());
	}
	
	private List<GridPoint> findEmptySites(){
		List<GridPoint> emptySites = new ArrayList<GridPoint>();
		Context context = ContextUtils.getContext(this);
		Grid grid = (Grid)context.getProjection("Grid");
		GridPoint pt = grid.getLocation(this);
		
		// Find Empty Moore neighbors
		// TODO automate via Repast API
		if (!grid.getObjectsAt(pt.getX()-1,pt.getY()+1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()-1,pt.getY()+1));
		if (!grid.getObjectsAt(pt.getX(),pt.getY()+1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX(),pt.getY()+1));
		if (!grid.getObjectsAt(pt.getX()+1,pt.getY()+1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()+1,pt.getY()+1));
		if (!grid.getObjectsAt(pt.getX()+1,pt.getY()).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()+1,pt.getY()));
		if (!grid.getObjectsAt(pt.getX()+1,pt.getY()-1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()+1,pt.getY()-1));
		if (!grid.getObjectsAt(pt.getX(),pt.getY()-1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX(),pt.getY()-1));
		if (!grid.getObjectsAt(pt.getX()-1,pt.getY()-1).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()-1,pt.getY()-1));
		if (!grid.getObjectsAt(pt.getX()-1,pt.getY()).iterator().hasNext())
			emptySites.add(new GridPoint(pt.getX()-1,pt.getY()));
		
		Collections.shuffle(emptySites);
		
		return emptySites;
	}
	
}
