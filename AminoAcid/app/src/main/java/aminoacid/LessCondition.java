package aminoacid;

public class LessCondition extends CountCondition {


  @Override
  protected boolean check(int count, int nbr) {
    return count < nbr;
  }
  
}
