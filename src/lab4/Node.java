/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4;

/**
 *
 * @author aliahmed
 */

import java.util.HashMap;

public class Node
{
    private String name; // the node name
    private int xPos, yPos; // the positions to draw the node
    private HashMap<String, Integer> neighbours; // the list of neighbours
    private RoutingTable routingTable;


    /**
     * @param n: the node name
     * @param xp: the x position
     * @param yp: the y position
     */
    public Node(String n, int xp, int yp)
    {
        this.name = n;
        this.xPos = xp;
        this.yPos = yp;
        this.neighbours = new HashMap<String, Integer>();
    }

    /**
     * Initializes the routing table
     * @param destinations: It is the name of all the posible destinations
     */
    public void initialise(String[] destinations)
    {
        this.routingTable = new RoutingTable(neighbours, destinations, name);
    }

    /**
     * Updates the routing table
     *
     */
    public void updateRoutingTable(String fromNode, String nodeData, int newCost)
    {
    }

    public void simulateNewLink(String fromNode, String nodeData, int newCost)
    {
    }

    /**
     * @param nodeName:the neighbour node name
     * @param cost: the cost of the link
     */
    public void addNeighbour(String nodeName, int cost)
    {
        this.neighbours.put(nodeName, cost);
    }

    /**
     * It prints the RoutingTable
     */
    public void printRoutingTable() {
    	String message = "D(" + name + ")";
    	routingTable.printTable(message);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * @param xPos the xPos to set
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the yPos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * @param yPos the yPos to set
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return the neighbours
     */
    public HashMap<String, Integer> getNeighbours() {
        return neighbours;
    }

    /**
     * @param neighbours the neighbours to set
     */
    public void setNeighbours(HashMap<String, Integer> neighbours) {
        this.neighbours = neighbours;
    }

	public RoutingTable getRoutingTable() {
		return routingTable;
	}

	public void setRoutingTable(RoutingTable routingTable) {
		this.routingTable = routingTable;
	}
}
