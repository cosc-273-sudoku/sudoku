import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class EliminationTask implements Runnable {
  private Board board; // sudoku board
  private int cornerRow; // row index of the mini-grid's top left corner
  private int cornerCol; // col index of the mini-grid's top left corner
  private AtomicBoolean valueSet; // whether a value has been set in the grid

  public EliminationTask(Board board, int cornerRow, int cornerCol, AtomicBoolean valueSet) {
    this.board = board;
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.valueSet = valueSet;
  }

  public void run() {
    Cell[][] grid = this.board.getGrid();
    // loop through each cell in the mini-grid
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        // get cell in row and col index
        int row = this.cornerRow + r;
        int col = this.cornerCol + c;
        Cell cell = grid[row][col];
        // if the value of cell hasn't been set
        if (cell.getValue() == 0) {
          // get possible values for cell
          Set<Integer> possibleValues = cell.getPossibleValues();
          // if there is only one possible value
          if (possibleValues.size() == 1) {
            // set value of cell to the single possible value
            Integer value = possibleValues.toArray(new Integer[1])[0];
            cell.setValue(value);
            // indicate a value has been set in the grid
            this.valueSet.set(true);
            // remove the value from the set of possible values
            // for each cell in the row, col, and mini-grid
            this.board.removePossibleValue(row, col, value);
          }
        }
      }
    }
  }
}
