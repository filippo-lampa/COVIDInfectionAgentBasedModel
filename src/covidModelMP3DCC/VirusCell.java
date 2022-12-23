package covidModelMP3DCC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class VirusCell {

	ContinuousSpace<Object>space;
	Context context;
	Grid grid;
	String state;
	
	TupleSpace tupleSpace;
	
	private static final double MOVE_DISTANCE = 0.5; // Set the distance to move each tick

	public VirusCell(Context context, ContinuousSpace<Object> space, Grid Grid, String state) {
		this.state = state;
		this.tupleSpace = new TupleSpace();
		this.space = space;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step1() {
		
		if(this.state.equals("ingoing"))
			this.moveTowards(this.getCloserRibosome(), state);
		else if(this.state.equals("outgoing")) {
			GridPoint randomLocationOutsideEnvironment = this.getRandomLocationOutsideEnvironment();
			this.moveTowards(randomLocationOutsideEnvironment, null);
		}
	}
	
	private GridPoint getRandomLocationOutsideEnvironment() {
		int x = RandomHelper.nextIntFromTo(100, 200);
		int y = RandomHelper.nextIntFromTo(100, 200);
		int z = RandomHelper.nextIntFromTo(100, 200);
		return new GridPoint(x,y,z);
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
	
	public void moveTowards(GridPoint pt, String ingoingType) { 
		
		GridPoint myPoint = grid.getLocation(this);
		
		double distance = Math.sqrt(Math.pow(pt.getX() - myPoint.getX(), 2) + Math.pow(pt.getY() - myPoint.getY(), 2) + Math.pow(pt.getZ() - myPoint.getZ(), 2));
		
		if (!(myPoint.getX() == 50 && myPoint.getY() == 50 && myPoint.getZ() == 50)) {
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
		} else {
			Tuple tupleEntry = this.tupleSpace.in("cellula");

			if(tupleEntry.object != null) {
			//Entry cellula disponibile e riservata a questa virus cell
				this.initIngoingVirus(ingoingType);
			} else {
			//Entry cellula non disponibile
			moveTowards(new GridPoint(-pt.getX(),-pt.getY(),-pt.getZ()),null);
			}
		}
	}
	
	
	private void initIngoingVirus(String ingoingType) {
		//int randomEnteringChoice = RandomHelper.nextIntFromTo(0,1); 
		if(ingoingType.equals("sac")) {
			initializeSac();
		} else initializeGenes();
		
	}
	
	private void initializeSac() {
		Sac newSac = new Sac(context, space, grid);
		GridPoint thisCellPoint = this.grid.getLocation(this);
		context.add(newSac);

		space.moveTo(newSac,thisCellPoint.getX(),thisCellPoint.getY(),thisCellPoint.getZ());
		grid.moveTo(newSac, thisCellPoint.getX(),thisCellPoint.getY(),thisCellPoint.getZ());

		context.remove(this);
	}
	
	private void initializeGenes() {
		Gene newGene = new Gene(context, space, grid);
		GridPoint thisCellPoint = this.grid.getLocation(this);
		context.add(newGene);

		space.moveTo(newGene,thisCellPoint.getX(),thisCellPoint.getY(),thisCellPoint.getZ());
		grid.moveTo(newGene, thisCellPoint.getX(),thisCellPoint.getY(),thisCellPoint.getZ());

		context.remove(this);
	}
	
}
