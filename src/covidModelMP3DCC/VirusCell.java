package covidModelMP3DCC;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class VirusCell {

	ContinuousSpace<Object>space;
	Context<Object> context;
	Grid<Object> grid;
	String state;
	GridPoint spawnPoint;
	TupleSpace tupleSpace;
	GridPoint randomLocationOutsideEnvironment;
	String wayOfInfection;

	private static final double MOVE_DISTANCE = 0.06; // Set the distance to move each tick

	public VirusCell(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, String state) {
		this.state = state;
		this.grid = grid;
		this.context = context;
		this.tupleSpace = TupleSpace.getInstance();
		this.space = space;
		Parameters p = RunEnvironment.getInstance().getParameters();	
		this.wayOfInfection = (String)p.getValue("wayOfInfection");
	}
	
	@ScheduledMethod(start=1, interval=1, priority = 2)

	public void step1() {
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();

		System.out.println(schedule.getTickCount());
		if(this.spawnPoint == null)
			this.spawnPoint = grid.getLocation(this);

		if(this.state.equals("ingoing"))
			this.moveTowards(this.getCloserRibosome(), state);
		else if(this.state.equals("outgoing")) {
			if(this.randomLocationOutsideEnvironment == null)
				randomLocationOutsideEnvironment = this.getRandomLocationOutsideEnvironment();
			this.moveBackwardsToRandomLocation();
		} else if(this.state.equals("rejected")) {
			this.moveBackwardsToSpawn();
		}
	}
	
	private GridPoint getRandomLocationOutsideEnvironment() {
		
		int x = RandomHelper.nextIntFromTo(-200, 250);
		while(!(x <= -50 || x >= 100)) {
			x = RandomHelper.nextIntFromTo(-200, 250);
		}
		int y = RandomHelper.nextIntFromTo(-200, 250);
		while(!(y <= -50 || y >= 100)) {
			y = RandomHelper.nextIntFromTo(-200, 250);
		}
		int z = RandomHelper.nextIntFromTo(-200, 250);
		while(!(z <= -50 || z >= 100)) {
			z = RandomHelper.nextIntFromTo(-200, 250);
		}
		return new GridPoint(x,y,z);
	}

	private GridPoint getCloserRibosome() {
		GridPoint closerRibosomePoint = null;
		double closerDistance = Double.MAX_VALUE;
		for(Object obj : this.grid.getObjects())
			if(obj instanceof Ribosome) {
				GridPoint gridObjPoint = this.grid.getLocation(obj);
				System.out.println("ssssssssssss" + this.grid.getLocation(this)+ " " + gridObjPoint);
				double currentGridDistance = this.grid.getDistance(this.grid.getLocation(this), gridObjPoint);
				if(currentGridDistance < closerDistance) {
					closerDistance = currentGridDistance;
					closerRibosomePoint = gridObjPoint;
				}
			}
		return closerRibosomePoint;
	}
	
	public void moveTowards(GridPoint pt, String ingoingType) { 

		GridPoint myPoint = grid.getLocation(this);
		
		if (!((myPoint.getX() <= 50 && myPoint.getX() >= 0) && (myPoint.getY() <= 50 && myPoint.getY() >= 0) && (myPoint.getZ() <= 50 && myPoint.getZ() >= 0) )) {
		    // Calculate the direction in which to move
			double directionX = pt.getX() - myPoint.getX();
		    double directionY = pt.getY() - myPoint.getY();
		    double directionZ = pt.getZ() - myPoint.getZ();
		    
		    // Move in the direction of the target
		    grid.moveTo(this, (int)(myPoint.getX() + directionX * MOVE_DISTANCE), (int)(myPoint.getY() + directionY * MOVE_DISTANCE), (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));
		    
			GridPoint currentGridLocation = this.grid.getLocation(this);
			space.moveTo(this, currentGridLocation.getX(), currentGridLocation.getY(), currentGridLocation.getZ());
		} else {
			Tuple tupleEntry = this.tupleSpace.in("cellula");
			if(tupleEntry != null) {
			//Entry cellula disponibile e riservata a questa virus cell
				this.initIngoingVirus(this.wayOfInfection);
			} else {
			//Entry cellula non disponibile
				this.state = "rejected";
			}
		}
	}
	
	public void moveBackwardsToRandomLocation() { 
		GridPoint myPoint = grid.getLocation(this);

		if(!myPoint.equals(randomLocationOutsideEnvironment)) {
			//Calculate the direction in which to move
			double directionX = this.randomLocationOutsideEnvironment.getX() - myPoint.getX();
			double directionY = this.randomLocationOutsideEnvironment.getY() - myPoint.getY();
			double directionZ = this.randomLocationOutsideEnvironment.getZ() - myPoint.getZ();

			// Move in the direction of the target
			grid.moveTo(this, (int)(myPoint.getX() + directionX * MOVE_DISTANCE), (int)(myPoint.getY() + directionY * MOVE_DISTANCE), (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));

			GridPoint currentGridLocation = this.grid.getLocation(this);
			space.moveTo(this, currentGridLocation.getX(), currentGridLocation.getY(), currentGridLocation.getZ());
		} else {
			context.remove(this);
		}
	}

	public void moveBackwardsToSpawn() { 
		GridPoint myPoint = grid.getLocation(this);

		if(!myPoint.equals(spawnPoint)) {
			//Calculate the direction in which to move
			double directionX = this.spawnPoint.getX() - myPoint.getX();
			double directionY = this.spawnPoint.getY() - myPoint.getY();
			double directionZ = this.spawnPoint.getZ() - myPoint.getZ();
			
			// Move in the direction of the target
			grid.moveTo(this, (int)(myPoint.getX() + directionX * MOVE_DISTANCE), (int)(myPoint.getY() + directionY * MOVE_DISTANCE), (int)(myPoint.getZ() + directionZ * MOVE_DISTANCE));

			GridPoint currentGridLocation = this.grid.getLocation(this);
			space.moveTo(this, currentGridLocation.getX(), currentGridLocation.getY(), currentGridLocation.getZ());
		} else {
			context.remove(this);
		}
	}
	
	private void initIngoingVirus(String ingoingType) {
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
