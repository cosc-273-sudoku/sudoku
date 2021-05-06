public class Puzzle{
    private int[][] puzzle; //9x9 array of values 0-9. Value 0 indicates blank/unfilled.
    
    public Puzzle(int[][] puzzle){
        this.puzzle = puzzle;
    }

    public int[][] getPuzzle(){
        return this.puzzle;
    }

    public void setValue(int row, int col, int val){
        if(row < 0 || row >= puzzle.length || col < 0 || col > puzzle[0].length){ //Out of grid bounds
            System.out.println("Attempted to set a value out of bounds. Row: "+row+", Col: "+col);
            return;
        }
        if(val < 0 || val > 9){ //invalid value
            System.out.println("Attempted to set a value out of range [0,9]");
            return;
        }
        
        this.puzzle[row][col] = val;
    }
    
    public int getValue(int row, int col){
        if(row < 0 || row >= puzzle.length || col < 0 || col > puzzle[0].length){
            System.out.println("Attempted to access a value out of bounds. Row: "+row+", Col: "+col);
            return -999;
        }
        return this.puzzle[row][col];
    }

    @Override
    public String toString(){
        String s = "";
        for(int r=0; r<puzzle.length; r++){
            for(int c=0; c<puzzle[0].length; c++){
                s += puzzle[r][c]+" ";
            }
            s += "\n";
        }
        return s;
    }
}