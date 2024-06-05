package aminoacid;

public class GreaterCondition extends CountCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count > nbr;
  }
  
}
