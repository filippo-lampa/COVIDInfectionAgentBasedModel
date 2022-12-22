package covidModelMP3DCC;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.SimpleGridAdder;

public class HostCell implements ContextBuilder<Object> {

	private ArrayList<Object>tuplespace;
	
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
		
		int numberOfRibosomes = (Integer)p.getValue("numberOfRibosomes");
		 
		int numberOfInfectedCells = (Integer)p.getValue("numberOfInfectedCells");
		
		this.initializeRibosomes(numberOfRibosomes, context, space, grid);
		initIngoingVirus(context, space, grid, numberOfInfectedCells);
		
		return context;
	}
	
	private void initIngoingVirus(Context<Object> context, ContinuousSpace<Object> space, Grid grid, int numberOfInfectedCells) {
		int randomEnteringChoice = RandomHelper.nextIntFromTo(0,1); 
		if(randomEnteringChoice == 0)
			initializeSac(context, space, grid, numberOfInfectedCells);
		else initializeGenes(context, space, grid, numberOfInfectedCells);
		
	}
	
	private void initializeSac(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, int numberOfInfectedCells) {
		Sac newSac = new Sac(space, grid, numberOfInfectedCells);
		int x = RandomHelper.nextIntFromTo(60, 80);
		int y = RandomHelper.nextIntFromTo(60, 80);
		int z = RandomHelper.nextIntFromTo(60, 80);
		context.add(newSac);
		if(!space.getObjectsAt(x,y,z).iterator().hasNext()) {
			space.moveTo(newSac,x,y,z);
			grid.moveTo(newSac, x,y,z);
		}
		System.out.println("Sac init position " + x + " " + y + " " + z);
	}
	
	
	private void initializeRibosomes(int numberOfRibosomes, Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid) {
		for(int i=0; i < numberOfRibosomes; i++) {
			Ribosome newRibosome = new Ribosome(space, grid);
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
	
	private void initializeGenes(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, int numberOfInfectedCells) {
		Gene newGene = new Gene(space, grid, numberOfInfectedCells);
		int x = RandomHelper.nextIntFromTo(60, 80);
		int y = RandomHelper.nextIntFromTo(60, 80);
		int z = RandomHelper.nextIntFromTo(60, 80);
		context.add(newGene);
		if(!space.getObjectsAt(x,y,z).iterator().hasNext()) {
			space.moveTo(newGene,x,y,z);
			grid.moveTo(newGene, x,y,z);
		}
		System.out.println("Sac init position " + x + " " + y + " " + z);
	}
	
	//TUPLE SPACE OPERATIONS
	
	public void in(String name){
		
	}
	
	public void rd(String name){
		
	}
	
	public void rdp(String name){
		
	}
	
	public void inp(String name){
		
	}
	
	public void eval(String name){
		
	}
	
	public void out(Object object){
		
	}
	
	
	

}
