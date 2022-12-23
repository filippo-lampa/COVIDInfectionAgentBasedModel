package covidModelMP3DCC;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Ribosome {

	Context<Object> context;
	ContinuousSpace<Object> space;
	Grid<Object> grid;
	TupleSpace tupleSpace;

	public Ribosome(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid) {
		this.context = context;
		this.tupleSpace = TupleSpace.getInstance();
		this.space = space;
		this.grid = grid;
	}


	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void step() {

		Parameters p = RunEnvironment.getInstance().getParameters();			

		int numberOfOutgoingInfectedCells = (Integer)p.getValue("numberOfOutgoingInfectedCells");

		Tuple tuple = this.tupleSpace.rd("ribosome_call");
		if(tuple != null) {

			VirusCell newCell;
			GridPoint thisPoint = this.grid.getLocation(this);
			GridPoint genePoint = (GridPoint)tuple.getObject();
			double distance = Math.sqrt(Math.pow(thisPoint.getX() - genePoint.getX(), 2) + Math.pow(thisPoint.getY() - genePoint.getY(), 2) + Math.pow(thisPoint.getZ() - genePoint.getZ(), 2));
			if(distance <= 2) {
				this.tupleSpace.in("ribosome_call");
				for(int i=0; i<numberOfOutgoingInfectedCells; i++) {
					newCell = new VirusCell(this.context, this.space, this.grid, "outgoing");
					context.add(newCell);
					this.grid.moveTo(newCell, thisPoint.getX(), thisPoint.getY(), thisPoint.getZ());
					this.space.moveTo(newCell, thisPoint.getX(), thisPoint.getY(), thisPoint.getZ());
				}
			}
		}
	}

}
