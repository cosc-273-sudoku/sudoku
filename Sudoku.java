public class Sudoku{
    /**Backtracking-7 algorithm:
     * Function isValid() that checks if a given matrix is a valid (filled) sudoku grid. 
     * Recursive function that takes the grid and current position for the value we are trying to fill in
     * - Base cases:
     *      - If the position is at the end of the puzzle grid, check if the grid is valid. If so, return true.
     *      - When position is at the last column, move to next row
     * - if current position is blank/unassigned, fill it in with 1-9 and recur for all 9 cases with the position of the next element. If the recursive call returns true, break the loop and return true.
     * - if the current index is assigned, then call the recursive function with position of next element
     * 
     * Algorithm used from https://see.stanford.edu/materials/icspacs106b/H19-RecBacktrackExamples.pdf 
     *  */
    
    /**Checks if filling the @param num in at location @param row, @param col makes the puzzle invalid (unsolvable) */
    public static boolean isValid(int[][] puzzle, int row, int col, int num){
        // Check that number is not already present in the row or column
        for(int j=0; j<puzzle.length; j++){
            if(puzzle[row][j] == num || puzzle[j][col] == num) return false;
        }
        // Check that number is not already present in 3x3 grid
        int gLen = (int) Math.sqrt(puzzle.length);
        int gRow = row - row % gLen;
        int gCol = col - col % gLen;
        for(int r=gRow; r < gRow+gLen; r++){
            for(int c=gCol; c < gCol+gLen; c++){
                if(puzzle[r][c] == num) return false;
            }
        }
        return true; //no rules broke
    }

    /**Uses backtracking-7 algorithm to solve a partially filled @param puzzle grid and
       attempts to assign values at all blank positions for a valid solution if possible*/
    public static boolean solveSudoku(Puzzle puz){
        int[][] puzzle = puz.getPuzzle();
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        for(int r=0; r<puzzle.length; r++){
            for(int c=0; c<puzzle[0].length; c++){
                if(puzzle[r][c] == 0){
                    row = r;
                    col = c;
                    isEmpty = false; //There are still unfilled values in puzzle
                    break;
                }
            }
            if(!isEmpty) break; //Unfilled values means we continue to backtracking
        }
        if(isEmpty) return true; //fully filled puzzle --> solution found
        
        // Backtrack --> assign a number from 1-9 --> if valid, recursively call, until a full solution is found or no solutions remain
        for(int num=1; num <= puzzle.length; num++){
            if(isValid(puzzle,row,col,num)){ //placing num at row,col is valid (will be further backtracked)
                puz.setValue(row,col,num);
                if(solveSudoku(puz)){
                    return true;
                }else{
                    puz.setValue(row,col,0);
                }
            }
        }
        return false;
    }
    
    public static void main(String args[]){
        // EXAMPLE RUN OF SEQUENTIAL BACKTRACKING ALGORITHM:
        Puzzle p1 = new Puzzle(new int[][] {
            {0,0,0,0,6,9,0,0,0},
            {1,3,0,0,5,0,0,0,0},
            {0,7,6,4,0,0,5,0,0},
            {0,0,0,8,0,0,0,6,4},
            {9,0,0,0,4,0,0,0,3},
            {6,1,0,0,0,7,0,0,0},
            {0,0,5,0,0,4,2,7,0},
            {0,0,0,0,9,0,0,8,5},
            {0,0,0,6,7,0,0,0,0}});
        if(solveSudoku(p1)) System.out.println(p1.toString());
        else System.out.println("No solution");
    }
}