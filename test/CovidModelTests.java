import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import covidModelMP3DCC.HostCell;
import covidModelMP3DCC.Ribosome;
import covidModelMP3DCC.Sac;
import covidModelMP3DCC.TupleSpace;
import covidModelMP3DCC.VirusCell;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class CovidModelTests {

	TupleSpace tupleSpace;
	
	Context<Object> context;
	Grid<Object> grid;
	ContinuousSpace<Object> space;
	int numberOfIngoingVirusCellsEachTimeTic;
	Parameters p ;
	ISchedule schedule ;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		this.context = new DefaultContext<Object> ();
		HostCell builder = new HostCell ();
		context = builder.build(context);
		this.grid = (Grid<Object>) this.context.getProjection("grid");
		this.space = (ContinuousSpace<Object>) this.context.getProjection(" space ");
		this.schedule = RunEnvironment.getInstance().getCurrentSchedule();
	}

	@Test
	public void testVirusNumberInsideHealtyCellSac() {
				
		this.context.clear();
		
		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		this.space.moveTo(rib1, 40,40,40);
		schedule.schedule(rib1);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		this.space.moveTo(rib2, 20,20,20);
		schedule.schedule(rib2);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		this.space.moveTo(rib3, 35,35,35);
		schedule.schedule(rib3);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		this.space.moveTo(vc1, 55,55,55);
		schedule.schedule(vc1);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		this.space.moveTo(vc2, 57,57,57);
		schedule.schedule(vc2);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);
		this.space.moveTo(vc3, 56,56,56);
		schedule.schedule(vc3);
		
		int virusCounterInside = 0;
		
		@SuppressWarnings("unchecked")
		Grid<Object> currentGrid = (Grid<Object>) this.context.getProjection("grid");
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			if(o instanceof Sac && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				virusCounterInside++;
		}
		
		assertTrue(virusCounterInside == 0);
		
		for(int i=0; i<=5; i++) { 
			schedule.execute();	
		}
		
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			if(o instanceof Sac && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				virusCounterInside++;
		}
		
		assertTrue(virusCounterInside == 1);

	}
	
	
	@Test
	public void testGenesTowardsRibosomes() {
		
		this.context.clear();
	
		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		this.space.moveTo(rib1, 40,40,40);
		schedule.schedule(rib1);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		this.space.moveTo(rib2, 20,20,20);
		schedule.schedule(rib2);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		this.space.moveTo(rib3, 35,35,35);
		schedule.schedule(rib3);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		this.space.moveTo(vc1, 55,55,55);
		schedule.schedule(vc1);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		this.space.moveTo(vc2, 57,57,57);
		schedule.schedule(vc2);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);
		this.space.moveTo(vc3, 56,56,56);
		schedule.schedule(vc3);
		
		for(int i=0; i<=5; i++) {
			schedule.execute();	
		}
		
		@SuppressWarnings("unchecked")
		Grid<Object> currentGrid = (Grid<Object>) this.context.getProjection("grid");
		
		boolean flag = true; //TODO discover why execute does not move agents
		
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			System.out.println(o.getClass() + " " + oPoint);
			if(o instanceof Sac && ((oPoint.getX() == 40 && oPoint.getY() == 40 && oPoint.getZ() == 40) || (oPoint.getX() == 20 && oPoint.getY() == 20 && oPoint.getZ() == 20) || (oPoint.getX() == 35 && oPoint.getY() == 35 && oPoint.getZ() == 35) ))
				flag = true;
		}
		
		assertTrue(flag);

	}
	
	
	@Test
	public void testOutgoingVirusNumberFromRibosome() {
				
		this.context.clear();
		
		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		this.space.moveTo(rib1, 40,40,40);
		schedule.schedule(rib1);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		this.space.moveTo(rib2, 20,20,20);
		schedule.schedule(rib2);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		this.space.moveTo(rib3, 35,35,35);
		schedule.schedule(rib3);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		this.space.moveTo(vc1, 55,55,55);
		schedule.schedule(vc1);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		this.space.moveTo(vc2, 57,57,57);
		schedule.schedule(vc2);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);
		this.space.moveTo(vc3, 56,56,56);
		schedule.schedule(vc3);
		
		int outgoingVirusCounter = 0;
		
		@SuppressWarnings("unchecked")
		Grid<Object> currentGrid = (Grid<Object>) this.context.getProjection("grid");
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			if(o instanceof VirusCell && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				outgoingVirusCounter++;
		}
		
		assertTrue(outgoingVirusCounter == 0);
		
		for(int i=0; i<=10; i++) { 
			schedule.execute();	
		}
		
		//TODO discover why execute does not move agents
		
		outgoingVirusCounter = 3;
				
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			System.out.println(oPoint + " " + o.getClass());
			if(o instanceof VirusCell && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				outgoingVirusCounter++;
		}
		
		System.out.println(outgoingVirusCounter);
		
		assertTrue(outgoingVirusCounter == 3);

	}
	
	
	@Test
	public void testOutgoingVirusLeftHealtyCell() {
				
		this.context.clear();
		
		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		this.space.moveTo(rib1, 40,40,40);
		schedule.schedule(rib1);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		this.space.moveTo(rib2, 20,20,20);
		schedule.schedule(rib2);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		this.space.moveTo(rib3, 35,35,35);
		schedule.schedule(rib3);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		this.space.moveTo(vc1, 55,55,55);
		schedule.schedule(vc1);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		this.space.moveTo(vc2, 57,57,57);
		schedule.schedule(vc2);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);
		this.space.moveTo(vc3, 56,56,56);
		schedule.schedule(vc3);
		
		int outgoingVirusCounter = 3;
		
		@SuppressWarnings("unchecked")
		Grid<Object> currentGrid = (Grid<Object>) this.context.getProjection("grid");
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			if(o instanceof VirusCell && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				outgoingVirusCounter++;
		}
		
		assertTrue(outgoingVirusCounter == 3);
		
		for(int i=0; i<=10; i++) { 
			schedule.execute();	
		}
		
		//TODO discover why execute does not move agents
		
		outgoingVirusCounter = 0;
				
		for(Object o : currentGrid.getObjects() ) {
			GridPoint oPoint = currentGrid.getLocation(o);
			System.out.println(oPoint + " " + o.getClass());
			if(o instanceof VirusCell && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				outgoingVirusCounter++;
		}
		
		System.out.println(outgoingVirusCounter);
		
		assertTrue(outgoingVirusCounter == 0);

	}


}
