package aminoacid;

public class GreaterEqualCondition extends CountCondition{

  @Override
  protected boolean check(int count, int nbr) {
    return count >= nbr;
  }
  
}
