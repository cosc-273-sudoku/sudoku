import java.util.HashSet;
import java.util.Set;

public class Cell {
  private int row;
  private int col;
  private int value;
  private Set<Integer> possibleValues = new HashSet<Integer>();

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
