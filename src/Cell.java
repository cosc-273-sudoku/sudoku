public class Cell {
  private int value;
  private ArrayList<Integer> valids;

  public Cell(int value) {
    this.value = value;
    valids = new ArrayList();
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public ArrayList<Integer> getValids () {
    return this.valids;
  }

  public void setValids(ArrayList<Integer> valids) {
    this.valids = valids;
  }
}
