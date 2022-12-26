package covidModelMP3DCC;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Sac {

	private static final double MOVE_DISTANCE = 0.5; // Set the distance to move each tick
	 
	int[] randomPoint; 
	ContinuousSpace<Object>space;
	Grid<Object> grid;
	Context<Object> context;
	
	public Sac(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid) {
		this.context = context;
		this.space = space;
		this.grid = grid;
		this.randomPoint = this.getRandomLocation();
	}
	
	private int[] getRandomLocation() {
		int x = RandomHelper.nextIntFromTo(0, 50);
		int y = RandomHelper.nextIntFromTo(0, 50);
		int z = RandomHelper.nextIntFromTo(0, 50);
		return new int[]{x,y,z};
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 4)

	public void step1() {
		this.moveTowards(new GridPoint(this.randomPoint[0],this.randomPoint[1],this.randomPoint[2]));
		
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
			Gene newGene = new Gene(this.context, this.space, this.grid);
			context.add(newGene);
			GridPoint myLocation = this.grid.getLocation(this);
			this.grid.moveTo(newGene, myLocation.getX(),myLocation.getY(),myLocation.getZ());
			this.space.moveTo(newGene, myLocation.getX(),myLocation.getY(),myLocation.getZ());
			context.remove(this);
		}
	}
	
}
