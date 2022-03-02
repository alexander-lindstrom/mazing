package com.company;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Class simulating the runner in the maze
public class Simulate
{
    private Grid g;
    private ArrayList<Position> path;
    private ArrayList<Double> timeList = new ArrayList<Double>();
    private ArrayList<Point> directionList = new ArrayList<Point>();
    private ArrayList<Point> pointList = new ArrayList<Point>();
    private Map<Double, List<Point>> clapMap = new HashMap<>();

    //constants
    private final double timeStep = 0.01; //Seconds
    private final double regularSpeed = 4.8; //Squares per second
    private final double slowedSpeed = regularSpeed/2; // squares per second
    private final double slowDuration = 5; //Seconds
    private final double turnRate = 0.5/0.03; //Radians per second
    private final double eps = 0.0001; // avoid floating point issues

    //values to update
    private double time = 0;
    private Point currentDirection = new Point (1, 0);
    private Point currentPos;
    private boolean slowed = false;
    private double slowRemaining = 0;
    private double speed = regularSpeed;


    public Simulate(Grid g, ArrayList<Position> path)
    {
        this.g = g;
        this.path = path;

        //Sanity check path
        if (path.size() == 0)
        {
            throw new IllegalStateException("Length zero path given as input");
        }

        currentPos = Common.positionToPoint(path.get(0));

        runSimulation();
    }

    private void runSimulation()
    {
        for(int i = 0; i < path.size()-1; i++)
        {


            Point source = Common.positionToPoint(path.get(i));
            Point target = Common.positionToPoint(path.get(i+1));
            Point newDirection = Common.getDirection(source, target);
            System.out.println(" Source, target, newDirection, speed:");
            System.out.println(source);
            System.out.println(target);
            System.out.println(newDirection);
            System.out.println(speed);

            moveToPosition(source, target, newDirection);

        }
    }

    private void moveToPosition(Point source, Point target, Point newDirection)
    {
        //Write initial state
        recordState();

        //Turn toward the new direction
        turnToDirection(newDirection);
        //Write state
        recordState();

        boolean targetReached = false;
        int iterations = 0;

        while(!targetReached)
        {
            //System.out.println(currentPos);

            double xDiff = timeStep*speed*newDirection.x;
            double yDiff = timeStep*speed*newDirection.y;

            time += timeStep;
            currentPos = new Point(currentPos.x + xDiff, currentPos.y + yDiff);

            if (slowed)
            {
                slowRemaining -= timeStep;
                if (slowRemaining <= 0)
                {
                    slowRemaining= 0;
                    slowed = false;
                    speed = regularSpeed;
                }
            }

            //Check if one or more claps were triggered
            if(clapTriggered())
            {

                slowed = true;
                slowRemaining = slowDuration;
                speed = slowedSpeed;
            }

            //Check if target was reached
            if(positionReached(source, currentPos, target))
            {
                targetReached = true;
                currentPos = target;
            }

            //Write state
            recordState();
            iterations++;
            if(iterations > 1000)
            {
                throw new IllegalStateException("Maximum number of iterations exceeded");
            }
        }
    }

    //Check if one or more clapTower are triggered
    //Add clapEvent to list if triggered
    private boolean clapTriggered()
    {

        boolean clap = false;
        List<Point> claps = new ArrayList<>();

        for(ClapTower t: g.clapTowers)
        {
            if (t.center.distance(currentPos) <= t.clapRadius)
            {
                if (!t.coolDown(time))
                {
                    t.clapEvents.add(time);
                    claps.add(t.center);
                    clap = true;
                }
            }
        }

        if(clap){
            clapMap.put(time, claps);
            return true;
        }
        return false;
    }

    // Update lists with information to be use for animating. ClapEvents are handled separately.
    private void recordState()
    {

        timeList.add(time);
        directionList.add(currentDirection);
        pointList.add(currentPos);
    }

    private void turnToDirection(Point newDirection)
    {

        //Turn to face the correct direction
        double angle = Common.getAngleRadians(currentDirection, newDirection);
        double turnDelay = Common.turnDelay(turnRate, angle);

        time += turnDelay;
        currentDirection = newDirection;
    }

    private boolean positionReached(Point source, Point current, Point target)
    {
        //Check if target lies between source and current
        return Common.pointBetweenPoints(source, current, target, eps);
    }

    public Map<Double, List<Point>> getClapMap()
    {
        return clapMap;
    }

    public ArrayList<Point> getPointList()
    {
        return pointList;
    }

    public ArrayList<Point> getDirectionList()
    {
        return directionList;
    }

    public ArrayList<Double> getTimeList()
    {
        return timeList;
    }
}

