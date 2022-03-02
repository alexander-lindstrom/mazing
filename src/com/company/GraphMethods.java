package com.company;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.*;
import java.util.ArrayList;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import java.util.List;

//Helper class for graph stuff
public class GraphMethods
{
    public static ArrayList<Position> shortestPath(Grid g)
    {
        Graph<Integer, DefaultWeightedEdge> graph = graphFromGrid(g);
        DijkstraShortestPath<Integer, DefaultWeightedEdge> DSP = new DijkstraShortestPath<>(graph);
        GraphPath<Integer, DefaultWeightedEdge> path = DSP.getPath(-1, -2);

        if (path == null){
            return null;
        }

        return getPositionsFromGraphPath(path, g. height, g.width);
    }

    public static ArrayList<Position> getPositionsFromGraphPath(GraphPath<Integer, DefaultWeightedEdge> graphPath,
                                                                int height, int width){

        ArrayList<Position> path = new ArrayList<>();
        List<Integer> vertices = graphPath.getVertexList();
        int center = (width-1)/2;

        for(int v: vertices){

            Position p = switch (v) {
                case -1 -> new Position(-1, center);
                case -2 -> new Position(height, center);
                default -> Common.positionFromIndex(v, width);
            };
            path.add(p);
        }

        return path;
    }

    public static Graph<Integer, DefaultWeightedEdge> graphFromGrid(Grid grid){

        Graph<Integer, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        addVertices(graph, grid.height, grid.width);
        addEdges(graph, grid);
        addExits(graph, grid);

        return graph;

    }

    public static void addVertices(Graph<Integer,DefaultWeightedEdge> graph, int height, int width){

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                graph.addVertex(Common.getIndex(i, j, width));
            }
        }
    }

    public static void addEdges(Graph<Integer, DefaultWeightedEdge> graph, Grid grid){

        //Need to check bottom left also
        int[] row = {0, 1, 1, 1};
        int[] col = {1, -1, 0, 1};
        DefaultWeightedEdge e;

        for(int i = 0; i < grid.height; i++){
            for(int j = 0; j < grid.width; j++){

                if (grid.grid[i][j] != grid.empty){
                    continue;
                }

                //Only need to consider adjacent vertices with a higher index -> TR, BL, BR
                //If cell is free add an edge. (adjust weight for diagonal edges)

                for(int k = 0; k < 4; k++){

                    int candidateRow = i + row[k];
                    int candidateCol = j + col[k];

                    //System.out.printf("i: %d, j: %d\n",i ,j);
                    //System.out.printf("cRow: %d, cCol: %d\n", candidateRow, candidateCol);

                    if(!grid.positionOnGrid(new Position(candidateRow, candidateCol))){
                        continue;
                    }
                    if (grid.grid[candidateRow][candidateCol] != grid.empty){
                        continue;
                    }
                    double weight = 1;

                    //Check for diagonal move
                    if(k % 2 != 0){
                        weight = Math.sqrt(2);
                        if (!grid.diagonalMoveAllowed(i, j, candidateRow, candidateCol)){
                            continue;
                        }
                    }

                    int sourceIndex = Common.getIndex(i, j, grid.width);
                    int sinkIndex = Common.getIndex(candidateRow, candidateCol, grid.width);

                    e = graph.addEdge(sourceIndex, sinkIndex);
                    graph.setEdgeWeight(e, weight);
                }
            }
        }
    }

    //Add one extra vertex + edges at entrance and exit
    public static void addExits(Graph<Integer, DefaultWeightedEdge> graph, Grid grid){

        DefaultWeightedEdge e;
        //Add vertices, -1 for start, -2 for end
        graph.addVertex(-1);
        graph.addVertex(-2);

        //Add starting edges
        int row = 0;
        int center = (grid.width-1)/2;
        int[] cols = {center-1, center, center +1};

        for(int i = 0; i < 3; i++){
            double weight = i == 1 ? 1 : Math.sqrt(2);
            int col = cols[i];
            if(grid.grid[row][col] == grid.empty){
                e = graph.addEdge(-1, Common.getIndex(row, col, grid.width));
                graph.setEdgeWeight(e, weight);
            }
        }

        //Add end edges
        row = grid.height-1;
        for(int i = 0; i < 3; i++) {
            double weight = i == 1 ? 1 : Math.sqrt(2);
            int col = cols[i];
            if (grid.grid[row][col] == grid.empty) {
                e = graph.addEdge(-2, Common.getIndex(row, col, grid.width));
                graph.setEdgeWeight(e, weight);
            }
        }
    }
}
