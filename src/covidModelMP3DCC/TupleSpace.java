package covidModelMP3DCC;

import java.util.ArrayList;

public class TupleSpace {

	private ArrayList<Tuple>space;

	
	public TupleSpace() {
		this.space = new ArrayList<>();
	}
	
	
	public Tuple in(String name, Object object){
		Tuple chosenEntry = null;
		for(Tuple currentEntry : this.space)
			if(currentEntry.getName().equals(name))
				chosenEntry = currentEntry;
		this.space.remove(chosenEntry);
		return chosenEntry;
	}
	
	public Tuple rd(String name){
		Tuple chosenEntry = null;
		for(Tuple currentEntry : this.space)
			if(currentEntry.getName().equals(name))
				chosenEntry = currentEntry;
		return chosenEntry;
	}
	
	public void out(String name, Object object){
		this.space.add(new Tuple(name, object));
	}
	
}
