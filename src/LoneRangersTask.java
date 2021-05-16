import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class LoneRangersTask implements Runnable {
  private Board board;
  private int cornerRow;
  private int cornerCol;
  private boolean checkRows;
  private boolean checkCols;
  private boolean checkMiniGrid;
  private AtomicBoolean valueSet;

  public LoneRangersTask(
      Board board,
      int cornerRow,
      int cornerCol,
      boolean checkRows,
      boolean checkCols,
      boolean checkMiniGrid,
      AtomicBoolean valueSet) {
    this.board = board;
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.checkRows = checkRows;
    this.checkCols = checkCols;
    this.valueSet = valueSet;
  }

  public void run() {
    Cell[][] grid = this.board.getGrid();
    Set<Integer> possibleValues = new HashSet<Integer>();
    HashMap<Integer,List<Integer>> valFreq = new HashMap<Integer, List<Integer>>(); //record for each 1-9 value, the locations it can be in as two values --> row col
    for(int i=0; i<9; i++) valFreq.put(i,Collections.emptyList());

    int row = 0; int col = 0;

    for (int i = 0; i < 3; i++) {
      if(checkRows || checkMiniGrid){ row = this.cornerRow + i;
      }else if(checkCols){ col = this.cornerCol + i;
      }
      
      // if (!this.board.tryLockCell(row, 0)) {
      //   continue;
      // }

      for (int j = 0; j < 9; j++) { //for each cell, incrememnt freq counter for values' possible placements
        if(checkRows){ col = this.cornerCol + j; 
        }else if(checkCols){ row = this.cornerRow + j;
        }else{ //checkMiniGrid
          col = this.cornerCol + j/3;
        }
        
        possibleValues = grid[row][col].getPossibleValues();
        for(Integer num : possibleValues){
          List<Integer> newLocations = valFreq.get(num); 
          newLocations.add(row);
          newLocations.add(col);
          valFreq.put(num, newLocations); //add the row and col location number can be in to freq counter
        }
      }
    }
    
    //if any values can only be in one place
    for (HashMap.Entry<Integer,List<Integer>> entry : valFreq.entrySet()){
      int num = entry.getKey();
      List<Integer> locations = entry.getValue();

      if(locations.size() == 2){ //if only one location (row and col)
        row = locations.get(0);
        col = locations.get(1);
        grid[row][col].setValue(num); // set value of cell to the single possible value
        this.valueSet.set(true); // indicate a value has been set in the grid
        // this.board.removePossibleValue(row, col, value); //remove value from possible set for each cell in row, col, and minigrid
      }

    }

  }
}
