package com.company;
public class Square
{
    public final Position[] cells = new Position[4];

    Square(Position p)
    {
        cells[0] = p;
        cells[1] = p.getTopRight();
        cells[2] = p.getBottomLeft();
        cells[3] = p.getBottomRight();
    }

    public boolean collision(Square s)
    {
        for(Position p1: s.cells){
            for(Position p2: cells){
                if (p1.equals(p2)){
                    return true;
                }
            }
        }
        return false;
    }
}
