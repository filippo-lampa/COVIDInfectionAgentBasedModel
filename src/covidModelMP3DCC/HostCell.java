package covidModelMP3DCC;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.SimpleGridAdder;

public class HostCell extends DefaultContext<Object> implements ContextBuilder<Object> {
	
	TupleSpace tupleSpace;
	
	Context<Object> context;
	Grid<Object> grid;
	ContinuousSpace<Object> space;
	
	int numberOfIngoingVirusCellsEachTimeTic;
	
	@Override
	public Context<Object> build(Context<Object> context) {
		
		context.setId("CovidModelMP3DCC");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		this.space = spaceFactory.createContinuousSpace (" space ", context, new RandomCartesianAdder < Object >(),
				new repast.simphony.space.continuous.InfiniteBorders<>(), 50, 50, 50);
		
	
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
 		this.grid = gridFactory.createGrid("grid", context, GridBuilderParameters.multiOccupancyND( 
 				new SimpleGridAdder<Object>(), new InfiniteBorders<>(), 50, 50, 50));

 		this.context = context;
 		
 		Parameters p;
 		
		//Check for test purpose
 		if(RunEnvironment.getInstance() != null) {
 			p = RunEnvironment.getInstance().getParameters();
 			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
 		    ScheduleParameters params = ScheduleParameters.createRepeating(1, 20, 5);
 		    schedule.schedule(params, this, "getIncomingInfectedCells");
 		}
 		else {
 			//test case
 			Schedule schedule = new Schedule ();
 			ParametersCreator pc = new ParametersCreator();
 			pc.addParameter("wayOfInfection", String.class, "sac", false);
 			pc.addParameter("numberOfIngoingVirusCellsEachTimeTic", int.class, 3, false);
 			pc.addParameter("numberOfRibosomes", int.class, 3, false);
 			p = pc.createParameters();
 			RunEnvironment.init( schedule , null , p , true );	
 			ISchedule schedule1 = RunEnvironment.getInstance().getCurrentSchedule();
 		    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1, 5);
 		    schedule1.schedule(params, this, "testPurpose");
 		}
		
		int numberOfRibosomes;
		
		this.numberOfIngoingVirusCellsEachTimeTic = (Integer)p.getValue("numberOfIngoingVirusCellsEachTimeTic");
		
		numberOfRibosomes = (Integer)p.getValue("numberOfRibosomes");
		
		this.initializeRibosomes(numberOfRibosomes, context, space, grid);
		
		this.getIncomingInfectedCells();
		
		this.tupleSpace = TupleSpace.getInstance();
		
		this.tupleSpace.out("cellula", this);
		
		return context;
	}
	
	public void testPurpose() {
		
	}
	
	public void getIncomingInfectedCells() {
		
		VirusCell newVirusCell;
		int x,y,z;
		for(int i=0; i<numberOfIngoingVirusCellsEachTimeTic; i++) {	
			newVirusCell = new VirusCell(context, space, grid, "ingoing");
			
			x = RandomHelper.nextIntFromTo(-200, 200);
			y = RandomHelper.nextIntFromTo(-200, 200);
			z = RandomHelper.nextIntFromTo(-200, 200);
			while(!((x <= -20 || x >= 70) || (y <= -20 || y >= 70) || (z <= -20 || z >= 70))) {
				x = RandomHelper.nextIntFromTo(-200, 200);
				y = RandomHelper.nextIntFromTo(-200, 200);
				z = RandomHelper.nextIntFromTo(-200, 200);
			}

			context.add(newVirusCell);

			space.moveTo(newVirusCell,x,y,z);
			grid.moveTo(newVirusCell, x,y,z);
			
		}
	}
		
	private void initializeRibosomes(int numberOfRibosomes, Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid) {
		for(int i=0; i < numberOfRibosomes; i++) {
			Ribosome newRibosome = new Ribosome(context, space, grid);
			context.add(newRibosome);
			int x = RandomHelper.nextIntFromTo(0, 50 - 1);
			int y = RandomHelper.nextIntFromTo(0, 50 - 1);
			int z = RandomHelper.nextIntFromTo(0, 50 - 1);
			if(!space.getObjectsAt(x,y,z).iterator().hasNext()) {
				grid.moveTo(newRibosome,x,y,z);
				space.moveTo(newRibosome,x,y,z);
			}
		}
	}
	
}
