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
import ecs100.*;
import java.awt.Color;
import java.util.Scanner;
import java.io.*;
import java.util.Vector;
import java.util.Set;

public class Lab4
{
    Vector<Node> nodes = new Vector<Node>();

    /**
     * The empty constructor of lab4 just create the UI
     */
    public Lab4()
    {
        UI.addButton("load map", this::load);
        UI.addButton("Draw Nodes", this::draw);

        UI.addButton("Start", this::start);

    }


    public void start()
    {
        for(Node n : this.nodes)
        {
            //1. initialise
            //2. update neighbours
        }
    }

    public void load()
    {
        try
        {
            Scanner scan = new Scanner (new File(UIFileChooser.open("Select Map File")));
            while ( scan.hasNext() )
            {
                String n = scan.next();
                int x = scan.nextInt();
                int y = scan.nextInt();
                Node node = new Node(n, x, y);
                int count = scan.nextInt(); // the number of neighbouring nodes
                for(int i=0; i<count; i++) {
                    node.addNeighbour(scan.next(), scan.nextInt());
                }
                this.nodes.add(node);
                node.initialise();
            }
            scan.close();
        }
        catch(IOException e) {UI.println("File Failure: "+e);}
    }

    public void draw()
    {
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
                {
                    UI.drawLine(n.getxPos()+20, n.getyPos()+20, neighbour.getxPos()+20, neighbour.getyPos()+20);
                    //UI.drawString(n.getNeighbours().get(s), !, !);

                }

            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        new Lab4();
    }

}
