import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		
		this.context = new DefaultContext<Object> ();
		HostCell builder = new HostCell ();
		context = builder.build(context);
		this.grid = (Grid<Object>) this.context.getProjection("grid");

	}

	@Test
	public void testVirusNumberInsideHealtyCellSac() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		
		this.context.clear();
		
		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);
		int virusCounterInside = 0;
		@SuppressWarnings("unchecked")
		Grid<Object> currentGrid = (Grid<Object>) this.context.getProjection("grid");
		for(Object o : currentGrid.getObjects() ) {
			System.out.println(currentGrid.getLocation(o) + " " + o.getClass());
			GridPoint oPoint = currentGrid.getLocation(o);
			if(o instanceof Sac && (oPoint.getX() <= currentGrid.getDimensions().getWidth() && oPoint.getY() <= currentGrid.getDimensions().getHeight() && oPoint.getZ() <= currentGrid.getDimensions().getDepth()))
				virusCounterInside++;
		}
		
		assertTrue(virusCounterInside == 0);
		
		for(int i=0; i<=1500; i++) { 
			schedule.execute();	
		}
		
		System.out.println("---------AFTER-----------");
		for(Object o : this.grid.getObjects() ) {
			System.out.println(this.grid.getLocation(o) + " " + o.getClass());
		}
		
		System.out.println(schedule.getTickCount());
		
		assertTrue(virusCounterInside == 1);

	}
	
	
	/*@Test
	public void testVirusNumberInsideHealtyCellGene() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();

		p.setValue("wayOfInfection", "gene");

		Ribosome rib1 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib1);
		this.grid.moveTo(rib1, 40,40,40);
		Ribosome rib2 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib2);
		this.grid.moveTo(rib2, 20,20,20);
		Ribosome rib3 = new Ribosome(this.context,this.space,this.grid);
		this.context.add(rib3);
		this.grid.moveTo(rib3, 35,35,35);
		VirusCell vc1 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc1);
		this.grid.moveTo(vc1, 55,55,55);
		VirusCell vc2 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc2);
		this.grid.moveTo(vc2, 57,57,57);
		VirusCell vc3 = new VirusCell(context, space, grid, "ingoing");
		this.context.add(vc3);
		this.grid.moveTo(vc3, 56,56,56);

		int virusCounterInside = 0;
		for(Object o : this.grid.getObjects()) {
			GridPoint oPoint = this.grid.getLocation(o);
			if(o instanceof Gene && (oPoint.getX() <= this.grid.getDimensions().getWidth() && oPoint.getY() <= this.grid.getDimensions().getHeight() && oPoint.getZ() <= this.grid.getDimensions().getDepth()))
				virusCounterInside++;
		}
		
		assertTrue(virusCounterInside == 0);
		
		for(int i=0; i<=4; i++) {
			schedule.execute();	
		}
		
		assertTrue(virusCounterInside == 1);

	}*/


}
