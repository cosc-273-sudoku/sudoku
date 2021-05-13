import java.util.HashSet;
import java.util.Set;

public class Cell {
  private int value;
  private Set<Integer> possibleValues = new HashSet<Integer>();

  public Cell(int value) {
    this.value = value;
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
