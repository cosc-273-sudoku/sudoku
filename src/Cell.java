import java.util.HashSet;

public class Cell {
  private int value;
  private HashSet<Integer> possibleValues = new HashSet<Integer>();

  public Cell(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public HashSet<Integer> getPossibleValues() {
    return this.possibleValues;
  }

  public void setPossibleValues(HashSet<Integer> possibleValues) {
    this.possibleValues = possibleValues;
  }
}
