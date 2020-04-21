package lab4;

import java.util.HashMap;

public class NeighbourDestinations {
    private String neighbourName; // the node name
    private HashMap<String, Integer> destinations; // the list of destinations

	public NeighbourDestinations(String name, Integer cost, String[] destinations) {
		this.neighbourName = name;
		initializeDestinations(cost, destinations);
	}

	/**
	 * It initializes the destinations
	 * @param name: name of the position to update
	 * @param cost: cost to go to the node
	 * @param destinations: all the possible destinations
	 */
	private void initializeDestinations(Integer cost, String[] destinations) {
		this.destinations = new HashMap<String, Integer>();
		for(String s : destinations) {
			if(this.neighbourName.equals(s)) {//If it is the same name we have to save the cost of it
				this.destinations.put(this.neighbourName, cost);//I store the new cost
			}
			else {//The cost is unknown
				this.destinations.put(s, 999);//I store the -1 as - infinity
			}
		}
	}

	public String getNeighbourName() {
		return neighbourName;
	}

	public void setNeighbourName(String neighbourName) {
		this.neighbourName = neighbourName;
	}

	public HashMap<String, Integer> getDestinations() {
		return destinations;
	}

	public void setDestinations(HashMap<String, Integer> destinations) {
		this.destinations = destinations;
	}

}
