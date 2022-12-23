package covidModelMP3DCC;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.SimpleGridAdder;

public class HostCell implements ContextBuilder<Object> {
	
	TupleSpace tupleSpace;
	
	@Override
	public Context build(Context<Object> context) {
		
		context.setId("CovidModelMP3DCC");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace < Object > space = spaceFactory.createContinuousSpace (" space ", context, new RandomCartesianAdder < Object >(),
				new repast.simphony.space.continuous.InfiniteBorders<>(), 50, 50, 50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
 		Grid<Object> grid = gridFactory.createGrid("grid", context, GridBuilderParameters.multiOccupancyND( 
 				new SimpleGridAdder<Object>(), new InfiniteBorders<>(), 50, 50, 50));

 		
		Parameters p = RunEnvironment.getInstance().getParameters();			

		int numberOfIngoingVirusCellsEachTimeTic = (Integer)p.getValue("numberOfIngoingVirusCellsEachTimeTic");

		int numberOfRibosomes = (Integer)p.getValue("numberOfRibosomes");
		
		this.getIncomingInfectedCells(context, space, grid, numberOfIngoingVirusCellsEachTimeTic); 
		
		this.initializeRibosomes(numberOfRibosomes, context, space, grid);
		
		this.tupleSpace = new TupleSpace();
		
		this.tupleSpace.out("cellula", this);
				
		//this.initializeRibosomes(numberOfRibosomes, context, space, grid);
		//this.initIngoingVirus(context, space, grid, numberOfInfectedCells, ingoingType);
		
		return context;
	}
	
	@ScheduledMethod(start = 1, interval = 7)
	private void getIncomingInfectedCells(Context<Object> context, ContinuousSpace<Object> space, Grid grid, int numberOfIngoingVirusCellsEachTimeTic) {
		VirusCell newVirusCell;
		int x,y,z;
		for(int i=0; i<numberOfIngoingVirusCellsEachTimeTic; i++) {	
			newVirusCell = new VirusCell(context, space, grid, "ingoing");
			x = RandomHelper.nextIntFromTo(60, 80);
			y = RandomHelper.nextIntFromTo(60, 80);
			z = RandomHelper.nextIntFromTo(60, 80);
			context.add(newVirusCell);
			if(!space.getObjectsAt(x,y,z).iterator().hasNext()) {
				space.moveTo(newVirusCell,x,y,z);
				grid.moveTo(newVirusCell, x,y,z);
			}
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
