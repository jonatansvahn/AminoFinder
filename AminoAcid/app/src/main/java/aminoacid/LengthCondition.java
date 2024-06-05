package aminoacid;

public class LengthCondition {

  private int min;
  private int max;

  public LengthCondition(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public boolean checkCondition(AminoEntry aminoEntry) {
    int length = aminoEntry.getLength();
    return min <= length && length <= max;
  }
}
