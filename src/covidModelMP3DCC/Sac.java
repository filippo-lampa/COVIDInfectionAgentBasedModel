package covidModelMP3DCC;

import org.apache.commons.math3.geometry.Space;
import org.apache.poi.util.SystemOutLogger;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.Direction;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Sac {

	 private static final double MOVE_DISTANCE = 0.5; // Set the distance to move each tick
	 private static final double EPSILON = 1; // Set a small value to add to the y coordinate
	  
	ContinuousSpace<Object>space;
	Grid<Object> grid;
	int numberOfInfectedCells;
	
	public Sac(ContinuousSpace<Object> space, Grid<Object> grid, int numberOfInfectedCells) {
		this.numberOfInfectedCells = numberOfInfectedCells;
		this.space = space;
		this.grid = grid;
	}
	
	@ScheduledMethod(start = 1, interval = 10)
	public void step1() {
		System.out.println("Current sac position: " + this.grid.getLocation(this));
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
		System.out.println("Current closer ribosome position " + (int)closerRibosomePoint.getX() + " " + (int)closerRibosomePoint.getY() + " " + (int)closerRibosomePoint.getZ() + " current closer ribosome distance " + closerDistance);
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

		    System.out.println(directionX);
		    System.out.println(directionY);
		    System.out.println(directionZ);
		    
		    System.out.println("x: " + (int)(myPoint.getX() + directionX * MOVE_DISTANCE) + " y: "  + myPoint.getY() + (int)(directionY * MOVE_DISTANCE) + " z: " +  (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));
		    // Move in the direction of the target
		    grid.moveTo(this, (int)(myPoint.getX() + directionX * MOVE_DISTANCE), (int)(myPoint.getY() + directionY * MOVE_DISTANCE), (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));
		    
			GridPoint currentGridLocation = this.grid.getLocation(this);
			space.moveTo(this, currentGridLocation.getX(), currentGridLocation.getY(), currentGridLocation.getZ());
		} else System.out.println("NCS");
	}
	
}
