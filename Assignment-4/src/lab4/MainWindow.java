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
		private boolean same;//It is used to know when an update is performed


		/**
	     * The empty constructor of MainWindow just create the UI
	     */
	    public MainWindow()
	    {
	        UI.addButton("load map", this::load);
	        UI.addButton("Draw Nodes", this::draw);
	        UI.addButton("Start", this::start);
	        UI.addButton("Show Routing tables", this::showRoutingTables);
	        UI.addButton("New cost", this::newCost);
	        UI.addButton("Root", this::root);
	        UI.addButton("Measure the time", this::time);
	    }

	    /**
	     * It initializes the RoutingTables and perform the updates
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
	    	this.same = false;
	    	while(!this.same) {//While there is any change
	    		this.same = true;
	    		for(Node node : this.nodes){//I check all the nodes of the graph
		        	updateNeighbours(node);//I update the neighbours of the node
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

		/**
		 * It makes the checks in order to know if it has to update a value
		 * @param columnSender: the column of the sender which is going to be used
		 * @param neighbour: The node which is going to be updated
		 * @param columnReceiver: The column of the receiver that has the row which will be tried to be updated
		 * @param receiverWeight: The cost of going from the sender to the receiver
		 * @param row: the row where is the value we want to update
		 */
		private void updateOperations(NeighbourDestinations columnSender, Node neighbour, NeighbourDestinations columnReceiver, int receiverWeight, String row) {
			//Variables that I will need

			int minValue = columnSender.getDestinations().get(row);
			int actualValue = columnReceiver.getDestinations().get(row);//The actual value of the RoutingTable

			if(actualValue > minValue + receiverWeight) {//Check if we have a better value
				columnReceiver.getDestinations().remove(row);//I delete the former value
				columnReceiver.getDestinations().put(row, minValue + receiverWeight);//I update the column
				this.same = false;
			}
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
	        	nodes.removeAllElements();//I empty the vector
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
	    	UI.askString("What Routingtable do you want to display?");
	    	String routingtableToPrint = UI.next();
	    	switch(routingtableToPrint) {
	    	case "all"://Printing all the tables
	    		printAll();
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

	    /**
	     * It prints all the RoutingTables
	     */
	   private void printAll() {
		   for(Node n : nodes) {//I iterate through all the nodes
   			n.printRoutingTable();
   			UI.print("\n\n");
		   }
		}

	    /**
	     * It changes a cost of a edge and show the new RoutingTable
	     */
	    private void newCost() {
	    	UI.askString("What edge do you want to change? (Ex: A B changes the edge A-B)\n");
	    	String userInput = UI.nextLine();
	    	String [] vertexes = userInput.split("\\s+");//I split the string into the two vertex names
	    	if(!checksOfInput(vertexes)) {return;}//The input is wrong
	    	UI.askString("What new cost do you want to introduce?\n");
	    	int cost = UI.nextInt();
	    	UI.print("\t\tThe old RoutingTables are:\n\n\n");
	    	printAll();
	    	UI.print("\n\n\t\tAnd the new RoutingTables are:\n\n\n");
	    	changeCost(vertexes[0], vertexes[1], cost);//I change the cost of the edge in both directions
	    	changeCost(vertexes[1], vertexes[0], cost);
	    	start();
	    	draw();
	    	printAll();
	    }

	    /**
	     * It changes a previous cost in the table of string from de edge string-string1
	     * @param string: first vertex
	     * @param string2: second edge
	     * @param cost: the new cost
	     */
	    private void changeCost(String string, String string2, int cost) {
	    	Node node = returnNodeByName(string);//I obtain the node of one vertex
	    	node.getNeighbours().replace(string2, cost);
		}


		/**
	     * It checks the correctness of the input
	     * @param vertexes
	     * @return
	     */
	    private boolean checksOfInput(String[] vertexes) {
	    	if(vertexes.length != 2) {//It checks that the format is the correct one
	    		UI.print("Wrong format sorry\n");
	    		return false;
	    	}
	    	Node node = returnNodeByName(vertexes[0]);//I obtain the node of one vertex
	    	if(node == null) {//I check that the node exists
	    		UI.print("The edge does not exist\n");
	    		return false;
	    	}
	    	NeighbourDestinations columnToGetNode2 = node.getRoutingTable().returnNeighbourColumn(vertexes[1]);
	    	if (columnToGetNode2 == null) {//I check that the edge exists
	    		UI.print("The edge does not exist\n");
	    		return false;
			}
	    	if( columnToGetNode2.getDestinations().get(vertexes[1]) == null) {
	    		UI.print("The edge does not exist\n");
	    		return false;
	    	}
			return true;
		}

	    /**
	     *  It chooses randomly chose a source and a destination node and show
	     *  how data is routed from the former to the latter.
	     */
	    private void root() {
			int [] aux = generateRandonNumbers();
			Node source = nodes.get(aux[0]), destination = nodes.get(aux[1]);//I get the nodes that I am going to use
			UI.print("\n\n\t\tData is going to go from " + source.getName() + "  to " + destination.getName() + "\n\n");
			while(!source.getName().equals(destination.getName())) {//until we found the destination
				source = returnNodeByName(getBestEdge(source, destination.getName()));//I get the next node
			}
	    }

	    /**
	     * It gives the best edge to go to the node name from the source node
	     * @param source: origin node
	     * @param name: the name of the destination node
	     * @return the name of the node which is the one in the best path
	     */
	    private String getBestEdge(Node source, String name) {
	    	int cost = 999;
	    	String auxName = "";
			for(int i = 0; i < source.getRoutingTable().getNeighbourToDestination().size(); i++) {//I go through all the columns of the RoutingTable
				NeighbourDestinations aux = source.getRoutingTable().getNeighbourToDestination().get(i);
				for(HashMap.Entry<String, Integer> n : aux.getDestinations().entrySet()) {//I iterate through all the rows
					if(n.getKey().equals(name) && n.getValue() < cost) {//I check if it is a better path
						cost = n.getValue();//I copy the values
						auxName = aux.getNeighbourName();
					}
				}
			}
			UI.print("I from the node " + source.getName() + " to the node " + auxName +  "\n");
	    	return auxName;
		}


	    /**
	     * It creates two different random numbers and return and array with them
	     * The random numbers are between 0 and the size of the vector of nodes
	     * @return the two random numbers
	     */
		private int[] generateRandonNumbers() {
			int [] aux = new int [2];
	    	for(int i = 0; i < 2; i++){//I check all the nodes of the graph
	    		if(i == 0) {//I create the first random number
	    			aux[0] = (int)(Math.random() * nodes.size());
	    		}
	    		else {
	    			boolean newName = false;
	    			while(!newName) {//I check that both random numbers are different
		    			aux[1] = (int)(Math.random() * nodes.size());
		    			if(aux[0] != aux[1]) {
		    				newName = true;
		    			}
	    			}
	    		}
	    	}
	    	return aux;
		}

		/**
		 * It Measure the time it takes for the routing table to be generated
		 */
		private void time(){
			long startTime = System.nanoTime();
			start();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1000000;
		     UI.print("\n\nThe time that it takes for the routing table to be generated is " + duration + " milliseconds");
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


		public boolean getSame() {
			return same;
		}


		public void setSame(boolean same) {
			this.same = same;
		}

}
