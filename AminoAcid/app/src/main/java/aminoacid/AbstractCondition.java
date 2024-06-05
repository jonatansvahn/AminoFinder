package aminoacid;

abstract public class AbstractCondition {

  private String symbol;
  private int nbr;

  abstract protected boolean check(int count, int nbr);

  public boolean checkCondition(AminoEntry aminoEntry) {
    String sequence = aminoEntry.getSequence();
    int count = 0;
    for (String s : sequence.split("")) {
      if (s.toLowerCase().equals(symbol.toLowerCase())) {
        count++;
      }
    }
    return check(count, nbr);
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setNumber(int nbr) {
    this.nbr = nbr;
  }

}