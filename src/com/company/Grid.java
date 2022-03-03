package com.company;
import java.util.ArrayList;


//todo:
//seems to stop working as soon as the direction changes
//also seems to always be slowed.

//Class representing Grid
public class Grid
{
    //Square values
    public final int empty = 0;
    public final int BT = - 1;
    public final int SBT = -2;
    public final int CT = - 3;
    public final int SCT = - 4;

    //Constants
    public final int height = 30;
    public final int width = 25;

    private final int minNumBT = 5;
    private final int maxNumBT = 17;

    private final int minNumCT = 0;
    private final int maxNumCT = 3;

    private final int minGold = 5;
    private final int maxGold = 20;

    private final int minLumber = 0;
    private final int maxLumber = 3;

    //Derived values
    private int numBT;
    private int numCT;
    private final int gold = Common.getRandomInt(minGold, maxGold);
    private final int lumber = Common.getRandomInt(minLumber, maxLumber);

    int[][] grid = new int[height][width];
    ArrayList<ClapTower> clapTowers = new ArrayList<ClapTower>();

    Grid()
    {
        numBT = Common.getRandomInt(minNumBT, maxNumBT);
        numCT = Common.getRandomInt(minNumCT, maxNumCT);
        fillGrid();
    }

    Grid(int numBT, int numCT)
    {
        this.numBT = numBT;
        this.numCT = numCT;
        fillGrid();
    }

    //Randomly fill the grid with numBT block towers and numCT clap towers
    void fillGrid()
    {
        //Place clap towers
        int numCTPlaced = 0;
        while(numCTPlaced < numCT){
            randomlyPlaceTower(CT);
            numCTPlaced++;
        }

        //Place block towers
        int numBTPlaced = 0;
        while(numBTPlaced < numBT){
            randomlyPlaceTower(BT);
            numBTPlaced++;
        }
    }

    void randomlyPlaceTower(int type)
    {
        boolean success = false;
        int max_failures = 100;
        int i = 0;
        Position p = null;
        Square s = null;

        while(!success){
            p = randomPositionForTower();
            s = new Square(p);
            if (squareFree(s)){
                success = true;
            }
            if (i > max_failures){
                throw new IllegalStateException("Maximum number of iteration exceeded in randomlyPlaceTower()");
            }
            i++;
        }
        fillSquare(s, type);
        if(type == CT){
            clapTowers.add(new ClapTower(p));
        }
    }

    void fillSquare(Square s, int value)
    {
        for(Position p: s.cells) {
            grid[p.i][p.j] = value;

        }
    }

    Position randomPositionForTower()
    {
        //Leave 1 col, row room since towers are 2x2
        int i = Common.getRandomInt(0, height-1);
        int j = Common.getRandomInt(0, width-1);

        return new Position(i, j);
    }

    //Check if 2x2 square needed to place tower is free.
    public boolean squareFree(Square s)
    {
        if (squareValid(s) && squareEmpty(s)){
            return true;
        }
        return false;
    }

    public boolean positionOnGrid(Position p)
    {
        if (p.i < 0 || p.i >= height){
            return false;
        }
        if (p.j < 0 || p.j >= width){
            return false;
        }
        return true;
    }

    private boolean squareValid(Square s)
    {
        for(Position p: s.cells) {
            if (!positionOnGrid(p)){
                return false;
            }
        }
        return true;
    }

    private boolean squareEmpty(Square s)
    {
        for(Position p: s.cells) {
            if (grid[p.i][p.j] != empty){
                return false;
            }
        }
        return true;
    }

    public void printGrid()
    {
        for (int[] i : grid) {
            for (int j : i) {
                System.out.printf("%3d", j);
            }
            System.out.printf("\n");
        }
    }

    public boolean diagonalMoveAllowed(int i, int j, int k, int l)
    {
        int rowDiff = k-i;
        int colDiff = l-j;

        if (grid[i + rowDiff][j] != empty){
            if (grid[i][j + colDiff] != empty){
                return false;
            }
        }
        return true;
    }
}


