package com.company;

import java.util.Random;

//Helper method class
public class Common
{
    public static int getRandomInt(int min, int max)
    {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }

    public static int getIndex(int i, int j, int cols)
    {
        return i*cols + j;
    }

    public static int getIndex(Position p, int cols)
    {
        return p.i*cols + p.j;
    }

    public static Position positionFromIndex(int index, int cols){

        int row = index / cols;
        int col = index % cols;

        return new Position(row, col);
    }


    //Determine if c lies between a and b on the line AB. Assumes that A,B,C are all on AB
    //This is done by checking if: 0 < (b-a) dot (c-a) < dist(a,b)^2
    public static boolean pointBetweenPoints(Point a, Point b, Point c, double eps)
    {

        Point bMinusA = b.subtractPoint(a);
        Point cMinusA = c.subtractPoint(a);
        double dotProduct = dotProd2D(bMinusA, cMinusA);
        double lengthSquared = a.distanceSq(b);

        if (dotProduct < eps){
            return false;
        }
        return !(dotProduct > lengthSquared + eps);
    }

    public static double dotProd2D(Point a, Point b)
    {
        return a.x*b.x + a.y*b.y;
    }

    //Get the normalized direction vector
    public  static Point getDirection(Point p1, Point p2)
    {
        Point direction = p1.subtractPoint(p2);
        double norm = direction.norm();
        return direction.divideBy(norm);
    }

    //Return the angle in radians between two normalized vectors (2D)
    public static double getAngleRadians(Point p1, Point p2)
    {
        return Math.acos(dotProd2D(p1, p2));
    }

    //Return the time [seconds] needed to turn angle [radians]
    public static double turnDelay(double turnRate, double angle)
    {
        return angle/turnRate;
    }
    public static Point positionToPoint(Position p)
    {
        return new Point(p.i + 0.5, p.j + 0.5);
    }

}
