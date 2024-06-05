package aminoacid;

public class HydrophobeCondition extends AbstractCondition{
  private int nbr;

  public HydrophobeCondition(int nbr) {
    this.nbr = nbr;
  }

  public boolean checkCondition(AminoEntry aminoEntry) {
    String sequence = aminoEntry.getSequence();
    int count = 0;
    int largestCount = 0;
    for (String s : sequence.split("")) {
      if (isHydrophobic(s)) {
        count++;
        if (count > largestCount) {
          largestCount = count;
        }
      }
      else {
        count = 0;
      }
    }
    aminoEntry.setHydrophobicCount(largestCount);
    return largestCount >= nbr;
  }


  private boolean isHydrophobic(String aminoSymbol) {
    String aS = aminoSymbol.toLowerCase();
    return (aS.equals("a") || aS.equals("v") || aS.equals("l") || aS.equals("f"));
  }
}
