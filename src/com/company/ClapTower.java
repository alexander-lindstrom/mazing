package com.company;

import java.util.ArrayList;

public class ClapTower extends Square
{
    public double clapRadius = 3.5; //Squares
    public double CD = 5; //Seconds
    Point center;
    public ArrayList<Double> clapEvents = new ArrayList<>();

    ClapTower(Position p)
    {
        super(p);
        center = Common.positionToPoint(p);
    }

    public boolean coolDown(double time)
    {
        int n = clapEvents.size();
        if (n == 0){
            return false;
        }
        else{

            return (time - clapEvents.get(n-1)) < CD;
        }
    }
}
