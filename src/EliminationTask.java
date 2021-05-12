import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class EliminationTask implements Runnable {
  private Board board;
  private int cornerRow;
  private int cornerCol;
  private AtomicBoolean valueSet;

  public EliminationTask(Board board, int cornerRow, int cornerCol, AtomicBoolean valueSet) {
    this.board = board;
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.valueSet = valueSet;
  }

  public void run() {
    Cell[][] grid = this.board.getGrid();
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        int row = this.cornerRow + r;
        int col = this.cornerCol + c;
        Cell cell = grid[row][col];
        if (cell.getValue() == 0) {
          Set<Integer> possibleValues = cell.getPossibleValues();
          if (possibleValues.size() == 1) {
            Integer value = possibleValues.toArray(new Integer[1])[0];
            cell.setValue(value);
            this.valueSet.set(true);
            this.board.removePossibleValue(row, col, value);
          }
        }
      }
    }
  }
}
