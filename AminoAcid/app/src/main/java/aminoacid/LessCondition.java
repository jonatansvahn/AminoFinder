package aminoacid;

public class LessCondition extends AbstractCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count < nbr;
  }
  
}
