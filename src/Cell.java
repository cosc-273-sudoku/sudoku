import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Cell {
  private AtomicInteger value = new AtomicInteger();
  private Set<Integer> possibleValues = Collections.synchronizedSet(new HashSet<Integer>());

  public Cell(int value) {
    this.value.set(value);
  }

  public int getValue() {
    return this.value.get();
  }

  public void setValue(int value) {
    this.value.set(value);
  }

  public Set<Integer> getPossibleValues() {
    return this.possibleValues;
  }

  public void setPossibleValues(Set<Integer> possibleValues) {
    this.possibleValues = Collections.synchronizedSet(possibleValues);
  }
}
