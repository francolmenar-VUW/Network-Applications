package lab4;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ecs100.UI;


public class RoutingTable {
	private ArrayList<NeighbourDestinations> neighbourToDestination;//It is a list of the columns of the routing table
	private String [] destinations;//The name of all the possible destinations
	private final int sideOfSquare = 50;

	public RoutingTable(HashMap<String, Integer> neighbours, String[] destinationsName) {
		this.destinations = destinationsName;//I set all the destinations' names
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

	/**
	 * It prints the routing table
	 * @param name: The String that contains the name of the node
	 * @return: the routing table as a String
	 */
	public void printTable(String name) {
		UI.clearGraphics();
		UI.setColor(Color.black);
		drawTable(name);
	}

	/**
	 * It draws the table empty
	 * @param name: The String that contains the name of the node
	 */
	private void drawTable(String name) {
		drawHorizontalLines();
		drawParallelLines(name);

	}

	/**
	 * It prints the parallel lines of the table
	 * @param name: The String that contains the name of the node
	 */
	private void drawParallelLines(String name) {
		int x = sideOfSquare * 2, y = sideOfSquare;//The start coordinates of the table
		for(int i = 0; i <= neighbourToDestination.size(); i++ ) {//I go through all the neigbours
			UI.drawLine(x, y, x,  sideOfSquare *(destinations.length+2));
			drawParallelInfo(i, x, y, name);
			x += sideOfSquare;//To the next column
		}
	}

	/**
	 * It draws the rows'header
	 * @param i: iteration of the loop
	 * @param x: x coordinate of the square where the String has to be drawn
	 * @param y: y coordinate of the square where the String has to be drawn
	 * @param name: The String that contains the name of the node
	 */
	private void drawParallelInfo(int i, int x, int y, String name) {
		if(i != neighbourToDestination.size()) {//The last line has no letter
			String columnName = neighbourToDestination.get(i).getNeighbourName();
			drawString(columnName, x , y);//I draw the name of the header
			drawCosts(i);//I draw the costs of this column
		}
		else {//In the last iteration I write the name of the node which is the RoutingTable
			String columnName = "D(" + name + ")";
			drawString(columnName, sideOfSquare - sideOfSquare/5 , sideOfSquare);//I draw the name of the header
		}
	}


	/**
	 * It draws the costs of the table
	 * @param i: iteration of the loop
	 */
	private void drawCosts(int i) {
		int x = sideOfSquare, y = sideOfSquare ;//The start coordinates of the table
		NeighbourDestinations currentColumn = neighbourToDestination.get(i);
		for(int j = 0; j < destinations.length; j++) {
			System.out.println("Costs of destination " + destinations[j] + ": " + currentColumn.getDestinations().get(destinations[j]));
			System.out.println(destinations[j]);
			String cost = Integer.toString(currentColumn.getDestinations().get(destinations[j]));//I get the current cost
			if(j == 0) {
				System.out.println(cost + "First");
				drawString(cost, (i + 2)* x, y + sideOfSquare);
			}
			else {
				drawString(cost, (i + 2) * x, (y * j) + sideOfSquare * 2);
			}
		}
	}


	/**
	 * It draws a String in a certain part of the window
	 * @param message: The String to be drawn
	 * @param x: x coordinate of the square where the String has to be drawn
	 * @param y: y coordinate of the square where the String has to be drawn
	 */

	private void drawString(String message, int x, int y) {
		UI.drawString(message, x + (sideOfSquare/2) - (sideOfSquare/15), y + (sideOfSquare/2));	//I calculate more or less the center of the square to draw the String there
	}


	/**
	 * It draws the horizontal lines of the table
	 */
	private void drawHorizontalLines() {
		int x = sideOfSquare, y = sideOfSquare * 2;//The start coordinates of the table
		for(int i = 0; i <= destinations.length; i++ ) {//I go through all the destinations
			UI.drawLine(x, y, sideOfSquare *(neighbourToDestination.size()+2), y);
			if(i != destinations.length) {//The last line has no letter
				String rowName = destinations[i];
				drawString(rowName, x , y);//I draw the name of the header
			}
			y += sideOfSquare;//To the next row
		}
	}

}
