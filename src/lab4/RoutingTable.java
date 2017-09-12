package lab4;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import ecs100.UI;


public class RoutingTable {
    private String name; // the node name of the RoutingTable
	private ArrayList<NeighbourDestinations> neighbourToDestination;//It is a list of the columns of the routing table
	private String [] destinations;//The name of all the possible destinations

	public RoutingTable(HashMap<String, Integer> neighbours, String[] destinationsName, String name) {
		this.destinations = destinationsName;//I set all the destinations' names
		this.name = name;
		initializeRoutingTable(neighbours);
	}

	/**
	 * It initializes the RoutingTable
	 * @param neighbours: the neighbours of the node
	 */
	private void initializeRoutingTable(HashMap<String, Integer> neighbours) {
		neighbourToDestination = new ArrayList<NeighbourDestinations>();
		for(HashMap.Entry<String, Integer> n : neighbours.entrySet()) {//I iterate through all the entries of the HashMap
			newNeighbour(n.getKey(), n.getValue());//I create a new column of the RoutingTable
		}
	}

	/**
	 * It creates a new neighbour entry in the RoutingTable
	 * Graphically it is a new column
	 * @param name: The name of the node
	 * @param cost: Cost to go to that node
	 */
	private void newNeighbour(String name, Integer cost) {
		NeighbourDestinations newNeighbour = new NeighbourDestinations(name, cost, destinations);//I create a new column of the RoutingTable
		neighbourToDestination.add(newNeighbour);//I add the new neighbour-column
	}


	/**
	 * It prints the routing table
	 * @param message: The String that contains the name of the node
	 * @return: the routing table as a String
	 */
	public void printTable(String message) {
		for(int i = 0; i <= destinations.length; i++) {//I go through all the rows
			if(i == 0 || destinations[i-1].equals(name) == false) {//If it is not the same row as itself
				for(int j = 0; j <= neighbourToDestination.size(); j++) {//I go through all the columns
					if(i == 0) {//In the first row it is not print a destination
						if(j != 0) {//In the same position it is written the Dx
							message += "\t" + neighbourToDestination.get(j-1).getNeighbourName();
						}
					}
					else {//Not the first iteration
						if(j != 0) {//In the same position it is written the Dx
							int cost = neighbourToDestination.get(j-1).getDestinations().get(destinations[i-1]);//I get the cost
							if(cost == 999) {//Infinity
								message += "\t\u221e";
							}
							else {//I known number
								message += "\t" + Integer.toString(cost);
							}
						}
						else {//First column
							message += "  " + destinations[i-1];
						}
					}
				}
				message +="\n\n";
			}
		}
		UI.print(message);
	}

	/**
	 * It returns a specific column of the routing table
	 * @param name: It is the header of the column we want to return
	 * @return the column if it is found and null otherwise
	 */
	public NeighbourDestinations returnNeighbourColumn(String name) {
		for(int i = 0; i < neighbourToDestination.size(); i++) {//I go through all the columns of the routing table
			String neighbourName = neighbourToDestination.get(i).getNeighbourName();//I get the header of the column
			if(name.equals(neighbourName)){//If the name is the same as the header it is the wanted column
				return neighbourToDestination.get(i);//I return the column
			}
		}
		return null;//The column was not found
	}

	public ArrayList<NeighbourDestinations> getNeighbourToDestination() {
		return neighbourToDestination;
	}

	public void setNeighbourToDestination(ArrayList<NeighbourDestinations> neighbourToDestination) {
		this.neighbourToDestination = neighbourToDestination;
	}

	public String[] getDestinations() {
		return destinations;
	}

	public void setDestinations(String[] destinations) {
		this.destinations = destinations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
