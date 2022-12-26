package covidModelMP3DCC;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Gene {

	ContinuousSpace<Object> space;
	Grid<Object> grid;
	Context<?> context;
	TupleSpace tupleSpace;
	
	private static final double MOVE_DISTANCE = 0.5; // Set the distance to move each tick
	 
	public Gene(Context<?> context, ContinuousSpace<Object> space, Grid<Object> grid) {
		this.tupleSpace = TupleSpace.getInstance();
		this.context = context;
		this.space = space;
		this.grid = grid;
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 4)

	public void step1() {
		this.moveTowards(this.getCloserRibosome());
	}
	
	private GridPoint getCloserRibosome() {
		GridPoint closerRibosomePoint = null;
		double closerDistance = Double.MAX_VALUE;
		for(Object obj : this.grid.getObjects())
			if(obj instanceof Ribosome) {
				GridPoint gridObjPoint = this.grid.getLocation(obj);
				double currentGridDistance = this.grid.getDistance(this.grid.getLocation(this), gridObjPoint);
				if(currentGridDistance < closerDistance) {
					closerDistance = currentGridDistance;
					closerRibosomePoint = gridObjPoint;
				}
			}
		return closerRibosomePoint;
	}
	
	public void moveTowards(GridPoint pt) { 
		
		GridPoint myPoint = grid.getLocation(this);
		
		double distance = Math.sqrt(Math.pow(pt.getX() - myPoint.getX(), 2) + Math.pow(pt.getY() - myPoint.getY(), 2) + Math.pow(pt.getZ() - myPoint.getZ(), 2));
		
		if (distance > 2) {
		    // Calculate the direction in which to move
			double directionX = pt.getX() - myPoint.getX();
		    double directionY = pt.getY() - myPoint.getY();
		    double directionZ = pt.getZ() - myPoint.getZ();

		    // Move in the direction of the target
		    grid.moveTo(this, (int)(myPoint.getX() + directionX * MOVE_DISTANCE), (int)(myPoint.getY() + directionY * MOVE_DISTANCE), (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));
		    
			GridPoint currentGridLocation = this.grid.getLocation(this);
			space.moveTo(this, currentGridLocation.getX(), currentGridLocation.getY(), currentGridLocation.getZ());
		} else {
			this.tupleSpace.out("ribosome_call", grid.getLocation(this));
			context.remove(this);
		}
	}
	
}
