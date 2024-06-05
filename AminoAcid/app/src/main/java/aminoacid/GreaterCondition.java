package aminoacid;

public class GreaterCondition extends AbstractCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count > nbr;
  }
  
}
