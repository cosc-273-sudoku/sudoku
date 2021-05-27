import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Represents a cell in the sudoku grid
public class Cell {
  private int row; // cell row
  private int col; // cell column
  private int value; // cell value
  private Set<Integer> possibleValues =
      Collections.synchronizedSet(new HashSet<Integer>()); // possible values for cell

  public Cell(int value, int row, int col) {
    this.value = value;
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Set<Integer> getPossibleValues() {
    return this.possibleValues;
  }

  public void setPossibleValues(Set<Integer> possibleValues) {
    this.possibleValues = possibleValues;
  }
}
