package aminoacid;

public class GreaterEqualCondition extends AbstractCondition{

  @Override
  protected boolean check(int count, int nbr) {
    return count >= nbr;
  }
  
}
