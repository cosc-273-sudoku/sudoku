import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoneRangersTask implements Runnable {
  private Board board; // sudoku board
  private int cornerRow; // row index of the mini-grid's top left corner
  private int cornerCol; // col index of the min-grid's top left corner
  private boolean checkRows; // whether to check rows
  private boolean checkCols; // whether to check columns
  private AtomicBoolean valueSet; // whether a value has been set in the grid

  public LoneRangersTask(
      Board board,
      int cornerRow,
      int cornerCol,
      boolean checkRows,
      boolean checkCols,
      AtomicBoolean valueSet) {
    this.board = board;
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.checkRows = checkRows;
    this.checkCols = checkCols;
    this.valueSet = valueSet;
  }

  public void run() {
    // create a job queue
    Queue<Job> jobQueue = new LinkedList<Job>();
    if (checkRows) {
      // add row jobs
      for (int r = 0; r < 3; r++) {
        jobQueue.add(new RowJob(this.cornerRow + r));
      }
    } else if (checkCols) {
      // add column jobs
      for (int c = 0; c < 3; c++) {
        jobQueue.add(new ColJob(this.cornerCol + c));
      }
    }
    jobQueue.add(new MiniGridJob()); // add mini grid job
    // while there are still jobs to run
    while (!jobQueue.isEmpty()) {
      Job job = jobQueue.remove();
      if (job instanceof RowJob) {
        executeRowJob(job, jobQueue);
      } else if (job instanceof ColJob) {
        executeColJob(job, jobQueue);
      } else { // job is instance of MiniGridJob
        executeMiniGridJob(job, jobQueue);
      }
    }
  }

  /*
   * Try to find and set lone ranger in row
   */
  private void executeRowJob(Job job, Queue<Job> jobQueue) {
    int row = job.row;
    // try to lock row, col, and mini-grid of cell
    if (this.board.tryLockCell(row, 0)) {
      try {
        Cell[][] grid = this.board.getGrid();
        // map of possible value (1 - 9) to
        // cells with that value
        HashMap<Integer, ArrayList<Cell>> possibleValueCellsMap =
            new HashMap<Integer, ArrayList<Cell>>();
        // for each cell in row
        for (int col = 0; col < 9; col++) {
          // add possible values of cell to the map
          addPossibleValuesToMap(row, col, possibleValueCellsMap, grid);
        }
        // find and set the long ranger
        setLoneRanger(possibleValueCellsMap);
      } finally {
        // unlock row, col, and mini-grid of cell
        this.board.unlockCell(row, 0);
      }
    } else { // if unable to lock, add job back to jobQueue
      jobQueue.add(job);
    }
  }

  /*
   * Try to find and set lone ranger in column
   */
  private void executeColJob(Job job, Queue<Job> jobQueue) {
    int col = job.col;
    if (this.board.tryLockCell(0, col)) {
      try {
        Cell[][] grid = this.board.getGrid();
        HashMap<Integer, ArrayList<Cell>> possibleValueCellsMap =
            new HashMap<Integer, ArrayList<Cell>>();
        // for each cell in column
        for (int row = 0; row < 9; row++) {
          addPossibleValuesToMap(row, col, possibleValueCellsMap, grid);
        }
        setLoneRanger(possibleValueCellsMap);
      } finally {
        this.board.unlockCell(0, col);
      }
    } else {
      jobQueue.add(job);
    }
  }

  /*
   * Try to find and set lone ranger in mini-grid
   */
  private void executeMiniGridJob(Job job, Queue<Job> jobQueue) {
    if (this.board.tryLockCell(this.cornerRow, this.cornerCol)) {
      try {
        Cell[][] grid = this.board.getGrid();
        HashMap<Integer, ArrayList<Cell>> possibleValueCellsMap =
            new HashMap<Integer, ArrayList<Cell>>();
        // for each cell in mini-grid
        for (int r = 0; r < 3; r++) {
          for (int c = 0; c < 3; c++) {
            int row = this.cornerRow + r;
            int col = this.cornerCol + c;
            addPossibleValuesToMap(row, col, possibleValueCellsMap, grid);
          }
        }
        setLoneRanger(possibleValueCellsMap);
      } finally {
        this.board.unlockCell(this.cornerRow, this.cornerCol);
      }
    } else {
      jobQueue.add(job);
    }
  }

  /*
   * Add the possible values of the cell to the possibleValueCellsMap
   */
  private void addPossibleValuesToMap(
      int row, int col, HashMap<Integer, ArrayList<Cell>> possibleValueCellsMap, Cell[][] grid) {
    Cell cell = grid[row][col];
    Set<Integer> possibleValues = cell.getPossibleValues();
    // for each possible value
    for (Integer value : possibleValues) {
      // add possible value and corresponding cell to map
      if (possibleValueCellsMap.containsKey(value)) {
        possibleValueCellsMap.get(value).add(cell);
      } else {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        cells.add(cell);
        possibleValueCellsMap.put(value, cells);
      }
    }
  }

  /*
   * Try to find and set lone ranger using possibleValueCellsMap
   */
  private void setLoneRanger(HashMap<Integer, ArrayList<Cell>> possibleValueCellsMap) {
    // for each possibleValue, cells in the map
    for (Map.Entry<Integer, ArrayList<Cell>> entry : possibleValueCellsMap.entrySet()) {
      // if there is only one cell for the possible value
      if (entry.getValue().size() == 1) {
        // set the value of the cell
        Cell cell = entry.getValue().get(0);
        Integer value = entry.getKey();
        cell.setValue(value);
        // indicate a value has been set in the grid
        this.valueSet.set(true);
        // remove the value from the set of possible values
        // for each cell in the row, col, and mini-grid
        this.board.removePossibleValue(cell.getRow(), cell.getCol(), value);
      }
    }
  }
}
