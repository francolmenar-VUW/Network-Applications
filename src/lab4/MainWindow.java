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
	    	initializeVector();
	        for(Node n : this.nodes){//I check all the nodes of the graph
	        	sendInfoToUpdate(n);
	        }
	    }

	    /**
	     * It sends the routing table to its neighbours to update them in case it is necessary
	     * @param node: the node of which is the information we will send
	     */
	    private void sendInfoToUpdate(Node node) {
	    	RoutingTable routingTable = node.getRoutingTable();//Routingtable of the node
	        for(NeighbourDestinations neighbourInfo : routingTable.getNeighbourToDestination()){//I check all the columns of the Routingtable
	        	Node neighbour = returnNodeByName(neighbourInfo.getNeighbourName());//I obtain the node which is the same as the header of the column of the Routingtable
	        	//updateNode(neighbour, neighbourInfo);I update the info of the node
	        }
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
	     * It initializes the vector of nodes
	     */
	    private void initializeVector() {
			for(Node n : nodes) {//I iterate through all the nodes
				n.initialise(destinations);
			}
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
