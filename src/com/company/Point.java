package com.company;

import java.util.Objects;

public class Point
{

    public final double x;
    public final double y;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double distance(Point p)
    {
        double a = p.x - x;
        double b = p.y - y;
        return Math.sqrt(a*a + b*b);
    }

    public double distanceSq(Point p)
    {
        double a = p.x - x;
        double b = p.y - y;
        return a*a + b*b;

    }

    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    public Point subtractPoint(Point p)
    {
        return new Point(p.x - x, p.y - y);
    }

    public double norm()
    {
        return Math.sqrt(x*x + y*y);
    }

    public Point divideBy(double value)
    {
        return new Point(x/value, y/value);
    }
}
