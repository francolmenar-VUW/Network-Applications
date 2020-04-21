package lab4;

/**
*
* @author aliahmed
*/
import ecs100.*;
import java.awt.Color;
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
	    	 for(Node node : this.nodes){//I check all the nodes of the graph
		        	sendInfoToUpdate(node);
		        }
		}


		/**
	     * It sends the routing table to its neighbours to update them in case it is necessary
	     * @param node: the node of which is the information we will send
	     */
	    private void sendInfoToUpdate(Node node) {
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
			RoutingTable routingTableReceiver = neighbour.getRoutingTable();//I get the routing table of the receiver
			for(NeighbourDestinations columnSender : routingTable.getNeighbourToDestination()) {//I iterate through all the columns of the routing table of the sender
				String columnName = columnSender.getNeighbourName();//I get the name of the column which I am going to update
				NeighbourDestinations columnReceiver = routingTableReceiver.returnNeighbourColumn(columnName);//I get the column of the Receiver
			}
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
	                    String cost = Integer.toString(n.getNeighbours().get(s));
	                    nX = (int) ((nX + neighbourX) / 2);
	                    nY = (int) ((nY + neighbourY) / 2);
	                    UI.drawString(cost, nX, nY);

	                }

	            }
	        }
	    }

	    /**
	     * It ask to the user to show a specific RoutingTable
	     */
	    public void showRoutingTables() {
	    	UI.askString("What Routingtable do you want to display?");
	    	String routingtableToPrint = UI.next();
	    	for(Node n : nodes) {//I iterate through all the nodes
	    		if(n.getName().equals(routingtableToPrint)) {//If it is the desired node
	    			n.printRoutingTable();
	    		}
			}
	    }

	    public void redraw() {
	    	load();
	    	start();
	    	for(Node n : nodes) {//I iterate through all the nodes
	    		if(n.getName().equals("A")) {//If it is the desired node
	    			n.printRoutingTable();
	    		}
			}
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
