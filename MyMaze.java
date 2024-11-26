// Names: Weiwen Chen
// x500s: Chen6704

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i ++){
            for (int j = 0; j < cols; j ++){
                maze[i][j] = new Cell();
            }
        }
        this.startRow = startRow;
        this.endRow = endRow;
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int level) {
        Random r = new Random();
        MyMaze myM = null;
        if (level == 1){
            // instantiate a new MyMaze object:
            myM = new MyMaze(5, 5, r.nextInt(5), r.nextInt(5));
        } else if (level == 2){
            myM = new MyMaze(5, 20, r.nextInt(5), r.nextInt(5));
        } else if (level == 3){
            myM = new MyMaze(20, 20, r.nextInt(20), r.nextInt(20));
        }

        // generate the maze:
        Stack1Gen<int[]> stack1 = new Stack1Gen<>();                  // Initialize a stack
        int[] start = new int[]{myM.startRow, 0};                            // initialize the first index
        stack1.push(start);
        myM.maze[myM.startRow][0].setVisited(true);                      // set as visited

        // Loop until the stack is empty
        while (!stack1.isEmpty()) {
            int[] top = stack1.top();
            // create an array of reachable neighbours
            int[][] direction = null;
            if (top[0] - 1 < 0 && top[1] - 1 < 0){
                direction = new int[][]{{top[0] + 1, top[1]}, {top[0], top[1] + 1}};
            } else if (top[0] - 1 < 0 && top[1] + 1 >= myM.maze[0].length){
                direction = new int[][]{{top[0] + 1, top[1]}, {top[0], top[1] - 1}};
            } else if (top[0] + 1 >= myM.maze.length && top[1] - 1 < 0){
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0], top[1] + 1}};
            } else if (top[0] + 1 >= myM.maze.length && top[1] + 1 >= myM.maze[0].length){
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0], top[1] - 1}};
            } else if (top[0] - 1 < 0){
                direction = new int[][]{{top[0] + 1, top[1]}, {top[0], top[1] - 1}, {top[0], top[1] + 1}};
            } else if (top[0] + 1 >= myM.maze.length){
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0], top[1] - 1}, {top[0], top[1] + 1}};
            } else if (top[1] - 1 < 0){
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0] + 1, top[1]}, {top[0], top[1] + 1}};
            } else if (top[1] + 1 >= myM.maze[0].length){
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0] + 1, top[1]}, {top[0], top[1] - 1}};
            } else {
                direction = new int[][]{{top[0] - 1, top[1]}, {top[0] + 1, top[1]}, {top[0], top[1] - 1}, {top[0], top[1] + 1}};
            }

            int randNum = r.nextInt(direction.length);
            int[] neighbor = direction[randNum];
            boolean allVisited = false;                                                     // used to check if all neighbours are visited
            while (myM.maze[neighbor[0]][neighbor[1]].getVisited()) {        // check if visited
                allVisited = true;
                for (int i = 0; i < direction.length; i ++){                            // check if out of bound
                    if (!myM.maze[direction[i][0]][direction[i][1]].getVisited()){
                        allVisited = false;
                    }
                }
                if (allVisited) {                                                               // all neighbours are visited
                    break;
                }
                randNum = r.nextInt(direction.length);
                neighbor = direction[randNum];
            }

            if (allVisited){                                                                    // all neighbours are visited
                stack1.pop();
            } else {
                if (top[0] - neighbor[0] == 1) {                                    // top neighbour
                    stack1.push(neighbor);
                    myM.maze[neighbor[0]][neighbor[1]].setVisited(true);
                    myM.maze[neighbor[0]][neighbor[1]].setBottom(false);
                } else if (neighbor[0] - top[0] == 1) {                         // bottom neighbour
                    stack1.push(neighbor);
                    myM.maze[neighbor[0]][neighbor[1]].setVisited(true);
                    myM.maze[top[0]][top[1]].setBottom(false);
                } else if (top[1] - neighbor[1] == 1) {                         // left neighbour
                    stack1.push(neighbor);
                    myM.maze[neighbor[0]][neighbor[1]].setVisited(true);
                    myM.maze[neighbor[0]][neighbor[1]].setRight(false);
                } else if (neighbor[1] - top[1] == 1) {                         // right neighbour
                    stack1.push(neighbor);
                    myM.maze[neighbor[0]][neighbor[1]].setVisited(true);
                    myM.maze[top[0]][top[1]].setRight(false);
                }
            }
        }
        // set all cells to unvisited
        for (Cell[] x: myM.maze){
            for (Cell y: x){
                y.setVisited(false);
            }
        }
        return myM;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        int i, j;
        String[][] mazeFigure = new String[maze.length * 2 + 1][maze[0].length * 2 + 1];
        // print the first line and last line
        for (i = 0; i < maze[0].length * 2 + 1; i  += 2){
            mazeFigure[0][i] = "|";
            mazeFigure[maze.length * 2][i] = "|";
        }
        for (i = 1; i < maze[0].length * 2 + 1; i  += 2){
            mazeFigure[0][i] = "---";
            mazeFigure[maze.length * 2][i] = "---";
        }

        // print the lines of cells
        for (i = 1; i < maze.length * 2; i += 2){
            for (j = 0; j < maze[0].length * 2 + 1; j += 2){
                if (i == startRow * 2 + 1 && j == 0){
                    mazeFigure[i][j] = "";
                } else if (i == endRow * 2 + 1 && j == maze[0].length * 2){
                    mazeFigure[i][j] = "";
                } else if (j != 0 && !maze[i / 2][j / 2 - 1].getRight()){
                    mazeFigure[i][j] = "";
                } else {
                    mazeFigure[i][j] = "|";
                }
            }
            for (j = 1; j < maze[0].length * 2 + 1; j  += 2){
                if (maze[i/2][j/2].getVisited()){
                    mazeFigure[i][j] = " * ";
                } else {
                    mazeFigure[i][j] = "    ";
                }
            }
        }

        // print the lines of walls
        for (i = 2; i < maze.length * 2; i += 2){
            for (j = 0; j < maze[0].length * 2 + 1; j  += 2){
                mazeFigure[i][j] = "|";
            }
            for (j = 1; j < maze[0].length * 2 + 1; j  += 2){
                if (!maze[i/2 - 1][j/2].getBottom()){
                    mazeFigure[i][j] = "    ";
                } else {
                    mazeFigure[i][j] = "---";
                }
            }
        }

        for (String[] x: mazeFigure){
            for (String y: x){
                System.out.print(y);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q1Gen<int[]> queue = new Q1Gen<>();
        int[] start = new int[]{startRow, 0};
        queue.add(start);
        while (queue.length() != 0){
            int[] top = queue.remove();
            maze[top[0]][top[1]].setVisited(true);
            if (top[0] == endRow && top[1] == maze[0].length - 1){
                break;
            }

            // check reachable and un-visited neighbours
            int[][] neighbours = new int[][]{{top[0] - 1, top[1]}, {top[0] + 1, top[1]}, {top[0], top[1] - 1}, {top[0], top[1] + 1}};
            for (int[] x: neighbours){
                if (x[0] >= 0 && x[0] < maze.length && x[1] >= 0 && x[1] < maze[0].length){
                    if (!maze[x[0]][x[1]].getVisited()){
                        if (top[0] - x[0] == 1 && !maze[x[0]][x[1]].getBottom()) {
                            queue.add(x);
                        } else if (x[0] - top[0] == 1 && !maze[top[0]][top[1]].getBottom()){
                            queue.add(x);
                        } else if (top[1] - x[1] == 1 && !maze[x[0]][x[1]].getRight()){
                            queue.add(x);
                        } else if (x[1] - top[1] == 1 && !maze[top[0]][top[1]].getRight()){
                            queue.add(x);
                        }
                    }
                }
            }

        }
    }

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println("Select the level of the maze: ");
        System.out.println("1. Level 1");
        System.out.println("2. Level 2");
        System.out.println("3. Level 3");
        int levelInput = s.nextInt();
        // wrong input case:
        while (levelInput > 3){
            System.out.println("Error! Try it again! ");
            levelInput = s.nextInt();
        }

        MyMaze unsolvedMaze = makeMaze(levelInput);
        System.out.println("Level " + levelInput + " maze is generated: ");
        unsolvedMaze.printMaze();
        System.out.println(" ");
        unsolvedMaze.solveMaze();
        System.out.println("Maze solved:");
        unsolvedMaze.printMaze();
    }
}
