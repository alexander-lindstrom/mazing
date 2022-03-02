package com.company;
import java.util.ArrayList;





public class Main {

    public static void main(String[] args)
    {

        Grid g = generateGrid();
        g.printGrid();
        ArrayList<Position> path = GraphMethods.shortestPath(g);

        Simulate s = new Simulate(g, path);
        System.out.println(s.getClapMap());

    }

    public static Grid generateGrid()
    {
        Grid g = null;
        boolean success = false;
        while(!success)
        {
            g  = new Grid(30, 10);
            if (GraphMethods.shortestPath(g) != null)
            {
                success = true;
            }
        }
        return g;
    }
}
