package com.company;
import java.util.Objects;

public class Position
{
    public final int i;
    public final int j;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return i == position.i && j == position.j;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(i, j);
    }

    Position(int i, int j)
    {
        this.i = i;
        this.j = j;
    }
    
    public Position getTopRight()
    {
        return new Position(i+1, j);
    } 
    
    public Position getBottomLeft()
    {
        return new Position(i, j-1);
    }
    
    public Position getBottomRight()
    {
        return new Position(i+1, j-1);
    }

    public String toString()
    {
        return "(" + i + ", " + j + ")";
    }
}
