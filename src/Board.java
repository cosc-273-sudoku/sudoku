import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// inspiration for code from https://www.geeksforgeeks.org/program-sudoku-generator/
public class Board {
  private Cell[][] grid = new Cell[9][9];
  private Lock gridLock = new ReentrantLock();
  private Lock[] rowLocks = new ReentrantLock[9];
  private Lock[] colLocks = new ReentrantLock[9];
  private Lock[] miniGridLocks = new ReentrantLock[9];

  public Board() {
    // initialize grid to Cells with 0 as value
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid.length; c++) {
        grid[r][c] = new Cell(0);
      }
    }

    // initialize locks
    for (int i = 0; i < grid.length; i++) {
      rowLocks[i] = new ReentrantLock();
      colLocks[i] = new ReentrantLock();
      miniGridLocks[i] = new ReentrantLock();
    }
  }

  // prints the Sudoku board - used this to check if work is correct.
  @Override
  public String toString() {
    String s = "";
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid.length; c++) {
        s += grid[r][c].getValue() + " ";
      }
      s += "\n";
    }
    return s;
  }

  // gets the board
  public Cell[][] getBoard() {
    return this.grid;
  }

  // gets the legnth of the board
  public int getLength() {
    return this.grid.length;
  }

  // sets a value in the matrix
  public void setValue(int row, int col, int value) {
    if (row < 0
        || row >= this.grid.length
        || col < 0
        || col > this.grid.length) { // Out of grid bounds
      System.out.println("Attempted to set a value out of bounds. Row: " + row + ", Col: " + col);
      return;
    }
    if (value < 0 || value > 9) { // invalid value
      System.out.println("Attempted to set a value out of range [0,9]");
      return;
    }

    this.grid[row][col].setValue(value);
  }

  // gets a value in the matrix
  public int getValue(int row, int col) {
    if (row < 0 || row >= grid.length || col < 0 || col > grid.length) {
      System.out.println(
          "Attempted to access a value out of bounds. Row: " + row + ", Col: " + col);
      return -999;
    }
    return this.grid[row][col].getValue();
  }

  public void fillValues() {
    /*
     * first we fill in the diagonal 3x3 sub matrices (the upper leftmost, middle,
     * and lower righmost 3x3 sub matrices) note: if we fill in the diagonal 3x3 sub
     * matrices first we only have to check if the number exists in the 3x3 box, we
     * do not have to check the rows and col
     */
    fillDiagonal();
    /*
     * recursively fill the rest of the matrix note: now when we fill in each cell,
     * we must check if the number exists in the 3x3 box, the row, AND the col.
     */
    fillRemaining(0, 3);
    /*
     * once the matrix is full we randomly remove numbers and replace them with 0,
     * indicating that it is an empty cell note: this full matrix is our solution
     */
    removeRandDigits();
  }

  /*
   * Tries to lock the cell's row, column, and mini-grid.
   * Returns true if the all locks were successful,
   * and false otherwise.
   */
  public boolean tryLockCell(int row, int col) {
    gridLock.lock();
    try {
      int miniGridLockIndex = 3 * row + col;
      ArrayList<Lock> locks = new ArrayList<Lock>();
      if (this.rowLocks[row].tryLock()) {
        locks.add(this.rowLocks[row]);
      }
      if (this.colLocks[col].tryLock()) {
        locks.add(this.colLocks[col]);
      }
      if (this.miniGridLocks[miniGridLockIndex].tryLock()) {
        locks.add(this.miniGridLocks[miniGridLockIndex]);
      }
      if (locks.size() != 3) {
        for (Lock lock : locks) {
          lock.unlock();
        }
        return false;
      }
      return true;
    } finally {
      gridLock.unlock();
    }
  }

  /*
   * Unlocks the cell's row, column, and mini-grid.
   */
  public void unlockCell(int row, int col) {
    gridLock.lock();
    try {
      rowLocks[row].unlock();
      colLocks[col].unlock();
      miniGridLocks[3 * row + col].unlock();
    } finally {
      gridLock.unlock();
    }
  }

  // fills in the diagonal 3x3 sub matrices
  private void fillDiagonal() {
    for (int start = 0; start < 9; start = start + 3) {
      for (int boxRow = 0; boxRow < 3; boxRow++) {
        for (int boxCol = 0; boxCol < 3; boxCol++) {
          int num = (int) ((Math.random() * 9) + 1);
          while (!unusedInBox(start, start, num)) {
            num = (int) ((Math.random() * 9) + 1);
          }
          grid[start + boxRow][start + boxCol].setValue(num);
        }
      }
    }
  }

  // returns true if the number is not in the 3x3 box
  private boolean unusedInBox(int rowStart, int colStart, int num) {
    for (int boxRow = 0; boxRow < 3; boxRow++) {
      for (int boxCol = 0; boxCol < 3; boxCol++) {
        if (grid[rowStart + boxRow][colStart + boxCol].getValue() == num) return false;
      }
    }
    return true;
  }

  // recursively fills in the rest of the matrix
  private boolean fillRemaining(int row, int col) {
    // case 1: at the last col and last row
    if (row >= 9 && col >= 9) return true;
    // case 2: at the last col
    if (col >= 9) {
      row = row + 1;
      col = 0;
    }
    // case 3: at the upper left 3x3 sub matrix
    // note: we skip because it is already filled out
    if (row < 3) {
      if (col < 3) col = 3;
    }
    // case 4: skip if in the middle 3x3 sub matrix
    // note: we skip because it is already filled out
    else if (row < 6) {
      if (col == (row / 3) * 3) col = col + 3;
    }
    // case 5: skip if in the lower right 3x3 sub matrix
    // note: we skip because it is already filled out
    else {
      if (col == 6) {
        row = row + 1;
        col = 0;
        if (row >= 9) return true;
      }
    }
    // assign the first number from 1-9 that does not exist in the corresponding
    // box, row, and col of the cell
    for (int num = 1; num <= 9; num++) {
      if (CheckIfSafe(row, col, num)) {
        grid[row][col].setValue(num);
        // recursive part, should return true if successful
        if (fillRemaining(row, col + 1)) return true;
        grid[row][col].setValue(0);
      }
    }
    return false;
  }

  /*
   * unusedInRow, unusedInCol, unusedInBox must all return true for CheckIfSafe to
   * return true. In other words the number must not exist in the corresponding
   * box, row, and col of the cell
   */
  private boolean CheckIfSafe(int row, int col, int num) {
    return (unusedInRow(row, num)
        && unusedInCol(col, num)
        && unusedInBox(row - row % 3, col - col % 3, num));
  }

  // checks if the number exists in the row
  private boolean unusedInRow(int row, int num) {
    for (int col = 0; col < 9; col++) {
      if (grid[row][col].getValue() == num) return false;
    }
    return true;
  }

  // checks if the number exists in the col
  private boolean unusedInCol(int col, int num) {
    for (int row = 0; row < 9; row++) {
      if (grid[row][col].getValue() == num) return false;
    }
    return true;
  }

  // randomly removes numbers and replace them with 0, indicating that it's now
  // empty
  private void removeRandDigits() {
    // randomly determines how many numbers to remove (30-50)
    int count = (int) (Math.random() * (20)) + 30;
    while (count != 0) {
      // gets random row and col (0-9)
      int row = (int) (Math.random() * 9);
      int col = (int) (Math.random() * 9);
      // if the cell is not already empty, remove the number and dec count
      if (grid[row][col].getValue() != 0) {
        count--;
        grid[row][col].setValue(0);
      }
    }
  }
}
