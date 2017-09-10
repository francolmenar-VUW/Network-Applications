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
		System.out.println(cost + this.neighbourName);
		for(String s : destinations) {
			System.out.println("String " + s + " in the column "+ this.neighbourName);
			if(this.neighbourName.equals(s)) {//If it is the same name we have to save the cost of it
				System.out.println("Inside");
				this.destinations.put(this.neighbourName, cost);//I store the new cost
			}
			else {//The cost is unknown
				this.destinations.put(s, -1);//I store the -1 as - infinity
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
