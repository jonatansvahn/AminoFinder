package aminoacid;

public class EqualCondition extends AbstractCondition{



  @Override
  protected boolean check(int count, int nbr) {
    return count == nbr;
  }
  
}
