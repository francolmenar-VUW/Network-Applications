package lab4;

/**
*
* @author aliahmed
*/
import ecs100.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
import java.util.Vector;
import java.util.Set;

public class MainWindow{

		private Vector<Node> nodes = new Vector<Node>();
		private String [] destinations;


		/**
	     * The empty constructor of MainWindow just create the UI
	     */
	    public MainWindow()
	    {
	        UI.addButton("load map", this::load);
	        UI.addButton("Draw Nodes", this::draw);

	        UI.addButton("Start", this::start);
	        UI.addButton("Show Routing tables", this::showRoutingTables);
	        UI.addButton("Redraw", this::redraw);



	    }

	    /**
	     *  1. initialise DONE
	        2. update neighbours
	     */
	    public void start()
	    {
	    	initializeVector();//I initialize the vector of nodes
	    	updateVector();//I update the information of the nodes
	    }

	    /**
	     * It updates the vector of nodes
	     */
	    private void updateVector() {
	    	for(int i = 0; i < 2; i++) {
	    		 for(Node node : this.nodes){//I check all the nodes of the graph
				        	updateNeighbours(node);//I update the neighbours of the node
			     }
	    		 for(Node n : nodes) {//I iterate through all the nodes
	 	    		if(n.getName().equals("A") || n.getName().equals("B")) {//If it is the desired node
	 	    			n.printRoutingTable();
	 	    			UI.print("\n\n");
	 	    		}
	 			}
	    	}
		}

		/**
	     * It sends the routing table to its neighbours to update them in case it is necessary
	     * @param node: the node of which is the information we will send
	     */
	    private void updateNeighbours(Node node) {
	    	RoutingTable routingTable = node.getRoutingTable();//Routingtable of the node
	    	for(HashMap.Entry<String, Integer> neighbourHash : node.getNeighbours().entrySet()) {//I iterate through all the neighbours
	    		Node neighbour = returnNodeByName(neighbourHash.getKey());//I obtain the neighbour node
	        	updateNode(node.getName(), routingTable, neighbour);//I update the RoutingTable of the neighbour
	    	}
		}

	    /**
	     * It updates the RoutingTable if it is needed
	     * @param nodeName: The name of the node of which is the information from
	     * @param routingTable: It is the RoutingTable of nodeName which is send to the neighbour node
	     * @param neighbour: Node to which is going the information to
	     */
		private void updateNode(String nodeName, RoutingTable routingTable, Node neighbour) {
			NeighbourDestinations columnReceiver = neighbour.getRoutingTable().returnNeighbourColumn(nodeName);//I get the column of the receiver RoutingTable which corresponds to the sender
			int receiverWeight = getWeight(nodeName,nodeName, neighbour.getRoutingTable());//The cost from the neighbour to the node
			for(NeighbourDestinations columnSender : routingTable.getNeighbourToDestination()) {//I iterate through all the columns of the routing table of the sender
				for(int i = 0; i < columnSender.getDestinations().size(); i++) {//I go through all the elements of the column
					updateOperations(columnSender, neighbour, columnReceiver,  receiverWeight, destinations[i]);
				}


			}
		}

		private void updateOperations(NeighbourDestinations columnSender, Node neighbour, NeighbourDestinations columnReceiver, int receiverWeight, String row) {
			//Variables that I will need

			String columnName = columnSender.getNeighbourName();//I get the name of the column of the sender which I am going to use
			int minValue = columnSender.getDestinations().get(row);
			int actualValue = columnReceiver.getDestinations().get(row);//The actual value of the RoutingTable

			if(actualValue > minValue + receiverWeight) {//Check if we have a better value
				columnReceiver.getDestinations().remove(row);//I delete the former value
				columnReceiver.getDestinations().put(row, minValue + receiverWeight);//I update the column
			}
		}

		/**
		 * I calculate the minimum value of a column of the routing table
		 * @param destinations
		 * @param column: the column of the routing table
		 * @return the minimum value
		 */
		private int calculateMinColumn(NeighbourDestinations column, String[] destinations) {
			int min = 999;
			for(int i = 0; i < destinations.length; i++) {//I go through all the values of the column
				int actualValue = column.getDestinations().get(destinations[i]);//I get the actual value of the column
				if(min > actualValue) {//I have a new min
					min = actualValue;
				}
			}
			return min;
		}

		/**
		 * It returns the weight of going from nodeName to the node which is from the routingTable
		 * @param rowName: the name of the row where is the value
		 * @param columnName: The name of the node from we want to know the weight (the name of the column where is the value)
		 * @param routingTableReceiver: The routing table of the other node of the edge
		 * @return the weight of the edge
		 */
		private int getWeight(String columnName, String rowName, RoutingTable routingTableReceiver) {
			NeighbourDestinations column = routingTableReceiver.returnNeighbourColumn(columnName);//It is the column I want to get
			return column.getDestinations().get(rowName);
		}

		/**
	     * It loads the topology map from user choice
	     */
	    public void load()
	    {
	        try
	        {
	            Scanner scan = new Scanner (new File(UIFileChooser.open("Select Map File")));
	            while ( scan.hasNext() )
	            {
	                String n = scan.next();//name of the node
	                int x = scan.nextInt();
	                int y = scan.nextInt();
	                Node node = new Node(n, x, y);
	                int count = scan.nextInt(); // the number of neighbouring nodes
	                for(int i=0; i<count; i++) {//It checks all the neighbours nodes
	                    node.addNeighbour(scan.next(), scan.nextInt());//name and the cost to arrive to it
	                }
	                this.nodes.add(node);//I add the new node to the nodes' vector
	            }
	            createDestinations();//I store the array of the names of the destinations
	            scan.close();
	        }
	        catch(IOException e) {UI.println("File Failure: "+e);}//Error while opening the topology map
	    }

	    /**
	     * It creates the array of the names of all the possible destinations
	     */
	    private void createDestinations() {
	    	this.destinations = new String [nodes.size()];
	    	int i = 0;//Auxiliary for the loop
			for(Node n : nodes) {//I iterate through all the nodes
				destinations[i] = n.getName();
				i++;
			}
		}

	    /**
	     * It initializes the vector of nodes
	     */
	    private void initializeVector() {
			for(Node n : nodes) {//I iterate through all the nodes
				n.initialise(destinations);
			}
		}

		/**
	     * It draws the map
	     */
	    public void draw()
	    {
			UI.clearGraphics();
			ArrayList<String[]> checkForEdges = new ArrayList<>();
	        for (Node n : this.nodes)
	        {
	            UI.setColor(Color.green);
	            UI.fillOval(n.getxPos(), n.getyPos(), 40, 40);
	            UI.setColor(Color.blue);
	            UI.drawString(n.getName(), n.getxPos()+5, n.getyPos()+22);

	            UI.setColor(Color.red);

	            // loop on all neighbours
	            Set<String> keys = n.getNeighbours().keySet();
	            for(String s: keys)
	            {
	                //Search in the list of nodes for this node with name "s"
	                Node neighbour = null;
	                for(int i=0; i<this.nodes.size(); i++)
	                    if(this.nodes.get(i).getName().equals(s))
	                    {
	                        neighbour = this.nodes.get(i);
	                        break;
	                    }

	                if(neighbour != null) // there is a neighbour
	                {	//Variables of the points
	                	int nX = n.getxPos()+20, nY = n.getyPos()+20, neighbourX = neighbour.getxPos()+20, neighbourY = neighbour.getyPos()+20;
	                    UI.drawLine(nX, nY, neighbourX, neighbourY);
	                    drawCosts(checkForEdges, nX, nY, neighbourX, neighbourY, n, s);
	                }

	            }
	        }
	    }

	    /**
	     * It draws the cost of the edges
	     * @param checkForEdges: The list of the current edges
	     * @param nX: coordinates to draw the costs
	     * @param nY
	     * @param neighbourX
	     * @param neighbourY
	     * @param s: the name of one node of the edge
	     */
	    private void drawCosts(ArrayList<String[]> checkForEdges, int nX, int nY, int neighbourX, int neighbourY, Node n, String s) {
	     	String cost = Integer.toString(n.getNeighbours().get(s));
	    	for(int i = 0; i < checkForEdges.size(); i++) {//I go through all the list of edges
            	if((checkForEdges.get(i)[0].equals(s) && checkForEdges.get(i)[1].equals(n.getName()))  ||   //If the edge's cost has been drawn we do nothing
            		(checkForEdges.get(i)[0].equals(n.getName()) && checkForEdges.get(i)[1].equals(s)) ) {
            		return;//The edge has been drawn so we exit
            	}
	    	}
	    	nX = (int) ((nX * 2 + neighbourX) / 3);
            nY = (int) ((nY * 2 + neighbourY) / 3);
            UI.drawString(cost, nX, nY);//I draw the edge
            String [] newEdge = {s,n.getName()};
            checkForEdges.add(newEdge);//I add a new edge
		}


		/**
	     * It ask to the user to show a specific RoutingTable
	     */
	    public void showRoutingTables() {
	    	while(true) {
	    		UI.askString("What Routingtable do you want to display?");
		    	String routingtableToPrint = UI.next();
		    	switch(routingtableToPrint) {
		    	case "all"://Printing all the tables
		    		for(Node n : nodes) {//I iterate through all the nodes
			    			n.printRoutingTable();
			    			UI.print("\n\n");
					}
		    		break;
		    	case "clear"://Clear the text pane
		    		UI.clearText();
		    		break;
		    	default://Print a specific table
			    	for(Node n : nodes) {//I iterate through all the nodes
			    		if(n.getName().equals(routingtableToPrint)) {//If it is the desired node
			    			n.printRoutingTable();
			    		}
			    	}
			    	break;
				}
	    	}
	    }

	    public void redraw() {
	    	load();
	    	start();
	    }

	    public Vector<Node> getNodes() {
			return nodes;
		}

	    /**
	     * It returns a certain node of the vector of nodes given its name
	     * @param name: the name of the node we want to obtain
	     * @return the node we want to obtain
	     */
		private Node returnNodeByName(String name) {
			for(int i = 0; i < nodes.size(); i++) {
        		if(nodes.get(i).getName().equals(name)) {
        			return nodes.get(i);
        		}
        	}
			return null;
		}

		public void setNodes(Vector<Node> nodes) {
			this.nodes = nodes;
		}

		public String[] getDestinations() {
			return destinations;
		}

		public void setDestinations(String[] destinations) {
			this.destinations = destinations;
		}

}
